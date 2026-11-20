package com.campuspulse.exception;
import java.time.Instant;import java.util.*;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.http.converter.HttpMessageNotReadableException;import org.springframework.security.access.AccessDeniedException;
@RestControllerAdvice public class GlobalExceptionHandler{
@ExceptionHandler(ApiException.class) public ResponseEntity<?> api(ApiException e){return ResponseEntity.status(e.status).body(Map.of("message",e.getMessage(),"timestamp",Instant.now().toString()));}
@ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> invalid(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("message",e.getBindingResult().getFieldErrors().stream().findFirst().map(x->x.getField()+": "+x.getDefaultMessage()).orElse("Invalid request")));}
@ExceptionHandler(HttpMessageNotReadableException.class) public ResponseEntity<?> malformed(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Invalid JSON or enum value"));}
@ExceptionHandler(AccessDeniedException.class) public ResponseEntity<?> denied(Exception e){return ResponseEntity.status(403).body(Map.of("message","Access denied"));}
@ExceptionHandler(Exception.class) public ResponseEntity<?> unexpected(Exception e){return ResponseEntity.status(500).body(Map.of("message","An unexpected server error occurred"));}
}
