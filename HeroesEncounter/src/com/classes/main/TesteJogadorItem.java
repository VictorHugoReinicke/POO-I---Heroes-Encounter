package com.classes.main;

import java.util.List;

import com.classes.BO.ItemBO;
import com.classes.BO.JogadorBO;
import com.classes.BO.JogadorItemBO;
import com.classes.DTO.Guerreiro;
import com.classes.DTO.ItemArma;
import com.classes.DTO.ItemConsumivel;
import com.classes.DTO.Jogador;
import com.classes.DTO.JogadorItem;

public class TesteJogadorItem {

    public static void main(String[] args) {
        
        JogadorBO jogadorBO = new JogadorBO();
        ItemBO itemBO = new ItemBO();
        JogadorItemBO inventarioBO = new JogadorItemBO();

        // ----------------------------------------------------
        // --- 1. PREPARAÇÃO: CRIAÇÃO DE DADOS DE TESTE ---
        // ----------------------------------------------------
        System.out.println("--- 1. PREPARAÇÃO DE DADOS ---");
        
        // A. Criação/Busca do Jogador de Teste
        Jogador heroi = new Guerreiro(); 
        heroi.setNome("Herói do Inventário");
        if (jogadorBO.existe(heroi)) {
            heroi = jogadorBO.procurarPorNome(heroi);
            System.out.printf("ℹ️ Jogador '%s' encontrado. ID: %d\n", heroi.getNome(), heroi.getId());
        } else {
            jogadorBO.inserir(heroi);
            heroi = jogadorBO.procurarPorNome(heroi);
            System.out.printf("✅ Jogador '%s' inserido. ID: %d\n", heroi.getNome(), heroi.getId());
        }
        
        if (heroi == null || heroi.getId() == 0) return; // Parar se falhar a criação/busca
        
        // B. Criação/Busca dos Itens de Teste
        ItemArma espadaLonga = new ItemArma("Espada Longa", 100, 10, 5);
        ItemArma machadoGrande = new ItemArma("Machado Grande", 150, 15, 0);
        ItemConsumivel pocaoVida = new ItemConsumivel("Poção de Vida", 50, 50);

        // Garante que os itens existam no banco e recarrega para obter os IDs
        itemBO.inserir(espadaLonga); espadaLonga = (ItemArma) itemBO.procurarPorNome(espadaLonga);
        itemBO.inserir(machadoGrande); machadoGrande = (ItemArma) itemBO.procurarPorNome(machadoGrande);
        itemBO.inserir(pocaoVida); pocaoVida = (ItemConsumivel) itemBO.procurarPorNome(pocaoVida);

        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 2. TESTE DE ADIÇÃO (INSERÇÃO E QUANTIDADE) ---
        // ----------------------------------------------------
        System.out.println("--- 2. TESTE DE ADIÇÃO ---");
        
        // A. Adiciona um item novo (Espada Longa)
        inventarioBO.adicionarItem(heroi.getId(), espadaLonga, 1);
        
        // B. Adiciona um item que já existe (Poção de Vida)
        inventarioBO.adicionarItem(heroi.getId(), pocaoVida, 5);
        
        // C. Adiciona mais do mesmo item (teste de acúmulo)
        inventarioBO.adicionarItem(heroi.getId(), pocaoVida, 3); 
        
        // D. Adiciona um segundo item de slot único (Machado Grande)
        inventarioBO.adicionarItem(heroi.getId(), machadoGrande, 1);
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 3. TESTE DE LÓGICA DE EQUIPAR/SLOT ÚNICO ---
        // ----------------------------------------------------
        System.out.println("--- 3. TESTE DE EQUIPAR/SLOT ---");
        
        // A. Equipa a primeira arma (Espada Longa)
        inventarioBO.equiparItem(heroi.getId(), espadaLonga);
        
        // B. Verifica se a Espada foi equipada
        JogadorItem espadaRegistro = inventarioBO.procurarRegistro(heroi.getId(), espadaLonga.getId());
        if (espadaRegistro != null && espadaRegistro.isEquipado()) {
            System.out.println("✅ Slot: Espada Longa equipada com sucesso.");
        }
        
        // C. Equipa a segunda arma (Machado Grande) - Deve desequipar a Espada Longa
        inventarioBO.equiparItem(heroi.getId(), machadoGrande);

        // D. Verifica se o Machado está equipado
        JogadorItem machadoRegistro = inventarioBO.procurarRegistro(heroi.getId(), machadoGrande.getId());
        if (machadoRegistro != null && machadoRegistro.isEquipado()) {
            System.out.println("✅ Slot: Machado Grande equipado com sucesso.");
        }
        
        // E. Verifica se a Espada foi desequipada automaticamente
        espadaRegistro = inventarioBO.procurarRegistro(heroi.getId(), espadaLonga.getId());
        if (espadaRegistro != null && !espadaRegistro.isEquipado()) {
             System.out.println("✅ Slot: Espada Longa desequipada automaticamente. (Regra de Slot Único OK)");
        } else {
             System.out.println("❌ Slot: ERRO! Espada Longa não foi desequipada.");
        }
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 4. TESTE DE LISTAGEM E REMOÇÃO ---
        // ----------------------------------------------------
        System.out.println("--- 4. TESTE DE LISTAGEM E REMOÇÃO ---");
        
        // A. Lista o inventário completo
        List<JogadorItem> inventario = inventarioBO.listarItensPorJogador(heroi.getId());
        System.out.println("Inventário Atual:");
        for (JogadorItem ji : inventario) {
            System.out.printf("  - %s (x%d) | %s | Equipado: %s\n", 
                ji.getItem().getNome(), 
                ji.getQuantidade(),
                ji.getItem().getClass().getSimpleName(),
                ji.isEquipado() ? "SIM" : "NÃO"
            );
        }
        
        // B. Remove parte das poções (teste de diminuição de quantidade)
        inventarioBO.removerItem(heroi.getId(), pocaoVida, 5); 
        
        // C. Remove o restante (teste de exclusão de registro)
        inventarioBO.removerItem(heroi.getId(), pocaoVida, 3);
        
        // D. Verifica a lista final (Poção de Vida deve ter sumido)
        List<JogadorItem> inventarioFinal = inventarioBO.listarItensPorJogador(heroi.getId());
        long pocaoCount = inventarioFinal.stream().filter(ji -> ji.getItem().getNome().equals("Poção de Vida")).count();
        
        if (pocaoCount == 0) {
            System.out.println("✅ Remoção: Poção de Vida excluída corretamente após esgotar a quantidade.");
        } else {
            System.out.println("❌ Remoção: ERRO! Poção de Vida não foi removida. Quantidade restante: " + pocaoCount);
        }

        System.out.println("----------------------------------------");
    }
}