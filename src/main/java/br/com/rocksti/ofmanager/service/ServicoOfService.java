package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.Arquivo;
import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import br.com.rocksti.ofmanager.domain.ServicoOf;
import br.com.rocksti.ofmanager.domain.enumeration.Complexidade;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;
import br.com.rocksti.ofmanager.repository.ArquivoRepository;
import br.com.rocksti.ofmanager.repository.ServicoOfRepository;
import br.com.rocksti.ofmanager.security.AuthoritiesConstants;
import br.com.rocksti.ofmanager.service.dto.OrdemFornecimentoDTO;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;
import br.com.rocksti.ofmanager.service.mapper.ServicoOfMapper;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import liquibase.util.file.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Service Implementation for managing {@link ServicoOf}.
 */
@Service
@Transactional
public class ServicoOfService {

    private final Logger log = LoggerFactory.getLogger(ServicoOfService.class);

    private final ServicoOfRepository servicoOfRepository;

    private final ServicoOfMapper servicoOfMapper;

    private final UserService userService;

    private final ArquivoRepository arquivoRepository;

    public ServicoOfService(ServicoOfRepository servicoOfRepository,
                            ServicoOfMapper servicoOfMapper,
                            UserService userService,
                            ArquivoRepository arquivoRepository) {
        this.servicoOfRepository = servicoOfRepository;
        this.servicoOfMapper = servicoOfMapper;
        this.userService = userService;
        this.arquivoRepository = arquivoRepository;
    }

    /**
     * Save a servicoOf.
     *
     * @param servicoOfDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicoOfDTO save(ServicoOfDTO servicoOfDTO) {
        log.debug("Request to save ServicoOf : {}", servicoOfDTO);
        ServicoOf servicoOf = servicoOfMapper.toEntity(servicoOfDTO);
        servicoOf = servicoOfRepository.save(servicoOf);
        return servicoOfMapper.toDto(servicoOf);
    }

    /**
     * Get all the servicoOfs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicoOfDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServicoOfs");
        return servicoOfRepository.findAll(pageable)
            .map(servicoOfMapper::toDto);
    }

    /**
     * Get one servicoOf by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicoOfDTO> findOne(Long id) {
        log.debug("Request to get ServicoOf : {}", id);
        return servicoOfRepository.findById(id)
            .map(servicoOfMapper::toDto);
    }

    /**
     * Delete the servicoOf by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ServicoOf : {}", id);

        userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)))
            .map(user -> {
                servicoOfRepository.deleteById(id);
                return Optional.of(user);
            })
            .orElseGet(() -> {
                servicoOfRepository.findById(id)
                    .filter(servicoOf -> userService.getUserWithAuthorities().filter(user -> user.getId().equals(servicoOf.getUserid())).isPresent())
                    .map(servicoOf -> {
                        servicoOfRepository.deleteById(id);
                        return Optional.of(servicoOf);
                    })
                    .orElseGet(() -> {
                        throw new BadRequestAlertException("Of pertencente a outro usuário", "servicoOf", "servicoOfNaoPertencente");
                    });
                return Optional.empty();
            });
    }

    @Transactional(readOnly = true)
    public Page<ServicoOfDTO> findAllByUser(Pageable pageable) {
        return userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)))
            .map(user -> servicoOfRepository.findAllByUseridEquals(pageable, user.getId()).map(servicoOfMapper::toDto))
            .orElse(servicoOfRepository.findAll(pageable).map(servicoOfMapper::toDto));
    }

    public Optional<OrdemFornecimentoDTO> findOneOrdemFornecimento(Long id) {
        return servicoOfRepository.findById(id).map(servicoOf1 -> {
            OrdemFornecimentoDTO ordemFornecimentoDTO = new OrdemFornecimentoDTO();
            ordemFornecimentoDTO.setId(servicoOf1.getId());
            ordemFornecimentoDTO.setNumero(servicoOf1.getNumero());
            StringJoiner stringJoiner = new StringJoiner("\n");
            servicoOf1.getArquivoDaOfs()
                .stream()
                .map(arquivoDaOf -> arquivoDaOf.getArquivo().getCaminhoDoArquivo())
                .forEach(stringJoiner::add);
            ordemFornecimentoDTO.setListaDosArquivos(stringJoiner.toString());
            ordemFornecimentoDTO.setArquivoDaOfs(servicoOf1.getArquivoDaOfs());
            return ordemFornecimentoDTO;
        });
    }

    public OrdemFornecimentoDTO processar(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        final AtomicReference<ServicoOf> servicoOf = new AtomicReference<>(new ServicoOf());

        montarServicoOf(ordemFornecimentoDTO, servicoOf);

        List<String> listaCaminhoDosArquivos = getListaCaminhoDosArquivos(ordemFornecimentoDTO);

        List<Arquivo> listaArquivosQueExistem = arquivoRepository.findByCaminhoDoArquivoIn(listaCaminhoDosArquivos);

        List<Arquivo> listaArquivosNovos = getListaArquivosNovos(listaCaminhoDosArquivos, listaArquivosQueExistem);

        popularArquivosExistentes(servicoOf, listaArquivosQueExistem);

        popularArquivosNovos(servicoOf, listaArquivosNovos);

        retirarArquivosQueNaoEstemMaisNalista(servicoOf, listaArquivosQueExistem, listaArquivosNovos);

        servicoOfRepository.save(servicoOf.get());

        return findOneOrdemFornecimento(servicoOf.get().getId()).get();
    }

    private void montarServicoOf(OrdemFornecimentoDTO ordemFornecimentoDTO, AtomicReference<ServicoOf> servicoOf) {
        if (ordemFornecimentoDTO.getId() != null) {
            servicoOf.set(servicoOfRepository.findById(ordemFornecimentoDTO.getId()).map(servicoOf1 -> {
                servicoOf1.setNumero(ordemFornecimentoDTO.getNumero());
                return servicoOf1;
            }).orElseGet(ServicoOf::new));
        } else {
            servicoOf.get().setNumero(ordemFornecimentoDTO.getNumero());
            userService.getUserWithAuthorities().ifPresent(user -> servicoOf.get().setUserid(user.getId()));
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
                arquivo.setExtensao(FilenameUtils.getExtension(caminhoDoArquivo));
                return arquivo;
            })
            .collect(Collectors.toList());
    }

    private void popularArquivosExistentes(AtomicReference<ServicoOf> servicoOf, List<Arquivo> listaArquivosQueExistem) {
        listaArquivosQueExistem.stream()
            .filter(arquivo -> !servicoOf.get().getArquivoDaOfs().stream().map(ArquivoDaOf::getArquivo).collect(Collectors.toList()).contains(arquivo))
            .forEach(arquivo -> {
                ArquivoDaOf arquivoDaOf = new ArquivoDaOf();
                arquivoDaOf.setArquivo(arquivo);
                arquivoDaOf.setEstadoArquivo(EstadoArquivo.ALTERANDO);

                arquivo.addArquivoDaOf(arquivoDaOf);

                servicoOf.get().addArquivoDaOf(arquivoDaOf);
            });
    }

    private void popularArquivosNovos(AtomicReference<ServicoOf> servicoOf, List<Arquivo> listaArquivosNovos) {
        listaArquivosNovos.stream()
            .filter(arquivo -> !servicoOf.get().getArquivoDaOfs().stream().map(ArquivoDaOf::getArquivo).collect(Collectors.toList()).contains(arquivo))
            .forEach(arquivo -> {
                ArquivoDaOf arquivoDaOf = new ArquivoDaOf();
                arquivoDaOf.setArquivo(arquivo);
                arquivoDaOf.setEstadoArquivo(EstadoArquivo.CRIANDO);

                arquivo.addArquivoDaOf(arquivoDaOf);

                servicoOf.get().addArquivoDaOf(arquivoDaOf);
            });
    }

    private void retirarArquivosQueNaoEstemMaisNalista(AtomicReference<ServicoOf> servicoOf, List<Arquivo> listaArquivosQueExistem, List<Arquivo> listaArquivosNovos) {
        List<ArquivoDaOf> arquivosParaRemover = servicoOf.get().getArquivoDaOfs()
            .stream()
            .filter(arquivoDaOf -> !(listaArquivosNovos.contains(arquivoDaOf.getArquivo()) || listaArquivosQueExistem.contains(arquivoDaOf.getArquivo())))
            .collect(Collectors.toList());
        arquivosParaRemover.forEach(arquivoDaOf -> servicoOf.get().removeArquivoDaOf(arquivoDaOf));
    }

    public XSSFWorkbook produzirConteudoDaPlanilha(InputStream planilha, OrdemFornecimentoDTO ordemFornecimento) throws IOException {
        validarDados(ordemFornecimento);

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

    private void validarDados(OrdemFornecimentoDTO ordemFornecimentoDTO) {
        if (ordemFornecimentoDTO.getArquivoDaOfs()
            .stream()
            .anyMatch(arquivoDaOf -> arquivoDaOf.getArquivo().getComplexidade() == null)) {
            throw new BadRequestAlertException("Todos os arquivos devem ter a complexidade selecionada.", "servicoOf", "arquivosDevemTerComplexidadeSelecionada");
        }
    }

    public List<EstruturaDoArquivo> getEstruturaDoNegocioArquivoDaOf(OrdemFornecimentoDTO ordemFornecimento) {
        List<EstruturaDoArquivo> estruturaDoArquivoList = new ArrayList<>();

        ordemFornecimento.getArquivoDaOfs().forEach(arquivoDaOf -> {
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
                    estruturaDoArquivoList.add(estruturaDoArquivo);
                    return estruturaDoArquivo;
                });
        });

        return estruturaDoArquivoList.stream()
            .filter(estruturaDoArquivo -> estruturaDoArquivo.getDescricaoArtefato() != null)
            .collect(Collectors.toList());
    }

    public static class EstruturaDoArquivo {

        private String disciplina = "IMPLEMENTAÇÃO DE SOFTWARE";
        private String atividade = "Plataforma Distribuída";
        private String descricaoArtefato = DescricaoArtefato.ALTERAR_JAVA.getDescricao();
        private String complexidade = Complexidade.BAIXA.getDescricao();
        private String componenteItem = "N/A";
        private List<String> nomeDoArtefato = new ArrayList<>();

        public String getDisciplina() {
            return disciplina;
        }

        public void setDisciplina(String disciplina) {
            this.disciplina = disciplina;
        }

        public String getAtividade() {
            return atividade;
        }

        public void setAtividade(String atividade) {
            this.atividade = atividade;
        }

        public String getDescricaoArtefato() {
            return descricaoArtefato;
        }

        public void setDescricaoArtefato(String descricaoArtefato) {
            this.descricaoArtefato = descricaoArtefato;
        }

        public String getComplexidade() {
            return complexidade;
        }

        public void setComplexidade(String complexidade) {
            this.complexidade = complexidade;
        }

        public String getComponenteItem() {
            return componenteItem;
        }

        public void setComponenteItem(String componenteItem) {
            this.componenteItem = componenteItem;
        }

        public String getQtd() {
            return String.valueOf(nomeDoArtefato.size());
        }

        public List<String> getNomeDoArtefato() {
            return nomeDoArtefato;
        }

        public void setNomeDoArtefato(List<String> nomeDoArtefato) {
            this.nomeDoArtefato = nomeDoArtefato;
        }

        public String getStringNomeDoArtefato() {
            StringJoiner stringJoiner = new StringJoiner("\n");
            nomeDoArtefato.forEach(stringJoiner::add);
            return stringJoiner.toString();
        }

        public void addNomeDoArtefato(String nomeDoArtefato) {
            this.nomeDoArtefato.add(nomeDoArtefato);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EstruturaDoArquivo)) return false;
            EstruturaDoArquivo that = (EstruturaDoArquivo) o;
            return com.google.common.base.Objects.equal(descricaoArtefato, that.descricaoArtefato) &&
                com.google.common.base.Objects.equal(complexidade, that.complexidade);
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(descricaoArtefato, complexidade);
        }
    }

    public enum DescricaoArtefato {
        CRIAR_JAVA("Criação de objetos de Integração e Negócio Java  "),
        ALTERAR_JAVA("Alteração de Objetos de Integração e Negócio Java "),
        ALTERAR_JAVA2("Alteração de pacote de Objetos de Integração e Negócio Java "),
        CRIAR_HTML("Criação de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou \nAWT ou XUI "),
        ALTERAR_HTML("Alteração de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou \nAWT ou XUI "),
        CRIAR_CSS("Criação CSS ou SCSS "),
        ALTERAR_CSS("Alteração CSS ou SCSS "),
        CRIAR_JS("Criação JavaScript "),
        ALTERAR_JS("Alteração JavaScript "),
        CRIAR_XML("Criação de arquivo chave/valor ou tipo xml "),
        ALTERAR_XML("Alteração de arquivo chave/valor ou tipo xml "),
        CRIAR_C("Criação de objetos de Integração e Negócio C, C# e C++"),
        ALTERAR_C("Criação de objetos de Integração e Negócio C, C# e C++"),
        ALTERAR_C2("Alteração de pacote de Objetos de Integração e Negócio C, C# e C++"),
        CRIAR_NET("Criação de objetos de Integração e Negócio .Net "),
        ALTERAR_NET("Alteração de Objetos de Integração e Negócio .Net "),
        ALTERAR_NET2("Alteração de pacote de Objetos de Integração e Negócio .Net "),
        CRIAR_TEST("Criação de objeto de teste automatizado (não considerar o teste unitário previsto no PDSTI) ");

        private String descricao;

        DescricaoArtefato(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static String get(String extensao, EstadoArquivo estadoArquivo) {
            if (extensao == null || estadoArquivo == null) {
                return null;
            }

            switch (extensao.toLowerCase()) {
                case "java":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JAVA.getDescricao() : ALTERAR_JAVA.getDescricao();
                case "html":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_HTML.getDescricao() : ALTERAR_HTML.getDescricao();
                case "ts":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
                case "js":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
                case "css":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
                case "scss":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
                case "json":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
                case "xml":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
                case "yml":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
                case "sql":
                    return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            }

            return null;
        }
    }
}
