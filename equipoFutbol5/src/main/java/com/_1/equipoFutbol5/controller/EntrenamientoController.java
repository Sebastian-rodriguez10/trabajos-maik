package com._1.equipoFutbol5.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.equipoFutbol5.dto.EntrenamientoDTO;
import com._1.equipoFutbol5.dto.HttpGlobalResponse;
import com._1.equipoFutbol5.service.EntrenamientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entrenamiento")
@RequiredArgsConstructor
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;

    @PostMapping("/post-entrenamiento")
    public ResponseEntity<HttpGlobalResponse<EntrenamientoDTO>> guardarEntrenamiento(@Valid @RequestBody EntrenamientoDTO dto) {

        HttpGlobalResponse<EntrenamientoDTO> respuesta = entrenamientoService.guardarEntrenamiento(dto);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/get-titulares")
    public ResponseEntity<List<Object[]>> getTitulares() {

        List<Object[]> response = entrenamientoService.obtenerTitulares();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
