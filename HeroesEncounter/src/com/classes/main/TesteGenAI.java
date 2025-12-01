package com.classes.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TesteGenAI {

    private static final String API_KEY = "AIzaSyAhQWwM0sQ2Mqgmnqxr0s6jDSDx6aEQL18";

    public static void main(String[] args) throws Exception {

        String resposta = EnviarTexto("Olá, isso é um teste. Me retorne a palavra banana.");
        System.out.println("RESPOSTA DO MODELO:\n" + resposta);
    }

    public static String EnviarTexto(String texto) throws Exception {

        String modelo = "models/gemini-2.5-flash";

        HttpClient client = HttpClient.newHttpClient();

        String jsonInput = """
        {
            "contents": [{
                "parts": [{
                    "text": "%s"
                }]
            }]
        }
        """.formatted(texto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/" 
                                + modelo + ":generateContent?key=" + API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "ERRO (" + response.statusCode() + "): " + response.body();
        }

        return response.body();
    }
}
