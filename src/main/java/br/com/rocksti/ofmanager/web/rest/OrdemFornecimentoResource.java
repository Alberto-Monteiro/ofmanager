package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.security.AuthoritiesConstants;
import br.com.rocksti.ofmanager.service.ArquivoDaOfService;
import br.com.rocksti.ofmanager.service.ArquivoService;
import br.com.rocksti.ofmanager.service.ServicoOfService;
import br.com.rocksti.ofmanager.service.UserService;
import br.com.rocksti.ofmanager.service.dto.ArquivoDTO;
import br.com.rocksti.ofmanager.service.dto.ArquivoDaOfDTO;
import br.com.rocksti.ofmanager.service.dto.OrdemFornecimentoDTO;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrdemFornecimentoResource {

    private static final String ENTITY_NAME = "ordemFornecimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicoOfService servicoOfService;

    private final UserService userService;

    private final ArquivoService arquivoService;

    private final ArquivoDaOfService arquivoDaOfService;

    public OrdemFornecimentoResource(ServicoOfService servicoOfService,
                                     UserService userService,
                                     ArquivoService arquivoService,
                                     ArquivoDaOfService arquivoDaOfService) {
        this.servicoOfService = servicoOfService;
        this.userService = userService;
        this.arquivoService = arquivoService;
        this.arquivoDaOfService = arquivoDaOfService;
    }

    @GetMapping("/gerenciador_de_ofs/queryByUser")
    public ResponseEntity<List<ServicoOfDTO>> getAllServicoOfsByUser(Pageable pageable) {
        Page<ServicoOfDTO> page = servicoOfService.findAllByUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/gerenciador_de_ofs/{id}")
    public ResponseEntity<OrdemFornecimentoDTO> getOrdemFornecimento(@PathVariable Long id) {
        validarOfPertencenteDeUsuario(id);

        return ResponseUtil.wrapOrNotFound(servicoOfService.findOneOrdemFornecimento(id));
    }

    @PutMapping("/gerenciador_de_ofs/processar")
    public ResponseEntity<OrdemFornecimentoDTO> processar(@Valid @RequestBody OrdemFornecimentoDTO ordemFornecimentoDTO) {
        validarOfPertencenteDeUsuario(ordemFornecimentoDTO.getId());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordemFornecimentoDTO.toString()))
            .body(servicoOfService.processar(ordemFornecimentoDTO));
    }

    @PutMapping("/gerenciador_de_ofs/updateComplexidade")
    public ResponseEntity<ArquivoDTO> updateComplexidade(@Valid @RequestBody ArquivoDTO arquivoDTO) {
        if (arquivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ArquivoDTO result = arquivoService.updateComplexidade(arquivoDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/gerenciador_de_ofs/updateEstadoArquivo")
    public ResponseEntity<ArquivoDaOfDTO> updateEstadoArquivo(@RequestBody ArquivoDaOfDTO arquivoDaOfDTO) {
        if (arquivoDaOfDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ArquivoDaOfDTO result = arquivoDaOfService.updateEstadoArquivo(arquivoDaOfDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDaOfDTO.getId().toString()))
            .body(result);
    }

    private void validarOfPertencenteDeUsuario(Long idServicoOf) {
        if (idServicoOf != null) {
            userService.getUserWithAuthorities()
                .filter(user -> user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)))
                .ifPresent(user -> {
                    servicoOfService.findOne(idServicoOf).ifPresent(servicoOfDTO -> {
                        if (!servicoOfDTO.getUserid().equals(user.getId())) {
                            throw new BadRequestAlertException("Of pertencente a outro usuário", "servicoOf", "servicoOfNaoPertencente");
                        }
                    });
                });
        }
    }

    @GetMapping("/gerenciador_de_ofs/downloadPlanilha/{idServicoOf}")
    public void downloadPlanilha(HttpServletResponse response, @PathVariable Long idServicoOf) {
        validarOfPertencenteDeUsuario(idServicoOf);

        try {
            String localDoArquivo =
                Optional.of(System.getProperty("os.name"))
                    .filter(s -> s.toLowerCase().contains("windows"))
                    .map(s -> "src/main/resources/templates/OF-nova.xlsx")
                    .orElse("/app/resources/templates/OF-nova.xlsx");

            InputStream planilha = new FileInputStream(localDoArquivo);

            servicoOfService
                .produzirConteudoDaPlanilha(planilha, servicoOfService.findOneOrdemFornecimento(idServicoOf).get())
                .write(response.getOutputStream());

            //org.apache.commons.io.IOUtils.copy(planilha, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}