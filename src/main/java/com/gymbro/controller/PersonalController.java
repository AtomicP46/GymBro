package com.gymbro.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gymbro.dto.PersonalDTO;
import com.gymbro.service.PersonalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/personais")
@CrossOrigin(origins = "*")
@Validated
public class PersonalController {

    private final PersonalService personalService;

    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @PostMapping
    public ResponseEntity<PersonalDTO> criar(
            @Valid @RequestBody PersonalDTO dto) {

        PersonalDTO criado = personalService.criarPersonal(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<List<PersonalDTO>> listar() {
        List<PersonalDTO> lista = personalService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDTO> buscarPorId(
            @PathVariable Long id) {

        return personalService
                .buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<PersonalDTO> buscarPorEmail(
            @RequestParam("email") String email) {

        return personalService
                .buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonalDTO dto) {

        PersonalDTO atualizado = personalService.atualizarPersonal(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        personalService.deletarPersonal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/formacao")
    public ResponseEntity<List<PersonalDTO>> buscarPorFormacao(
            @RequestParam("formado") Boolean formado) {

        List<PersonalDTO> filtrados = personalService
                .buscarPorFormacao(formado);
        return ResponseEntity.ok(filtrados);
    }
}