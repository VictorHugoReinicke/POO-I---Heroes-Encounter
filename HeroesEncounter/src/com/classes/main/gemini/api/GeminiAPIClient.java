package com.classes.main.gemini.api;

import org.json.*;
import java.io.*;
import java.net.*;

public class GeminiAPIClient {
    
    public String chamarAPI(String prompt, String modelo) throws Exception {
        String urlString = GeminiAPIConfig.getUrl(modelo);
        if (urlString == null) {
            throw new Exception("API Key não configurada");
        }
        
        JSONObject request = criarRequestJSON(prompt);
        
        HttpURLConnection connection = configurarConexao(urlString);
        enviarDados(connection, request);
        
        return processarResposta(connection);
    }
    
    private JSONObject criarRequestJSON(String prompt) {
        JSONObject request = new JSONObject();
        
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.put(part);
        content.put("parts", parts);
        
        JSONArray contents = new JSONArray();
        contents.put(content);
        request.put("contents", contents);
        
        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.8);
        generationConfig.put("maxOutputTokens", 50);
        request.put("generationConfig", generationConfig);
        
        return request;
    }
    
    private HttpURLConnection configurarConexao(String urlString) throws Exception {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(GeminiAPIConfig.TIMEOUT_CONEXAO);
        connection.setReadTimeout(GeminiAPIConfig.TIMEOUT_LEITURA);
        connection.setDoOutput(true);
        
        return connection;
    }
    
    private void enviarDados(HttpURLConnection connection, JSONObject request) throws Exception {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = request.toString().getBytes("UTF-8");
            os.write(input, 0, input.length);
        }
    }
    
    private String processarResposta(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        System.out.println("📡 Código HTTP: " + responseCode);
        
        if (responseCode == 200) {
            return lerRespostaSucesso(connection);
        } else {
            throw criarExceptionErro(connection, responseCode);
        }
    }
    
    private String lerRespostaSucesso(HttpURLConnection connection) throws Exception {
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
            
            return extrairTextoResposta(responseBody);
        }
    }
    
    private String extrairTextoResposta(String responseBody) throws Exception {
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        if (jsonResponse.has("candidates") && 
            jsonResponse.getJSONArray("candidates").length() > 0) {
            
            JSONObject candidate = jsonResponse.getJSONArray("candidates").getJSONObject(0);
            
            String finishReason = candidate.optString("finishReason", "");
            if ("MAX_TOKENS".equals(finishReason)) {
                System.out.println("⚠️ Modelo atingiu limite de tokens");
            }
            
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
    
    private Exception criarExceptionErro(HttpURLConnection connection, int responseCode) throws IOException {
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
        
        return new Exception(errorMessage);
    }
}