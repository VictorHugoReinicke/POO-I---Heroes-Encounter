package com.classes.DTO.IA.estrategias;

import com.classes.DTO.IA.IABase;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.List;

public class IAMagico extends IABase {
    @Override
    public String decidirAcao(Inimigo inimigo, Jogador jogador, 
                            double hpInimigo, double hpJogador, 
                            List<String> acoes) {
        // Prioridade: defesa se estiver ferido
        if (hpInimigo < 0.4) {
            if (acoes.contains("PROTECAO_MAGICA")) {
                return "PROTECAO_MAGICA";
            } else if (acoes.contains("CURAR")) {
                return "CURAR";
            }
        }
        
        // Estratégia baseada no HP do jogador
        if (hpJogador > 0.6) {
            // Jogador forte - usar magias defensivas/debuff
            return decidirContraJogadorForte(acoes);
        } else if (hpJogador < 0.3) {
            // Jogador fraco - ataque direto
            return decidirContraJogadorFraco(acoes);
        } else {
            // Situação equilibrada - mistura
            return decidirSituacaoEquilibrada(acoes);
        }
    }
    
    private String decidirContraJogadorForte(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.3 && acoes.contains("PROTECAO_MAGICA")) {
            return "PROTECAO_MAGICA";
        } else if (chance < 0.5 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        } else if (chance < 0.7) {
            return "DEFENDER";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return acoes.contains("CURAR") ? "CURAR" : "DEFENDER";
        }
    }
    
    private String decidirContraJogadorFraco(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.4) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.7 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
    
    private String decidirSituacaoEquilibrada(List<String> acoes) {
        double chance = rand.nextDouble();
        
        if (chance < 0.2) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.4) {
            return "DEFENDER";
        } else if (chance < 0.6 && acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        } else if (chance < 0.7 && acoes.contains("PROTECAO_MAGICA")) {
            return "PROTECAO_MAGICA";
        } else if (chance < 0.8 && acoes.contains("CURAR")) {
            return "CURAR";
        } else if (chance < 0.9) {
            return "ATAQUE_NORMAL";
        } else {
            return "DEFENDER";
        }
    }
}