package com.classes.DTO;

public class Status {

    private int id;
    private String nome;
    private int danoTurno;
    private double modificadorDefesa;
    private int duracaoTurnos;
    private double chanceEsquiva;

    public Status() {
    }

    public Status(String nome, int danoTurno, double modificadorDefesa, int duracaoTurnos) {
        this.nome = nome;
        this.danoTurno = danoTurno;
        this.modificadorDefesa = modificadorDefesa;
        this.duracaoTurnos = duracaoTurnos;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDanoTurno() {
        return danoTurno;
    }

    public void setDanoTurno(int danoTurno) {
        this.danoTurno = danoTurno;
    }

    public double getModificadorDefesa() {
        return modificadorDefesa;
    }

    public void setModificadorDefesa(double modificadorDefesa) {
        this.modificadorDefesa = modificadorDefesa;
    }

    public int getDuracaoTurnos() {
        return duracaoTurnos;
    }

    public void setDuracaoTurnos(int duracaoTurnos) {
        this.duracaoTurnos = duracaoTurnos;
    }
    
    public double getChanceEsquiva() {
        return chanceEsquiva;
    }
    
    public void setChanceEsquiva(double chanceEsquiva) {
        this.chanceEsquiva = chanceEsquiva;
    }
}