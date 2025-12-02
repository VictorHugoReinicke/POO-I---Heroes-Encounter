package com.classes.DTO.IA.estrategias;

import com.classes.DTO.IA.IABase;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.List;

public class IALadrao extends IABase {
    @Override
    public String decidirAcao(Inimigo inimigo, Jogador jogador, 
                            double hpInimigo, double hpJogador, 
                            List<String> acoes) {
        if (hpInimigo < 0.25 && acoes.contains("FUGIR")) {
            return "FUGIR";
        }
        
        if (hpJogador > 0.7) {
            return decidirContraJogadorForte(acoes);
        } else if (hpInimigo > 0.6 && hpJogador < 0.5) {
            return decidirComVantagem(acoes);
        } else {
            return decidirSituacaoEquilibrada(acoes);
        }
    }
    
    private String decidirContraJogadorForte(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.3 && acoes.contains("ESQUIVAR")) {
            return "ESQUIVAR";
        } else if (chance < 0.6) {
            return "DEFENDER";
        } else if (chance < 0.8 && acoes.contains("ATAQUE_RAPIDO")) {
            return "ATAQUE_RAPIDO";
        } else {
            return "ATAQUE_NORMAL";
        }
    }
    
    private String decidirComVantagem(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.4 && acoes.contains("ATAQUE_RAPIDO")) {
            return "ATAQUE_RAPIDO";
        } else if (chance < 0.7 && acoes.contains("ROUBAR")) {
            return "ROUBAR";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
    
    private String decidirSituacaoEquilibrada(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.3) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.5) {
            return "DEFENDER";
        } else if (chance < 0.6 && acoes.contains("ESQUIVAR")) {
            return "ESQUIVAR";
        } else if (chance < 0.7 && acoes.contains("ATAQUE_RAPIDO")) {
            return "ATAQUE_RAPIDO";
        } else if (chance < 0.8 && acoes.contains("ROUBAR")) {
            return "ROUBAR";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
}