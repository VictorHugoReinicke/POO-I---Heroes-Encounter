package com.classes.DTO;

public class JogadorStatus {

    // Chaves compostas (Foreign Keys) que formam a chave primária
    private int idJogador;
    private int idStatus;

    // Atributo próprio
    private int turnosRestantes; 

    // Objetos DTOs completos para carregar informações no BO
    private Jogador jogador;
    private Status status;

    // Construtor Vazio
    public JogadorStatus() {
    }

    // Construtor com as chaves e o atributo
    public JogadorStatus(int idJogador, int idStatus, int turnosRestantes) {
        this.idJogador = idJogador;
        this.idStatus = idStatus;
        this.turnosRestantes = turnosRestantes;
    }

    // --- Getters e Setters ---

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