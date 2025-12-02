package com.classes.main.gemini.api;

import com.classes.DTO.*;
import com.classes.main.gemini.estrategias.*;
import com.classes.main.gemini.util.*;

public class GeminiAI {
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        // Verificar se a API Key está configurada
        if (GeminiAPIConfig.getApiKey() == null) {
            System.err.println("❌ API Key não configurada, usando InimigoIA como fallback");
            return com.classes.DTO.InimigoIA.decidirAcao(inimigo, jogador);
        }
        
        System.out.println("🤖 Iniciando decisão da IA Gemini...");
        System.out.println("📊 Inimigo: " + inimigo.getNome() + " (HP: " + inimigo.getHp() + "/" + inimigo.getHpMax() + ")");
        System.out.println("🎮 Jogador: " + jogador.getNome() + " (HP: " + jogador.getHp() + "/" + jogador.getHpMax() + ")");
        
        // Tentar primeiro o modelo principal
        try {
            return tentarComModelo(GeminiAPIConfig.MODELO_PRINCIPAL, inimigo, jogador);
        } catch (Exception e) {
            System.err.println("❌ Erro com " + GeminiAPIConfig.MODELO_PRINCIPAL + ": " + e.getMessage());
            
            // Tentar modelo reserva
            try {
                System.out.println("🔄 Tentando modelo reserva: " + GeminiAPIConfig.MODELO_RESERVA);
                return tentarComModelo(GeminiAPIConfig.MODELO_RESERVA, inimigo, jogador);
            } catch (Exception e2) {
                System.err.println("❌ Erro com " + GeminiAPIConfig.MODELO_RESERVA + ": " + e2.getMessage());
            }
        }
        
        System.out.println("🔄 Gemini falhou, usando InimigoIA como fallback...");
        return com.classes.DTO.InimigoIA.decidirAcao(inimigo, jogador);
    }
    
    private static String tentarComModelo(String modelo, Inimigo inimigo, Jogador jogador) throws Exception {
        System.out.println("🔍 Usando modelo: " + modelo);
        
        String prompt = GeminiPromptBuilder.criarPromptCompleto(inimigo, jogador);
        System.out.println("📝 Prompt criado (tamanho: " + prompt.length() + " chars)");
        
        GeminiAPIClient client = new GeminiAPIClient();
        String resposta = client.chamarAPI(prompt, modelo);
        
        if (resposta != null && !resposta.trim().isEmpty()) {
            String acao = GeminiResponseProcessor.processarResposta(resposta, inimigo);
            System.out.println("✅ Ação escolhida pelo Gemini: " + acao);
            return acao;
        }
        
        throw new Exception("Resposta vazia do modelo");
    }
    
    // Métodos de conveniência - delegam para as classes especializadas
    
    public static String getDescricaoIA(com.classes.Enums.TipoIA tipo) {
        return GeminiDescricaoIA.getDescricao(tipo);
    }
    
    public static void testarConexao() {
        GeminiConnectionTester.testarConexao();
    }
    
    public static void testarRapido() {
        GeminiConnectionTester.testarRapido();
    }
}