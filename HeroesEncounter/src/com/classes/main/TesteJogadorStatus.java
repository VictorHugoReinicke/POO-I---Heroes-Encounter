package com.classes.main;

import com.classes.BO.JogadorBO;
import com.classes.BO.StatusBO;
import com.classes.BO.JogadorStatusBO;
import com.classes.DTO.Guerreiro;
import com.classes.DTO.Jogador;
import com.classes.DTO.Status;
import com.classes.DTO.JogadorStatus;

import java.util.List;

public class TesteJogadorStatus {

    public static void main(String[] args) {
        
        JogadorBO jogadorBO = new JogadorBO();
        StatusBO statusBO = new StatusBO();
        JogadorStatusBO jsBO = new JogadorStatusBO();

        // ----------------------------------------------------
        // --- 1. PREPARAÇÃO: CRIAÇÃO DE DADOS DE TESTE ---
        // ----------------------------------------------------
        System.out.println("--- 1. PREPARAÇÃO DE DADOS ---");
        
        // A. Criação/Busca do Jogador de Teste
        Jogador heroi = new Guerreiro(); 
        heroi.setNome("Herói Status Teste");
        if (!jogadorBO.existe(heroi)) jogadorBO.inserir(heroi);
        heroi = jogadorBO.procurarPorNome(heroi);
        if (heroi == null || heroi.getId() == 0) return; 
        System.out.printf("✅ Jogador '%s' pronto (ID: %d).\n", heroi.getNome(), heroi.getId());

        // B. Criação/Busca dos Status de Teste
        
        // Status 1: Veneno (Dano por turno)
        Status veneno = new Status("Veneno", 10, 0.0, 3); // Dano 10, Dura 3 turnos
        if (!statusBO.existe(veneno)) statusBO.inserir(veneno);
        veneno = statusBO.procurarPorNome(veneno);
        System.out.printf("✅ Status '%s' pronto (ID: %d, Duração: %d).\n", veneno.getNome(), veneno.getId(), veneno.getDuracaoTurnos());
        
        // Status 2: Atordoamento (Modificador Defesa)
        Status atordoamento = new Status("Atordoamento", 0, 0.5, 2); // -50% Defesa, Dura 2 turnos
        if (!statusBO.existe(atordoamento)) statusBO.inserir(atordoamento);
        atordoamento = statusBO.procurarPorNome(atordoamento);
        System.out.printf("✅ Status '%s' pronto (ID: %d, Duração: %d).\n", atordoamento.getNome(), atordoamento.getId(), atordoamento.getDuracaoTurnos());

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 2. TESTE DE APLICAÇÃO E RENOVAÇÃO ---
        // ----------------------------------------------------
        System.out.println("--- 2. TESTE DE APLICAÇÃO ---");
        
        // A. Aplica o Status 1 (Veneno)
        jsBO.aplicarStatus(heroi.getId(), veneno.getId());
        
        // B. Aplica o Status 2 (Atordoamento)
        jsBO.aplicarStatus(heroi.getId(), atordoamento.getId());
        
        // C. Tenta aplicar o Status 1 novamente (Deve renovar a duração para 3)
        System.out.println("\n-> Teste de Renovação:");
        jsBO.aplicarStatus(heroi.getId(), veneno.getId());
        
        // D. Verificação inicial
        List<JogadorStatus> statusAtivos = jsBO.listarStatusAtivos(heroi.getId());
        System.out.printf("✅ Verificação Inicial: Jogador tem %d status ativos.\n", statusAtivos.size());
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 3. TESTE DE DECAIMENTO (SIMULAÇÃO DE TURNO) ---
        // ----------------------------------------------------
        System.out.println("--- 3. TESTE DE DECAIMENTO (Turno 1) ---");
        
        System.out.println("-> Processando fim do Turno 1:");
        jsBO.processarFimDeTurno(heroi.getId());

        // Verifica a duração após o Turno 1
        JogadorStatus venenoT1 = jsBO.procurarRegistro(heroi.getId(), veneno.getId());
        JogadorStatus atordoamentoT1 = jsBO.procurarRegistro(heroi.getId(), atordoamento.getId());
        
        if (venenoT1 != null && venenoT1.getTurnosRestantes() == 2) {
            System.out.println("✅ Veneno: Duração correta após T1 (Restam 2).");
        } else {
             System.out.println("❌ Veneno: Erro na duração após T1.");
        }
        
        if (atordoamentoT1 != null && atordoamentoT1.getTurnosRestantes() == 1) {
            System.out.println("✅ Atordoamento: Duração correta após T1 (Restam 1).");
        } else {
             System.out.println("❌ Atordoamento: Erro na duração após T1.");
        }
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 4. TESTE DE EXPIRAÇÃO E REMOÇÃO ---
        // ----------------------------------------------------
        System.out.println("--- 4. TESTE DE EXPIRAÇÃO (Turno 2) ---");

        System.out.println("-> Processando fim do Turno 2:");
        jsBO.processarFimDeTurno(heroi.getId());

        // Atordoamento deve ter expirado (duração 2)
        JogadorStatus atordoamentoT2 = jsBO.procurarRegistro(heroi.getId(), atordoamento.getId());

        if (atordoamentoT2 == null) {
            System.out.println("✅ Atordoamento: Removido corretamente após expirar.");
        } else {
             System.out.println("❌ Atordoamento: Erro! Não foi removido.");
        }
        
        // Veneno deve ter decaído para 1
        JogadorStatus venenoT2 = jsBO.procurarRegistro(heroi.getId(), veneno.getId());
        if (venenoT2 != null && venenoT2.getTurnosRestantes() == 1) {
            System.out.println("✅ Veneno: Duração correta após T2 (Restam 1).");
        } else {
             System.out.println("❌ Veneno: Erro na duração após T2.");
        }

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 5. TESTE DE EXPIRAÇÃO FINAL ---
        // ----------------------------------------------------
        System.out.println("--- 5. TESTE DE EXPIRAÇÃO FINAL (Turno 3) ---");

        System.out.println("-> Processando fim do Turno 3:");
        jsBO.processarFimDeTurno(heroi.getId());

        // Veneno deve ter expirado (duração 3)
        JogadorStatus venenoFinal = jsBO.procurarRegistro(heroi.getId(), veneno.getId());
        
        if (venenoFinal == null) {
            System.out.println("✅ Veneno: Removido corretamente após o T3. Inventário de Status Vazio.");
        } else {
             System.out.println("❌ Veneno: Erro! Não foi removido.");
        }
    }
}