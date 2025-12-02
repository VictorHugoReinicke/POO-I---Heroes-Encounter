package com.classes.DTO;

import java.util.Random;
public class CalculadoraCombate {
//valeu por ter adicionado o meu código, o intelij da muito problema pra dar commit
    private static final Random random = new Random();
    
    public static ResultadoAtaque calcularAtaqueFisico(Jogador atacante, Inimigo defensor) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusDano();
            
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
                danoFinal = (int)(danoFinal * 1.5);
            }
        }
        
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        danoFinal = Math.max(1, danoFinal - defesaInimigo);
        
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    public static ResultadoAtaque calcularAtaqueMagico(Jogador atacante, Inimigo defensor, Habilidade habilidade) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusMagico();
            
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
                danoFinal = (int)(danoFinal * 1.5);
            }
        }
        
        danoFinal = (int)(danoFinal * habilidade.getFatorDano());
        
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        int defesaEfetiva = defesaInimigo / 4;
        danoFinal = Math.max(1, danoFinal - defesaEfetiva);
        
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    public static boolean verificarEsquivaJogador(Jogador defensor) {
        return GerenciadorStatus.verificarEsquiva(defensor);
    }
    
    public static ResultadoAtaque calcularAtaqueInimigo(Inimigo inimigo, Jogador defensor) {
        int danoBase = inimigo.getAtaque();
        boolean critico = false;
        
        if (random.nextInt(100) < 5) {
            critico = true;
            danoBase = (int)(danoBase * 1.5);
        }
        
        int defesaJogador = defensor.getDefesa();
        
        int danoFinal = Math.max(1, danoBase - defesaJogador);
        
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    public static ResultadoAtaque calcularGolpeForte(Jogador atacante, Inimigo defensor) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusDano();
            
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
            }
        }
        
        danoFinal *= 2;
        
        if (!critico && random.nextInt(100) < 20) { // 20% chance extra
            critico = true;
        }
        
        if (critico) {
            danoFinal = (int)(danoFinal * 1.5);
        }
        
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        danoFinal = Math.max(1, danoFinal - defesaInimigo);
        
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    private static ItemArma getArmaEquipada(Jogador jogador) {
        if (jogador.getInventario() != null) {
            for (JogadorItem ji : jogador.getInventario()) {
                if (ji.isEquipado() && ji.getItem() instanceof ItemArma) {
                    return (ItemArma) ji.getItem();
                }
            }
        }
        return null;
    }
    
    public static String getInfoArmaEquipada(Jogador jogador) {
        ItemArma arma = getArmaEquipada(jogador);
        if (arma != null) {
            return String.format("%s | Dano: +%d | Mágico: +%d | Crítico: %d%%",
                arma.getNome(), arma.getBonusDano(), arma.getBonusMagico(), arma.getBonusCritico());
        }
        return "Nenhuma arma equipada";
    }
}