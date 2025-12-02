package com.classes.DTO.IA;

import com.classes.Enums.TipoIA;

public class IADescricao {
    public static String getDescricao(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo - Ataca sem piedade, raramente defende";
            case DEFENSIVA: return "🛡️ Defensivo - Prioriza sobrevivência, cura quando possível";
            case ESTRATEGICA: return "🎯 Estratégico - Usa buffs/debuffs, adapta-se à situação";
            case BALANCEADO: return "⚖️ Balanceado - Equilíbrio entre ataque e defesa";
            case ALEATORIA: return "🎲 Aleatório - Comportamento imprevisível";
            case CHEFE: return "👑 Chefe - Poderoso, usa habilidades especiais";
            default: return "Desconhecido";
        }
    }
}