package com.empresa.apiatencioncanales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>("El valor proporcionado no fue encontrado:" + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QueueEmptyException.class)
    public ResponseEntity<?> handleQueueEmptyException(QueueEmptyException e) {
        return new ResponseEntity<>("El recurso que está intentando ejecutar no tiene valores en la cola: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PriorityException.class)
    public ResponseEntity<?> handlePriorityException(PriorityException e) {
        return new ResponseEntity<>("Está intentando ejecutar un cambio de prioridad a un usuario inexistente: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
