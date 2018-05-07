package com.animais.sos.sosanimais.model;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Animal implements Serializable {
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private Long id;
    private Endereco endereco;
    private Categoria categoria;
    private byte[] imagem;
    private String descricao;
    private Date data;

    public Animal(){

    }

    public Animal(Long id, Date data, Endereco endereco, Categoria categoria, byte[] imagem, String descricao) {
        this.id = id;
        this.data = data;
        this.endereco = endereco;
        this.categoria = categoria;
        this.imagem = imagem;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCadastro() {
        return data;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.data = dataCadastro;
    }

    public String getDateString() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        return format.format(this.getDataCadastro());
    }
}
