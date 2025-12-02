package com.classes.main.gemini.util;

import com.classes.Enums.TipoIA;

public class GeminiDescricaoIA {
    
    public static String getDescricao(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo (Gemini)";
            case DEFENSIVA: return "🛡️ Defensivo (Gemini)";
            case ESTRATEGICA: return "🎯 Estratégico (Gemini)";
            case BALANCEADO: return "⚖️ Balanceado (Gemini)";
            case ALEATORIA: return "🎲 Aleatório (Gemini)";
            case CHEFE: return "👑 Chefe (Gemini IA)";
            default: return "🤖 IA (Gemini)";
        }
    }
}