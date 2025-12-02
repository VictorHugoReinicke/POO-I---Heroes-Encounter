package com.classes.main.gemini.util;

import com.classes.main.gemini.api.*;

public class GeminiConnectionTester {
    
    public static void testarConexao() {
        if (GeminiAPIConfig.getApiKey() == null) {
            System.err.println("❌ API Key não configurada!");
            return;
        }
        
        System.out.println("🧪 Testando conexão com Gemini API...");
        System.out.println("📡 Modelo principal: " + GeminiAPIConfig.MODELO_PRINCIPAL);
        
        try {
            GeminiAPIClient client = new GeminiAPIClient();
            String resposta = client.chamarAPI("Responda apenas com a palavra 'CONECTADO'", 
                GeminiAPIConfig.MODELO_PRINCIPAL);
            
            if (resposta != null && resposta.contains("CONECTADO")) {
                System.out.println("✅ Conexão estabelecida com sucesso!");
                System.out.println("🎯 Modelo principal funcionando: " + GeminiAPIConfig.MODELO_PRINCIPAL);
            } else {
                System.out.println("⚠️ Conexão OK, resposta: " + resposta);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Falha no modelo principal: " + e.getMessage());
            testarModeloReserva();
        }
    }
    
    private static void testarModeloReserva() {
        try {
            System.out.println("🔄 Testando modelo reserva: " + GeminiAPIConfig.MODELO_RESERVA);
            GeminiAPIClient client = new GeminiAPIClient();
            String resposta = client.chamarAPI("Resposta: OK", GeminiAPIConfig.MODELO_RESERVA);
            System.out.println("✅ Modelo reserva funcionando: " + resposta);
        } catch (Exception e) {
            System.err.println("❌ Ambos os modelos falharam: " + e.getMessage());
        }
    }
    
    public static void testarRapido() {
        System.out.println("⚡ Teste rápido do Gemini AI");
        System.out.println("🔑 API Key configurada: " + (GeminiAPIConfig.getApiKey() != null));
        System.out.println("🎯 Modelo principal: " + GeminiAPIConfig.MODELO_PRINCIPAL);
        System.out.println("🔄 Modelo reserva: " + GeminiAPIConfig.MODELO_RESERVA);
        
        testarConexao();
    }
}