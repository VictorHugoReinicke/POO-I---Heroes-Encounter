package com.classes.DTO.IA.estrategias;

import com.classes.DTO.IA.IABase;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.List;

public class IABesta extends IABase {
    @Override
    public String decidirAcao(Inimigo inimigo, Jogador jogador, 
                            double hpInimigo, double hpJogador, 
                            List<String> acoes) {
        if (hpInimigo < 0.3 && acoes.contains("ATAQUE_FEROZ")) {
            return "ATAQUE_FEROZ";
        }
        
        if (hpJogador < 0.4 && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        }
        
        double chance = rand.nextDouble();
        
        if (chance < 0.5) {
            return "ATAQUE_NORMAL";
        } else if (chance < 0.7 && acoes.contains("RUGIDO")) {
            return "RUGIDO";
        } else if (chance < 0.85 && acoes.contains("ATAQUE_FEROZ")) {
            return "ATAQUE_FEROZ";
        } else if (chance < 0.95) {
            return "DEFENDER";
        } else {
            return acoes.contains("GRITAR") ? "GRITAR" : "ATAQUE_NORMAL";
        }
    }
}