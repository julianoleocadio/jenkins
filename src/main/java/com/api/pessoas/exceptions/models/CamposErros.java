package com.api.pessoas.exceptions.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"campo", "mensagem", "valor"})
public class CamposErros {
    private final String campo;
    private final String mensagem;
    private final String valor;
}
