package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.model.ColaboradorFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ColaboradorWebMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorUseCase colaboradorUseCase;
    private final ColaboradorWebMapper mapper;

    public ColaboradorController(ColaboradorUseCase colaboradorUseCase, ColaboradorWebMapper mapper) {
        this.colaboradorUseCase = colaboradorUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorCriado = colaboradorUseCase.criar(colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> buscarPorId(@PathVariable Long id) {
        Colaborador colaborador = colaboradorUseCase.buscarPorId(id);
        ColaboradorResponse response = mapper.toResponse(colaborador);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> listarTodos() {
        
        List<Colaborador> colaboradores = colaboradorUseCase.listarTodos();
        List<ColaboradorResponse> response = colaboradores.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ColaboradorResponse>> buscarPorFiltros(
            @ModelAttribute ColaboradorFilterRequest filtrosRequest,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        ColaboradorFilter filtros = mapper.toDomain(filtrosRequest);
        Page<Colaborador> colaboradores = colaboradorUseCase.buscarPorFiltros(filtros, pageable);
        Page<ColaboradorResponse> response = mapper.toPageResponse(colaboradores);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorAtualizado = colaboradorUseCase.atualizar(id, colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        colaboradorUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 