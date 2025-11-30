package com.classes.main;

import com.classes.BO.StatusBO;
import com.classes.DTO.Status;
import java.util.List;

public class TesteStatus {

    public static void main(String[] args) {
        
        StatusBO statusBO = new StatusBO();

        // ----------------------------------------------------
        // --- 1. TESTE DE INSERÇÃO E BUSCA INICIAL ---
        // ----------------------------------------------------
        System.out.println("--- 1. TESTE DE INSERÇÃO E BUSCA INICIAL ---");
        
        // Status A: Veneno (Dura 3 turnos, 10 de dano por turno)
        Status veneno = new Status("Veneno", 10, 0.0, 3);
        
        if (statusBO.existe(veneno)) {
            System.out.println("⚠️ Status 'Veneno' já existe. Buscando registro...");
            veneno = statusBO.procurarPorNome(veneno);
        } else {
            if (statusBO.inserir(veneno)) {
                veneno = statusBO.procurarPorNome(veneno);
                System.out.printf("✅ Inserção: Status '%s' inserido com sucesso. ID: %d\n", veneno.getNome(), veneno.getId());
            } else {
                System.out.println("❌ Erro ao inserir Status 'Veneno'.");
                return;
            }
        }
        
        // Status B: Atordoamento (Dura 2 turnos, -50% Defesa)
        Status atordoamento = new Status("Atordoamento", 0, 0.5, 2);
        if (!statusBO.existe(atordoamento)) {
            statusBO.inserir(atordoamento);
            System.out.printf("✅ Inserção: Status '%s' inserido com sucesso.\n", atordoamento.getNome());
        }

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 2. TESTE DE ALTERAÇÃO (UPDATE) ---
        // ----------------------------------------------------
        System.out.println("--- 2. TESTE DE ALTERAÇÃO ---");
        
        // Altera o dano por turno do Veneno para 15 e a duração para 5
        int novoDano = 15;
        int novaDuracao = 5;
        veneno.setDanoTurno(novoDano);
        veneno.setDuracaoTurnos(novaDuracao);
        
        if (statusBO.alterar(veneno)) {
            Status venenoAlterado = statusBO.procurarPorCodigo(veneno.getId());
            if (venenoAlterado != null && venenoAlterado.getDanoTurno() == novoDano && venenoAlterado.getDuracaoTurnos() == novaDuracao) {
                System.out.printf("✅ Alteração: Status '%s' atualizado para Dano/Turno: %d e Duração: %d.\n", 
                    venenoAlterado.getNome(), venenoAlterado.getDanoTurno(), venenoAlterado.getDuracaoTurnos());
            } else {
                System.out.println("❌ Erro: Alteração no DTO não refletiu na busca por código.");
            }
        } else {
            System.out.println("❌ Erro ao alterar Status 'Veneno'.");
        }

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 3. TESTE DE PESQUISA GERAL ---
        // ----------------------------------------------------
        System.out.println("--- 3. TESTE DE PESQUISA GERAL ---");
        
        List<Status> todosStatus = statusBO.pesquisarTodos();
        
        if (todosStatus != null && todosStatus.size() >= 2) {
            System.out.printf("✅ Listagem: Encontrados %d status no banco.\n", todosStatus.size());
            for (Status s : todosStatus) {
                System.out.printf("  - ID %d: %s (Dano: %d, Def Mod: %.1f, Duração: %d)\n", 
                    s.getId(), s.getNome(), s.getDanoTurno(), s.getModificadorDefesa(), s.getDuracaoTurnos());
            }
        } else {
             System.out.println("❌ Erro na listagem de todos os Status.");
        }
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 4. TESTE DE EXCLUSÃO ---
        // ----------------------------------------------------
        System.out.println("--- 4. TESTE DE EXCLUSÃO ---");

        // Exclui o Atordoamento
        atordoamento = statusBO.procurarPorNome(atordoamento); // Garante que o ID está carregado
        if (atordoamento != null && statusBO.excluir(atordoamento)) {
            Status statusExcluido = statusBO.procurarPorCodigo(atordoamento.getId());
            if (statusExcluido == null) {
                System.out.printf("✅ Exclusão: Status '%s' removido com sucesso.\n", atordoamento.getNome());
            } else {
                System.out.println("❌ Erro: Status não foi removido do banco.");
            }
        } else {
             System.out.printf("❌ Erro ao excluir Status '%s'.\n", atordoamento.getNome());
        }

        System.out.println("----------------------------------------");
    }
}