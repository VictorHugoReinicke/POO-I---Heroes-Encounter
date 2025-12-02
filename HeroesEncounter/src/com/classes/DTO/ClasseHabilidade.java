package com.classes.DTO;

public class ClasseHabilidade {

    private int idClasse;
    private int idHabilidade;


    private Classe classe;
    private Habilidade habilidade;

    public ClasseHabilidade() {
    }

    public ClasseHabilidade(int idClasse, int idHabilidade) {
        this.idClasse = idClasse;
        this.idHabilidade = idHabilidade;
    }


    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public int getIdHabilidade() {
        return idHabilidade;
    }

    public void setIdHabilidade(int idHabilidade) {
        this.idHabilidade = idHabilidade;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
        if (classe != null) {
            this.idClasse = classe.getId();
        }
    }

    public Habilidade getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(Habilidade habilidade) {
        this.habilidade = habilidade;
        if (habilidade != null) {
            this.idHabilidade = habilidade.getId();
        }
    }
}