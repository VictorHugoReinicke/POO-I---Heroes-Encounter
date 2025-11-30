package com.classes.main; 

import com.classes.DTO.Classe;
import com.classes.BO.ClasseBO;

public class TesteSetup {

    public static void main(String[] args) {
        
        ClasseBO classeBO = new ClasseBO();
        System.out.println("--- 1. INICIALIZAÇÃO DAS CLASSES DO JOGO ---");
        
     
        Classe guerreiro = new Classe("Guerreiro", 5, 0, 3);
        
        if (classeBO.inserir(guerreiro)) {
            System.out.printf("Classe Guerreiro inserida com sucesso! (ID: %d)\n", guerreiro.getId());
        } else {
            
            Classe existente = classeBO.procurarPorNome(guerreiro);
            if (existente != null) {
                System.out.printf("Classe Guerreiro já existe no DB. (ID: %d)\n", existente.getId());
            } else {
                 System.out.println("Erro inesperado ao inserir/buscar a Classe Guerreiro.");
            }
        }
        
   
        Classe mago = new Classe("Mago", 0, 5, 1);
        
        if (classeBO.inserir(mago)) {
            System.out.printf("Classe Mago inserida com sucesso! (ID: %d)\n", mago.getId());
        } else {
            Classe existente = classeBO.procurarPorNome(mago);
            if (existente != null) {
                System.out.printf("Classe Mago já existe no DB. (ID: %d)\n", existente.getId());
            } else {
                 System.out.println("Erro inesperado ao inserir/buscar a Classe Mago.");
            }
        }
        

        Classe paladino = new Classe("Paladino", 3, 3, 3);
        
        if (classeBO.inserir(paladino)) {
            System.out.printf("Classe Paladino inserida com sucesso! (ID: %d)\n", paladino.getId());
        } else {
            Classe existente = classeBO.procurarPorNome(paladino);
            if (existente != null) {
                System.out.printf("Classe Paladino já existe no DB. (ID: %d)\n", existente.getId());
            } else {
                 System.out.println("Erro inesperado ao inserir/buscar a Classe Paladino.");
            }
        }
        
        System.out.println("\n--- 2. FIM DA INICIALIZAÇÃO ---");
        


    }
    }