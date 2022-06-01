package com.api.pessoas.logs;

import com.api.pessoas.exceptions.models.ErrorResponseDto;
import com.api.pessoas.logs.models.RequisicaoDto;
import com.api.pessoas.logs.models.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class APILogger {

    private APILogger() { throw new IllegalStateException("APILogger.class"); }
    private static final Logger logger = LoggerFactory.getLogger("jsonLogger");
    private static final String REQUISICAO_KEY = "requisicao";
    private static final String RESPOSTA_KEY = "resposta";
    private static final String HEADER_APPID = "x-itau.appid";
    private static final String HOST_ORIGEM = "host";

    public static void ok(Object response, HttpHeaders header) {
        logger.info(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(header))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.OK.getReasonPhrase(),
                        HttpStatus.OK.value()
                ))
        );
    }

    public static void notFound(Object response, HttpHeaders headers) {
        logger.info(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(headers))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        HttpStatus.NOT_FOUND.value()
                ))
        );
    }

    public static void badRequest(Object response, HttpHeaders headers) {
        logger.warn(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(headers))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        HttpStatus.BAD_REQUEST.value()
                ))
        );
    }

    public static void unauthorized(Object response, HttpHeaders headers) {
        logger.warn(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(headers))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        HttpStatus.UNAUTHORIZED.value()
                ))
        );
    }

    public static void internalServerError(Object response, HttpHeaders headers) {
        logger.warn(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(headers))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ))
        );
    }

    public static void noContent(List<ErrorResponseDto> response, HttpHeaders headers) {
        logger.warn(
                "{}{}",
                kv(REQUISICAO_KEY, getRequisicaoDto(filterHeader(headers))),
                kv(RESPOSTA_KEY, new ResponseDto<>(
                        response,
                        HttpStatus.NO_CONTENT.getReasonPhrase(),
                        HttpStatus.NO_CONTENT.value()
                ))
        );
    }

    private static RequisicaoDto getRequisicaoDto(HttpHeaders header) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        var url = ServletUriComponentsBuilder.fromRequest(request).build().toUriString();
        return new RequisicaoDto(request.getMethod(), url, header);
    }

    public static HttpHeaders filterHeader(HttpHeaders header) {
        List<String> appid = header.get(HOST_ORIGEM);
        HttpHeaders filteredHeaders = new HttpHeaders();
        if(appid != null && !appid.isEmpty()) filteredHeaders.set(HOST_ORIGEM, appid.get(0));
        return filteredHeaders;
    }

//    public static HttpHeaders filterHeader(HttpHeaders header) {
//        List<String> appid = header.get(HEADER_APPID);
//        HttpHeaders filteredHeaders = new HttpHeaders();
//        if(appid != null && !appid.isEmpty()) filteredHeaders.set(HEADER_APPID, appid.get(0));
//        return filteredHeaders;
//    }
}
