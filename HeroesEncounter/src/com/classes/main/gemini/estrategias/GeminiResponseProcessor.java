package com.classes.main.gemini.estrategias;

import com.classes.DTO.*;
import com.classes.DTO.IA.IAActions;
import java.util.*;

public class GeminiResponseProcessor {
    
    public static String processarResposta(String resposta, Inimigo inimigo) {
        if (resposta == null) {
            System.out.println("⚠️ Resposta nula do Gemini");
            return "ATAQUE_NORMAL";
        }
        
        String respostaLimpa = resposta.trim().toUpperCase();
        System.out.println("📝 Resposta bruta do Gemini: " + resposta);
        System.out.println("🔍 Resposta processada: " + respostaLimpa);
        
        List<String> acoes = IAActions.getAcoesDisponiveis(inimigo);
        
        // 1. Verificar correspondência exata
        for (String acao : acoes) {
            if (respostaLimpa.equals(acao)) {
                System.out.println("✅ Ação encontrada (exata): " + acao);
                return acao;
            }
        }
        
        // 2. Verificar se contém o nome da ação
        for (String acao : acoes) {
            if (respostaLimpa.contains(acao) || acao.contains(respostaLimpa)) {
                System.out.println("✅ Ação encontrada (contém): " + acao);
                return acao;
            }
        }
        
        // 3. Mapeamento de palavras-chave
        if (tentarMapeamentoPalavrasChave(respostaLimpa, acoes) != null) {
            return tentarMapeamentoPalavrasChave(respostaLimpa, acoes);
        }
        
        // 4. Fallback inteligente
        System.out.println("⚠️ Resposta não reconhecida: " + resposta + ". Usando fallback inteligente.");
        return GeminiFallbackStrategy.getFallbackInteligente(inimigo, acoes);
    }
    
    private static String tentarMapeamentoPalavrasChave(String resposta, List<String> acoes) {
        Map<String, String> mapeamento = criarMapeamentoPalavrasChave();
        
        for (Map.Entry<String, String> entry : mapeamento.entrySet()) {
            if (resposta.contains(entry.getKey()) && acoes.contains(entry.getValue())) {
                System.out.println("✅ Ação encontrada (palavra-chave): " + entry.getValue());
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    private static Map<String, String> criarMapeamentoPalavrasChave() {
        Map<String, String> mapeamento = new HashMap<>();
        mapeamento.put("ATACAR", "ATAQUE_NORMAL");
        mapeamento.put("ATTACK", "ATAQUE_NORMAL");
        mapeamento.put("NORMAL", "ATAQUE_NORMAL");
        mapeamento.put("BASIC", "ATAQUE_NORMAL");
        
        mapeamento.put("POWER", "ATAQUE_PODEROSO");
        mapeamento.put("STRONG", "ATAQUE_PODEROSO");
        mapeamento.put("HEAVY", "ATAQUE_PODEROSO");
        
        mapeamento.put("SPECIAL", "ATAQUE_ESPECIAL");
        mapeamento.put("ULTIMATE", "ATAQUE_ESPECIAL");
        
        mapeamento.put("DEFEND", "DEFENDER");
        mapeamento.put("GUARD", "DEFENDER");
        mapeamento.put("BLOCK", "DEFENDER");
        mapeamento.put("PROTECT", "DEFENDER");
        
        mapeamento.put("HEAL", "CURAR");
        mapeamento.put("CURE", "CURAR");
        
        mapeamento.put("SHOUT", "GRITAR");
        mapeamento.put("YELL", "GRITAR");
        mapeamento.put("ROAR", "RUGIDO");
        
        mapeamento.put("RUN", "FUGIR");
        mapeamento.put("ESCAPE", "FUGIR");
        mapeamento.put("FLEE", "FUGIR");
        
        mapeamento.put("REGEN", "REGENERAR");
        mapeamento.put("REGENERATE", "REGENERAR");
        
        return mapeamento;
    }
}