package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.domain.enumeration.ComplexidadeArtefato;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoArtefato;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoOrdemDeFornecimento;
import br.com.rocksti.ofmanager.filtropesquisa.FiltroPesquisaServicoOf;
import br.com.rocksti.ofmanager.planilha.DescricaoArtefato;
import br.com.rocksti.ofmanager.planilha.EstruturaDoArquivo;
import br.com.rocksti.ofmanager.repository.ArtefatoOrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.repository.ArtefatoRepository;
import br.com.rocksti.ofmanager.repository.OrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.security.AuthoritiesConstants;
import br.com.rocksti.ofmanager.service.dto.*;
import br.com.rocksti.ofmanager.service.mapper.OrdemDeFornecimentoMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import liquibase.util.file.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class OrdemFornecimentoService {

    private final OrdemDeFornecimentoRepository ordemDeFornecimentoRepository;

    private final OrdemDeFornecimentoMapper ordemDeFornecimentoMapper;

    private final UserService userService;

    private final ArtefatoRepository artefatoRepository;

    private final ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository;

    public OrdemFornecimentoService(OrdemDeFornecimentoRepository ordemDeFornecimentoRepository,
                                    OrdemDeFornecimentoMapper ordemDeFornecimentoMapper,
                                    UserService userService,
                                    ArtefatoRepository artefatoRepository,
                                    ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository) {
        this.ordemDeFornecimentoRepository = ordemDeFornecimentoRepository;
        this.ordemDeFornecimentoMapper = ordemDeFornecimentoMapper;
        this.userService = userService;
        this.artefatoRepository = artefatoRepository;
        this.artefatoOrdemDeFornecimentoRepository = artefatoOrdemDeFornecimentoRepository;
    }

    private void validarOfPertencenteDeUsuario(Long idOrdemDeFornecimento) {
        if (idOrdemDeFornecimento != null) {
            userService.getUserWithAuthorities()
                .filter(user -> user.getAuthorities()
                    .stream()
                    .map(Authority::getName)
                    .noneMatch(name -> Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.GESTOR_OF).contains(name)))
                .ifPresent(user -> ordemDeFornecimentoRepository.findById(idOrdemDeFornecimento).
                    ifPresent(ordemDeFornecimento -> {
                        if (!ordemDeFornecimento.getDonoDaOf().getId().equals(user.getId())) {
                            throw new RuntimeException("Of pertencente a outro usuário");
                        }
                    }));
        }
    }

    @Transactional(readOnly = true)
    public Page<OrdemDeFornecimentoDTO> findAllByUser(Pageable pageable, FiltroPesquisaServicoOf filtroPesquisa) {
        Long idUsuarioGestor = Optional.ofNullable(filtroPesquisa.getUsuarioGestor()).map(UserDTO::getId).orElse(null);

        return userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities()
                .stream()
                .map(Authority::getName)
                .noneMatch(name -> Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.GESTOR_OF).contains(name)))
            .map(user -> ordemDeFornecimentoRepository.findAllByDonoDaOf_IdEqualsAndNumeroEquals(pageable, user.getId(), filtroPesquisa.getNumeroOF())
                .map(ordemDeFornecimentoMapper::toDto))
            .orElse(ordemDeFornecimentoRepository.findAllByNumeroEqualsAndGestorDaOf_Id(pageable, filtroPesquisa.getNumeroOF(), idUsuarioGestor)
                .map(ordemDeFornecimentoMapper::toDto));
    }

    @Transactional(readOnly = true)
    public Optional<OrdemFornecimentoDTO> findOneOrdemFornecimento(Long id) {
        validarOfPertencenteDeUsuario(id);

        return ordemDeFornecimentoRepository.findById(id)
            .map(ordemDeFornecimento -> {
                ordemDeFornecimento.setDonoDaOf(limpaDadosUsuario(ordemDeFornecimento.getDonoDaOf()));
                ordemDeFornecimento.setGestorDaOf(limpaDadosUsuario(ordemDeFornecimento.getGestorDaOf()));
                OrdemFornecimentoDTO ordemFornecimentoDTO = new OrdemFornecimentoDTO();
                ordemFornecimentoDTO.setOrdemDeFornecimento(ordemDeFornecimento);
                StringJoiner stringJoiner = new StringJoiner("\n");
                ordemDeFornecimento.getArtefatoOrdemDeFornecimentos()
                    .stream()
                    .map(artefatoOrdemDeFornecimento -> artefatoOrdemDeFornecimento.getArtefato().getLocalDoArtefato())
                    .forEach(stringJoiner::add);
                ordemFornecimentoDTO.setListaDosArquivos(stringJoiner.toString());
                return ordemFornecimentoDTO;
            });
    }

    private User limpaDadosUsuario(User user) {
        return Optional.of(user).map(user1 -> {
            User userReturn = new User();
            userReturn.setId(user1.getId());
            userReturn.setFirstName(user1.getFirstName());
            userReturn.setLogin(user1.getLogin());
            return userReturn;
        }).get();
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsuariosGestor() {
        return userService.getUsersByAuthorities(AuthoritiesConstants.GESTOR_OF)
            .stream()
            .map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLogin(user.getLogin());
                return userDTO;
            })
            .collect(Collectors.toList());
    }

    public OrdemFornecimentoDTO updateGestorDaOf(@Valid OrdemFornecimentoDTO ordemFornecimentoDTO) {
        validarOfPertencenteDeUsuario(ordemFornecimentoDTO.getOrdemDeFornecimento().getId());

        AtomicReference<OrdemDeFornecimentoDTO> ordemDeFornecimentoReference = new AtomicReference<>();

        userService.getUserWithAuthorities(ordemFornecimentoDTO.getOrdemDeFornecimento().getGestorDaOf().getId())
            .ifPresent(user ->
                ordemDeFornecimentoRepository.findById(ordemFornecimentoDTO.getOrdemDeFornecimento().getId())
                    .ifPresent(ordemDeFornecimento -> {
                        ordemDeFornecimento.setGestorDaOf(user);
                        OrdemDeFornecimento ordemDeFornecimento1 = ordemDeFornecimentoRepository.save(ordemDeFornecimento);
                        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento1);
                        ordemDeFornecimentoReference.set(ordemDeFornecimentoDTO);
                    })
            );

        return findOneOrdemFornecimento(ordemFornecimentoDTO.getOrdemDeFornecimento().getId()).orElseGet(OrdemFornecimentoDTO::new);
    }

    public OrdemDeFornecimentoDTO updateEstadoDaOf(OrdemDeFornecimentoDTO ordemDeFornecimentoDTO) {
        AtomicReference<OrdemDeFornecimentoDTO> ordemDeFornecimentoReference = new AtomicReference<>();

        ordemDeFornecimentoRepository.findById(ordemDeFornecimentoDTO.getId())
            .ifPresent(ordemDeFornecimento -> {
                ordemDeFornecimento.setEstado(ordemDeFornecimentoDTO.getEstado());
                OrdemDeFornecimento ordemDeFornecimento1 = ordemDeFornecimentoRepository.save(ordemDeFornecimento);
                OrdemDeFornecimentoDTO ordemDeFornecimentoDTO1 = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento1);
                ordemDeFornecimentoReference.set(ordemDeFornecimentoDTO1);
            });

        return ordemDeFornecimentoReference.get();
    }

    public OrdemFornecimentoDTO processar(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        validarOfPertencenteDeUsuario(ordemFornecimentoDTO.getOrdemDeFornecimento().getId());

        Long ordemDeFornecimentoId = Optional.ofNullable(ordemFornecimentoDTO.getOrdemDeFornecimento().getId()).orElse(0L);
        OrdemDeFornecimento ordemDeFornecimento = ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).orElseGet(OrdemDeFornecimento::new);
        final AtomicReference<OrdemDeFornecimento> ordemDeFornecimentoAtomicReference = new AtomicReference<>(ordemDeFornecimento);

        preparaOrdemDeFornecimento(ordemFornecimentoDTO, ordemDeFornecimentoAtomicReference.get());

        List<String> listaLocalArtefatosTextArea = getListaLocalArtefatosTextArea(ordemFornecimentoDTO);

        List<ArtefatoOrdemDeFornecimento> listaArtefatosDaOrdemDeFornecimento = ordemDeFornecimentoAtomicReference.get().getArtefatoOrdemDeFornecimentos();

        removerArtefatosDaLista(ordemDeFornecimento, listaLocalArtefatosTextArea, listaArtefatosDaOrdemDeFornecimento);

        adicionarArtefatosNaLista(ordemDeFornecimento, listaLocalArtefatosTextArea, listaArtefatosDaOrdemDeFornecimento);

        ordemDeFornecimentoAtomicReference.get().setLastModifiedDate(Instant.now());
        ordemDeFornecimentoRepository.save(ordemDeFornecimentoAtomicReference.get());

        return findOneOrdemFornecimento(ordemDeFornecimentoAtomicReference.get().getId()).orElseGet(OrdemFornecimentoDTO::new);
    }

    private void preparaOrdemDeFornecimento(OrdemFornecimentoDTO ordemFornecimentoDTO, OrdemDeFornecimento ordemDeFornecimento) {
        if (ordemDeFornecimento.getId() == null) {
            ordemDeFornecimento.setEstado(EstadoOrdemDeFornecimento.NOVA);
            ordemDeFornecimento.setDonoDaOf(userService.getUserWithAuthorities().orElse(null));
            ordemDeFornecimento.setNumero(ordemFornecimentoDTO.getOrdemDeFornecimento().getNumero());
        }
        ordemDeFornecimento.setNumero(ordemFornecimentoDTO.getOrdemDeFornecimento().getNumero());
        ordemDeFornecimento.setValorUstibb(ordemFornecimentoDTO.getOrdemDeFornecimento().getValorUstibb());
        ordemDeFornecimento.setGestorDaOf(ordemFornecimentoDTO.getOrdemDeFornecimento().getGestorDaOf());
    }

    private List<String> getListaLocalArtefatosTextArea(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        return Arrays.stream(ordemFornecimentoDTO.getListaDosArquivos().split("\n"))
            .filter(s -> s.length() > 5)
            .map(s -> s = s.replace("\\", "/").replace("\"", "").replace("'", ""))
            .collect(Collectors.toList());
    }

    private void removerArtefatosDaLista(OrdemDeFornecimento ordemDeFornecimento, List<String> listaLocalArtefatosTextArea, List<ArtefatoOrdemDeFornecimento> listaArtefatosDaOrdemDeFornecimento) {
        List<ArtefatoOrdemDeFornecimento> listaParaRemover = listaArtefatosDaOrdemDeFornecimento
            .stream()
            .filter(artefatoOrdemDeFornecimento -> !listaLocalArtefatosTextArea.contains(artefatoOrdemDeFornecimento.getArtefato().getLocalDoArtefato()))
            .collect(Collectors.toList());
        listaParaRemover.forEach(ordemDeFornecimento::removeArtefatoOrdemDeFornecimento);
        artefatoOrdemDeFornecimentoRepository.deleteAll(listaParaRemover);
    }

    private void adicionarArtefatosNaLista(OrdemDeFornecimento ordemDeFornecimento, List<String> listaLocalArtefatosTextArea, List<ArtefatoOrdemDeFornecimento> listaArtefatosDaOrdemDeFornecimento) {
        List<ArtefatoOrdemDeFornecimento> listaParaAdicionar = listaLocalArtefatosTextArea
            .stream()
            .filter(s -> !listaArtefatosDaOrdemDeFornecimento.stream().map(ArtefatoOrdemDeFornecimento::getArtefato).map(Artefato::getLocalDoArtefato).collect(Collectors.toList()).contains(s))
            .map(localDoArtefato -> {

                Artefato artefato = artefatoRepository.findByLocalDoArtefato(localDoArtefato)
                    .orElseGet(() -> {
                        Artefato artefato1 = new Artefato();
                        artefato1.setArtefatoDeTest(false);
                        artefato1.setLocalDoArtefato(localDoArtefato);
                        artefato1.setExtensao(FilenameUtils.getExtension(localDoArtefato).toLowerCase());
                        return artefato1;
                    });

                ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = new ArtefatoOrdemDeFornecimento();
                artefatoOrdemDeFornecimento.setArtefato(artefato);
                artefatoOrdemDeFornecimento.setEstado(artefato.getId() == null ? EstadoArtefato.CRIANDO : EstadoArtefato.ALTERANDO);

                if (artefato.getId() == null) {
                    artefatoRepository.save(artefato);
                }

                return artefatoOrdemDeFornecimento;
            })
            .collect(Collectors.toList());
        listaParaAdicionar.forEach(ordemDeFornecimento::addArtefatoOrdemDeFornecimento);
        artefatoOrdemDeFornecimentoRepository.saveAll(listaParaAdicionar);
    }

    public void updateIsTestArquivo(ArtefatoDTO artefatoDTO, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        artefatoRepository.findById(artefatoDTO.getId()).ifPresent(artefato -> {
            artefato.setComplexidade(null);
            artefato.setArtefatoDeTest(artefatoDTO.isArtefatoDeTest());
            artefatoRepository.save(artefato);

            ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).ifPresent(ordemDeFornecimento -> {
                ordemDeFornecimento.setLastModifiedDate(Instant.now());
                ordemDeFornecimentoRepository.save(ordemDeFornecimento);
            });
        });
    }

    public void updateComplexidade(ArtefatoDTO artefatoDTO, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        artefatoRepository.findById(artefatoDTO.getId()).ifPresent(arquivo -> {
            arquivo.setComplexidade(artefatoDTO.getComplexidade());
            artefatoRepository.save(arquivo);

            ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).ifPresent(ordemDeFornecimento -> {
                ordemDeFornecimento.setLastModifiedDate(Instant.now());
                ordemDeFornecimentoRepository.save(ordemDeFornecimento);
            });
        });
    }

    public void updateEstadoArquivo(ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        artefatoOrdemDeFornecimentoRepository.findById(artefatoOrdemDeFornecimentoDTO.getId()).ifPresent(artefatoOrdemDeFornecimento -> {
            artefatoOrdemDeFornecimento.getOrdemDeFornecimento().setLastModifiedDate(Instant.now());
            artefatoOrdemDeFornecimento.setEstado(artefatoOrdemDeFornecimentoDTO.getEstado());
            artefatoOrdemDeFornecimentoRepository.save(artefatoOrdemDeFornecimento);

            ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).ifPresent(ordemDeFornecimento -> {
                ordemDeFornecimento.setLastModifiedDate(Instant.now());
                ordemDeFornecimentoRepository.save(ordemDeFornecimento);
            });
        });
    }

    public OrdemFornecimentoDTO deletarArquivoDaOf(Long id) {
        return artefatoOrdemDeFornecimentoRepository.findById(id)
            .flatMap(artefatoOrdemDeFornecimento1 -> {
                OrdemDeFornecimento ordemDeFornecimento = artefatoOrdemDeFornecimento1.getOrdemDeFornecimento();

                validarOfPertencenteDeUsuario(ordemDeFornecimento.getId());

                ordemDeFornecimento.removeArtefatoOrdemDeFornecimento(artefatoOrdemDeFornecimento1);
                ordemDeFornecimento.setLastModifiedDate(Instant.now());
                ordemDeFornecimentoRepository.save(ordemDeFornecimento);

                return findOneOrdemFornecimento(ordemDeFornecimento.getId());
            }).orElse(null);
    }

    public void updateValorUstibb(BigDecimal valorUstibb, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).ifPresent(ordemDeFornecimento -> {
            ordemDeFornecimento.setValorUstibb(valorUstibb);
            ordemDeFornecimentoRepository.save(ordemDeFornecimento);
        });
    }

    public XSSFWorkbook produzirConteudoDaPlanilha(OrdemFornecimentoDTO ordemFornecimento) throws IOException {
        String localDoArquivo =
            Optional.of(System.getProperty("os.name"))
                .filter(s -> s.toLowerCase().contains("windows"))
                .map(s -> "src/main/resources/templates/OF-nova.xlsx")
                .orElse("/app/resources/templates/OF-nova.xlsx");

        InputStream planilha = new FileInputStream(localDoArquivo);

        XSSFWorkbook workbook = new XSSFWorkbook(planilha);

        Sheet sheet = workbook.getSheetAt(1);

        Stream<Row> targetStream = StreamSupport.stream(sheet.spliterator(), false);

        List<List<Cell>> listsCell = targetStream
            .skip(3)
            .map(cells -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(cells.cellIterator(), Spliterator.ORDERED), false).collect(Collectors.toList()))
            .collect(Collectors.toList());

        List<EstruturaDoArquivo> estruturaDoNegocioArquivoDaOf = getEstruturaDoNegocioArquivoDaOf(ordemFornecimento)
            .stream()
            .filter(estruturaDoArquivo -> estruturaDoArquivo.getDescricaoArtefato() != null)
            .collect(Collectors.toList());
        estruturaDoNegocioArquivoDaOf.forEach(estruturaDoArquivo -> {
            List<Cell> cells = listsCell.get(estruturaDoNegocioArquivoDaOf.indexOf(estruturaDoArquivo));
            cells.get(2).setCellValue(estruturaDoArquivo.getDisciplina());
            cells.get(3).setCellValue(estruturaDoArquivo.getAtividade());
            cells.get(4).setCellValue(estruturaDoArquivo.getDescricaoArtefato());
            cells.get(6).setCellValue(estruturaDoArquivo.getComplexidade());
            cells.get(7).setCellValue(estruturaDoArquivo.getComponenteItem());
            cells.get(10).setCellValue(estruturaDoArquivo.getQtd());
            cells.get(11).setCellValue(estruturaDoArquivo.getStringNomeDoArtefato());
        });

        return workbook;
    }

    public String produzirConteudoDoTxt(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        return ordemFornecimentoDTO.getListaDosArquivos().replace("\n", ";;");
    }

    public String produzirConteudoDoTxt2(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        StringJoiner stringJoiner = new StringJoiner("\n");

        List<EstruturaDoArquivo> estruturaDoNegocioArquivoDaOf = getEstruturaDoNegocioArquivoDaOf(ordemFornecimentoDTO);
        List<EstruturaDoArquivo> estruturaDoNegocioArquivoTests = estruturaDoNegocioArquivoDaOf
            .stream()
            .filter(estruturaDoArquivo -> estruturaDoArquivo.getDescricaoArtefato() != null &&
                estruturaDoArquivo.getDescricaoArtefato().equals(DescricaoArtefato.CRIAR_TEST.getDescricao()))
            .collect(Collectors.toList());
        estruturaDoNegocioArquivoDaOf.removeAll(estruturaDoNegocioArquivoTests);

        Multimap<String, EstruturaDoArquivo> mapComplexidadesArquivos = ArrayListMultimap.create();
        Multimap<String, EstruturaDoArquivo> mapTestsArquivos = ArrayListMultimap.create();
        estruturaDoNegocioArquivoDaOf.forEach(estruturaDoArquivo -> mapComplexidadesArquivos.put(estruturaDoArquivo.getComplexidade(), estruturaDoArquivo));
        estruturaDoNegocioArquivoTests.forEach(estruturaDoArquivo -> mapTestsArquivos.put(estruturaDoArquivo.getComplexidade(), estruturaDoArquivo));

        preparaConteudoArquivoTxt(stringJoiner, mapComplexidadesArquivos);
        if (!mapTestsArquivos.isEmpty()) {
            stringJoiner.add("<<<<<<<< - ARQUIVOS DE TEST - >>>>>>>>");
            preparaConteudoArquivoTxt(stringJoiner, mapTestsArquivos);
        }

        return stringJoiner.toString();
    }

    private void preparaConteudoArquivoTxt(StringJoiner stringJoiner, Multimap<String, EstruturaDoArquivo> multimap) {
        multimap.asMap().forEach((s, estruturaDoArquivos) -> {
            stringJoiner.add("##### - Arquivos de complexidade: " + s + " - #####");
            Multimap<String, String> arquivos = ArrayListMultimap.create();
            estruturaDoArquivos
                .stream()
                .map(EstruturaDoArquivo::getNomeDoArtefato)
                .reduce(new ArrayList<>(), (strings, strings2) -> {
                    strings.addAll(strings2);
                    return strings;
                })
                .forEach(s1 -> arquivos.put(FilenameUtils.getExtension(s1).toLowerCase(), s1));
            arquivos.asMap().forEach((s1, strings) -> {
                stringJoiner.add("## - Arquivos de extensão: " + s1.toUpperCase() + " - ##");
                strings.forEach(stringJoiner::add);
                stringJoiner.add("");
            });
            stringJoiner.add("");
        });
        stringJoiner.add("");
        stringJoiner.add("");
    }

    public List<EstruturaDoArquivo> getEstruturaDoNegocioArquivoDaOf(OrdemFornecimentoDTO ordemFornecimento) {
        List<EstruturaDoArquivo> estruturaDoArquivoList = new ArrayList<>();

        ordemFornecimento.getOrdemDeFornecimento().getArtefatoOrdemDeFornecimentos().forEach(artefatoOrdemDeFornecimento -> {
            EstruturaDoArquivo estruturaDoArquivo = new EstruturaDoArquivo();
            estruturaDoArquivo.setDescricaoArtefato(DescricaoArtefato.get(artefatoOrdemDeFornecimento.getArtefato().isArtefatoDeTest(), artefatoOrdemDeFornecimento.getArtefato().getExtensao(), artefatoOrdemDeFornecimento.getEstado()));
            estruturaDoArquivo.setComplexidade(Optional.ofNullable(artefatoOrdemDeFornecimento.getArtefato().getComplexidade()).map(ComplexidadeArtefato::getDescricao).orElse("N/A"));
            estruturaDoArquivo.addNomeDoArtefato(artefatoOrdemDeFornecimento.getArtefato().getLocalDoArtefato());

            estruturaDoArquivoList.stream()
                .filter(estruturaDoArquivo::equals)
                .findFirst()
                .map(estruturaDoArquivo1 -> {
                    estruturaDoArquivo1.getNomeDoArtefato().addAll(estruturaDoArquivo.getNomeDoArtefato());
                    return estruturaDoArquivo1;
                })
                .orElseGet(() -> {
                    estruturaDoArquivoList.add(estruturaDoArquivo);
                    return estruturaDoArquivo;
                });
        });

        return estruturaDoArquivoList;
    }

    public void salvarNumeroOf(Integer numeroOf, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        ordemDeFornecimentoRepository.findById(ordemDeFornecimentoId).ifPresent(ordemDeFornecimento -> {
            ordemDeFornecimento.setNumero(numeroOf);
            ordemDeFornecimentoRepository.save(ordemDeFornecimento);
        });
    }
}
