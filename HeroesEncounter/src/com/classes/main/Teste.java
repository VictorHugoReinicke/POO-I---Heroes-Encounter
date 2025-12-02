package com.classes.main;

import java.util.Random;

import com.classes.DTO.Besta;
import com.classes.DTO.Guerreiro;
import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;

public class Teste {
    public static void main(String[] args) {
        System.out.println("Testando Gemini AI...");

        try {
            Inimigo inimigo = new Besta();
            
            Jogador jogador = new Guerreiro();
            jogador.setNome("Herói Teste");
            jogador.setHp(80);
            jogador.setHpMax(100);
            jogador.setAtaque(12);
            
            System.out.println("Inimigo: " + inimigo.getNome());
            System.out.println("Jogador: " + jogador.getNome());
            
            String decisao = GeminiAI.decidirAcao(inimigo, jogador);
            System.out.println("Decisão da Gemini: " + decisao);
            System.out.println("Teste concluído!");
            
        } catch (Exception e) {
            System.err.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
            
            System.out.println("Usando IA local como fallback...");
            String decisaoFallback = usarIALocal();
            System.out.println("Decisão fallback: " + decisaoFallback);
        }
    }
    
    private static String usarIALocal() {
        String[] acoes = {"ATAQUE_NORMAL", "DEFENDER", "ATAQUE_FEROZ"};
        Random rand = new Random();
        return acoes[rand.nextInt(acoes.length)];
    }
}