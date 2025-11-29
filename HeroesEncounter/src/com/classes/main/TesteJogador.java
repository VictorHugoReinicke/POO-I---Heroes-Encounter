package com.classes.main; 

import com.classes.DTO.Guerreiro;
import com.classes.DTO.Mago;
import com.classes.DTO.Paladino;
import com.classes.DTO.Jogador;
import com.classes.BO.JogadorBO;
import java.util.List;

public class TesteJogador {

    public static void main(String[] args) {
        
        JogadorBO jogadorBO = new JogadorBO();
        
      
        System.out.println("--- 1. INSERINDO NOVOS JOGADORES ---");
        
        Guerreiro g = new Guerreiro();
        g.setNome("Guts_BR");
        
        Mago m = new Mago();
        m.setNome("Merlin_SC");
        
        Paladino p = new Paladino();
        p.setNome("Arthur_PR");

        
        inserirJogador(jogadorBO, g);
        inserirJogador(jogadorBO, m);
        inserirJogador(jogadorBO, p);

       
        System.out.println("\n--- 3. VERIFICANDO TODOS OS JOGADORES INSERIDOS ---");
        List<Jogador> todos = jogadorBO.pesquisarTodos();
        
        if (todos != null && !todos.isEmpty()) {
            for (Jogador jog : todos) {
                String tipo = "Desconhecido";
                String especializacao = "N/A";

               
                if (jog instanceof Guerreiro) {
                    tipo = "Guerreiro (ID: " + jog.getIdClasse() + ")";
               
                    especializacao = "Arma Permitida: " + ((Guerreiro)jog).getTiposArmasPermitidas().get(0);
                } else if (jog instanceof Mago) {
                    tipo = "Mago (ID: " + jog.getIdClasse() + ")";
                    especializacao = "Dano Mágico: " + ((Mago)jog).getDanoMagico();
                } else if (jog instanceof Paladino) {
                    tipo = "Paladino (ID: " + jog.getIdClasse() + ")";
                }
                
                System.out.printf("✅ Encontrado: %s | Tipo DB: %s | HP: %d | Ouro: %d | Especialização: %s\n", 
                                  jog.getNome(), tipo, jog.getHp(), jog.getOuro(), especializacao);
            }
        } else {
            System.out.println("❌ Nenhuma jogador encontrado ou lista vazia.");
        }
        

        System.out.println("\n--- 4. TESTE DE ALTERAÇÃO ---");
        if (g.getId() != 0) { 
            g.setOuro(g.getOuro() + 500);
            g.setHp(g.getHp() - 10);
            
            boolean alterado = jogadorBO.alterar(g);
            if (alterado) {
                System.out.println("✅ Guerreiro alterado: Novo Ouro=" + g.getOuro() + ", HP=" + g.getHp());
            }
        }
    }
    
    
    private static void inserirJogador(JogadorBO bo, Jogador jog) {
        if (!bo.existe(jog)) {
             bo.inserir(jog);
             
             Jogador objNoDB = bo.procurarPorNome(jog);
             if (objNoDB != null) {
                 jog.setId(objNoDB.getId());
                 System.out.println("✅ Inserido: " + jog.getNome() + " (ID: " + jog.getId() + ")");
             } else {
                 System.out.println("❌ Erro ao buscar ID após inserção de " + jog.getNome());
             }
        } else {
            System.out.println("⚠️ Jogador " + jog.getNome() + " já existe.");
        }
    }
}