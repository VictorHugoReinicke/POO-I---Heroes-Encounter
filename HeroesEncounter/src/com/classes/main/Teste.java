package com.classes.main;

import java.util.Random;

import com.classes.DTO.Besta;
import com.classes.DTO.Guerreiro;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;

public class Teste {
    public static void main(String[] args) {
        System.out.println("🧪 Testando Gemini AI...");
        
        // Método 1: Propriedade do sistema
        System.setProperty("GEMINI_API_KEY", "AIzaSyCyKUuKGoJu4kloryKgJ2L7S0dHyAjC_Lg");
        
        // Método 2: Ou use variável de ambiente (recomendado para produção)
        // export GEMINI_API_KEY="sua_chave_aqui"
        
        try {
            // Testar
            Inimigo inimigo = new Besta();
            
            // Configurar jogador
            Jogador jogador = new Guerreiro();
            jogador.setNome("Herói Teste");
            jogador.setHp(80);
            jogador.setHpMax(100);
            jogador.setAtaque(12);
            
            System.out.println("🎮 Inimigo: " + inimigo.getNome());
            System.out.println("🎮 Jogador: " + jogador.getNome());
            
            String decisao = GeminiAI.decidirAcao(inimigo, jogador);
            System.out.println("🎯 Decisão da Gemini: " + decisao);
            System.out.println("✅ Teste concluído!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback para IA local
            System.out.println("🔄 Usando IA local como fallback...");
            String decisaoFallback = usarIALocal();
            System.out.println("🎯 Decisão fallback: " + decisaoFallback);
        }
    }
    
    private static String usarIALocal() {
        // IA local simples como fallback
        String[] acoes = {"ATAQUE_NORMAL", "DEFENDER", "ATAQUE_FEROZ"};
        Random rand = new Random();
        return acoes[rand.nextInt(acoes.length)];
    }
}