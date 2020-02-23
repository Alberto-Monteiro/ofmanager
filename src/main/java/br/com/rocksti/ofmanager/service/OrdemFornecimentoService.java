package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.Arquivo;
import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import br.com.rocksti.ofmanager.domain.ServicoOf;
import br.com.rocksti.ofmanager.domain.User;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoOf;
import br.com.rocksti.ofmanager.planilha.DescricaoArtefato;
import br.com.rocksti.ofmanager.planilha.EstruturaDoArquivo;
import br.com.rocksti.ofmanager.repository.ArquivoDaOfRepository;
import br.com.rocksti.ofmanager.repository.ArquivoRepository;
import br.com.rocksti.ofmanager.repository.ServicoOfRepository;
import br.com.rocksti.ofmanager.security.AuthoritiesConstants;
import br.com.rocksti.ofmanager.service.dto.*;
import br.com.rocksti.ofmanager.service.mapper.ArquivoDaOfMapper;
import br.com.rocksti.ofmanager.service.mapper.ArquivoMapper;
import br.com.rocksti.ofmanager.service.mapper.ServicoOfMapper;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import liquibase.util.file.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ServicoOfRepository servicoOfRepository;

    private final ServicoOfMapper servicoOfMapper;

    private final UserService userService;

    private final ArquivoRepository arquivoRepository;

    private final ArquivoMapper arquivoMapper;

    private final ArquivoDaOfRepository arquivoDaOfRepository;

    private final ArquivoDaOfMapper arquivoDaOfMapper;

    public OrdemFornecimentoService(ServicoOfRepository servicoOfRepository,
                                    ServicoOfMapper servicoOfMapper,
                                    UserService userService,
                                    ArquivoRepository arquivoRepository,
                                    ArquivoMapper arquivoMapper,
                                    ArquivoDaOfRepository arquivoDaOfRepository,
                                    ArquivoDaOfMapper arquivoDaOfMapper) {
        this.servicoOfRepository = servicoOfRepository;
        this.servicoOfMapper = servicoOfMapper;
        this.userService = userService;
        this.arquivoRepository = arquivoRepository;
        this.arquivoMapper = arquivoMapper;
        this.arquivoDaOfRepository = arquivoDaOfRepository;
        this.arquivoDaOfMapper = arquivoDaOfMapper;
    }

    private void validarAntesDeProduzirConteudoDaPlanilha(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        if (ordemFornecimentoDTO.getServicoOf().getArquivoDaOfs()
            .stream()
            .anyMatch(arquivoDaOf -> arquivoDaOf.getArquivo().getComplexidade() == null)) {
            throw new BadRequestAlertException("Todos os arquivos devem ter a complexidade selecionada.", "servicoOf", "arquivosDevemTerComplexidadeSelecionada");
        }
    }

    private void validarOfPertencenteDeUsuario(Long idServicoOf) {
        if (idServicoOf != null) {
            userService.getUserWithAuthorities()
                .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                    || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
                .ifPresent(user -> servicoOfRepository.findById(idServicoOf).ifPresent(servicoOf -> {
                    if (!servicoOf.getDonoDaOf().getId().equals(user.getId())) {
                        throw new BadRequestAlertException("Of pertencente a outro usuário", "servicoOf", "servicoOfNaoPertencente");
                    }
                }));
        }
    }

    @Transactional(readOnly = true)
    public Page<ServicoOfDTO> findAllByUser(Pageable pageable) {
        return userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities()
                .stream()
                .noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                    || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
            .map(user -> servicoOfRepository.findAllByDonoDaOf_IdEquals(pageable, user.getId()).map(servicoOfMapper::toDto))
            .orElse(servicoOfRepository.findAll(pageable).map(servicoOfMapper::toDto));
    }

    @Transactional(readOnly = true)
    public Optional<OrdemFornecimentoDTO> findOneOrdemFornecimento(Long id) {
        validarOfPertencenteDeUsuario(id);

        return servicoOfRepository.findById(id)
            .map(servicoOf -> {
                servicoOf.setDonoDaOf(limpaDadosUsuario(servicoOf.getDonoDaOf()));
                servicoOf.setGestorDaOf(limpaDadosUsuario(servicoOf.getGestorDaOf()));
                OrdemFornecimentoDTO ordemFornecimentoDTO = new OrdemFornecimentoDTO();
                ordemFornecimentoDTO.setServicoOf(servicoOf);
                StringJoiner stringJoiner = new StringJoiner("\n");
                servicoOf.getArquivoDaOfs()
                    .stream()
                    .map(arquivoDaOf -> arquivoDaOf.getArquivo().getCaminhoDoArquivo())
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

    public OrdemFornecimentoDTO processar(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        validarOfPertencenteDeUsuario(ordemFornecimentoDTO.getServicoOf().getId());

        final AtomicReference<ServicoOf> servicoOf = new AtomicReference<>(new ServicoOf());

        montarServicoOf(ordemFornecimentoDTO, servicoOf);

        List<String> listaCaminhoDosArquivos = getListaCaminhoDosArquivos(ordemFornecimentoDTO);

        List<Arquivo> listaArquivosQueExistem = arquivoRepository.findByCaminhoDoArquivoIn(listaCaminhoDosArquivos);

        List<Arquivo> listaArquivosNovos = getListaArquivosNovos(listaCaminhoDosArquivos, listaArquivosQueExistem);

        prepararArquivosDaOf(servicoOf, listaCaminhoDosArquivos, listaArquivosQueExistem, listaArquivosNovos);

        servicoOfRepository.save(servicoOf.get());

        return findOneOrdemFornecimento(servicoOf.get().getId()).orElseGet(OrdemFornecimentoDTO::new);
    }

    private void montarServicoOf(OrdemFornecimentoDTO ordemFornecimentoDTO, AtomicReference<ServicoOf> servicoOf) {
        if (ordemFornecimentoDTO.getServicoOf().getId() != null) {
            servicoOfRepository.findById(ordemFornecimentoDTO.getServicoOf().getId())
                .ifPresent(servicoOf1 -> {
                    servicoOf.set(servicoOf1);
                    servicoOf.get().setNumero(ordemFornecimentoDTO.getServicoOf().getNumero());
                    servicoOf.get().setGestorDaOf(ordemFornecimentoDTO.getServicoOf().getGestorDaOf());
                });
        } else {
            userService.getUserWithAuthorities()
                .ifPresent(user -> {
                    servicoOf.get().setEstado(EstadoOf.NOVA);
                    servicoOf.get().setDonoDaOf(user);
                    servicoOf.get().setNumero(ordemFornecimentoDTO.getServicoOf().getNumero());
                    servicoOf.get().setGestorDaOf(ordemFornecimentoDTO.getServicoOf().getGestorDaOf());
                });
        }
    }

    private List<String> getListaCaminhoDosArquivos(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        return Arrays.stream(ordemFornecimentoDTO.getListaDosArquivos().split("\n"))
            .filter(s -> s.length() > 5)
            .map(s -> s = s.replace("\\", "/").replace("\"", "").replace("'", ""))
            .collect(Collectors.toList());
    }

    private List<Arquivo> getListaArquivosNovos(List<String> listaCaminhoDosArquivos, List<Arquivo> listaArquivosQueExistem) {
        return listaCaminhoDosArquivos
            .stream()
            .filter(caminhoDoArquivo -> !listaArquivosQueExistem.stream().map(Arquivo::getCaminhoDoArquivo).collect(Collectors.toList()).contains(caminhoDoArquivo))
            .map(caminhoDoArquivo -> {
                Arquivo arquivo = new Arquivo();
                arquivo.setCaminhoDoArquivo(caminhoDoArquivo);
                arquivo.setExtensao(FilenameUtils.getExtension(caminhoDoArquivo).toLowerCase());
                return arquivo;
            })
            .collect(Collectors.toList());
    }

    private void prepararArquivosDaOf(AtomicReference<ServicoOf> servicoOf, List<String> listaCaminhoDosArquivos, List<Arquivo> listaArquivosQueExistem, List<Arquivo> listaArquivosNovos) {
        List<Arquivo> listaArquivosPreparados = listaCaminhoDosArquivos
            .stream()
            .map(s -> listaArquivosQueExistem
                .stream()
                .filter(arquivo -> arquivo.getCaminhoDoArquivo().equals(s))
                .findFirst()
                .orElseGet(() -> listaArquivosNovos
                    .stream()
                    .filter(arquivo -> arquivo.getCaminhoDoArquivo().equals(s))
                    .findFirst()
                    .get()))
            .collect(Collectors.toList());

        servicoOf.get().getArquivoDaOfs().clear();
        listaArquivosPreparados
            .forEach(arquivo -> {
                ArquivoDaOf arquivoDaOf = new ArquivoDaOf();
                arquivoDaOf.setArquivo(arquivo);
                arquivoDaOf.setEstadoArquivo(arquivo.getId() == null ? EstadoArquivo.CRIANDO : EstadoArquivo.ALTERANDO);

                arquivo.addArquivoDaOf(arquivoDaOf);

                servicoOf.get().addArquivoDaOf(arquivoDaOf);
            });
    }

    public ArquivoDTO updateIsTestArquivo(ArquivoDTO arquivoDTO) {
        AtomicReference<Arquivo> arquivoReference = new AtomicReference<>();

        arquivoRepository.findById(arquivoDTO.getId()).ifPresent(arquivo -> {
            arquivo.setArquivoDeTest(arquivoDTO.isArquivoDeTest());
            arquivoRepository.save(arquivo);
            arquivoReference.set(arquivo);
        });

        return arquivoMapper.toDto(arquivoReference.get());
    }

    public ArquivoDTO updateComplexidade(ArquivoDTO arquivoDTO) {
        AtomicReference<Arquivo> arquivoReference = new AtomicReference<>();

        arquivoRepository.findById(arquivoDTO.getId()).ifPresent(arquivo -> {
            arquivo.setComplexidade(arquivoDTO.getComplexidade());
            arquivoRepository.save(arquivo);
            arquivoReference.set(arquivo);
        });

        return arquivoMapper.toDto(arquivoReference.get());
    }

    public ArquivoDaOfDTO updateEstadoArquivo(ArquivoDaOfDTO arquivoDaOfDTO) {
        AtomicReference<ArquivoDaOf> arquivoDaOfReference = new AtomicReference<>();

        arquivoDaOfRepository.findById(arquivoDaOfDTO.getId()).ifPresent(arquivoDaOf -> {
            arquivoDaOf.setEstadoArquivo(arquivoDaOfDTO.getEstadoArquivo());
            arquivoDaOfRepository.save(arquivoDaOf);
            arquivoDaOfReference.set(arquivoDaOf);
        });

        return arquivoDaOfMapper.toDto(arquivoDaOfReference.get());
    }

    public OrdemFornecimentoDTO deletarArquivoDaOf(Long id) {
        Optional<ArquivoDaOf> arquivoDaOf = arquivoDaOfRepository.findById(id);
        Optional<ServicoOf> servicoOf = arquivoDaOf.map(ArquivoDaOf::getServicoOf);

        return servicoOf.flatMap(servicoOf1 -> {
            servicoOf1.removeArquivoDaOf(arquivoDaOf.get());
            servicoOfRepository.save(servicoOf1);
            return findOneOrdemFornecimento(servicoOf1.getId());
        }).get();
    }

    public XSSFWorkbook produzirConteudoDaPlanilha(InputStream planilha, OrdemFornecimentoDTO ordemFornecimento) throws IOException {
        validarAntesDeProduzirConteudoDaPlanilha(ordemFornecimento);

        XSSFWorkbook workbook = new XSSFWorkbook(planilha);

        Sheet sheet = workbook.getSheetAt(1);

        Stream<Row> targetStream = StreamSupport.stream(sheet.spliterator(), false);

        List<List<Cell>> listsCell = targetStream
            .skip(3)
            .map(cells -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(cells.cellIterator(), Spliterator.ORDERED), false).collect(Collectors.toList()))
            .collect(Collectors.toList());

        List<EstruturaDoArquivo> estruturaDoNegocioArquivoDaOf = getEstruturaDoNegocioArquivoDaOf(ordemFornecimento);
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

    public List<EstruturaDoArquivo> getEstruturaDoNegocioArquivoDaOf(OrdemFornecimentoDTO ordemFornecimento) {
        List<EstruturaDoArquivo> estruturaDoArquivoList = new ArrayList<>();

        ordemFornecimento.getServicoOf().getArquivoDaOfs().forEach(arquivoDaOf -> {
            EstruturaDoArquivo estruturaDoArquivo = new EstruturaDoArquivo();
            estruturaDoArquivo.setDescricaoArtefato(DescricaoArtefato.get(arquivoDaOf.getArquivo().getExtensao(), arquivoDaOf.getEstadoArquivo()));
            estruturaDoArquivo.setComplexidade(arquivoDaOf.getArquivo().getComplexidade().getDescricao());
            estruturaDoArquivo.addNomeDoArtefato(arquivoDaOf.getArquivo().getCaminhoDoArquivo());

            estruturaDoArquivoList.stream()
                .filter(estruturaDoArquivo::equals)
                .findFirst()
                .map(estruturaDoArquivo1 -> {
                    estruturaDoArquivo1.getNomeDoArtefato().addAll(estruturaDoArquivo.getNomeDoArtefato());
                    return estruturaDoArquivo1;
                })
                .orElseGet(() -> {
                    if (estruturaDoArquivo.getDescricaoArtefato() != null) {
                        estruturaDoArquivoList.add(estruturaDoArquivo);
                    }
                    return estruturaDoArquivo;
                });
        });

        return estruturaDoArquivoList;
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

    public ServicoOfDTO updateEstadoDaOf(ServicoOfDTO servicoOfDTO) {
        userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
            .ifPresent(user -> {
                throw new BadRequestAlertException("O estado da OF só pode ser editado pelo gestor", "servicoOf", "servicoOfEstadoEditadoGestor");
            });

        AtomicReference<ServicoOfDTO> servicoOfReference = new AtomicReference<>();

        servicoOfRepository.findById(servicoOfDTO.getId()).ifPresent(servicoOf -> {
            servicoOf.setEstado(servicoOfDTO.getEstado());
            servicoOfReference.set(servicoOfMapper.toDto(servicoOfRepository.saveAndFlush(servicoOf)));
        });

        return servicoOfReference.get();
    }
}
