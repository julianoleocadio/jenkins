package com.api.pessoas.model;

import com.api.pessoas.Utils.ValidaDocumento;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pessoa {
    @Id
    private String id;
    private Date dataCriacao;
    private String tipoDoc;
    private String documento;
    private String nome;
    private String rg;
    private String sexo;
    private String nomePai;
    private String nomeMae;
    private String estCivil;

    public Pessoa(Date dataCriacao, String tipoDoc, String documento, String nome, String rg, String sexo, String nomePai, String nomeMae, String estCivil) {
        this.dataCriacao = new Date();
        this.tipoDoc = tipoDoc;
        this.documento = ValidaDocumento.removeCaracteresEspeciais(documento);
        this.nome = nome;
        this.rg = rg;
        this.sexo = ajustaSexo(sexo);
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.estCivil = estCivil;
    }

    private String ajustaSexo(String sexo) {
        return sexo.toUpperCase();
    }
}
