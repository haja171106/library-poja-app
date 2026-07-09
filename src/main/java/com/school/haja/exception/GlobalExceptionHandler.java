package com.school.haja.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private Map<String, Object> body(int status, String message, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", status);
    body.put("message", message);
    body.put("path", request.getDescription(false).replace("uri=", ""));
    body.put("timestamp", Instant.now().toString());
    return body;
  }

  /** Ressource métier introuvable (ex: livre, bibliothèque avec un id inexistant). */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(
      EntityNotFoundException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body(404, ex.getMessage(), request));
  }

  /** URL tapée qui ne correspond à aucune route existante. */
  @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
  public ResponseEntity<Map<String, Object>> handleRouteNotFound(Exception ex, WebRequest request) {
    String path = request.getDescription(false).replace("uri=", "");
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(body(404, "Route inexistante : " + path, request));
  }

  /** Paramètre d'URL invalide (ex: un id qui n'est pas un UUID, un format qui n'existe pas). */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String expectedType =
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valeur attendue";
    String message =
        String.format(
            "Le paramètre '%s' avec la valeur '%s' est invalide (attendu : %s)",
            ex.getName(), ex.getValue(), expectedType);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body(400, message, request));
  }

  /** Corps de requête JSON malformé ou absent. */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleMalformedJson(
      HttpMessageNotReadableException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(body(400, "Corps de requête JSON invalide ou manquant", request));
  }

  /** Paramètre de requête obligatoire manquant (ex: ?format=). */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, Object>> handleMissingParam(
      MissingServletRequestParameterException ex, WebRequest request) {
    String message = "Le paramètre obligatoire '" + ex.getParameterName() + "' est manquant";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body(400, message, request));
  }

  /** Violation de contrainte en base (ex: suppression d'une library encore référencée). */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleDataIntegrity(
      DataIntegrityViolationException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            body(
                409,
                "Opération impossible : cette ressource est encore référencée ailleurs",
                request));
  }

  /** Filet de sécurité pour toute exception non prévue explicitement. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(body(500, "Erreur interne inattendue", request));
  }
}
