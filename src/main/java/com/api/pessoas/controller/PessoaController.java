package com.api.pessoas.controller;

import com.api.pessoas.logs.APILogger;
import com.api.pessoas.logs.models.ResponseDto;
import com.api.pessoas.model.Pessoa;
import com.api.pessoas.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;

@RestController
@Validated
@RequestMapping({"api/v1/pessoas"})
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<?> pessoaFindAll(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers
    ) {
        var result = pessoaService.findAll();
        var response = new ResponseEntity<>(result, HttpStatus.OK);
        var responseLog = new ResponseDto<>(result);
        APILogger.ok(responseLog.getData(), APILogger.filterHeader(headers));
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> pessoaById(
            HttpServletRequest request,
            @PathVariable(name = "id") String id,
            @RequestHeader HttpHeaders headers
            ) {
        var result = pessoaService.findById(id).orElseThrow();
        var response = new ResponseEntity<>(result, HttpStatus.OK);
        var responseLog = new ResponseDto<>(result);
        APILogger.ok(responseLog.getData(), APILogger.filterHeader(headers));
        return response;
    }

    @PostMapping
    public ResponseEntity<Pessoa> cadastraPessoa(
            HttpServletRequest request,
            @RequestBody Pessoa pessoa,
            @RequestHeader HttpHeaders headers
    ) {
        if(pessoa.toString().isEmpty()) {
            throw new ConstraintViolationException("Pessoa Vazia.", new HashSet<>());
        } else {
            var result = pessoaService.insert(pessoa);
            var response = new ResponseEntity<>(result, HttpStatus.OK);
            var responseLog = new ResponseDto<>(result);
            APILogger.ok(responseLog.getData(), APILogger.filterHeader(headers));
            return response;
        }
    }
}
