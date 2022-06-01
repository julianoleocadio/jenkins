package com.api.pessoas.exceptions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"codigo", "mensagem", "campos"})
public class DetalhesExcecao {

    @JsonProperty("codigo")
    @Getter
    private String codigo;

    @JsonProperty("mensagem")
    @Getter
    private String mensagem;

    @JsonProperty("campos")
    @Getter
    private List<CamposErros> campos;

    public DetalhesExcecao(String codigo, String mensagem) {
        super();
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    @JsonProperty("codigo")
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @JsonProperty("mensagem")
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @JsonProperty("campos")
    public void setCampos(List<CamposErros> campos) {
        this.campos = campos;
    }
}
