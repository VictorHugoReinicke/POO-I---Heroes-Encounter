package com.classes.DTO.IA.estrategias;

import com.classes.DTO.IA.IABase;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import com.classes.Enums.TipoIA;
import java.util.List;

public class IAGenerico extends IABase {
    @Override
    public String decidirAcao(Inimigo inimigo, Jogador jogador, 
                            double hpInimigo, double hpJogador, 
                            List<String> acoes) {
        // Sistema baseado na personalidade do inimigo
        TipoIA tipoIA = inimigo.getTipoIA();
        
        switch (tipoIA) {
            case AGRESSIVO:
                return decidirAcaoAgressivo(inimigo, jogador, hpInimigo, hpJogador, acoes);
                
            case DEFENSIVA:
                return decidirAcaoDefensivo(inimigo, jogador, hpInimigo, hpJogador, acoes);
                
            case ESTRATEGICA:
                return decidirAcaoEstrategico(inimigo, jogador, hpInimigo, hpJogador, acoes);
                
            case ALEATORIA:
                return acoes.get(rand.nextInt(acoes.size()));
                
            case BALANCEADO:
                return decidirAcaoBalanceado(inimigo, jogador, hpInimigo, hpJogador, acoes);
                
            default: // CHEFE ou outros
                return decidirAcaoBalanceado(inimigo, jogador, hpInimigo, hpJogador, acoes);
        }
    }
    
    private String decidirAcaoAgressivo(Inimigo inimigo, Jogador jogador,
                                       double hpInimigo, double hpJogador,
                                       List<String> acoes) {
        if (hpJogador < 0.5 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        }
        
        double chance = rand.nextDouble();
        
        if (chance < 0.5) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.7 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        } else if (chance < 0.8 && acoes.contains("GRITAR")) {
            return "GRITAR";
        } else if (chance < 0.9 && acoes.contains("RUGIDO")) {
            return "RUGIDO";
        } else {
            return "DEFENDER";
        }
    }
    
    private String decidirAcaoDefensivo(Inimigo inimigo, Jogador jogador,
                                       double hpInimigo, double hpJogador,
                                       List<String> acoes) {
        if (hpInimigo < 0.5) {
            if (acoes.contains("DEFENDER_FORTE")) return "DEFENDER_FORTE";
            if (acoes.contains("CURAR")) return "CURAR";
            return "DEFENDER";
        }
        
        double chance = rand.nextDouble();
        
        if (chance < 0.3) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.5) {
            return "DEFENDER";
        } else if (chance < 0.6 && acoes.contains("DEFENDER_FORTE")) {
            return "DEFENDER_FORTE";
        } else if (chance < 0.7 && acoes.contains("CURAR")) {
            return "CURAR";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
    
    private String decidirAcaoEstrategico(Inimigo inimigo, Jogador jogador,
                                         double hpInimigo, double hpJogador,
                                         List<String> acoes) {
        if (hpJogador < 0.4 && hpInimigo > 0.6) {
            return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
        }
        
        if (hpInimigo < 0.5) {
            if (acoes.contains("BUFF_DEFESA")) return "BUFF_DEFESA";
            return "DEFENDER";
        }
        
        double chance = rand.nextDouble();
        
        if (chance < 0.2) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.4) {
            return "DEFENDER";
        } else if (chance < 0.5 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        } else if (chance < 0.6 && acoes.contains("BUFF_DEFESA")) {
            return "BUFF_DEFESA";
        } else if (chance < 0.7 && acoes.contains("DEBUFF_JOGADOR")) {
            return "DEBUFF_JOGADOR";
        } else if (chance < 0.9) {
            return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
        } else {
            return acoes.contains("GRITAR") ? "GRITAR" : "ATAQUE_NORMAL";
        }
    }
    
    private String decidirAcaoBalanceado(Inimigo inimigo, Jogador jogador,
                                        double hpInimigo, double hpJogador,
                                        List<String> acoes) {
        if (hpInimigo < 0.4) {
            return "DEFENDER";
        }
        
        if (hpJogador < 0.4) {
            if (acoes.contains("ATAQUE_PODEROSO")) return "ATAQUE_PODEROSO";
            return "ATAQUE_NORMAL";
        }
        
        double chance = rand.nextDouble();
        
        if (chance < 0.3) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.5) {
            return "DEFENDER";
        } else if (chance < 0.6 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        } else if (chance < 0.7 && acoes.contains("GRITAR")) {
            return "GRITAR";
        } else if (chance < 0.8 && acoes.contains("RUGIDO")) {
            return "RUGIDO";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
}