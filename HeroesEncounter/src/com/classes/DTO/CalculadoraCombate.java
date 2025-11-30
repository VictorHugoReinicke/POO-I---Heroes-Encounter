package com.classes.DTO;

import java.util.Random;

public class CalculadoraCombate {
    private static final Random random = new Random();
    
    public static ResultadoAtaque calcularAtaqueFisico(Jogador atacante, Inimigo defensor) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        // ✅ APLICAR MODIFICADORES DE STATUS DO JOGADOR
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        // Aplicar bônus da arma equipada
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusDano();
            
            // CHANCE DE CRÍTICO
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
                danoFinal = (int)(danoFinal * 1.5);
            }
        }
        
        // ✅ CALCULAR DEFESA INIMIGA COM STATUS
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        // Reduzir pela defesa
        danoFinal = Math.max(1, danoFinal - defesaInimigo);
        
        // Variação aleatória (80% - 120%)
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    public static ResultadoAtaque calcularAtaqueMagico(Jogador atacante, Inimigo defensor, Habilidade habilidade) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        // ✅ APLICAR MODIFICADORES DE STATUS DO JOGADOR
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        // Aplicar bônus da arma mágica
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusMagico();
            
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
                danoFinal = (int)(danoFinal * 1.5);
            }
        }
        
        // Aplicar multiplicador da habilidade
        danoFinal = (int)(danoFinal * habilidade.getFatorDano());
        
        // ✅ CALCULAR DEFESA INIMIGA COM STATUS (dano mágico usa defesa reduzida)
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        // Dano mágico ignora parte da defesa (apenas 25% da defesa conta)
        int defesaEfetiva = defesaInimigo / 4;
        danoFinal = Math.max(1, danoFinal - defesaEfetiva);
        
        // Variação aleatória
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    // ✅ NOVO: Método para verificar esquiva (separado do cálculo de dano)
    public static boolean verificarEsquivaJogador(Jogador defensor) {
        return GerenciadorStatus.verificarEsquiva(defensor);
    }
    
    public static ResultadoAtaque calcularAtaqueInimigo(Inimigo inimigo, Jogador defensor) {
        int danoBase = inimigo.getAtaque();
        boolean critico = false;
        
        // Inimigos também podem dar crítico (5% base)
        if (random.nextInt(100) < 5) {
            critico = true;
            danoBase = (int)(danoBase * 1.5);
        }
        
        // ✅ CALCULAR DEFESA JOGADOR (versão simplificada sem modificador de status)
        int defesaJogador = defensor.getDefesa();
        
        // Reduzir pela defesa do jogador
        int danoFinal = Math.max(1, danoBase - defesaJogador);
        
        // Variação aleatória
        double variacao = 0.8 + (random.nextDouble() * 0.4);
        danoFinal = (int)(danoFinal * variacao);
        
        return new ResultadoAtaque(Math.max(1, danoFinal), critico);
    }
    
    // ✅ MÉTODO PARA GOLPE FORTE (Guerreiro)
    public static ResultadoAtaque calcularGolpeForte(Jogador atacante, Inimigo defensor) {
        int danoBase = atacante.getAtaque();
        boolean critico = false;
        int danoFinal = danoBase;
        
        // ✅ APLICAR MODIFICADORES DE STATUS DO JOGADOR
        double modificadorAtaque = GerenciadorStatus.calcularModificadorAtaqueJogador(atacante);
        danoFinal = (int)(danoFinal * (1 + modificadorAtaque));
        
        // Aplicar bônus da arma equipada
        ItemArma arma = getArmaEquipada(atacante);
        if (arma != null) {
            danoFinal += arma.getBonusDano();
            
            // Chance de crítico da arma
            if (random.nextInt(100) < arma.getBonusCritico()) {
                critico = true;
            }
        }
        
        // ✅ GOLPE FORTE: DOBRO DE DANO
        danoFinal *= 2;
        
        // Chance extra de crítico para Golpe Forte
        if (!critico && random.nextInt(100) < 20) { // 20% chance extra
            critico = true;
        }
        
        if (critico) {
            danoFinal = (int)(danoFinal * 1.5);
        }
        
        // ✅ CALCULAR DEFESA INIMIGA COM STATUS
        int defesaInimigo = defensor.getDefesa();
        double modificadorDefesa = GerenciadorStatus.calcularModificadorDefesaInimigo(defensor);
        defesaInimigo = (int)(defesaInimigo * (1 + modificadorDefesa));
        
        // Reduzir pela defesa
        danoFinal = Math.max(1, danoFinal - defesaInimigo);
        
        // Variação aleatória
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
    
    // Método para mostrar informações da arma equipada
    public static String getInfoArmaEquipada(Jogador jogador) {
        ItemArma arma = getArmaEquipada(jogador);
        if (arma != null) {
            return String.format("⚔️ %s | Dano: +%d | Mágico: +%d | Crítico: %d%%", 
                arma.getNome(), arma.getBonusDano(), arma.getBonusMagico(), arma.getBonusCritico());
        }
        return "⚔️ Nenhuma arma equipada";
    }
}