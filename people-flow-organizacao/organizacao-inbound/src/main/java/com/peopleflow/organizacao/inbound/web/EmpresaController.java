package com.peopleflow.organizacao.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.input.EmpresaUseCase;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaResponse;
import com.peopleflow.organizacao.inbound.web.mapper.EmpresaWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Cadastro de Empresas")
public class EmpresaController {

    private final EmpresaUseCase empresaUseCase;
    private final EmpresaWebMapper mapper;

    @PostMapping
    @Operation(summary = "Criar nova empresa", description = "Cadastra uma nova empresa")
    public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody EmpresaRequest request) {
        Empresa empresa = mapper.toDomain(request);
        Empresa empresaCriado = empresaUseCase.criar(empresa);
        EmpresaResponse response = mapper.toResponse(empresaCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
        Empresa empresa = mapper.toDomain(request);
        Empresa empresaAtualizada = empresaUseCase.atualizar(id, empresa);
        EmpresaResponse response = mapper.toResponse(empresaAtualizada);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por ID", description = "Retorna os dados de uma empresa específico")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id) {
        Empresa empresa = empresaUseCase.buscarPorId(id);
        EmpresaResponse response = mapper.toResponse(empresa);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar empresas com filtros e paginação",
            description = "Busca empresas aplicando filtros opcionais e paginação. " +
                    "Sempre retorna resultados paginados para garantir performance. " +
                    "Use parâmetros de query para filtrar e ordenar os resultados."
    )
    public ResponseEntity<Page<EmpresaResponse>> buscarPorFiltros(
            @ModelAttribute EmpresaFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        EmpresaFilter filtros = mapper.toDomain(filtrosRequest);

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

        PagedResult<Empresa> resultado = empresaUseCase.buscarPorFiltros(filtros, pagination);
        Page<EmpresaResponse> response = mapper.toPageResponse(resultado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar empresa", description = "Altera o status do empresa para ATIVO")
    public ResponseEntity<EmpresaResponse> ativar(@PathVariable Long id) {
        Empresa ativado = empresaUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar empresa", description = "Altera o status da empresa para INATIVO")
    public ResponseEntity<EmpresaResponse> inativar(@PathVariable Long id) {
        Empresa inativado = empresaUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }


    @PatchMapping("/{id}/excluir")
    @Operation(summary = "Excluir empresa", description = "Marca a empresa como excluído (soft delete)")
    public ResponseEntity<EmpresaResponse> excluir(@PathVariable Long id) {
        Empresa excluido = empresaUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }
}
