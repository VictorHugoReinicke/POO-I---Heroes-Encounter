package com.classes.networking;

import com.classes.DTO.*;

public class Multiplayer{
    
    /**
     * ✅ MÉTODO PÚBLICO ALTERNATIVO PARA CLONAR JOGADOR SEM PRECISAR DO MÉTODO PRIVADO
     */
    public static Jogador criarJogadorParaMultiplayer(Jogador original) {
        try {
            // Determina o tipo do jogador
            if (original instanceof Guerreiro) {
                return criarGuerreiro((Guerreiro) original);
            } else if (original instanceof Mago) {
                return criarMago((Mago) original);
            } else if (original instanceof Paladino) {
                return criarPaladino((Paladino) original);
            } else {
                throw new IllegalArgumentException("Tipo de jogador não suportado: " + original.getClass().getSimpleName());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar jogador para multiplayer: " + e.getMessage());
            return criarJogadorBasico(original);
        }
    }
    
    private static Guerreiro criarGuerreiro(Guerreiro original) {
        Guerreiro clone = new Guerreiro();
        copiarAtributosBasicos(original, clone);
        return clone;
    }
    
    private static Mago criarMago(Mago original) {
        Mago clone = new Mago();
        copiarAtributosBasicos(original, clone);
        clone.setDanoMagico(original.getDanoMagico());
        return clone;
    }
    
    private static Paladino criarPaladino(Paladino original) {
        Paladino clone = new Paladino();
        copiarAtributosBasicos(original, clone);
        return clone;
    }
    
    private static void copiarAtributosBasicos(Jogador original, Jogador clone) {
        clone.setId(original.getId());
        clone.setNome(original.getNome());
        clone.setHp(original.getHp());
        clone.setHpMax(original.getHpMax());
        clone.setMana(original.getMana());
        clone.setManaMax(original.getManaMax());
        clone.setAtaque(original.getAtaque());
        clone.setOuro(original.getOuro());
        
        // Copia inventário (referências compartilhadas são OK para multiplayer)
        if (original.getInventario() != null) {
            clone.setInventario(new java.util.ArrayList<>(original.getInventario()));
        }
        
        // Copia habilidades (referências compartilhadas são OK para multiplayer)
        if (original.getHabilidades() != null) {
            clone.setHabilidades(new java.util.ArrayList<>(original.getHabilidades()));
        }
        
        System.out.println("✅ Jogador criado para multiplayer: " + clone.getNome() + " (" + clone.getClass().getSimpleName() + ")");
    }
    
    private static Jogador criarJogadorBasico(Jogador original) {
        try {
            Jogador basico;
            
            if (original instanceof Guerreiro) {
                basico = new Guerreiro();
            } else if (original instanceof Mago) {
                basico = new Mago();
            } else if (original instanceof Paladino) {
                basico = new Paladino();
            } else {
                basico = new Guerreiro();
            }
            
            basico.setId(original.getId());
            basico.setNome(original.getNome());
            basico.setHp(original.getHp());
            basico.setHpMax(original.getHpMax());
            basico.setMana(original.getMana());
            basico.setManaMax(original.getManaMax());
            basico.setAtaque(original.getAtaque());
            basico.setOuro(original.getOuro());
            
            return basico;
            
        } catch (Exception e) {
            System.err.println("❌ Erro crítico no fallback: " + e.getMessage());
            return original;
        }
    }
}