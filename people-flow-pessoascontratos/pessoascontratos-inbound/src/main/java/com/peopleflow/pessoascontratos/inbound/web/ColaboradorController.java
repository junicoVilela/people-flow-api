package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import com.peopleflow.pessoascontratos.inbound.web.dto.DemissaoRequest;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ColaboradorWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/colaboradores")
@Tag(name = "Colaboradores", description = "Gerenciamento de colaboradores")
public class ColaboradorController {

    private final ColaboradorUseCase colaboradorUseCase;
    private final ColaboradorWebMapper mapper;

    public ColaboradorController(ColaboradorUseCase colaboradorUseCase, ColaboradorWebMapper mapper) {
        this.colaboradorUseCase = colaboradorUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Criar novo colaborador", description = "Cadastra um novo colaborador no sistema")
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorCriado = colaboradorUseCase.criar(colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar colaborador por ID", description = "Retorna os dados de um colaborador específico")
    public ResponseEntity<ColaboradorResponse> buscarPorId(@PathVariable Long id) {
        Colaborador colaborador = colaboradorUseCase.buscarPorId(id);
        ColaboradorResponse response = mapper.toResponse(colaborador);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar colaboradores com filtros e paginação", 
        description = "Busca colaboradores aplicando filtros opcionais e paginação. " +
                      "Sempre retorna resultados paginados para garantir performance. " +
                      "Use parâmetros de query para filtrar e ordenar os resultados."
    )
    public ResponseEntity<Page<ColaboradorResponse>> buscarPorFiltros(
            @ModelAttribute ColaboradorFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        ColaboradorFilter filtros = mapper.toDomain(filtrosRequest);
        Page<Colaborador> colaboradores = colaboradorUseCase.buscarPorFiltros(filtros, pageable);
        Page<ColaboradorResponse> response = mapper.toPageResponse(colaboradores);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar colaborador", description = "Atualiza os dados de um colaborador existente")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorAtualizado = colaboradorUseCase.atualizar(id, colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar colaborador", description = "Remove permanentemente um colaborador do sistema")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        colaboradorUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/demitir")
    @Operation(summary = "Demitir colaborador", description = "Registra a demissão de um colaborador")
    public ResponseEntity<ColaboradorResponse> demitir(
            @PathVariable Long id, @RequestBody @Valid DemissaoRequest demissaoRequest) {
        Colaborador demitido = colaboradorUseCase.demitir(id, demissaoRequest.getDataDemissao());
        return ResponseEntity.ok(mapper.toResponse(demitido));
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar colaborador", description = "Altera o status do colaborador para ATIVO")
    public ResponseEntity<ColaboradorResponse> ativar(@PathVariable Long id) {
        Colaborador ativado = colaboradorUseCase.ativar(id);
        return ResponseEntity.ok(mapper.toResponse(ativado));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar colaborador", description = "Altera o status do colaborador para INATIVO")
    public ResponseEntity<ColaboradorResponse> inativar(@PathVariable Long id) {
        Colaborador inativado = colaboradorUseCase.inativar(id);
        return ResponseEntity.ok(mapper.toResponse(inativado));
    }

    @PatchMapping("/{id}/excluir")
    @Operation(summary = "Excluir colaborador", description = "Marca o colaborador como excluído (soft delete)")
    public ResponseEntity<ColaboradorResponse> excluir(@PathVariable Long id) {
        Colaborador excluido = colaboradorUseCase.excluir(id);
        return ResponseEntity.ok(mapper.toResponse(excluido));
    }

} 