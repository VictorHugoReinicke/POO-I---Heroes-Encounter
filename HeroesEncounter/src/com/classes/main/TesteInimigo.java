package com.classes.main;
import java.util.List;

import com.classes.BO.InimigoBO;
import com.classes.DTO.Besta;
import com.classes.DTO.Chefe;
import com.classes.DTO.Inimigo;
import com.classes.DTO.InimigoMagico;
import com.classes.DTO.Ladrao;

public class TesteInimigo {

    public static void main(String[] args) {
        
        InimigoBO inimigoBO = new InimigoBO();
        
        // 1. CRIAÇÃO DE INSTÂNCIAS CONCRETAS
        System.out.println("--- 1. Criando Inimigos ---");
        
        Inimigo besta = new Besta();
        besta.setNome("Urso da Floresta"); // Renomeando para evitar duplicatas
        
        Inimigo ladrao = new Ladrao();
        ladrao.setNome("Assaltante Sorrateiro");
        
        Inimigo magico = new InimigoMagico();
        magico.setNome("Feiticeiro das Sombras");
        
        Inimigo chefe = new Chefe();
        chefe.setNome("O Dragão Supremo");
        
        // 2. INSERÇÃO NO BANCO DE DADOS
        System.out.println("\n--- 2. Inserindo Inimigos (via InimigoBO) ---");

        if (inimigoBO.inserir(besta)) {
            System.out.println("✅ Besta inserida com ID: " + besta.getId());
        } else {
            System.out.println("❌ Falha ao inserir Besta.");
        }
        
        if (inimigoBO.inserir(ladrao)) {
            System.out.println("✅ Ladrao inserido com ID: " + ladrao.getId());
        } else {
            System.out.println("❌ Falha ao inserir Ladrao.");
        }
        
        if (inimigoBO.inserir(magico)) {
            System.out.println("✅ Inimigo Magico inserido com ID: " + magico.getId());
        } else {
            System.out.println("❌ Falha ao inserir Inimigo Magico.");
        }
        
        if (inimigoBO.inserir(chefe)) {
            System.out.println("✅ Chefe inserido com ID: " + chefe.getId());
        } else {
            System.out.println("❌ Falha ao inserir Chefe.");
        }
        
        // 3. RECUPERAÇÃO E TESTE DA FACTORY (Pesquisar Todos)
        System.out.println("\n--- 3. Recuperando todos os Inimigos (Teste Factory) ---");
        
        List<Inimigo> todosInimigos = inimigoBO.pesquisarTodos();
        
        if (todosInimigos != null && !todosInimigos.isEmpty()) {
            System.out.println("✅ Sucesso! Total de inimigos encontrados: " + todosInimigos.size());
            
            for (Inimigo i : todosInimigos) {
                // O teste crucial: verificar a instância e os atributos.
                String tipoReal = i.getClass().getSimpleName(); 
                System.out.println(" - ID: " + i.getId() + " | Nome: " + i.getNome() + 
                                   " | Tipo Real: " + tipoReal + 
                                   " | HP Max: " + i.getHpMax() + 
                                   " | IA: " + i.getTipoIA());
            }
        } else {
            System.out.println("❌ Falha: Nenhuma ou poucas instâncias de inimigo foram recuperadas.");
        }
        
        // 4. TESTE DE BUSCA POR CÓDIGO (individual)
        System.out.println("\n--- 4. Teste de Busca por ID ---");
        if (besta.getId() > 0) {
            Inimigo bestaRecuperada = inimigoBO.procurarPorCodigo(besta.getId());
            if (bestaRecuperada != null && bestaRecuperada instanceof Besta) {
                System.out.println("✅ Sucesso! Besta recuperada corretamente. (ID: " + bestaRecuperada.getId() + ")");
            } else {
                System.out.println("❌ Falha ao recuperar Besta por código ou a Factory falhou na tipagem.");
            }
        }
    }
}