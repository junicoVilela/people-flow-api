package com.peopleflow.pessoascontratos.inbound.web;

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
import com.peopleflow.pessoascontratos.inbound.web.mapper.ColaboradorWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Tag(name = "Colaboradores", description = "Gerenciamento de colaboradores")
public class ColaboradorController {

    private final ColaboradorUseCase colaboradorUseCase;
    private final ColaboradorWebMapper mapper;
    private final SecurityContextHelper securityHelper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:criar')")
    @Operation(summary = "Criar novo colaborador", description = "Cadastra um novo colaborador no sistema")
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        
        if (colaborador.getEmpresaId() == null) {
            Long empresaId = securityHelper.getEmpresaId();
            if (empresaId != null) {
                colaborador = colaborador.toBuilder().empresaId(empresaId).build();
            }
        }

        Colaborador colaboradorCriado = colaboradorUseCase.criar(colaborador);
        
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
        description = "Busca colaboradores aplicando filtros opcionais e paginação. " +
                      "Sempre retorna resultados paginados para garantir performance. " +
                      "Use parâmetros de query para filtrar e ordenar os resultados."
    )
    public ResponseEntity<PagedResult<ColaboradorResponse>> buscarPorFiltros(
            @ModelAttribute ColaboradorFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        ColaboradorFilter filtros = mapper.toDomain(filtrosRequest);
        
        Pagination pagination = Pagination.of( pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElse(null),
            pageable.getSort().stream()
                .findFirst()
                .map(order -> order.getDirection() == Sort.Direction.ASC ? Pagination.SortDirection.ASC : Pagination.SortDirection.DESC)
                .orElse(Pagination.SortDirection.ASC)
        );
        
        PagedResult<Colaborador> resultado = colaboradorUseCase.buscarPorFiltros(filtros, pagination);
        
        PagedResult<ColaboradorResponse> response = mapper.toPagedResponse(resultado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:atualizar')")
    @Operation(summary = "Atualizar colaborador", description = "Atualiza os dados de um colaborador existente")
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
    @PreAuthorize("hasRole('colaborador:atualizar')")
    @Operation(summary = "Ativar colaborador", description = "Altera o status do colaborador para ATIVO")
    public ResponseEntity<ColaboradorResponse> ativar(@PathVariable Long id) {
        Colaborador ativado = colaboradorUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('colaborador:atualizar')")
    @Operation(summary = "Inativar colaborador", description = "Altera o status do colaborador para INATIVO")
    public ResponseEntity<ColaboradorResponse> inativar(@PathVariable Long id) {
        Colaborador inativado = colaboradorUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }

    @PatchMapping("/{id}/excluir")
    @PreAuthorize("hasRole('colaborador:demitir')")
    @Operation(summary = "Excluir colaborador", description = "Marca o colaborador como excluído (soft delete)")
    public ResponseEntity<ColaboradorResponse> excluir(@PathVariable Long id) {
        Colaborador excluido = colaboradorUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }

} 