package com.classes.main.gemini.api;

public class GeminiAPIConfig {
    public static final String MODELO_PRINCIPAL = "gemini-2.0-flash-001";
    public static final String MODELO_RESERVA = "gemini-1.5-flash";
    public static final int TIMEOUT_CONEXAO = 15000;
    public static final int TIMEOUT_LEITURA = 15000;
    
    public static String getApiKey() {
        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }
        
        String testKey = "AIzaSyCyKUuKGoJu4kloryKgJ2L7S0dHyAjC_Lg";
        if (testKey != null && !testKey.trim().isEmpty()) {
            System.out.println("⚠️ Usando API Key de teste");
            return testKey.trim();
        }
        
        return null;
    }
    
    public static String getUrl(String modelo) {
        String apiKey = getApiKey();
        if (apiKey == null) return null;
        
        return String.format(
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
            modelo, 
            apiKey
        );
    }
}