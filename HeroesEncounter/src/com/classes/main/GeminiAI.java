package com.classes.main;

import com.classes.DTO.*;
import com.classes.Enums.TipoIA;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class GeminiAI {
    
    private static final String API_KEY = getApiKey();
    
    private static final String MODELO_PRINCIPAL = "gemini-2.0-flash-001";
    private static final String MODELO_RESERVA = "gemini-1.5-flash";
    
    private static String getApiKey() {
        // 1. Variável de ambiente
        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }
        
        // 2. Usar a chave de teste fornecida
        String testKey = "AIzaSyCyKUuKGoJu4kloryKgJ2L7S0dHyAjC_Lg";
        if (testKey != null && !testKey.trim().isEmpty()) {
            System.out.println("⚠️ Usando API Key de teste");
            return testKey.trim();
        }
        
        return null;
    }
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        // Verificar se a API Key está configurada
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("❌ API Key não configurada, usando fallback local");
            return usarFallbackLocal(inimigo, jogador);
        }
        
        System.out.println("🤖 Iniciando decisão da IA Gemini...");
        System.out.println("📊 Inimigo: " + inimigo.getNome() + " (HP: " + inimigo.getHp() + "/" + inimigo.getHpMax() + ")");
        System.out.println("🎮 Jogador: " + jogador.getNome() + " (HP: " + jogador.getHp() + "/" + jogador.getHpMax() + ")");
        
        // Tentar primeiro o modelo principal (agora gemini-2.0-flash-001)
        try {
            return tentarComModelo(MODELO_PRINCIPAL, inimigo, jogador);
        } catch (Exception e) {
            System.err.println("❌ Erro com " + MODELO_PRINCIPAL + ": " + e.getMessage());
            
            // Tentar modelo reserva
            try {
                System.out.println("🔄 Tentando modelo reserva: " + MODELO_RESERVA);
                return tentarComModelo(MODELO_RESERVA, inimigo, jogador);
            } catch (Exception e2) {
                System.err.println("❌ Erro com " + MODELO_RESERVA + ": " + e2.getMessage());
            }
        }
        
        System.out.println("🔄 Usando IA local como fallback...");
        return usarFallbackLocal(inimigo, jogador);
    }
    
    private static String tentarComModelo(String modelo, Inimigo inimigo, Jogador jogador) throws Exception {
        System.out.println("🔍 Usando modelo: " + modelo);
        
        String prompt = criarPromptEficiente(inimigo, jogador);
        System.out.println("📝 Prompt criado (tamanho: " + prompt.length() + " chars)");
        
        String resposta = chamarGeminiAPI(prompt, modelo);
        
        if (resposta != null && !resposta.trim().isEmpty()) {
            String acao = processarRespostaEficiente(resposta, inimigo);
            System.out.println("✅ Ação escolhida pelo Gemini: " + acao);
            return acao;
        }
        
        throw new Exception("Resposta vazia do modelo");
    }
    
    private static String chamarGeminiAPI(String prompt, String modelo) throws Exception {
        String urlString = String.format(
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
            modelo, 
            API_KEY
        );
        
        // JSON da requisição
        JSONObject request = new JSONObject();
        
        // Conteúdo
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.put(part);
        content.put("parts", parts);
        
        JSONArray contents = new JSONArray();
        contents.put(content);
        request.put("contents", contents);
        
        // Configurações otimizadas para gemini-2.0-flash-001
        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 50);
        
        request.put("generationConfig", generationConfig);
        
        // Configurar conexão
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoOutput(true);
        
        // Enviar dados
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = request.toString().getBytes("UTF-8");
            os.write(input, 0, input.length);
        }
        
        // Obter resposta
        int responseCode = connection.getResponseCode();
        System.out.println("📡 Código HTTP: " + responseCode);
        
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                
                String responseBody = response.toString();
                System.out.println("📥 Resposta bruta (início): " + 
                    responseBody.substring(0, Math.min(100, responseBody.length())) + "...");
                
                // Parsear resposta
                JSONObject jsonResponse = new JSONObject(responseBody);
                
                // Extrair texto da resposta
                if (jsonResponse.has("candidates") && 
                    jsonResponse.getJSONArray("candidates").length() > 0) {
                    
                    JSONObject candidate = jsonResponse.getJSONArray("candidates").getJSONObject(0);
                    
                    // Verificar finishReason
                    String finishReason = candidate.optString("finishReason", "");
                    if ("MAX_TOKENS".equals(finishReason)) {
                        System.out.println("⚠️ Modelo atingiu limite de tokens");
                    }
                    
                    // Tentar extrair conteúdo
                    if (candidate.has("content")) {
                        JSONObject contentObj = candidate.getJSONObject("content");
                        if (contentObj.has("parts")) {
                            JSONArray partsArray = contentObj.getJSONArray("parts");
                            if (partsArray.length() > 0) {
                                String text = partsArray.getJSONObject(0).optString("text", "").trim();
                                if (!text.isEmpty()) {
                                    return text;
                                }
                            }
                        }
                    }
                }
                
                throw new Exception("Resposta da API em formato inesperado");
            }
            
        } else {
            // Ler erro
            String errorMessage = "HTTP " + responseCode;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "UTF-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
                
                try {
                    JSONObject errorJson = new JSONObject(errorResponse.toString());
                    if (errorJson.has("error")) {
                        errorMessage = errorJson.getJSONObject("error").getString("message");
                    }
                } catch (JSONException e) {
                    errorMessage += ": " + errorResponse.toString();
                }
            } catch (Exception e) {
                errorMessage += " (não foi possível ler detalhes)";
            }
            
            throw new Exception(errorMessage);
        }
    }
    
    private static String criarPromptEficiente(Inimigo inimigo, Jogador jogador) {
        List<String> acoes = getAcoesDisponiveis(inimigo);
        String acoesStr = String.join(", ", acoes);
        
        return String.format(
            "Você é '%s' (HP: %d/%d) lutando contra %s (HP: %d/%d).\n" +
            "Personalidade: %s.\n" +
            "Escolha uma ação:\n%s\n\n" +
            "Responda APENAS com o nome da ação em MAIÚSCULAS. Exemplo: ATAQUE_NORMAL",
            
            inimigo.getNome(),
            inimigo.getHp(), inimigo.getHpMax(),
            jogador.getNome(),
            jogador.getHp(), jogador.getHpMax(),
            inimigo.getTipoIA().toString(),
            acoesStr
        );
    }
    
    private static List<String> getAcoesDisponiveis(Inimigo inimigo) {
        List<String> acoes = new ArrayList<>();
        
        acoes.add("ATAQUE_NORMAL");
        acoes.add("DEFENDER");
        
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
            case DEFENSIVA:
                acoes.add("CURAR");
                acoes.add("DEFENDER_FORTE");
                break;
            case ESTRATEGICA:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("BUFF_DEFESA");
                break;
            case CHEFE:
                acoes.add("ATAQUE_ESPECIAL");
                acoes.add("GRITAR_GUERRA");
                acoes.add("REGENERAR");
                break;
            case ALEATORIA:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                acoes.add("CURAR");
                break;
            default: // BALANCEADO
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
        }
        
        if (inimigo.getTipoIA() != TipoIA.CHEFE && 
            inimigo.getHp() < inimigo.getHpMax() * 0.3) {
            acoes.add("FUGIR");
        }
        
        return acoes;
    }
    
    private static String processarRespostaEficiente(String resposta, Inimigo inimigo) {
        if (resposta == null) return "ATAQUE_NORMAL";
        
        String respostaLimpa = resposta.trim().toUpperCase();
        
        respostaLimpa = respostaLimpa.replaceAll("[^A-Z0-9_]", "");
        
        System.out.println("📝 Resposta processada: " + respostaLimpa);
        
        List<String> acoes = getAcoesDisponiveis(inimigo);
        
        // 1. Verificar se a resposta já é uma ação válida
        for (String acao : acoes) {
            if (respostaLimpa.equals(acao)) {
                return acao;
            }
        }
        
        // 2. Verificar se contém o nome de uma ação
        for (String acao : acoes) {
            if (respostaLimpa.contains(acao)) {
                return acao;
            }
        }
        
        // 3. Mapeamento direto de palavras-chave
        if (respostaLimpa.contains("ATACAR") || respostaLimpa.contains("ATAC") || respostaLimpa.contains("ATTACK")) {
            return "ATAQUE_NORMAL";
        }
        if (respostaLimpa.contains("PODEROSO") || respostaLimpa.contains("POWER")) {
            return "ATAQUE_PODEROSO";
        }
        if (respostaLimpa.contains("ESPECIAL") || respostaLimpa.contains("SPECIAL")) {
            return "ATAQUE_ESPECIAL";
        }
        if (respostaLimpa.contains("DEFENDER") || respostaLimpa.contains("DEFESA") || respostaLimpa.contains("DEFEND")) {
            return "DEFENDER";
        }
        if (respostaLimpa.contains("CURAR") || respostaLimpa.contains("HEAL")) {
            return acoes.contains("CURAR") ? "CURAR" : "DEFENDER";
        }
        if (respostaLimpa.contains("GRITAR") || respostaLimpa.contains("SHOUT")) {
            return "GRITAR";
        }
        if (respostaLimpa.contains("FUGIR") || respostaLimpa.contains("RUN")) {
            return acoes.contains("FUGIR") ? "FUGIR" : "DEFENDER";
        }
        if (respostaLimpa.contains("REGENERAR") || respostaLimpa.contains("REGEN")) {
            return acoes.contains("REGENERAR") ? "REGENERAR" : "DEFENDER";
        }
        
        // 4. Fallback baseado no tipo
        System.out.println("⚠️ Resposta não reconhecida: " + resposta + ". Usando fallback.");
        return getFallbackPorTipo(inimigo);
    }
    
    private static String getFallbackPorTipo(Inimigo inimigo) {
        Random rand = new Random();
        
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
            case CHEFE:
                return "ATAQUE_NORMAL";
            case DEFENSIVA:
                return inimigo.getHp() < 50 ? "DEFENDER" : "ATAQUE_NORMAL";
            case ESTRATEGICA:
                return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
            case ALEATORIA:
                List<String> acoes = getAcoesDisponiveis(inimigo);
                return acoes.get(rand.nextInt(acoes.size()));
            default: // BALANCEADO
                return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
        }
    }
    
    private static String usarFallbackLocal(Inimigo inimigo, Jogador jogador) {
        // IA local melhorada
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        
        List<String> acoes = getAcoesDisponiveis(inimigo);
        Random rand = new Random();
        
        // Lógica baseada na personalidade
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                if (hpPercentJogador < 0.5 && acoes.contains("ATAQUE_PODEROSO")) {
                    return "ATAQUE_PODEROSO";
                }
                return "ATAQUE_NORMAL";
                
            case DEFENSIVA:
                if (hpPercentInimigo < 0.4 && acoes.contains("CURAR")) {
                    return "CURAR";
                }
                if (hpPercentInimigo < 0.7) {
                    return "DEFENDER";
                }
                return "ATAQUE_NORMAL";
                
            case ESTRATEGICA:
                if (hpPercentJogador < 0.4 && hpPercentInimigo > 0.6) {
                    return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
                }
                if (hpPercentInimigo < 0.5) {
                    return "DEFENDER";
                }
                return "ATAQUE_NORMAL";
                
            case CHEFE:
                if (hpPercentInimigo < 0.3 && acoes.contains("REGENERAR")) {
                    return "REGENERAR";
                }
                if (hpPercentJogador < 0.5 && acoes.contains("ATAQUE_ESPECIAL")) {
                    return "ATAQUE_ESPECIAL";
                }
                if (rand.nextDouble() < 0.3 && acoes.contains("GRITAR_GUERRA")) {
                    return "GRITAR_GUERRA";
                }
                return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
                
            case ALEATORIA:
                return acoes.get(rand.nextInt(acoes.size()));
                
            default: // BALANCEADO
                if (hpPercentInimigo < 0.4) {
                    return "DEFENDER";
                }
                if (hpPercentJogador < 0.4 && rand.nextBoolean()) {
                    return acoes.contains("ATAQUE_PODEROSO") ? "ATAQUE_PODEROSO" : "ATAQUE_NORMAL";
                }
                return rand.nextBoolean() ? "ATAQUE_NORMAL" : "DEFENDER";
        }
    }
    
    // Teste de conexão atualizado para usar o novo modelo principal
    public static void testarConexao() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("❌ API Key não configurada!");
            return;
        }
        
        System.out.println("🧪 Testando conexão com Gemini API...");
        System.out.println("📡 Modelo principal: " + MODELO_PRINCIPAL);
        
        try {
            String prompt = "Responda apenas com a palavra 'CONECTADO'";
            String resposta = chamarGeminiAPI(prompt, MODELO_PRINCIPAL);
            
            if (resposta != null && resposta.contains("CONECTADO")) {
                System.out.println("✅ Conexão estabelecida com sucesso!");
                System.out.println("🎯 Modelo principal funcionando: " + MODELO_PRINCIPAL);
            } else {
                System.out.println("⚠️ Conexão OK, resposta: " + resposta);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Falha no modelo principal: " + e.getMessage());
            
            // Testar modelo reserva
            try {
                System.out.println("🔄 Testando modelo reserva: " + MODELO_RESERVA);
                String resposta = chamarGeminiAPI("Resposta: OK", MODELO_RESERVA);
                System.out.println("✅ Modelo reserva funcionando: " + resposta);
            } catch (Exception e2) {
                System.err.println("❌ Ambos os modelos falharam");
            }
        }
    }
    
    public static String getDescricaoIA(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo (Gemini)";
            case DEFENSIVA: return "🛡️ Defensivo (Gemini)";
            case ESTRATEGICA: return "🎯 Estratégico (Gemini)";
            case BALANCEADO: return "⚖️ Balanceado (Gemini)";
            case ALEATORIA: return "🎲 Aleatório (Gemini)";
            case CHEFE: return "👑 Chefe (Gemini)";
            default: return "🤖 IA (Gemini)";
        }
    }
    
    // Método para testar rapidamente
    public static void testarRapido() {
        System.out.println("⚡ Teste rápido do Gemini AI");
        System.out.println("🔑 API Key configurada: " + (API_KEY != null && !API_KEY.isEmpty()));
        System.out.println("🎯 Modelo principal: " + MODELO_PRINCIPAL);
        System.out.println("🔄 Modelo reserva: " + MODELO_RESERVA);
        
        testarConexao();
    }
}