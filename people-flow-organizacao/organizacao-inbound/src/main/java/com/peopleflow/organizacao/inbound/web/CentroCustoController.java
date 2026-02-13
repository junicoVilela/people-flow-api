package com.peopleflow.organizacao.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.ports.input.CentroCustoUseCase;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoRequest;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoResponse;
import com.peopleflow.organizacao.inbound.web.mapper.CentroCustoWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/centrocusto")
@RequiredArgsConstructor
@Tag(name = "Centro Custo", description = "Cadastro de Centro de Custo")
public class CentroCustoController {

    private final CentroCustoUseCase centroCustoUseCase;
    private final CentroCustoWebMapper mapper;

    @PostMapping
    @Operation(summary = "Criar novo centro de custo", description = "Cadastra um novo centro de custo")
    public ResponseEntity<CentroCustoResponse> criar(@Valid @RequestBody CentroCustoRequest request) {
        CentroCusto centroCusto = mapper.toDomain(request);
        CentroCusto centroCustoCriado = centroCustoUseCase.criar(centroCusto);
        CentroCustoResponse response = mapper.toResponse(centroCustoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar centro de custo", description = "Atualiza os dados de um centro de custo existente")
    public ResponseEntity<CentroCustoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CentroCustoRequest request) {
        CentroCusto centroCusto = mapper.toDomain(request);
        CentroCusto centroCustoAtualizada = centroCustoUseCase.atualizar(id, centroCusto);
        CentroCustoResponse response = mapper.toResponse(centroCustoAtualizada);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar centro de custo por ID", description = "Retorna os dados de uma centro de custo específico")
    public ResponseEntity<CentroCustoResponse> buscarPorId(@PathVariable Long id) {
        CentroCusto centroCusto = centroCustoUseCase.buscarPorId(id);
        CentroCustoResponse response = mapper.toResponse(centroCusto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar centro de custo com filtros e paginação",
            description = "Busca centro de custo aplicando filtros opcionais e paginação. " +
                    "Sempre retorna resultados paginados para garantir performance. " +
                    "Use parâmetros de query para filtrar e ordenar os resultados."
    )
    public ResponseEntity<PagedResult<CentroCustoResponse>> buscarPorFiltros(
            @ModelAttribute CentroCustoFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        CentroCustoFilter filtros = mapper.toDomain(filtrosRequest);

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

        PagedResult<CentroCusto> resultado = centroCustoUseCase.buscarPorFiltros(filtros, pagination);
        PagedResult<CentroCustoResponse> response = mapper.toPagedResponse(resultado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar centro de custo", description = "Altera o status do centro de custo para ATIVO")
    public ResponseEntity<CentroCustoResponse> ativar(@PathVariable Long id) {
        CentroCusto ativado = centroCustoUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar centro de custo", description = "Altera o status da centro de custo para INATIVO")
    public ResponseEntity<CentroCustoResponse> inativar(@PathVariable Long id) {
        CentroCusto inativado = centroCustoUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }


    @PatchMapping("/{id}/excluir")
    @Operation(summary = "Excluir centro de custo", description = "Marca a centro de custo como excluído (soft delete)")
    public ResponseEntity<CentroCustoResponse> excluir(@PathVariable Long id) {
        CentroCusto excluido = centroCustoUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }
}
