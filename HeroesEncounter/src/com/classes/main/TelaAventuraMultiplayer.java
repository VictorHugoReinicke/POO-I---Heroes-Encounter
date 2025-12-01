package com.classes.main;

import com.classes.DTO.*;
import com.classes.network.NetworkManager;
import javax.swing.*;
import java.awt.*;

public class TelaAventuraMultiplayer extends TelaAventura {
    private NetworkManager networkManager;
    private boolean isHost;
    private Jogador jogadorAliado;
    
    public TelaAventuraMultiplayer(Jogador jogador, Jogador jogadorAliado, NetworkManager networkManager, boolean isHost) {
        super(jogador);
        this.networkManager = networkManager;
        this.isHost = isHost;
        this.jogadorAliado = jogadorAliado;
        
        setTitle("Heroes Encounter - Multiplayer - " + (isHost ? "Host" : "Cliente"));
        adicionarLog("🎮 MODO MULTIPLAYER - " + (isHost ? "VOCÊ É O HOST" : "VOCÊ É O CONVIDADO"));
        if (jogadorAliado != null) {
            adicionarLog("🤝 Aliado: " + jogadorAliado.getNome() + " - " + determinarClasse(jogadorAliado));
        }
    }
    

    @Override
    protected void proximaBatalha() {
        // Sincronizar com o outro jogador
        if (isHost) {
            networkManager.sendObjectSafe("BATALHA_INICIAR");
        }
        super.proximaBatalha();
    }
    
    @Override
    public void batalhaVencida() {
        super.batalhaVencida();
        // Sincronizar vitória
        networkManager.sendObjectSafe("BATALHA_VENCIDA");
    }
    
    @Override
    public void batalhaPerdida() {
        super.batalhaPerdida();
        // Sincronizar derrota
        networkManager.sendObjectSafe("BATALHA_PERDIDA");
    }
}