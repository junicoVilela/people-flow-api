package com.peopleflow.organizacao.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.input.UnidadeUseCase;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeRequest;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeResponse;
import com.peopleflow.organizacao.inbound.web.mapper.UnidadeWebMapper;
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
@RequestMapping("/api/v1/unidades")
@RequiredArgsConstructor
@Tag(name = "Unidade", description = "Cadastro de Unidades")
public class UnidadeController {

    private final UnidadeUseCase unidadeUseCase;
    private final UnidadeWebMapper mapper;

    @PostMapping
    @Operation(summary = "Criar nova unidade", description = "Cadastra uma nova unidade")
    public ResponseEntity<UnidadeResponse> criar(@Valid @RequestBody UnidadeRequest request) {
        Unidade unidade = mapper.toDomain(request);
        Unidade unidadeCriado = unidadeUseCase.criar(unidade);
        UnidadeResponse response = mapper.toResponse(unidadeCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade", description = "Atualiza os dados de uma unidade existente")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UnidadeRequest request) {
        Unidade unidade = mapper.toDomain(request);
        Unidade unidadeAtualizada = unidadeUseCase.atualizar(id, unidade);
        UnidadeResponse response = mapper.toResponse(unidadeAtualizada);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade por ID", description = "Retorna os dados de uma unidade específico")
    public ResponseEntity<UnidadeResponse> buscarPorId(@PathVariable Long id) {
        Unidade unidade = unidadeUseCase.buscarPorId(id);
        UnidadeResponse response = mapper.toResponse(unidade);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar unidades com filtros e paginação",
            description = "Busca unidades aplicando filtros opcionais e paginação. " +
                    "Sempre retorna resultados paginados para garantir performance. " +
                    "Use parâmetros de query para filtrar e ordenar os resultados."
    )
    public ResponseEntity<PagedResult<UnidadeResponse>> buscarPorFiltros(
            @ModelAttribute UnidadeFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        UnidadeFilter filtros = mapper.toDomain(filtrosRequest);

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

        PagedResult<Unidade> resultado = unidadeUseCase.buscarPorFiltros(filtros, pagination);
        PagedResult<UnidadeResponse> response = mapper.toPagedResponse(resultado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar unidade", description = "Altera o status do unidade para ATIVO")
    public ResponseEntity<UnidadeResponse> ativar(@PathVariable Long id) {
        Unidade ativado = unidadeUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar unidade", description = "Altera o status da unidade para INATIVO")
    public ResponseEntity<UnidadeResponse> inativar(@PathVariable Long id) {
        Unidade inativado = unidadeUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }


    @PatchMapping("/{id}/excluir")
    @Operation(summary = "Excluir unidade", description = "Marca a unidade como excluído (soft delete)")
    public ResponseEntity<UnidadeResponse> excluir(@PathVariable Long id) {
        Unidade excluido = unidadeUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }
}
