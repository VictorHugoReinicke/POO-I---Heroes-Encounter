package com.classes.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PermissoesIA {
    public static void main(String[] args) throws Exception {

        String apiKey = "AIzaSyAhQWwM0sQ2Mqgmnqxr0s6jDSDx6aEQL18";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("STATUS: " + response.statusCode());
        System.out.println("MODELOS DISPONÍVEIS:");
        System.out.println(response.body());
    }
}
