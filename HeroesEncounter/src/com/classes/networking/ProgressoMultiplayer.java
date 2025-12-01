package com.classes.networking;

import java.util.*;

public class ProgressoMultiplayer {
    private static ProgressoMultiplayer instancia;
    private int batalhasVencidas;
    private boolean jornadaAtiva;
    private Set<Integer> jogadoresProntos;
    
    private ProgressoMultiplayer() {
        this.batalhasVencidas = 0;
        this.jornadaAtiva = false;
        this.jogadoresProntos = new HashSet<>();
    }
    
    public static ProgressoMultiplayer getInstancia() {
        if (instancia == null) {
            instancia = new ProgressoMultiplayer();
        }
        return instancia;
    }
    
    /**
     * ✅ INICIAR JORNADA MULTIPLAYER
     */
    public synchronized boolean iniciarJornada() {
        if (jornadaAtiva) return false;
        
        jornadaAtiva = true;
        batalhasVencidas = 0;
        jogadoresProntos.clear();
        
        System.out.println("🏁 Jornada multiplayer iniciada");
        return true;
    }
    
    /**
     * ✅ MARCAR JOGADOR PRONTO
     */
    public synchronized void jogadorPronto(int jogadorId) {
        jogadoresProntos.add(jogadorId);
        System.out.println("✅ Jogador " + jogadorId + " está pronto");
    }
    
    /**
     * ✅ VERIFICAR SE TODOS ESTÃO PRONTOS
     */
    public synchronized boolean todosProntos(int totalJogadores) {
        return jogadoresProntos.size() >= totalJogadores;
    }
    
    /**
     * ✅ REGISTRAR VITÓRIA NA BATALHA
     */
    public synchronized void registrarVitoria() {
        batalhasVencidas++;
        jogadoresProntos.clear(); // Resetar para próxima batalha
        System.out.println("🎉 Vitória multiplayer registrada: " + batalhasVencidas + "/3");
    }
    
    /**
     * ✅ VERIFICAR SE JORNADA ESTÁ COMPLETA
     */
    public boolean isJornadaCompleta() {
        return batalhasVencidas >= 3;
    }
    
    // Getters
    public int getBatalhasVencidas() { return batalhasVencidas; }
    public boolean isJornadaAtiva() { return jornadaAtiva; }
    public void setJornadaAtiva(boolean ativa) { this.jornadaAtiva = ativa; }
}