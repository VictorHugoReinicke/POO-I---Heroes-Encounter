package com.classes.networking;

import com.classes.DTO.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessaoMultiplayer {
    private static SessaoMultiplayer instancia;
    private Map<Integer, Jogador> jogadoresConectados;
    private Map<Integer, List<JogadorItem>> inventarios;
    private int hostId;
    private boolean sessaoAtiva;
    
    private SessaoMultiplayer() {
        this.jogadoresConectados = new ConcurrentHashMap<>();
        this.inventarios = new ConcurrentHashMap<>();
        this.sessaoAtiva = false;
    }
    
    public static SessaoMultiplayer getInstancia() {
        if (instancia == null) {
            instancia = new SessaoMultiplayer();
        }
        return instancia;
    }
    
    /**
     * ✅ ADICIONAR JOGADOR À SESSÃO
     */
    public void adicionarJogador(Jogador jogador) {
        jogadoresConectados.put(jogador.getId(), jogador);
        
        // ✅ SE FOR O PRIMEIRO JOGADOR, É O HOST
        if (jogadoresConectados.size() == 1) {
            hostId = jogador.getId();
        }
        
        System.out.println("🎮 Jogador adicionado à sessão: " + jogador.getNome());
    }
    
    /**
     * ✅ OBTER JOGADOR POR ID
     */
    public Jogador getJogador(int id) {
        return jogadoresConectados.get(id);
    }
    
    /**
     * ✅ OBTER TODOS OS JOGADORES
     */
    public List<Jogador> getTodosJogadores() {
        return new ArrayList<>(jogadoresConectados.values());
    }
    
    /**
     * ✅ OBTER JOGADOR HOST
     */
    public Jogador getHost() {
        return jogadoresConectados.get(hostId);
    }
    
    /**
     * ✅ VERIFICAR SE É O HOST
     */
    public boolean isHost(int jogadorId) {
        return jogadorId == hostId;
    }
    
    /**
     * ✅ SINCRONIZAR INVENTÁRIO
     */
    public void sincronizarInventario(int jogadorId, List<JogadorItem> inventario) {
        inventarios.put(jogadorId, new ArrayList<>(inventario));
    }
    
    /**
     * ✅ OBTER INVENTÁRIO
     */
    public List<JogadorItem> getInventario(int jogadorId) {
        return inventarios.getOrDefault(jogadorId, new ArrayList<>());
    }
    
    /**
     * ✅ INICIAR SESSÃO
     */
    public void iniciarSessao() {
        this.sessaoAtiva = true;
        System.out.println("🚀 Sessão multiplayer iniciada com " + jogadoresConectados.size() + " jogadores");
    }
    
    /**
     * ✅ LIMPAR SESSÃO
     */
    public void limparSessao() {
        jogadoresConectados.clear();
        inventarios.clear();
        sessaoAtiva = false;
        System.out.println("🧹 Sessão multiplayer limpa");
    }
    
    public boolean isSessaoAtiva() {
        return sessaoAtiva;
    }
    
    public int getQuantidadeJogadores() {
        return jogadoresConectados.size();
    }
}