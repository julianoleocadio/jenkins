package com.api.pessoas.exceptions;

import com.api.pessoas.exceptions.models.CamposErros;
import com.api.pessoas.exceptions.models.DetalhesExcecao;
import com.api.pessoas.exceptions.models.ErrorResponseDto;
import com.api.pessoas.logs.APILogger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExcpetionHandler extends ResponseEntityExceptionHandler {

    private final static String NO_CONTENT_CODE = "204 NO CONTENT";
    private final static String NO_CONTENT_MESSAGE = "Solicitação Imprópria - Conteúdo insuficiente.";
    private final static String NOT_FOUND_CODE = "404 NOT FOUND";
    private final static String NOT_FOUND_MESSAGE = "Entidade não encontrada";
    private final static String UNAUTHORIZED_CODE = "401 UNAUTHORIZED";
    private final static String UNAUTHORIZED_MESSAGE = "Solicitação Imprópria - Token inválido.";
    private final static String INVALID_CODE = "400 BAD REQUEST";
    private final static String INVALID_MESSAGE = "Solicitação imprópria, falta parametros no request.";
    private final static String INTERNAL_SERVER_ERROR_CODE = "500 INTERNAL SERVER ERROR";
    private final static String INTERNAL_SERVER_ERROR_MESSAGE = "Erro interno do servidor.";

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DetalhesExcecao detalhes = new DetalhesExcecao(INVALID_CODE, INVALID_MESSAGE);
        var response = new ResponseEntity<>(List.of(detalhes), HttpStatus.BAD_REQUEST);
        HttpHeaders headersFormatados = formataHeader(request.getHeaderNames(), request);
        APILogger.badRequest(response, APILogger.filterHeader(headersFormatados));
        return new ResponseEntity<>(List.of(detalhes), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.api.pessoas.exceptions.NotFoundException.class)
    protected ResponseEntity<List<ErrorResponseDto>> handleEntityNotFound(com.api.pessoas.exceptions.NotFoundException ex, HttpServletRequest request) {
        List<ErrorResponseDto> response = Collections.singletonList(new ErrorResponseDto(NOT_FOUND_CODE, ex.getMessage()));
        HttpHeaders headers = formataHeader(request.getHeaderNames(), request);
        APILogger.notFound(response, APILogger.filterHeader(headers));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(com.api.pessoas.exceptions.NoContentRuntimeException.class)
    protected ResponseEntity<List<ErrorResponseDto>> handleNoContentRuntimeException(com.api.pessoas.exceptions.NoContentRuntimeException ex, HttpServletRequest request) {
        List<ErrorResponseDto> response = Collections.singletonList(new ErrorResponseDto(NO_CONTENT_CODE, ex.getMessage()));
        HttpHeaders headers = formataHeader(request.getHeaderNames(), request);
        APILogger.noContent(response, APILogger.filterHeader(headers));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @ExceptionHandler(com.api.pessoas.exceptions.UnauthorizedException.class)
    protected ResponseEntity<List<ErrorResponseDto>> handleUnauthorizedException(com.api.pessoas.exceptions.UnauthorizedException ex, HttpServletRequest request) {
        List<ErrorResponseDto> response = Collections.singletonList(new ErrorResponseDto(UNAUTHORIZED_CODE, ex.getMessage()));
        HttpHeaders headers = formataHeader(request.getHeaderNames(), request);
        APILogger.unauthorized(response, APILogger.filterHeader(headers));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        DetalhesExcecao detalhes = new DetalhesExcecao(INVALID_CODE, ex.getParameterName() + "");
        var response = new ResponseEntity<>(List.of(detalhes), HttpStatus.BAD_REQUEST);
        HttpHeaders headersFormatados = formataHeader(request.getHeaderNames(), request);
        APILogger.badRequest(response, APILogger.filterHeader(headersFormatados));
        return new ResponseEntity<>(List.of(detalhes), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<DetalhesExcecao> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest  request) {
        var camposErrosLista = ex
                .getConstraintViolations()
                .stream()
                .map(this::toCamposErros)
                .collect(Collectors.toList());
        DetalhesExcecao detalhes = new DetalhesExcecao(INVALID_CODE, ex.getMessage(), camposErrosLista);
        var resposta = new ResponseEntity<>(detalhes, HttpStatus.BAD_REQUEST);
        HttpHeaders headers = formataHeader(request.getHeaderNames(), request);
        APILogger.badRequest(resposta, APILogger.filterHeader(headers));
        return resposta;
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<List<ErrorResponseDto>> internalServerErrorHandler(Exception ex, HttpServletRequest request) {
        List<ErrorResponseDto> response = Collections.singletonList(new ErrorResponseDto(INTERNAL_SERVER_ERROR_CODE, ex.getMessage()));
        HttpHeaders headers = formataHeader(request.getHeaderNames(), request);
        APILogger.internalServerError(response, APILogger.filterHeader(headers));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public CamposErros toCamposErros(ConstraintViolation<?> violation) {
        var it = violation.getPropertyPath().iterator();
        var leaf = it.next();
        while(it.hasNext()) leaf = it.next();
        return new CamposErros(
                leaf.getName(),
                violation.getMessageTemplate(),
                violation.getInvalidValue().toString());
    }

    public HttpHeaders formataHeader(Enumeration<String> names, HttpServletRequest request) {
        StringBuilder headerIt = new StringBuilder();
        HttpHeaders headers = new HttpHeaders();
        while(names.hasMoreElements()) {
            headerIt.append(names.nextElement());
            headers.add(headerIt.toString(), request.getHeader(headers.toString()));
        }
        return headers;
    }

    public HttpHeaders formataHeader(Iterator<String> names, WebRequest request) {
        StringBuilder headerIt = new StringBuilder();
        HttpHeaders headers = new HttpHeaders();
        while(names.hasNext()) {
            headerIt.append(names.next());
            headers.add(headerIt.toString(), request.getHeader(headers.toString()));
        }
        return headers;
    }
}
