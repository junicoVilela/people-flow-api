package com.peopleflow.organizacao.inbound.web;

import com.peopleflow.common.pagination.PageablePagination;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.ports.input.AreaUseCase;
import com.peopleflow.organizacao.core.query.AreaFilter;
import com.peopleflow.organizacao.inbound.web.dto.AreaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.AreaRequest;
import com.peopleflow.organizacao.inbound.web.dto.AreaResponse;
import com.peopleflow.organizacao.inbound.web.mapper.AreaWebMapper;
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
@RequestMapping("/api/v1/area")
@RequiredArgsConstructor
@Tag(name = "Área", description = "Cadastro de Áreas organizacionais")
public class AreaController {

    private final AreaUseCase areaUseCase;
    private final AreaWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('organizacao:criar')")
    @Operation(summary = "Criar nova área", description = "Cadastra uma nova área organizacional")
    public ResponseEntity<AreaResponse> criar(@Valid @RequestBody AreaRequest request) {
        Area area = mapper.toDomain(request);
        Area areaCriada = areaUseCase.criar(area);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(areaCriada));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('organizacao:editar')")
    @Operation(summary = "Atualizar área", description = "Atualiza os dados de uma área existente")
    public ResponseEntity<AreaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AreaRequest request) {
        Area area = mapper.toDomain(request);
        Area areaAtualizada = areaUseCase.atualizar(id, area);
        return ResponseEntity.ok(mapper.toResponse(areaAtualizada));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('organizacao:ler')")
    @Operation(summary = "Buscar área por ID", description = "Retorna os dados de uma área específica")
    public ResponseEntity<AreaResponse> buscarPorId(@PathVariable Long id) {
        Area area = areaUseCase.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(area));
    }

    @GetMapping
    @PreAuthorize("hasRole('organizacao:ler')")
    @Operation(
            summary = "Listar áreas com filtros e paginação",
            description = "Lista áreas com filtros opcionais (nome, codigo, status) e paginação."
    )
    public ResponseEntity<PagedResult<AreaResponse>> buscarPorFiltros(
            @ModelAttribute AreaFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        AreaFilter filtros = mapper.toDomain(filtrosRequest);

        Pagination pagination = PageablePagination.from(pageable);
        PagedResult<Area> resultado = areaUseCase.buscarPorFiltros(filtros, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('organizacao:editar')")
    @Operation(summary = "Ativar área", description = "Altera o status da área para ATIVO")
    public ResponseEntity<AreaResponse> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(areaUseCase.ativar(id)));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('organizacao:editar')")
    @Operation(summary = "Inativar área", description = "Altera o status da área para INATIVO")
    public ResponseEntity<AreaResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(areaUseCase.inativar(id)));
    }

    @PatchMapping("/{id}/excluir")
    @PreAuthorize("hasRole('organizacao:deletar')")
    @Operation(summary = "Excluir área", description = "Marca a área como excluída (soft delete)")
    public ResponseEntity<AreaResponse> excluir(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(areaUseCase.excluir(id)));
    }
}
