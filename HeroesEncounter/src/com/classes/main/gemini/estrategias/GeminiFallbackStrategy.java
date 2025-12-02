package com.classes.main.gemini.estrategias;

import com.classes.DTO.*;
import com.classes.Enums.TipoIA;
import java.util.List;

public class GeminiFallbackStrategy {
    
    public static String getFallbackInteligente(Inimigo inimigo, List<String> acoes) {
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = 0.8; // Aproximação default
        
        String tipoInimigo = inimigo.getClass().getSimpleName();
        
        switch (tipoInimigo) {
            case "Chefe":
                return getFallbackChefe(hpPercentInimigo, hpPercentJogador, acoes);
                
            case "Besta":
                return getFallbackBesta(hpPercentInimigo, acoes);
                
            case "Ladrao":
                return getFallbackLadrao(hpPercentInimigo, hpPercentJogador, acoes);
                
            case "InimigoMagico":
                return getFallbackMagico(hpPercentInimigo, acoes);
                
            default:
                return getFallbackGenerico(inimigo.getTipoIA(), hpPercentInimigo, acoes);
        }
    }
    
    private static String getFallbackChefe(double hpInimigo, double hpJogador, List<String> acoes) {
        if (hpInimigo < 0.4 && acoes.contains("REGENERAR")) {
            return "REGENERAR";
        }
        if (hpJogador < 0.6 && acoes.contains("ATAQUE_ESPECIAL")) {
            return "ATAQUE_ESPECIAL";
        }
        return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
    }
    
    private static String getFallbackBesta(double hpInimigo, List<String> acoes) {
        if (hpInimigo < 0.3 && acoes.contains("ATAQUE_FEROZ")) {
            return "ATAQUE_FEROZ";
        }
        return "ATAQUE_NORMAL";
    }
    
    private static String getFallbackLadrao(double hpInimigo, double hpJogador, List<String> acoes) {
        if (hpInimigo < 0.25 && acoes.contains("FUGIR")) {
            return "FUGIR";
        }
        if (hpJogador > 0.7 && acoes.contains("ESQUIVAR")) {
            return "ESQUIVAR";
        }
        return "ATAQUE_NORMAL";
    }
    
    private static String getFallbackMagico(double hpInimigo, List<String> acoes) {
        if (hpInimigo < 0.4 && acoes.contains("PROTECAO_MAGICA")) {
            return "PROTECAO_MAGICA";
        }
        if (acoes.contains("FEITICO_MAGICO")) {
            return "FEITICO_MAGICO";
        }
        return "ATAQUE_NORMAL";
    }
    
    private static String getFallbackGenerico(TipoIA tipoIA, double hpInimigo, List<String> acoes) {
        if (tipoIA == TipoIA.DEFENSIVA && hpInimigo < 0.5) {
            return "DEFENDER";
        }
        if (tipoIA == TipoIA.AGRESSIVO && acoes.contains("ATAQUE_PODEROSO")) {
            return "ATAQUE_PODEROSO";
        }
        return "ATAQUE_NORMAL";
    }
}