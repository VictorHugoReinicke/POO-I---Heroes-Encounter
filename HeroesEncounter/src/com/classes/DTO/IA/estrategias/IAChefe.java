package com.classes.DTO.IA.estrategias;

import com.classes.DTO.IA.IABase;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.List;

public class IAChefe extends IABase {
    @Override
    public String decidirAcao(Inimigo inimigo, Jogador jogador, 
                            double hpInimigo, double hpJogador, 
                            List<String> acoes) {
        // Prioridade: curar se estiver muito ferido
        if (hpInimigo < 0.4 && acoes.contains("REGENERAR")) {
            return "REGENERAR";
        }
        
        // Prioridade: ataque especial se jogador estiver fraco
        if (hpJogador < 0.5 && acoes.contains("ATAQUE_ESPECIAL")) {
            return "ATAQUE_ESPECIAL";
        }
        
        // Sistema de rotação mais complexo para chefes
        double chance = rand.nextDouble();
        
        if (chance < 0.3) {
            // 30% - Ataques variados
            if (acoes.contains("ATAQUE_ESPECIAL") && rand.nextDouble() < 0.3) {
                return "ATAQUE_ESPECIAL";
            } else if (acoes.contains("ATAQUE_PODEROSO")) {
                return "ATAQUE_PODEROSO";
            } else {
                return "ATAQUE_NORMAL";
            }
        } else if (chance < 0.5 && acoes.contains("GRITAR_GUERRA")) {
            // 20% - Buffs
            return "GRITAR_GUERRA";
        } else if (chance < 0.6) {
            // 10% - Defender
            return "DEFENDER";
        } else if (chance < 0.7 && acoes.contains("RUGIDO")) {
            // 10% - Intimidação
            return "RUGIDO";
        } else if (chance < 0.85) {
            // 15% - Ataque normal
            return "ATAQUE_NORMAL";
        } else {
            // 15% - Ataque poderoso
            return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
        }
    }
}