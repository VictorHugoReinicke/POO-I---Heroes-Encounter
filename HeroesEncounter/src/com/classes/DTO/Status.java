package com.classes.DTO;

public class Status {

    private int id;
    private String nome; // Ex: Veneno, Queimadura, Atordoamento
    private int danoTurno; // Dano que o status causa no final do turno (pode ser 0)
    private double modificadorDefesa; // Modificador (%) na defesa do alvo (Ex: 0.5 para -50%)
    private int duracaoTurnos; // Quantos turnos o efeito dura
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