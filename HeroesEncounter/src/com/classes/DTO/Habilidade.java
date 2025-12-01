package com.classes.DTO;

public class Habilidade {

    private int id;
    private String nome;
    private int custoMana;
    private double fatorDano;
    private String tipo;
    private int idStatus;
    private Status efeitoStatus;

    public Habilidade(String nome, int custoMana, double fatorDano, String tipo) {
        this.nome = nome;
        this.custoMana = custoMana;
        this.fatorDano = fatorDano;
        this.tipo = tipo;
        this.idStatus = 0;         // padrão: sem status
        this.efeitoStatus = null;
    }

    public Habilidade() {}
    public Habilidade(String nome, int custoMana, double fatorDano, String tipo, int idStatus) {
        this(nome, custoMana, fatorDano, tipo);
        this.idStatus = idStatus;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getCustoMana() { return custoMana; }
    public void setCustoMana(int custoMana) { this.custoMana = custoMana; }

    public double getFatorDano() { return fatorDano; }
    public void setFatorDano(double fatorDano) { this.fatorDano = fatorDano; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getIdStatus() { return idStatus; }
    public void setIdStatus(int idStatus) { this.idStatus = idStatus; }

    public Status getEfeitoStatus() { return efeitoStatus; }

    public void setEfeitoStatus(Status efeitoStatus) {
        this.efeitoStatus = efeitoStatus;
        if (efeitoStatus != null) {
            this.idStatus = efeitoStatus.getId();
        }
    }
}
