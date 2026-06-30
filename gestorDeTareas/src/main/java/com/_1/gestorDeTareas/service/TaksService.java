package com._1.gestorDeTareas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import com._1.gestorDeTareas.dto.AllTaksResponseDTO;
import com._1.gestorDeTareas.dto.HttpGlobalResponse;
import com._1.gestorDeTareas.dto.HttpMessageResponse;
import com._1.gestorDeTareas.dto.PathStateRequestDTO;
import com._1.gestorDeTareas.dto.TaksSaveRequestDTO;
import com._1.gestorDeTareas.entity.Taks;
import com._1.gestorDeTareas.enums.TaksStateEnum;
import com._1.gestorDeTareas.repository.TaksRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaksService {

    private final TaksRepository repository;

    public HttpGlobalResponse<List<AllTaksResponseDTO>> getTaks() {

        HttpGlobalResponse<List<AllTaksResponseDTO>> response = new HttpGlobalResponse<>();
        List<Taks> taksFound = repository.findAll();
        List<AllTaksResponseDTO> taksList = new ArrayList<>();

        if (taksFound.isEmpty()) {
            response.setMessage("No hay tareas");
            response.setSuccess(false);
            return response;
        }

        for (Taks tak : taksFound) {
            AllTaksResponseDTO dto = new AllTaksResponseDTO();
            dto.setTitulo(tak.getTitle());
            dto.setDescripcion(tak.getDescription());
            dto.setEstado(tak.getState());
            dto.setPrioridad(tak.getPriority());
            dto.setCreationDate(tak.getCreationDate());
            dto.setLastUpdated(tak.getLastUpdated());

            taksList.add(dto);
        }

        response.setData(taksList);
        response.setMessage("LISTA DE TAREAS");
        response.setSuccess(true);

        return response;
    }

    public HttpGlobalResponse<AllTaksResponseDTO> getOneTaks (Long id) {
        Optional<Taks> taksFound = repository.findById(id);
        HttpGlobalResponse<AllTaksResponseDTO> response = new HttpGlobalResponse<>();
        
        if (taksFound.isEmpty()) {
            response.setMessage("Tarea no encontrado");
            response.setSuccess(false);
            return response;
        }
        AllTaksResponseDTO dto = new AllTaksResponseDTO();
        Taks taks = taksFound.get();

        dto.setTitulo(taks.getTitle());
        dto.setDescripcion(taks.getDescription());
        dto.setEstado(taks.getState());
        dto.setPrioridad(taks.getPriority());
        dto.setCreationDate(taks.getCreationDate());
        dto.setLastUpdated(taks.getLastUpdated());

        response.setData(dto);
        response.setMessage("Tarea ecnontrada");
        response.setSuccess(true);

        return response;
    }

    public HttpGlobalResponse<TaksSaveRequestDTO> addTaks(TaksSaveRequestDTO request) {

        HttpGlobalResponse<TaksSaveRequestDTO> response = new HttpGlobalResponse<>();

        Taks taksSave = new Taks();

        taksSave.setTitle(request.getTitulo());
        taksSave.setDescription(request.getDescripcion());
        taksSave.setState(TaksStateEnum.PENDIENTE);
        taksSave.setPriority(request.getPrioridad());
        taksSave.prePersist();
        taksSave.preUpdate();

        repository.save(taksSave);

        response.setData(request);
        response.setMessage("tarea agregada correctamente");
        response.setSuccess(true);

        return response;
    }

    public HttpMessageResponse deleteTaks(Long id) {

        Optional<Taks> taksFound = repository.findById(id);
        HttpMessageResponse response = new HttpMessageResponse();

        if (taksFound.isEmpty()) {
            response.setMessage("El id de la tarea que quiere eliminar no existe");
            response.setSuccess(false);
            return response;
        }

        repository.deleteById(id);

        response.setMessage("Tarea eliminada correctamente");
        response.setSuccess(true);
        return response;
    }

    public HttpMessageResponse patchState(PathStateRequestDTO stateRequest, Long id) {

        Optional<Taks> taksFound = repository.findById(id);
        HttpMessageResponse response = new HttpMessageResponse();
        
        if (taksFound.isEmpty()) {
            response.setMessage("Error tarea no encontrada");
            response.setSuccess(false);
            return response;
        }
        
        Taks tak = taksFound.get();
        
        tak.setState(stateRequest.getEstado());

        repository.save(tak);

        response.setMessage("Estado actualizado correctamente");
        response.setSuccess(true);

        return response;
    }

    public HttpGlobalResponse<List<AllTaksResponseDTO>> getTaksState(PathStateRequestDTO requestState) {

        HttpGlobalResponse<List<AllTaksResponseDTO>> response = new HttpGlobalResponse<>();
        List<Taks> listFound = repository.findByState(requestState.getEstado());
        List<AllTaksResponseDTO> list = new ArrayList<>();

        if (listFound.isEmpty()) {
            response.setMessage("No hay tareas con el estado: " + requestState.getEstado());
            response.setSuccess(false);
        }

        for (Taks taks: listFound) {
            AllTaksResponseDTO dto = new AllTaksResponseDTO();
            dto.setTitulo(taks.getTitle());
            dto.setDescripcion(taks.getDescription());
            dto.setEstado(taks.getState());
            dto.setPrioridad(taks.getPriority());
            dto.setCreationDate(taks.getCreationDate());
            dto.setLastUpdated(taks.getLastUpdated());

            list.add(dto);
        }
        
        response.setData(list);
        response.setMessage("Lista por categoria: " + requestState.getEstado());
        response.setSuccess(true);

        return response;
    }
}
