package com.classes.DTO.IA;

import com.classes.DTO.Inimigo;
import com.classes.DTO.Jogador;
import java.util.Random;

public class IAExecutor {
	private static Random rand = new Random();

	// Adicione este mapa para armazenar buffs/debuffs temporários
	private static java.util.Map<Integer, Integer> debuffsAtaqueJogador = new java.util.HashMap<>();
	private static java.util.Map<Integer, Integer> debuffsDefesaJogador = new java.util.HashMap<>();

	public static void executarAcao(Inimigo inimigo, Jogador jogador, String acao) {
		switch (acao) {
		case "ATAQUE_NORMAL":
			atacar(inimigo, jogador, 1.0, false);
			break;
		case "ATAQUE_PODEROSO":
			executarAtaquePoderoso(inimigo, jogador);
			break;
		case "ATAQUE_FEROZ":
			executarAtaqueFeroz(inimigo, jogador);
			break;
		case "ATAQUE_RAPIDO":
			executarAtaqueRapido(inimigo, jogador);
			break;
		case "ATAQUE_ESPECIAL":
			executarAtaqueEspecial(inimigo, jogador);
			break;
		case "DEFENDER":
			executarDefender(inimigo);
			break;
		case "DEFENDER_FORTE":
			executarDefenderForte(inimigo);
			break;
		case "ESQUIVAR":
			executarEsquivar(inimigo);
			break;
		case "GRITAR":
			executarGritar(inimigo, jogador);
			break;
		case "GRITAR_GUERRA":
			executarGritarGuerra(inimigo);
			break;
		case "RUGIDO":
			executarRugido(inimigo, jogador);
			break;
		case "CURAR":
			executarCurar(inimigo);
			break;
		case "REGENERAR":
			executarRegenerar(inimigo);
			break;
		case "FEITICO_MAGICO":
			executarFeiticoMagico(inimigo, jogador);
			break;
		case "PROTECAO_MAGICA":
			executarProtecaoMagica(inimigo);
			break;
		case "BUFF_DEFESA":
			executarBuffDefesa(inimigo);
			break;
		case "DEBUFF_JOGADOR":
			executarDebuffJogador(inimigo, jogador);
			break;
		case "ROUBAR":
			executarRoubar(inimigo, jogador);
			break;
		case "FUGIR":
			executarFugir(inimigo);
			break;
		default:
			atacar(inimigo, jogador, 1.0, false);
			break;
		}
	}

	private static void atacar(Inimigo inimigo, Jogador jogador, double multiplicador, boolean mensagemEspecial) {
		int danoBase = (int) (inimigo.getAtaque() * multiplicador);

		double variacao = 0.9 + rand.nextDouble() * 0.2;
		danoBase = (int) (danoBase * variacao);

		// Aplica debuff no jogador se existir
		int defesaJogador = jogador.getDefesa();
		Integer debuffDefesa = debuffsDefesaJogador.get(jogador.getId());
		if (debuffDefesa != null) {
			defesaJogador = Math.max(0, defesaJogador - debuffDefesa);
		}

		int danoFinal = Math.max(1, danoBase - defesaJogador);

		jogador.receberDano(danoFinal);

		if (!mensagemEspecial) {
			System.out.println(
					"⚔️ " + inimigo.getNome() + " ataca " + jogador.getNome() + " causando " + danoFinal + " de dano!");
		}
	}

	private static void executarAtaquePoderoso(Inimigo inimigo, Jogador jogador) {
		atacar(inimigo, jogador, 1.5 + rand.nextDouble() * 0.3, true);
		System.out.println("💥 " + inimigo.getNome() + " usa um ataque poderoso!");
	}

	private static void executarAtaqueFeroz(Inimigo inimigo, Jogador jogador) {
		atacar(inimigo, jogador, 1.8 + rand.nextDouble() * 0.4, true);
		System.out.println("🐾 " + inimigo.getNome() + " ataca ferozmente!");
	}

	private static void executarAtaqueRapido(Inimigo inimigo, Jogador jogador) {
		atacar(inimigo, jogador, 0.7 + rand.nextDouble() * 0.2, true);
		System.out.println("⚡ " + inimigo.getNome() + " usa um ataque rápido!");

		if (rand.nextDouble() < 0.3) {
			System.out.println("⚡ Ataque rápido extra!");
			atacar(inimigo, jogador, 0.5 + rand.nextDouble() * 0.2, false);
		}
	}

	private static void executarAtaqueEspecial(Inimigo inimigo, Jogador jogador) {
		atacar(inimigo, jogador, 2.0 + rand.nextDouble() * 0.5, true);
		System.out.println("💥 " + inimigo.getNome() + " usa um ataque especial devastador!");
	}

	private static void executarDefender(Inimigo inimigo) {
		System.out.println("🛡️ " + inimigo.getNome() + " se defende!");
		inimigo.setDefesa(inimigo.getDefesa() + 5);
	}

	private static void executarDefenderForte(Inimigo inimigo) {
		System.out.println("🛡️ " + inimigo.getNome() + " assume uma postura defensiva!");
		inimigo.setDefesa(inimigo.getDefesa() + 10);
	}

	private static void executarEsquivar(Inimigo inimigo) {
		System.out.println("🌀 " + inimigo.getNome() + " esquivou!");
		inimigo.setEsquivandoProximoAtaque(true);
	}

	private static void executarGritar(Inimigo inimigo, Jogador jogador) {
		System.out.println("🗣️ " + inimigo.getNome() + " grita para intimidar!");
		// Armazena debuff no jogador
		debuffsAtaqueJogador.put(jogador.getId(), 3); // Reduz 3 pontos de ataque
	}

	private static void executarGritarGuerra(Inimigo inimigo) {
		System.out.println("⚔️ " + inimigo.getNome() + " solta um grito de guerra!");
		inimigo.setAtaque(inimigo.getAtaque() + 3 + rand.nextInt(3));
	}

	private static void executarRugido(Inimigo inimigo, Jogador jogador) {
		System.out.println("🦁 " + inimigo.getNome() + " solta um rugido assustador!");
		// Armazena debuff na defesa do jogador
		debuffsDefesaJogador.put(jogador.getId(), 2 + rand.nextInt(3)); // Reduz 2-4 pontos de defesa
	}

	private static void executarCurar(Inimigo inimigo) {
		int cura = 10 + rand.nextInt(15);
		inimigo.setHp(Math.min(inimigo.getHp() + cura, inimigo.getHpMax()));
		System.out.println("💚 " + inimigo.getNome() + " se cura em " + cura + " HP!");
	}

	private static void executarRegenerar(Inimigo inimigo) {
		int regeneracao = 20 + rand.nextInt(20);
		inimigo.setHp(Math.min(inimigo.getHp() + regeneracao, inimigo.getHpMax()));
		System.out.println("✨ " + inimigo.getNome() + " se regenera em " + regeneracao + " HP!");
	}

	private static void executarFeiticoMagico(Inimigo inimigo, Jogador jogador) {
		atacar(inimigo, jogador, 1.3 + rand.nextDouble() * 0.4, true);
		System.out.println("🔮 " + inimigo.getNome() + " lança um feitiço mágico!");
	}

	private static void executarProtecaoMagica(Inimigo inimigo) {
		int aumentoDefesa = 8 + rand.nextInt(5);
		System.out.println("🛡️✨ " + inimigo.getNome() + " conjura uma proteção mágica!");
		inimigo.setDefesa(inimigo.getDefesa() + aumentoDefesa);
	}

	private static void executarBuffDefesa(Inimigo inimigo) {
		System.out.println("🛡️⬆️ " + inimigo.getNome() + " fortalece sua defesa!");
		inimigo.setDefesa(inimigo.getDefesa() + 7 + rand.nextInt(4));
	}

	private static void executarDebuffJogador(Inimigo inimigo, Jogador jogador) {
		System.out.println("📉 " + inimigo.getNome() + " enfraquece o jogador!");
		// Aplica debuff no ataque e defesa do jogador
		debuffsAtaqueJogador.put(jogador.getId(), 4 + rand.nextInt(3));
		debuffsDefesaJogador.put(jogador.getId(), 3 + rand.nextInt(2));
	}

	private static void executarRoubar(Inimigo inimigo, Jogador jogador) {
		System.out.println("💰 " + inimigo.getNome() + " tenta roubar o jogador!");
		if (rand.nextDouble() < 0.4) {
			// Tenta obter ouro por reflexão, se não existir, apenas mensagem
			try {
				java.lang.reflect.Method getOuro = jogador.getClass().getMethod("getOuro");
				java.lang.reflect.Method setOuro = jogador.getClass().getMethod("setOuro", int.class);

				int ouroAtual = (int) getOuro.invoke(jogador);
				if (ouroAtual > 0) {
					int ouroRoubado = Math.min(10 + rand.nextInt(20), ouroAtual);
					setOuro.invoke(jogador, ouroAtual - ouroRoubado);
					inimigo.setRecompensaOuro(inimigo.getRecompensaOuro() + ouroRoubado);
					System.out.println("💸 " + inimigo.getNome() + " roubou " + ouroRoubado + " de ouro!");
				}
			} catch (Exception e) {
				System.out
						.println("💸 " + inimigo.getNome() + " tentou roubar, mas o jogador não tem sistema de ouro!");
			}
		} else {
			System.out.println("❌ O roubo falhou!");
		}
	}

	private static void executarFugir(Inimigo inimigo) {
		System.out.println("🏃 " + inimigo.getNome() + " tenta fugir do combate!");
		double chanceFuga = 0.3 + (1.0 - (double) inimigo.getHp() / inimigo.getHpMax()) * 0.7;

		if (rand.nextDouble() < chanceFuga) {
			System.out.println("✅ " + inimigo.getNome() + " fugiu com sucesso!");
			inimigo.setFugiu(true);
		} else {
			System.out.println("❌ " + inimigo.getNome() + " falhou em fugir!");
		}
	}

	public static void limparDebuffs(int jogadorId) {
		debuffsAtaqueJogador.remove(jogadorId);
		debuffsDefesaJogador.remove(jogadorId);
	}

	public static int getAtaqueComDebuff(Jogador jogador) {
		int ataqueBase = jogador.getAtaque();
		Integer debuff = debuffsAtaqueJogador.get(jogador.getId());
		return debuff != null ? Math.max(0, ataqueBase - debuff) : ataqueBase;
	}

	public static int getDefesaComDebuff(Jogador jogador) {
		int defesaBase = jogador.getDefesa();
		Integer debuff = debuffsDefesaJogador.get(jogador.getId());
		return debuff != null ? Math.max(0, defesaBase - debuff) : defesaBase;
	}
}