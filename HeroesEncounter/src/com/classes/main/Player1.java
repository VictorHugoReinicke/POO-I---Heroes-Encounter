package com.classes.main;

import com.classes.DTO.*;
import java.io.*;

public class Player1 {
    public static void main(String[] args) throws Exception {

    	
    	System.out.println("Escolha sua classe:");
        System.out.println("1 - Guerreiro");
        System.out.println("2 - Mago");
        System.out.println("3 - Paladino");
        System.out.print("Digite o número da classe: ");

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        int escolha = Integer.parseInt(teclado.readLine());

        System.out.print("Digite o nome do seu jogador: ");
        String nome = teclado.readLine();

        Jogador player1 = JogadorFactory.criarJogador(escolha, nome);

        System.out.println("\n==============================");
        System.out.println("Jogador criado com sucesso!");
        System.out.println("ID: " + player1.getId());
        System.out.println("Nome: " + player1.getNome());
        System.out.println("Classe: " + player1.getClass().getSimpleName());
        System.out.println("HP: " + player1.getHp());
        System.out.println("Mana: " + player1.getMana());
        System.out.println("Ouro: " + player1.getOuro());
        System.out.println("Arma equipada: " + 
            (player1.getArmaEquipada() != null ? player1.getArmaEquipada().getNome() : "Nenhuma"));

        System.out.println("Inventário:");

        for (JogadorItem ji : player1.getInventario()) {
            String equipadoInfo = ji.isEquipado() ? " [EQUIPADO]" : "";
            System.out.println(" - " + ji.getItem().getNome() + " (x" + ji.getQuantidade() + ")" + equipadoInfo);
        }

        System.out.println("Habilidades:");
        player1.getHabilidades().forEach(h -> System.out.println(" - " + h.getNome()));

        System.out.println("==============================\n");
    }
}