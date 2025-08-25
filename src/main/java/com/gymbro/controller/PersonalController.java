package com.gymbro.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @PostMapping
    public ResponseEntity<PersonalDTO> criar(@Valid @RequestBody PersonalDTO dto) {
        try {
            PersonalDTO criado = personalService.criarPersonal(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(criado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build(); // Email duplicado ou licença inválida
        }
    }

    @GetMapping
    public ResponseEntity<List<PersonalDTO>> listar() {
        return ResponseEntity.ok(personalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDTO> buscarPorId(@PathVariable Long id) {
        return personalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonalDTO> buscarPorEmail(@PathVariable String email) {
        return personalService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDTO> atualizar(@PathVariable Long id,
                                                 @Valid @RequestBody PersonalDTO dto) {
        try {
            PersonalDTO atualizado = personalService.atualizarPersonal(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build(); // Email duplicado ou licença inválida
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            personalService.deletarPersonal(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/formacao")
    public ResponseEntity<List<PersonalDTO>> buscarPorFormacao(@RequestParam("formado") Boolean formado) {
        return ResponseEntity.ok(personalService.buscarPorFormacao(formado));
    }
}