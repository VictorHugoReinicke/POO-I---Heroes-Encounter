package com.classes.main;

import com.classes.DTO.*;
import com.classes.Enums.TipoIA;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class InimigoIA {
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final boolean USE_LOCAL_FALLBACK = true;
    
    public static String decidirAcao(Inimigo inimigo, Jogador jogador) {
        try {
            // Se for CHEFE ou quer usar API externa
            if (inimigo.getTipoIA() == TipoIA.CHEFE && !USE_LOCAL_FALLBACK) {
                return usarAPIAvançada(inimigo, jogador);
            }
            
            // Usa IA local para todos os tipos
            return usarIALocal(inimigo, jogador);
            
        } catch (Exception e) {
            System.err.println("Erro na API de IA, usando fallback local: " + e.getMessage());
            return usarIALocal(inimigo, jogador);
        }
    }
    
    private static String usarAPIAvançada(Inimigo inimigo, Jogador jogador) throws Exception {
        List<String> acoesDisponiveis = getAcoesDisponiveis(inimigo);
        
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", criarPrompt(inimigo, jogador, acoesDisponiveis));
        
        requestBody.put("messages", new JSONObject[]{message});
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.7);
        
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                JSONObject jsonResponse = new JSONObject(response.toString());
                String decision = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
                
                return parseDecision(decision, acoesDisponiveis);
            }
        } else {
            throw new RuntimeException("API retornou código: " + responseCode);
        }
    }
    
    private static String criarPrompt(Inimigo inimigo, Jogador jogador, List<String> acoesDisponiveis) {
        return String.format(
            "Você é um inimigo CHEFE em um jogo RPG. Decida sua próxima ação:\n\n" +
            "SITUAÇÃO:\n" +
            "- CHEFE: %s (HP: %d/%d, Ataque: %d, Defesa: %d)\n" +
            "- Jogador: %s (HP: %d/%d, Classe: %s)\n" +
            "- Ações disponíveis: %s\n\n" +
            "Como CHEFE, você deve:\n" +
            "1. Ser impiedoso e agressivo\n" +
            "2. Usar habilidades especiais quando oportuno\n" +
            "3. Nunca fugir (você é um CHEFE!)\n" +
            "4. Intimidar o jogador\n\n" +
            "Responda APENAS com uma destas ações: %s",
            inimigo.getNome(), inimigo.getHp(), inimigo.getHpMax(), 
            inimigo.getAtaque(), inimigo.getDefesa(),
            jogador.getNome(), jogador.getHp(), jogador.getHpMax(), getClasseJogador(jogador),
            String.join(", ", acoesDisponiveis),
            String.join(", ", acoesDisponiveis)
        );
    }
    
    private static String getClasseJogador(Jogador jogador) {
        if (jogador instanceof Guerreiro) return "Guerreiro";
        if (jogador instanceof Mago) return "Mago";
        if (jogador instanceof Paladino) return "Paladino";
        return "Desconhecida";
    }
    
    private static String parseDecision(String decision, List<String> acoesDisponiveis) {
        decision = decision.toUpperCase().replace(" ", "_");
        
        for (String acao : acoesDisponiveis) {
            if (decision.contains(acao)) {
                return acao;
            }
        }
        
        return "ATAQUE_PODEROSO"; // Fallback para chefes
    }
    
    private static String usarIALocal(Inimigo inimigo, Jogador jogador) {
        List<String> acoesDisponiveis = getAcoesDisponiveis(inimigo);
        Map<String, Double> pesos = calcularPesosPorPersonalidade(inimigo, jogador, acoesDisponiveis);
        
        return escolherAcaoBaseadaNosPesos(pesos, acoesDisponiveis);
    }
    
    private static Map<String, Double> calcularPesosPorPersonalidade(Inimigo inimigo, Jogador jogador, List<String> acoesDisponiveis) {
        Map<String, Double> pesos = new HashMap<>();
        double hpPercentInimigo = (double) inimigo.getHp() / inimigo.getHpMax();
        double hpPercentJogador = (double) jogador.getHp() / jogador.getHpMax();
        Random random = new Random();
        
        // Pesos base por tipo de IA
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                pesos.put("ATAQUE_NORMAL", 50.0);
                pesos.put("ATAQUE_PODEROSO", 40.0);
                pesos.put("DEFENDER", 5.0);
                pesos.put("GRITAR", 5.0);
                pesos.put("FUGIR", 0.0);
                break;
                
            case DEFENSIVA:
                pesos.put("ATAQUE_NORMAL", 25.0);
                pesos.put("ATAQUE_PODEROSO", 15.0);
                pesos.put("DEFENDER", 50.0);
                pesos.put("CURAR", 10.0);
                pesos.put("FUGIR", 0.0);
                break;
                
            case ESTRATEGICA:
                pesos.put("ATAQUE_NORMAL", 30.0);
                pesos.put("ATAQUE_PODEROSO", 35.0);
                pesos.put("DEFENDER", 20.0);
                pesos.put("BUFF_DEFESA", 10.0);
                pesos.put("DEBUFF_JOGADOR", 5.0);
                pesos.put("FUGIR", 0.0);
                break;
                
            case BALANCEADO:
                pesos.put("ATAQUE_NORMAL", 40.0);
                pesos.put("ATAQUE_PODEROSO", 30.0);
                pesos.put("DEFENDER", 25.0);
                pesos.put("GRITAR", 5.0);
                pesos.put("FUGIR", 0.0);
                break;
                
            case ALEATORIA:
                // Totalmente aleatório entre ações disponíveis
                for (String acao : acoesDisponiveis) {
                    pesos.put(acao, random.nextDouble() * 100);
                }
                break;
                
            case CHEFE:
                pesos.put("ATAQUE_PODEROSO", 40.0);
                pesos.put("ATAQUE_ESPECIAL_CHEFE", 30.0);
                pesos.put("GRITAR_GUERRA", 15.0);
                pesos.put("DEFENDER", 10.0);
                pesos.put("REGENERAR", 5.0);
                pesos.put("FUGIR", 0.0);
                break;
        }
        
        // Ajustes baseados na situação de combate
        aplicarAjustesSituacionais(pesos, hpPercentInimigo, hpPercentJogador, inimigo);
        
        // Remover ações não disponíveis
        for (String acao : new ArrayList<>(pesos.keySet())) {
            if (!acoesDisponiveis.contains(acao)) {
                pesos.remove(acao);
            }
        }
        
        return pesos;
    }
    
    private static void aplicarAjustesSituacionais(Map<String, Double> pesos, double hpInimigo, double hpJogador, Inimigo inimigo) {
        // Ajustes baseados na vida do inimigo
        if (hpInimigo < 0.3) {
            if (inimigo.getTipoIA() == TipoIA.DEFENSIVA || inimigo.getTipoIA() == TipoIA.BALANCEADO) {
                pesos.put("DEFENDER", pesos.getOrDefault("DEFENDER", 0.0) + 30.0);
                if (pesos.containsKey("CURAR")) {
                    pesos.put("CURAR", pesos.get("CURAR") + 20.0);
                }
            }
            
            // Chefes ficam mais perigosos quando estão quase morrendo
            if (inimigo.getTipoIA() == TipoIA.CHEFE) {
                pesos.put("ATAQUE_ESPECIAL_CHEFE", pesos.getOrDefault("ATAQUE_ESPECIAL_CHEFE", 0.0) + 25.0);
                pesos.put("GRITAR_GUERRA", pesos.getOrDefault("GRITAR_GUERRA", 0.0) + 15.0);
            }
        }
        
        // Ajustes baseados na vida do jogador
        if (hpJogador < 0.3) {
            // Se jogador está fraco, atacar mais
            pesos.put("ATAQUE_PODEROSO", pesos.getOrDefault("ATAQUE_PODEROSO", 0.0) + 20.0);
            pesos.put("ATAQUE_NORMAL", pesos.getOrDefault("ATAQUE_NORMAL", 0.0) + 15.0);
        } else if (hpJogador < 0.6) {
            // Jogador com vida média
            pesos.put("ATAQUE_PODEROSO", pesos.getOrDefault("ATAQUE_PODEROSO", 0.0) + 10.0);
        }
        
        // Inimigos mágicos ou com alto dano preferem ataques poderosos
        if (inimigo.getAtaque() > 25) {
            pesos.put("ATAQUE_PODEROSO", pesos.getOrDefault("ATAQUE_PODEROSO", 0.0) + 15.0);
        }
        
        // Inimigos com alta defesa são mais defensivos
        if (inimigo.getDefesa() > 20 && inimigo.getTipoIA() != TipoIA.AGRESSIVO) {
            pesos.put("DEFENDER", pesos.getOrDefault("DEFENDER", 0.0) + 10.0);
        }
    }
    
    private static List<String> getAcoesDisponiveis(Inimigo inimigo) {
        List<String> acoes = new ArrayList<>();
        
        // Ações básicas disponíveis para todos
        acoes.add("ATAQUE_NORMAL");
        acoes.add("DEFENDER");
        
        // Ações baseadas no tipo de IA
        switch (inimigo.getTipoIA()) {
            case AGRESSIVO:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
                
            case DEFENSIVA:
                acoes.add("CURAR");
                acoes.add("DEFESA_TOTAL");
                break;
                
            case ESTRATEGICA:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("BUFF_DEFESA");
                acoes.add("DEBUFF_JOGADOR");
                break;
                
            case BALANCEADO:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("GRITAR");
                break;
                
            case CHEFE:
                acoes.add("ATAQUE_PODEROSO");
                acoes.add("ATAQUE_ESPECIAL_CHEFE");
                acoes.add("GRITAR_GUERRA");
                acoes.add("REGENERAR");
                break;
                
            case ALEATORIA:
                // Todas as ações possíveis
                acoes.addAll(Arrays.asList(
                    "ATAQUE_PODEROSO", "GRITAR", "CURAR", 
                    "BUFF_DEFESA", "DEBUFF_JOGADOR", "ATAQUE_ESPECIAL"
                ));
                break;
        }
        
        // Inimigos normais podem fugir se estiverem muito feridos (exceto chefes)
        if (inimigo.getTipoIA() != TipoIA.CHEFE && 
            inimigo.getHp() < inimigo.getHpMax() * 0.3) {
            acoes.add("FUGIR");
        }
        
        return acoes;
    }
    
    private static String escolherAcaoBaseadaNosPesos(Map<String, Double> pesos, List<String> acoesDisponiveis) {
        if (pesos.isEmpty()) {
            return "ATAQUE_NORMAL"; // Fallback seguro
        }
        
        double totalWeight = pesos.values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalWeight == 0) {
            return "ATAQUE_NORMAL";
        }
        
        double randomValue = new Random().nextDouble() * totalWeight;
        double currentWeight = 0;
        
        for (Map.Entry<String, Double> entry : pesos.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue <= currentWeight) {
                return entry.getKey();
            }
        }
        
        return acoesDisponiveis.get(0);
    }
    
    // Método para obter descrição da IA (para logs e UI)
    public static String getDescricaoIA(TipoIA tipo) {
        switch (tipo) {
            case AGRESSIVO: return "⚔️ Agressivo - Ataca sem piedade";
            case DEFENSIVA: return "🛡️ Defensivo - Prioriza defesa";
            case ESTRATEGICA: return "🎯 Estratégico - Usa táticas inteligentes";
            case BALANCEADO: return "⚖️ Balanceado - Equilíbrio entre ataque e defesa";
            case ALEATORIA: return "🎲 Aleatório - Comportamento imprevisível";
            case CHEFE: return "👑 Chefe - Poderoso e impiedoso";
            default: return "Desconhecido";
        }
    }
}