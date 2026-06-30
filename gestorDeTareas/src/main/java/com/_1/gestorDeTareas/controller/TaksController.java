package com._1.gestorDeTareas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.gestorDeTareas.dto.AllTaksResponseDTO;
import com._1.gestorDeTareas.dto.HttpGlobalResponse;
import com._1.gestorDeTareas.dto.HttpMessageResponse;
import com._1.gestorDeTareas.dto.PathStateRequestDTO;
import com._1.gestorDeTareas.dto.TaksSaveRequestDTO;
import com._1.gestorDeTareas.service.TaksService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/tareas")
@RequiredArgsConstructor
public class TaksController {

    private final TaksService service;

    @GetMapping("/lista")
    public ResponseEntity<HttpGlobalResponse<List<AllTaksResponseDTO>>> getTaks() {
        try {

            HttpGlobalResponse<List<AllTaksResponseDTO>> response = service.getTaks();
            return ResponseEntity.status(200).body(response);   

        } catch (Exception e) {

            HttpGlobalResponse<List<AllTaksResponseDTO>> error = new HttpGlobalResponse<>();
            error.setMessage("Recurso no encontrado");
            error.setSuccess(false);

            return ResponseEntity.status(404).body(error);
        }
    }

    @GetMapping("/buscar-tarea/{id}")
    public ResponseEntity<HttpGlobalResponse<AllTaksResponseDTO>> getOneTaks(@PathVariable Long id) {
        try {

            HttpGlobalResponse<AllTaksResponseDTO> response = service.getOneTaks(id);
            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            HttpGlobalResponse<AllTaksResponseDTO> error = new HttpGlobalResponse<>();
            error.setMessage("Recurso no encontrado");
            error.setSuccess(false);

            return ResponseEntity.status(404).body(error);

        }
    }
    

    @PostMapping("/crear")
    public ResponseEntity<HttpGlobalResponse<TaksSaveRequestDTO>> addTaks(@Valid @RequestBody TaksSaveRequestDTO request) {
        try {
            HttpGlobalResponse<TaksSaveRequestDTO> response = service.addTaks(request);
            return ResponseEntity.status(201).body(response);        
        } catch (Exception e) {
            HttpGlobalResponse<TaksSaveRequestDTO> error = new HttpGlobalResponse<>();
            error.setMessage("Validación fallida o datos incorrectos");
            error.setSuccess(false);

            return ResponseEntity.status(400).body(error);
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpMessageResponse> deleteTaks (@PathVariable Long id){
        try {

            HttpMessageResponse response = service.deleteTaks(id);
            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {

            return ResponseEntity.status(400).body(null);
            
        }
    }

    @PatchMapping("/cambiar-estado/{id}")
    public ResponseEntity<HttpMessageResponse> patchState(@RequestBody PathStateRequestDTO stateRequest, @PathVariable Long id) {
        try {

            HttpMessageResponse response = service.patchState(stateRequest, id);
            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {
            HttpMessageResponse error = new HttpMessageResponse();
            error.setSuccess(false);
            error.setMessage("error en la peticion");

            return ResponseEntity.status(400).body(error);
         
        }
 
    }

    @GetMapping("/listar-estado")
    public ResponseEntity<HttpGlobalResponse<List<AllTaksResponseDTO>>> getTaksState(@RequestParam PathStateRequestDTO requestState) {
        try {

            HttpGlobalResponse<List<AllTaksResponseDTO>> response = service.getTaksState(requestState);
            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    
}
