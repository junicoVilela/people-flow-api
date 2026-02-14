package com.peopleflow.organizacao.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.ports.input.DepartamentoUseCase;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoRequest;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoResponse;
import com.peopleflow.organizacao.inbound.web.mapper.DepartamentoWebMapper;
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
@RequestMapping("/api/v1/departamento")
@RequiredArgsConstructor
@Tag(name = "Departamento", description = "Cadastro de Departamento")
public class DepartamentoController {

    private final DepartamentoUseCase departamentoUseCase;
    private final DepartamentoWebMapper mapper;
    private final AccessValidatorPort accessValidator;

    @PostMapping
    @Operation(summary = "Criar novo departamento", description = "Cadastra um novo departamento")
    public ResponseEntity<DepartamentoResponse> criar(@Valid @RequestBody DepartamentoRequest request) {
        Departamento departamento = mapper.toDomain(request);
        Departamento departamentoCriado = departamentoUseCase.criar(departamento);
        DepartamentoResponse response = mapper.toResponse(departamentoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar departamento", description = "Atualiza os dados de um departamento existente")
    public ResponseEntity<DepartamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DepartamentoRequest request) {
        Departamento departamento = mapper.toDomain(request);
        Departamento departamentoAtualizada = departamentoUseCase.atualizar(id, departamento);
        DepartamentoResponse response = mapper.toResponse(departamentoAtualizada);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar departamento por ID", description = "Retorna os dados de um departamento específico")
    public ResponseEntity<DepartamentoResponse> buscarPorId(@PathVariable Long id) {
        Departamento departamento = departamentoUseCase.buscarPorId(id);
        DepartamentoResponse response = mapper.toResponse(departamento);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar departamentos com filtros e paginação",
            description = "Lista departamentos com filtros opcionais (empresaId, unidadeId, nome, codigo, status) e paginação. " +
                    "Parâmetros de query: empresaId, unidadeId, nome, codigo, status (ativo/inativo/excluido). " +
                    "Para usuário não-admin, recomenda-se enviar empresaId do escopo permitido."
    )
    public ResponseEntity<PagedResult<DepartamentoResponse>> buscarPorFiltros(
            @ModelAttribute DepartamentoFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        if (!accessValidator.isAdmin() && filtrosRequest.getEmpresaId() == null && accessValidator.getEmpresaIdUsuario() != null) {
            filtrosRequest.setEmpresaId(accessValidator.getEmpresaIdUsuario());
        }
        DepartamentoFilter filtros = mapper.toDomain(filtrosRequest);

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

        PagedResult<Departamento> resultado = departamentoUseCase.buscarPorFiltros(filtros, pagination);
        PagedResult<DepartamentoResponse> response = mapper.toPagedResponse(resultado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar departamento", description = "Altera o status do departamento para ATIVO")
    public ResponseEntity<DepartamentoResponse> ativar(@PathVariable Long id) {
        Departamento ativado = departamentoUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar departamento", description = "Altera o status do departamento para INATIVO")
    public ResponseEntity<DepartamentoResponse> inativar(@PathVariable Long id) {
        Departamento inativado = departamentoUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }


    @PatchMapping("/{id}/excluir")
    @Operation(summary = "Excluir departamento", description = "Marca o departamento como excluído (soft delete)")
    public ResponseEntity<DepartamentoResponse> excluir(@PathVariable Long id) {
        Departamento excluido = departamentoUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }
}
