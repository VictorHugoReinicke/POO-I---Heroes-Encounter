package com.classes.DTO.IA;

import java.util.Random;

public abstract class IABase implements IIAStrategy {
    protected Random rand = new Random();
    
    protected String decidirComChances(String[] opcoes, double[] chances) {
        double total = 0;
        for (double chance : chances) {
            total += chance;
        }
        
        double valor = rand.nextDouble() * total;
        double acumulado = 0;
        
        for (int i = 0; i < opcoes.length; i++) {
            acumulado += chances[i];
            if (valor < acumulado) {
                return opcoes[i];
            }
        }
        
        return opcoes[opcoes.length - 1];
    }
}