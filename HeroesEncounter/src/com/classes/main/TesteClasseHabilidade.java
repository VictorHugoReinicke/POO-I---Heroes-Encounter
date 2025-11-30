package com.classes.main;

import com.classes.BO.ClasseBO;
import com.classes.BO.HabilidadesBO;
import com.classes.BO.ClasseHabilidadeBO;
import com.classes.DTO.Classe;
import com.classes.DTO.Habilidade;
import com.classes.DTO.ClasseHabilidade;
import java.util.List;

public class TesteClasseHabilidade {

    public static void main(String[] args) {
        
        ClasseBO classeBO = new ClasseBO();
        HabilidadesBO habilidadesBO = new HabilidadesBO();
        ClasseHabilidadeBO chBO = new ClasseHabilidadeBO();

        // ----------------------------------------------------
        // --- 1. PREPARAÇÃO: DADOS DE TESTE (Classe e Habilidade) ---
        // ----------------------------------------------------
        System.out.println("--- 1. PREPARAÇÃO DE DADOS ---");
        
        // A. Criação/Busca da Classe de Teste (Guerreiro)
        Classe guerreiro = new Classe("Guerreiro", 5, 0, 5); // nome, bForca, bMagia, bEsquiva
        if (!classeBO.existe(guerreiro)) classeBO.inserir(guerreiro);
        guerreiro = classeBO.procurarPorNome(guerreiro);
        System.out.printf("✅ Classe '%s' pronta (ID: %d).\n", guerreiro.getNome(), guerreiro.getId());

        // B. Criação/Busca de Habilidades de Teste
        // Habilidade 1: Ataque Simples (Físico, sem efeito status)
        Habilidade ataqueSimples = new Habilidade();
        ataqueSimples.setNome("Ataque Simples");
        ataqueSimples.setCustoMana(0);
        ataqueSimples.setFatorDano(1.0);
        ataqueSimples.setTipo("Físico");
        if (!habilidadesBO.existe(ataqueSimples)) habilidadesBO.inserir(ataqueSimples);
        ataqueSimples = habilidadesBO.procurarPorNome(ataqueSimples);
        System.out.printf("✅ Habilidade '%s' pronta (ID: %d).\n", ataqueSimples.getNome(), ataqueSimples.getId());

        // Habilidade 2: Grito de Guerra (Suporte/Buff, sem custo)
        Habilidade gritoGuerra = new Habilidade();
        gritoGuerra.setNome("Grito de Guerra");
        gritoGuerra.setCustoMana(5);
        gritoGuerra.setFatorDano(0.0); // Não causa dano
        gritoGuerra.setTipo("Suporte");
        // OBS: idStatus seria setado aqui se a tabela Status estivesse implementada
        if (!habilidadesBO.existe(gritoGuerra)) habilidadesBO.inserir(gritoGuerra);
        gritoGuerra = habilidadesBO.procurarPorNome(gritoGuerra);
        System.out.printf("✅ Habilidade '%s' pronta (ID: %d).\n", gritoGuerra.getNome(), gritoGuerra.getId());
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 2. TESTE DE ATRIBUIÇÃO (INSERÇÃO E DUPLICIDADE) ---
        // ----------------------------------------------------
        System.out.println("--- 2. TESTE DE ATRIBUIÇÃO ---");

        // A. Atribui a Habilidade 1
        System.out.println("-> Atribuindo Ataque Simples:");
        chBO.atribuirHabilidade(guerreiro.getId(), ataqueSimples.getId());

        // B. Atribui a Habilidade 2
        System.out.println("-> Atribuindo Grito de Guerra:");
        chBO.atribuirHabilidade(guerreiro.getId(), gritoGuerra.getId());
        
        // C. Tenta atribuir a Habilidade 1 novamente (Deve falhar/cair no aviso)
        System.out.println("-> Tentando atribuir Ataque Simples de novo (Teste de Duplicidade):");
        chBO.atribuirHabilidade(guerreiro.getId(), ataqueSimples.getId());
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 3. TESTE DE LISTAGEM E VERIFICAÇÃO ---
        // ----------------------------------------------------
        System.out.println("--- 3. TESTE DE LISTAGEM E VERIFICAÇÃO ---");

        // A. Lista todas as habilidades do Guerreiro
        List<ClasseHabilidade> habilidadesGuerreiro = chBO.listarHabilidadesPorClasse(guerreiro.getId());
        
        if (habilidadesGuerreiro != null && habilidadesGuerreiro.size() == 2) {
            System.out.printf("✅ Verificação: Encontradas %d habilidades para o Guerreiro. (Correto)\n", 
                habilidadesGuerreiro.size());
            
            System.out.println("Habilidades Atribuídas:");
            for (ClasseHabilidade ch : habilidadesGuerreiro) {
                Habilidade h = ch.getHabilidade();
                System.out.printf("  - %s | Custo Mana: %d | Fator Dano: %.1f\n", 
                    h.getNome(), h.getCustoMana(), h.getFatorDano());
            }

        } else {
             System.out.printf("❌ Verificação: Erro ao listar! Esperado 2, encontrado %d.\n", 
                habilidadesGuerreiro != null ? habilidadesGuerreiro.size() : 0);
        }
        
        System.out.println("----------------------------------------");


        // ----------------------------------------------------
        // --- 4. TESTE DE REMOÇÃO ---
        // ----------------------------------------------------
        System.out.println("--- 4. TESTE DE REMOÇÃO ---");

        // Remove a habilidade 2
        chBO.removerAtribuicao(guerreiro.getId(), gritoGuerra.getId());
        
        // Verifica se a lista foi reduzida
        List<ClasseHabilidade> habilidadesPosRemocao = chBO.listarHabilidadesPorClasse(guerreiro.getId());
        
        if (habilidadesPosRemocao != null && habilidadesPosRemocao.size() == 1) {
            System.out.printf("✅ Remoção: Habilidade removida com sucesso. Restam %d.\n", 
                habilidadesPosRemocao.size());
        } else {
             System.out.printf("❌ Remoção: ERRO! Esperado 1, encontrado %d.\n", 
                habilidadesPosRemocao != null ? habilidadesPosRemocao.size() : 0);
        }
    }
}