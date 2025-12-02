package com.classes.DTO;

public class JogadorStatus {

    private int idJogador;
    private int idStatus;

    private int turnosRestantes;

    private Jogador jogador;
    private Status status;

    public JogadorStatus() {
    }

    public JogadorStatus(int idJogador, int idStatus, int turnosRestantes) {
        this.idJogador = idJogador;
        this.idStatus = idStatus;
        this.turnosRestantes = turnosRestantes;
    }


    public int getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(int idJogador) {
        this.idJogador = idJogador;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }

    public void setTurnosRestantes(int turnosRestantes) {
        this.turnosRestantes = turnosRestantes;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
        if (jogador != null) {
            this.idJogador = jogador.getId();
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        if (status != null) {
            this.idStatus = status.getId();
        }
    }
}