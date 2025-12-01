package com.classes.DTO;
import java.util.Scanner;

public class ControladorCombate {

	private SerVivo turnoAtual;
	private SerVivo outro;
    private boolean acaoExecutada = false;
    private boolean combateAtivo = false;

    private final Scanner scanner = new Scanner(System.in);

    public void iniciarCombate(Jogador p, Inimigo e) {
        System.out.println("\n===== COMBATE INICIADO =====");

        turnoAtual = p;
        outro = e;
        combateAtivo = true;

        System.out.println("Jogador: " + p.getNome() + "  VS  Inimigo: " + e.getNome());
        System.out.println("------------------------------------");

        while (combateAtivo) {
            executarTurno();
        }

        System.out.println("===== FIM DO COMBATE =====");
    }

    private void executarTurno() {
        acaoExecutada = false;

        System.out.println("\n> Turno de: " + turnoAtual.getNome());
        System.out.println(turnoAtual.getNome() + " HP: " + turnoAtual.getHp());
        System.out.println(outro.getNome() + " HP: " + outro.getHp());

        if (turnoAtual instanceof Jogador) {
            turnoJogador();
        } else if (turnoAtual instanceof Inimigo inimigo) {
            turnoInimigo(inimigo);
        }

        if (!outro.estaVivo()) {
            System.out.println("\n" + outro.getNome() + " foi derrotado!");
            combateAtivo = false;
            return;
        }

        proximoTurno();
    }
private void turnoJogador() {
        while (!acaoExecutada) {
            System.out.print("Escolha sua ação (atk/heal/status/sair): ");
            String acao = scanner.nextLine();

            switch (acao) {
                case "atk":
                    atacar(turnoAtual, outro);
                    break;

                case "heal":
                    curar(turnoAtual);
                    break;

                case "status":
                    mostrarStatus(turnoAtual);
                    break;

                case "sair":
                    System.out.println("Encerrando combate...");
                    combateAtivo = false;
                    acaoExecutada = true;
                    break;

                default:
                    System.out.println("Ação inválida.");
            }
        }
    }

    private void turnoInimigo(Inimigo inimigo) {
        System.out.println(inimigo.getNome() + " está decidindo a ação...");
        inimigo.decidirAcao((Jogador) outro); // IA decide

        atacar(inimigo, outro);

        acaoExecutada = true;
    }
private void atacar(SerVivo atacante, SerVivo alvo) {
        if (!alvo.estaVivo()) return;

        int danoBase = atacante.getAtaque();

        int danoFinal = Math.max(1, danoBase - alvo.getDefesa());

        alvo.receberDano(danoFinal);

        System.out.println(atacante.getNome() + " atacou " + alvo.getNome() +
                " causando " + danoFinal + " de dano!");

        acaoExecutada = true;
    }

    private void curar(SerVivo s) {
        int cura = 5;
        System.out.println(s.getNome() + " recuperou " + cura + " de HP!");
        acaoExecutada = true;
    }

    private void mostrarStatus(SerVivo s) {
        System.out.println("Status de " + s.getNome() + ":");
        if (s.getStatusAtivo().isEmpty()) {
            System.out.println("Nenhum status ativo.");
        } else {
            s.getStatusAtivo().forEach(st ->
                    System.out.println("- " + st.getNome() + " (" + st.getDuracaoTurnos() + " turnos)")
            );
        }
    }

    public void proximoTurno() {
        SerVivo temp = turnoAtual;
        turnoAtual = outro;
        outro = temp;
    }

    public boolean acaoExecutada(SerVivo s) {
        return acaoExecutada;
    }
}

