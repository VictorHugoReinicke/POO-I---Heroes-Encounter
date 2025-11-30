package com.classes.DTO;

public class ClasseHabilidade {

    // Chaves compostas (Foreign Keys) que formam a chave primária
    private int idClasse;
    private int idHabilidade;

    // Atributos próprios (Ex: Nível mínimo para aprender a habilidade, se houvesse)
    // No seu DER, não há atributos próprios, então focamos nas chaves.

    // Objetos DTOs completos para carregar informações no BO (opcional)
    private Classe classe;
    private Habilidade habilidade;

    // Construtor Vazio
    public ClasseHabilidade() {
    }

    // Construtor com as chaves
    public ClasseHabilidade(int idClasse, int idHabilidade) {
        this.idClasse = idClasse;
        this.idHabilidade = idHabilidade;
    }

    // --- Getters e Setters ---

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
        // Atualiza a FK ao setar o objeto
        if (classe != null) {
            this.idClasse = classe.getId();
        }
    }

    public Habilidade getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(Habilidade habilidade) {
        this.habilidade = habilidade;
        // Atualiza a FK ao setar o objeto
        if (habilidade != null) {
            this.idHabilidade = habilidade.getId();
        }
    }
}