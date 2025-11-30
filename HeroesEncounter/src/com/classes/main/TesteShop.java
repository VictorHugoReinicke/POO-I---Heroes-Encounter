package com.classes.main;

import com.classes.BO.ItemBO;
import com.classes.BO.ShopBO;
import com.classes.BO.ShopItemBO;
import com.classes.DTO.Shop;
import com.classes.DTO.ItemArma;
import com.classes.DTO.ItemConsumivel;
import com.classes.DTO.Jogador;
import com.classes.DTO.Guerreiro; // ✅ NOVO IMPORT NECESSÁRIO
import com.classes.DTO.ShopItem;

public class TesteShop {

    public static void main(String[] args) {
        
        ShopBO shopBO = new ShopBO();
        ShopItemBO shopItemBO = new ShopItemBO();
        ItemBO itemBO = new ItemBO();

      
        ItemArma espadaLonga = new ItemArma("Espada Longa", 10, 0, 5);
        ItemConsumivel pocaoVida = new ItemConsumivel("Poção de Vida", 50, 0);

        if (!itemBO.existe(espadaLonga)) itemBO.inserir(espadaLonga);
        if (!itemBO.existe(pocaoVida)) itemBO.inserir(pocaoVida);
        
        
        espadaLonga = (ItemArma) itemBO.procurarPorNome(espadaLonga);
        pocaoVida = (ItemConsumivel) itemBO.procurarPorNome(pocaoVida);

       
        Jogador heroi = new Guerreiro(); 
        // ------------------------------------------------------------------
        heroi.setNome("HeroiTestador");
        heroi.setOuro(500);


        System.out.println("--- 1. TESTE DE INSERÇÃO DA LOJA ---");
        Shop lojaCentral = new Shop("Loja Central");
        
        if (shopBO.inserir(lojaCentral)) {
            System.out.printf("✅ Loja '%s' inserida (ID: %d)\n", lojaCentral.getNome(), lojaCentral.getId());
        } else {
         
            Shop lojaExistente = shopBO.procurarPorNome(lojaCentral); 
            if (lojaExistente != null) {
                lojaCentral = lojaExistente;
            } else {
                 
                 lojaCentral = shopBO.procurarPorCodigo(1); 
            }

            if (lojaCentral == null) {
                System.out.println("❌ Falha crítica: Loja não inserida e não encontrada.");
                return;
            }
            System.out.printf("ℹ️ Loja '%s' já existia (ID: %d)\n", lojaCentral.getNome(), lojaCentral.getId());
        }
        System.out.println("----------------------------------------");


   
        System.out.println("--- 2. TESTE DE ESTOQUE (ShopItem) ---");

       
        ShopItem estoqueEspada = new ShopItem(lojaCentral.getId(), espadaLonga.getId(), 100, 5);
        ShopItem estoquePocao = new ShopItem(lojaCentral.getId(), pocaoVida.getId(), 25, 20);

        if (shopItemBO.inserir(estoqueEspada)) {
            System.out.printf("✅ Estoque da Espada Longa adicionado. Preço: %d\n", estoqueEspada.getPrecoVenda());
        } else {
             shopItemBO.alterar(estoqueEspada); 
             System.out.println("ℹ️ Estoque da Espada Longa atualizado (já existia).");
        }
        shopItemBO.inserir(estoquePocao);
        System.out.println("----------------------------------------");

        
        System.out.println("--- 3. TESTE DE LISTAGEM DE ITENS ---");
        shopBO.listarItens(lojaCentral);
        System.out.println("----------------------------------------");

        
        System.out.println("--- 4. TESTE DE VENDA ---");

        System.out.printf("JOGADOR GOLD ANTES: %d\n", heroi.getOuro());
        shopBO.venderParaJogador(heroi, espadaLonga, lojaCentral);

        heroi.setOuro(10); // Reduz Gold drasticamente
        shopBO.venderParaJogador(heroi, espadaLonga, lojaCentral);
        
        ShopItem estoqueVerificado = shopItemBO.procurarRegistro(lojaCentral.getId(), espadaLonga.getId());
        if (estoqueVerificado != null && estoqueVerificado.getQuantidade() == 4) {
            System.out.printf("✅ Verificação: Estoque da Espada Longa foi reduzido para 4. (Correto)\n");
        } else if (estoqueVerificado != null) {
             System.out.printf("❌ Verificação: Estoque incorreto! Esperado 4, encontrado %d.\n", estoqueVerificado.getQuantidade());
        } else {
            System.out.println("❌ Erro de Teste: Não foi possível recuperar o registro ShopItem para verificação.");
        }
        System.out.printf("JOGADOR GOLD DEPOIS: %d\n", heroi.getOuro());
        System.out.println("----------------------------------------");
    }
}