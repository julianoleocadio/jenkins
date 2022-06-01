package com.api.pessoas.exceptions.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {
    private String codigo;
    private String mensagem;
    private List<CamposErros> campos;

    public ErrorResponseDto(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }
}
