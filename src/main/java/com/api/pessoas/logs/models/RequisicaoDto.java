package com.api.pessoas.logs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoDto {
    private String metodo;
    private String uri;
    private HttpHeaders headers;
}
