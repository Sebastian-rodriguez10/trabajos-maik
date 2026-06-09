package com._1.equipoFutbol5.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com._1.equipoFutbol5.dto.EntrenamientoDTO;
import com._1.equipoFutbol5.dto.HttpGlobalResponse;
import com._1.equipoFutbol5.entity.Entrenamiento;
import com._1.equipoFutbol5.repository.EntrenamientoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntrenamientoService {

    private final EntrenamientoRepository repository;

    public HttpGlobalResponse<EntrenamientoDTO> guardarEntrenamiento(EntrenamientoDTO dto) {

        HttpGlobalResponse<EntrenamientoDTO> respuesta = new HttpGlobalResponse<>();
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNumeroEntrenamiento(dto.getNumeroEntrenamiento());

        entrenamiento.setJugador(dto.getJugador());

        entrenamiento.setPotenciaTiro(dto.getPotenciaTiro());

        entrenamiento.setVelocidad(dto.getVelocidad());

        entrenamiento.setPases(dto.getPases());

        Double resultado = dto.getPotenciaTiro() * 0.2 + dto.getVelocidad() * 0.3 + dto.getPases() * 0.5;

        entrenamiento.setResultado(resultado);

        repository.save(entrenamiento);
        respuesta.setData(dto);
        respuesta.setMessage("jugador registrado al entrenamiento de hoy");

        return respuesta;
    }

    public List<Object[]> obtenerTitulares() {

        if (repository.countByNumeroEntrenamiento(1) == 0 ||
            repository.countByNumeroEntrenamiento(2) == 0 ||
            repository.countByNumeroEntrenamiento(3) == 0) {

            List<Object[]> res = new ArrayList<>();
            res.add(new Object[] { "No hay suficiente informacion" });
            return res;
        }

        List<Object[]> response = repository.obtenerTitular();

        return response;
    }
}
