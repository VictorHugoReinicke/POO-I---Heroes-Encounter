package com.classes.main;

import com.classes.DTO.*;
import com.classes.BO.ItemBO;

public class TesteItem {

    public static void main(String[] args) {
        
        ItemBO itemBO = new ItemBO();

        // 1. DADOS DE TESTE (Um de cada subtipo)
        // ItemArma(nome, bonusDano, bonusMagia, bonusCritico)
        ItemArma espadaFogo = new ItemArma("Espada de Fogo", 15, 5, 10);
        
        // ItemDefesa(nome, bonusDefesa, bonusEsquiva)
        ItemDefesa escudoReforcado = new ItemDefesa("Escudo Reforçado", 10, 5);
        
        // ItemConsumivel(nome, cura, mana)
        ItemConsumivel megaPocao = new ItemConsumivel("Mega Poção", 100, 50);


        // ----------------------------------------------------
        // --- TESTE 1: INSERIR (CREATE) ---
        // ----------------------------------------------------
        System.out.println("--- 1. TESTE DE INSERÇÃO (CREATE) ---");

        if (itemBO.inserir(espadaFogo)) {
            System.out.printf("✅ ARMA inserida: %s (ID: %d)\n", espadaFogo.getNome(), espadaFogo.getId());
        } else {
            System.out.printf("ℹ️ ARMA já existe: %s\n", espadaFogo.getNome());
            espadaFogo = (ItemArma) itemBO.procurarPorNome(espadaFogo); 
        }

        if (itemBO.inserir(escudoReforcado)) {
            System.out.printf("✅ DEFESA inserida: %s (ID: %d)\n", escudoReforcado.getNome(), escudoReforcado.getId());
        } else {
            System.out.printf("ℹ️ DEFESA já existe: %s\n", escudoReforcado.getNome());
            escudoReforcado = (ItemDefesa) itemBO.procurarPorNome(escudoReforcado);
        }

        if (itemBO.inserir(megaPocao)) {
            System.out.printf("✅ CONSUMIVEL inserido: %s (ID: %d)\n", megaPocao.getNome(), megaPocao.getId());
        } else {
            System.out.printf("ℹ️ CONSUMIVEL já existe: %s\n", megaPocao.getNome());
            megaPocao = (ItemConsumivel) itemBO.procurarPorNome(megaPocao);
        }

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- TESTE 2: PROCURAR POR CÓDIGO E NOME (READ) ---
        // ----------------------------------------------------
        System.out.println("--- 2. TESTE DE BUSCA (READ) ---");

       
        Item itemBuscado = itemBO.procurarPorCodigo(espadaFogo.getId());
        if (itemBuscado != null && itemBuscado instanceof ItemArma) {
            ItemArma armaBuscada = (ItemArma) itemBuscado;
            System.out.printf("✅ Busca por ID (%d) sucesso. Tipo: %s, Dano: %d, Magia: %d\n", 
                armaBuscada.getId(), armaBuscada.getClass().getSimpleName(), armaBuscada.getBonusDano(), armaBuscada.getBonusMagico());
        } else {
            System.out.println("❌ Falha na busca por ID da Arma ou tipo incorreto.");
        }

        
        ItemConsumivel consumivelBusca = new ItemConsumivel();
        consumivelBusca.setNome("Mega Poção");
        itemBuscado = itemBO.procurarPorNome(consumivelBusca);

        if (itemBuscado != null && itemBuscado instanceof ItemConsumivel) {
            ItemConsumivel pocaoBuscada = (ItemConsumivel) itemBuscado;
            System.out.printf("✅ Busca por Nome ('%s') sucesso. Tipo: %s, Cura: %d, Mana: %d\n", 
                pocaoBuscada.getNome(), pocaoBuscada.getClass().getSimpleName(), pocaoBuscada.getCura(), pocaoBuscada.getMana());
        } else {
            System.out.println("❌ Falha na busca por Nome do Consumível ou tipo incorreto.");
        }
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- TESTE 3: ALTERAR (UPDATE) ---
        // ----------------------------------------------------
        System.out.println("--- 3. TESTE DE ALTERAÇÃO (UPDATE) ---");

        // Altera Defesa
        escudoReforcado.setBonusDefesa(50); // Alteração significativa na tabela Defesa
        escudoReforcado.setBonusEsquiva(10);
        escudoReforcado.setNome("Escudo LENDÁRIO"); // Alteração na tabela Item

        if (itemBO.alterar(escudoReforcado)) {
            System.out.printf("✅ DEFESA alterada com sucesso! Novo nome: %s\n", escudoReforcado.getNome());

            // Verifica a alteração no banco
            ItemDefesa defesaVerificada = (ItemDefesa) itemBO.procurarPorCodigo(escudoReforcado.getId());
            if (defesaVerificada != null && defesaVerificada.getBonusDefesa() == 50) {
                System.out.printf("✅ Verificação: Defesa atualizada para 50. (Correto)\n");
            } else {
                System.out.printf("❌ Verificação: Defesa não foi atualizada no banco. Valor: %d. (Erro)\n", defesaVerificada.getBonusDefesa());
            }
        } else {
            System.out.println("❌ Falha ao alterar DEFESA.");
        }
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- TESTE 4: EXCLUIR (DELETE) ---
        // ----------------------------------------------------
        System.out.println("--- 4. TESTE DE EXCLUSÃO (DELETE) ---");

        // Exclui a Arma
        if (itemBO.excluir(espadaFogo)) {
            System.out.printf("✅ ARMA excluída: %s (ID: %d)\n", espadaFogo.getNome(), espadaFogo.getId());
            
            // Verifica a exclusão
            Item itemDeletado = itemBO.procurarPorCodigo(espadaFogo.getId());
            if (itemDeletado == null) {
                System.out.println("✅ Verificação: Item não encontrado no banco. (Correto)");
            } else {
                System.out.println("❌ Verificação: Item ainda encontrado no banco. (Erro)");
            }
            
        } else {
            System.out.println("❌ Falha ao excluir ARMA.");
        }
        System.out.println("----------------------------------------");
    }
}