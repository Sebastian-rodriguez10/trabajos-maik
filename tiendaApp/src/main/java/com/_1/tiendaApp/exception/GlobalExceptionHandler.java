package com._1.tiendaApp.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com._1.tiendaApp.dto.ErrorResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    /**
     * metodo que captura los errores de @valid
     * @param ex error que llega (MethodArgumentNotValidException)
     * @return retorna el errorResponse con su codigo http
     */
    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){
        
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("validation_error");
        response.setMessage("Los datos enviados contienen errores");

        List<ErrorResponse.FieldErrorDetail> errorsDetail = ex.getBindingResult()
        .getFieldErrors().stream().map(fieldError -> {
            ErrorResponse.FieldErrorDetail detail = new  ErrorResponse.FieldErrorDetail();
            detail.setField(fieldError.getField());
            detail.setRejectedValue(fieldError.getRejectedValue());
            detail.setMessage(fieldError.getDefaultMessage());
            return detail;
        }).toList();

        response.setErrors(errorsDetail);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * coje las excepciones personalizadas
     * @param ex
     * @return la respuesta de error segun la excepcion personalizada
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(ex.getStatus().value());
        response.setError(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        response.setErrors(null); // No son errores de campos DTO, van en null

        return new ResponseEntity<>(response, ex.getStatus());
    }

    /**
     * metodo que atapa cualquier otro error
     * @param ex exception
     * @return respuesta de con su hhtpstatus(Internal server)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError("INTERNAL_SERVER_ERROR");
        response.setMessage("Ocurrió un error inesperado en el servidor.");
        response.setErrors(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
