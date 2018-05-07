package com.animais.sos.sosanimais.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categoria implements Serializable {
    private String descricao;

    public Categoria(){

    }
    public Categoria(String descricao){
        this.descricao = descricao;
    }

    public List<Categoria> getCaterias(){
        List<Categoria> lst = new ArrayList<Categoria>();
        lst.add(new Categoria("Amfíbio"));
        lst.add(new Categoria("Ave"));
        lst.add(new Categoria("Mamífero"));
        lst.add(new Categoria("Peixe"));
        lst.add(new Categoria("Réptil"));
        return lst;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
