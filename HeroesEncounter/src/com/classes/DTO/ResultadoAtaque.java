package com.classes.DTO;

public class ResultadoAtaque {
    private int dano;
    private boolean critico;
    
    public ResultadoAtaque(int dano, boolean critico) {
        this.dano = dano;
        this.critico = critico;
    }
    
    public int getDano() { return dano; }
    public boolean isCritico() { return critico; }
}