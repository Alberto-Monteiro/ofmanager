package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.Artefato;
import br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento;
import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;
import br.com.rocksti.ofmanager.domain.User;
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
import br.com.rocksti.ofmanager.service.mapper.ArtefatoMapper;
import br.com.rocksti.ofmanager.service.mapper.ArtefatoOrdemDeFornecimentoMapper;
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

    private final ArtefatoMapper artefatoMapper;

    private final ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository;

    private final ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper;

    public OrdemFornecimentoService(OrdemDeFornecimentoRepository ordemDeFornecimentoRepository,
                                    OrdemDeFornecimentoMapper ordemDeFornecimentoMapper,
                                    UserService userService,
                                    ArtefatoRepository artefatoRepository,
                                    ArtefatoMapper artefatoMapper,
                                    ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository,
                                    ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper) {
        this.ordemDeFornecimentoRepository = ordemDeFornecimentoRepository;
        this.ordemDeFornecimentoMapper = ordemDeFornecimentoMapper;
        this.userService = userService;
        this.artefatoRepository = artefatoRepository;
        this.artefatoMapper = artefatoMapper;
        this.artefatoOrdemDeFornecimentoRepository = artefatoOrdemDeFornecimentoRepository;
        this.artefatoOrdemDeFornecimentoMapper = artefatoOrdemDeFornecimentoMapper;
    }

    private void validarOfPertencenteDeUsuario(Long idOrdemDeFornecimento) {
        if (idOrdemDeFornecimento != null) {
            userService.getUserWithAuthorities()
                .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                    || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
                .ifPresent(user -> ordemDeFornecimentoRepository.findById(idOrdemDeFornecimento).ifPresent(ordemDeFornecimento -> {
                    if (!ordemDeFornecimento.getDonoDaOf().getId().equals(user.getId())) {
                        throw new RuntimeException("Of pertencente a outro usuário");
                    }
                }));
        }
    }

    @Transactional(readOnly = true)
    public Page<OrdemDeFornecimentoDTO> findAllByUser(Pageable pageable, FiltroPesquisaServicoOf filtroPesquisa) {
        return userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities()
                .stream()
                .noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                    || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
            .map(user -> ordemDeFornecimentoRepository.findAllByDonoDaOf_IdEqualsAndNumeroEquals(pageable, user.getId(), filtroPesquisa.getNumeroOF()).map(ordemDeFornecimentoMapper::toDto))
            .orElse(ordemDeFornecimentoRepository.findAllByNumeroEqualsAndGestorDaOf_Id(pageable, filtroPesquisa.getNumeroOF(), Optional.ofNullable(filtroPesquisa.getUsuarioGestor()).map(UserDTO::getId).orElse(null)).map(ordemDeFornecimentoMapper::toDto));
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

    private User limpaDadosUsuario(User donoDaOf) {
        return Optional.of(donoDaOf).map(user -> {
            User user1 = new User();
            user1.setId(user.getId());
            user1.setFirstName(user.getFirstName());
            user1.setLogin(user.getLogin());
            return user1;
        }).get();
    }

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

        userService.getUserWithAuthorities(ordemFornecimentoDTO.getOrdemDeFornecimento().getGestorDaOf().getId()).ifPresent(user -> {
            ordemDeFornecimentoRepository.findById(ordemFornecimentoDTO.getOrdemDeFornecimento().getId()).ifPresent(ordemDeFornecimento -> {
                ordemDeFornecimento.setGestorDaOf(user);
                ordemDeFornecimentoReference.set(ordemDeFornecimentoMapper.toDto(ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento)));
            });
        });

        return findOneOrdemFornecimento(ordemFornecimentoDTO.getOrdemDeFornecimento().getId()).orElseGet(OrdemFornecimentoDTO::new);
    }

    public OrdemDeFornecimentoDTO updateEstadoDaOf(OrdemDeFornecimentoDTO ordemDeFornecimentoDTO) {
        userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
            .ifPresent(user -> {
                throw new RuntimeException("O estado da OF só pode ser editado pelo gestor");
            });

        AtomicReference<OrdemDeFornecimentoDTO> ordemDeFornecimentoReference = new AtomicReference<>();

        ordemDeFornecimentoRepository.findById(ordemDeFornecimentoDTO.getId()).ifPresent(ordemDeFornecimento -> {
            ordemDeFornecimento.setEstado(ordemDeFornecimentoDTO.getEstado());
            ordemDeFornecimentoReference.set(ordemDeFornecimentoMapper.toDto(ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento)));
        });

        return ordemDeFornecimentoReference.get();
    }

    public OrdemFornecimentoDTO processar(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        validarOfPertencenteDeUsuario(ordemFornecimentoDTO.getOrdemDeFornecimento().getId());

        final AtomicReference<OrdemDeFornecimento> ordemDeFornecimento = new AtomicReference<>(ordemDeFornecimentoRepository.findById(Optional.ofNullable(ordemFornecimentoDTO.getOrdemDeFornecimento().getId()).orElse(0L)).orElseGet(OrdemDeFornecimento::new));

        preparaServicoOf(ordemFornecimentoDTO, ordemDeFornecimento);

        List<String> listaCaminhoDosArquivos = getListaCaminhoDosArquivos(ordemFornecimentoDTO);

        List<Artefato> listaArquivosQueExistem = artefatoRepository.findByLocalDoArtefatoIn(listaCaminhoDosArquivos);

        List<Artefato> listaArquivosNovos = getListaArquivosNovos(listaCaminhoDosArquivos, listaArquivosQueExistem);

        prepararArquivosDaOf(ordemDeFornecimento, listaCaminhoDosArquivos, listaArquivosQueExistem, listaArquivosNovos);

        ordemDeFornecimentoRepository.save(ordemDeFornecimento.get());

        return findOneOrdemFornecimento(ordemDeFornecimento.get().getId()).orElseGet(OrdemFornecimentoDTO::new);
    }

    private void preparaServicoOf(OrdemFornecimentoDTO ordemFornecimentoDTO, AtomicReference<OrdemDeFornecimento> ordemDeFornecimento) {
        if (ordemDeFornecimento.get().getId() == null) {
            ordemDeFornecimento.get().setEstado(EstadoOrdemDeFornecimento.NOVA);
            ordemDeFornecimento.get().setDonoDaOf(userService.getUserWithAuthorities().orElse(null));
            ordemDeFornecimento.get().setNumero(ordemFornecimentoDTO.getOrdemDeFornecimento().getNumero());
        }
        ordemDeFornecimento.get().setGestorDaOf(ordemFornecimentoDTO.getOrdemDeFornecimento().getGestorDaOf());
    }

    private List<String> getListaCaminhoDosArquivos(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        return Arrays.stream(ordemFornecimentoDTO.getListaDosArquivos().split("\n"))
            .filter(s -> s.length() > 5)
            .map(s -> s = s.replace("\\", "/").replace("\"", "").replace("'", ""))
            .collect(Collectors.toList());
    }

    private List<Artefato> getListaArquivosNovos(List<String> listaCaminhoDosArquivos, List<Artefato> listaArquivosQueExistem) {
        return listaCaminhoDosArquivos
            .stream()
            .filter(caminhoDoArquivo -> !listaArquivosQueExistem.stream().map(Artefato::getLocalDoArtefato).collect(Collectors.toList()).contains(caminhoDoArquivo))
            .map(localDoArtefato -> {
                Artefato artefato = new Artefato();
                artefato.setArtefatoDeTest(false);
                artefato.setLocalDoArtefato(localDoArtefato);
                artefato.setExtensao(FilenameUtils.getExtension(localDoArtefato).toLowerCase());
                return artefato;
            })
            .collect(Collectors.toList());
    }

    private void prepararArquivosDaOf(AtomicReference<OrdemDeFornecimento> ordemDeFornecimento, List<String> listaCaminhoDosArquivos, List<Artefato> listaArtefatosQueExistem, List<Artefato> listaArtefatosNovos) {
        List<Artefato> listaArtefatosPreparados = listaCaminhoDosArquivos
            .stream()
            .map(s -> listaArtefatosQueExistem
                .stream()
                .filter(artefato -> artefato.getLocalDoArtefato().equals(s))
                .findFirst()
                .orElseGet(() -> listaArtefatosNovos
                    .stream()
                    .filter(artefato -> artefato.getLocalDoArtefato().equals(s))
                    .findFirst()
                    .get()))
            .collect(Collectors.toList());

        ordemDeFornecimento.get().getArtefatoOrdemDeFornecimentos().clear();
        listaArtefatosPreparados
            .forEach(artefato -> {
                ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = new ArtefatoOrdemDeFornecimento();
                artefatoOrdemDeFornecimento.setArtefato(artefato);
                artefatoOrdemDeFornecimento.setEstado(artefato.getId() == null ? EstadoArtefato.CRIANDO : EstadoArtefato.ALTERANDO);

                ordemDeFornecimento.get().addArtefatoOrdemDeFornecimento(artefatoOrdemDeFornecimento);
            });
    }

    public ArtefatoDTO updateIsTestArquivo(ArtefatoDTO artefatoDTO) {
        AtomicReference<Artefato> artefatoReference = new AtomicReference<>();

        artefatoRepository.findById(artefatoDTO.getId()).ifPresent(artefato -> {
            artefato.setArtefatoDeTest(artefatoDTO.isArtefatoDeTest());
            artefatoRepository.save(artefato);
            artefatoReference.set(artefato);
        });

        return artefatoMapper.toDto(artefatoReference.get());
    }

    public OrdemFornecimentoDTO updateComplexidade(ArtefatoDTO artefatoDTO, Long ordemDeFornecimentoId) {
        validarOfPertencenteDeUsuario(ordemDeFornecimentoId);

        AtomicReference<Artefato> artefatoReference = new AtomicReference<>();

        artefatoRepository.findById(artefatoDTO.getId()).ifPresent(arquivo -> {
            arquivo.setComplexidade(artefatoDTO.getComplexidade());
            artefatoRepository.save(arquivo);
            artefatoReference.set(arquivo);
        });

        //return findOneOrdemFornecimento(ordemDeFornecimentoId).orElseGet(OrdemFornecimentoDTO::new);
        return null;
    }

    public ArtefatoOrdemDeFornecimentoDTO updateEstadoArquivo(ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO) {
        AtomicReference<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoReference = new AtomicReference<>();

        artefatoOrdemDeFornecimentoRepository.findById(artefatoOrdemDeFornecimentoDTO.getId()).ifPresent(artefatoOrdemDeFornecimento -> {
            artefatoOrdemDeFornecimento.setEstado(artefatoOrdemDeFornecimentoDTO.getEstado());
            artefatoOrdemDeFornecimentoRepository.save(artefatoOrdemDeFornecimento);
            artefatoOrdemDeFornecimentoReference.set(artefatoOrdemDeFornecimento);
        });

        return artefatoOrdemDeFornecimentoMapper.toDto(artefatoOrdemDeFornecimentoReference.get());
    }

    public OrdemFornecimentoDTO deletarArquivoDaOf(Long id) {
        Optional<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoRepository.findById(id);
        Optional<OrdemDeFornecimento> ordemDeFornecimento = artefatoOrdemDeFornecimento.map(ArtefatoOrdemDeFornecimento::getOrdemDeFornecimento);

        ordemDeFornecimento.ifPresent(ordemDeFornecimento1 -> validarOfPertencenteDeUsuario(ordemDeFornecimento1.getId()));

        return ordemDeFornecimento.flatMap(ordemDeFornecimento1 -> {
            ordemDeFornecimento1.removeArtefatoOrdemDeFornecimento(artefatoOrdemDeFornecimento.get());
            ordemDeFornecimentoRepository.save(ordemDeFornecimento1);
            return findOneOrdemFornecimento(ordemDeFornecimento1.getId());
        }).get();
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
}
