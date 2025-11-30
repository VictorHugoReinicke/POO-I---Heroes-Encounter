package com.classes.DTO;

public class JogadorItem {

    private int idJogador;
    private int idItem;

    private int quantidade;
    private boolean equipado;

    private Jogador jogador;
    private Item item;

    public JogadorItem() {
    }

    public JogadorItem(int idJogador, int idItem, int quantidade, boolean equipado) {
        this.idJogador = idJogador;
        this.idItem = idItem;
        this.quantidade = quantidade;
        this.equipado = equipado;
    }

    public int getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(int idJogador) {
        this.idJogador = idJogador;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isEquipado() {
        return equipado;
    }

    public void setEquipado(boolean equipado) {
        this.equipado = equipado;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public int getEquipadoDB() {
        return this.equipado ? 1 : 0;
    }
}