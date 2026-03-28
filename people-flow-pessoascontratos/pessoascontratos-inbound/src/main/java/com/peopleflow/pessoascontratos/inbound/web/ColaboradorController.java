package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PageablePagination;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.security.SecurityContextHelper;
import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import com.peopleflow.pessoascontratos.inbound.web.dto.DemissaoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ReativacaoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.TransferenciaRequest;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ColaboradorWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colaboradores")
@RequiredArgsConstructor
@Tag(
        name = "Colaboradores",
        description = "Gerenciamento de colaboradores. Todas as listagens paginadas usam os parâmetros Spring Data "
                + "(`page`, `size`, `sort`); o front pode enviar `sort` em todas elas."
)
public class ColaboradorController {

    private final ColaboradorUseCase colaboradorUseCase;
    private final ColaboradorWebMapper mapper;
    private final SecurityContextHelper securityHelper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:criar')")
    @Operation(
        summary = "Criar novo colaborador",
        description = "Cadastra um novo colaborador. Status inicial é sempre ATIVO. " +
                      "Para alterar o status use os endpoints PATCH dedicados."
    )
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);

        if (colaborador.getEmpresaId() == null) {
            Long empresaId = securityHelper.getEmpresaId();
            if (empresaId != null) {
                colaborador = colaborador.toBuilder().empresaId(empresaId).build();
            }
        }

        boolean requerAcesso = Boolean.TRUE.equals(request.getRequerAcessoSistema());
        Colaborador colaboradorCriado = colaboradorUseCase.criar(colaborador, requerAcesso);

        ColaboradorResponse response = mapper.toResponse(colaboradorCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar colaborador por ID", description = "Retorna os dados de um colaborador específico")
    public ResponseEntity<ColaboradorResponse> buscarPorId(@PathVariable Long id) {
        Colaborador colaborador = colaboradorUseCase.buscarPorId(id);
        ColaboradorResponse response = mapper.toResponse(colaborador);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(
        summary = "Listar colaboradores com filtros e paginação", 
        description = "Busca colaboradores aplicando filtros opcionais (modelo de query) e paginação Spring Data "
                      + "(`page`, `size`, `sort`). Apenas o primeiro critério de ordenação é aplicado no domínio. "
                      + "Padrão sem `sort`: nome ascendente."
    )
    public ResponseEntity<PagedResult<ColaboradorResponse>> buscarPorFiltros(
            @ModelAttribute ColaboradorFilterRequest filtrosRequest,
            @PageableDefault(size = Pagination.DEFAULT_PAGE_SIZE, sort = "nome") Pageable pageable) {

        ColaboradorFilter filtros = mapper.toDomain(filtrosRequest);

        Pagination pagination = PageablePagination.from(pageable);
        PagedResult<Colaborador> resultado = colaboradorUseCase.buscarPorFiltros(filtros, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(
        summary = "Atualizar colaborador",
        description = "Atualiza os dados cadastrais do colaborador. " +
                      "empresaId é imutável — deve repetir o valor original. " +
                      "Status não é alterado por este endpoint; use PATCH /ativar, /inativar, /demitir ou /excluir."
    )
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        
        if (colaborador.getEmpresaId() == null) {
            Long empresaId = securityHelper.getEmpresaId();
            if (empresaId != null) {
                colaborador = colaborador.toBuilder().empresaId(empresaId).build();
            }
        }
        
        Colaborador colaboradorAtualizado = colaboradorUseCase.atualizar(id, colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorAtualizado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/demitir")
    @PreAuthorize("hasRole('colaborador:demitir')")
    @Operation(summary = "Demitir colaborador", description = "Registra a demissão de um colaborador")
    public ResponseEntity<ColaboradorResponse> demitir(
            @PathVariable Long id, @RequestBody @Valid DemissaoRequest demissaoRequest) {
        Colaborador demitido = colaboradorUseCase.demitir(id, demissaoRequest.getDataDemissao());
        return ResponseEntity.ok(mapper.toResponse(demitido));
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Ativar colaborador", description = "Altera o status do colaborador para ATIVO")
    public ResponseEntity<ColaboradorResponse> ativar(@PathVariable Long id) {
        Colaborador ativado = colaboradorUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Inativar colaborador", description = "Altera o status do colaborador para INATIVO")
    public ResponseEntity<ColaboradorResponse> inativar(@PathVariable Long id) {
        Colaborador inativado = colaboradorUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }

    @PatchMapping("/{id}/excluir")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir colaborador", description = "Marca o colaborador como excluído (soft delete)")
    public ResponseEntity<ColaboradorResponse> excluir(@PathVariable Long id) {
        Colaborador excluido = colaboradorUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }

    @PatchMapping("/{id}/transferir")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(
        summary = "Transferir colaborador",
        description = "Transfere o colaborador para outra empresa ou departamento. " +
                      "O colaborador deve estar com status ATIVO."
    )
    public ResponseEntity<ColaboradorResponse> transferir(
            @PathVariable Long id,
            @Valid @RequestBody TransferenciaRequest request) {
        Colaborador transferido = colaboradorUseCase.transferir(
                id,
                request.getNovaEmpresaId(),
                request.getNovoDepartamentoId(),
                request.getNovoCentroCustoId(),
                request.getDataTransferencia()
        );
        return ResponseEntity.ok(mapper.toResponse(transferido));
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(
        summary = "Reativar colaborador excluído",
        description = "Reativa um colaborador com status EXCLUIDO, registrando nova data de admissão. " +
                      "Diferente de /ativar, que apenas muda o status de inativo para ativo."
    )
    public ResponseEntity<ColaboradorResponse> reativar(
            @PathVariable Long id,
            @Valid @RequestBody ReativacaoRequest request) {
        Colaborador reativado = colaboradorUseCase.reativar(id, request.getNovaDataAdmissao());
        return ResponseEntity.ok(mapper.toResponse(reativado));
    }

}
