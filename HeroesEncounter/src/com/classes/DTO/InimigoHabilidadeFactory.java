package com.classes.DTO;

import java.util.*;

import com.classes.BO.*;

public class InimigoHabilidadeFactory {

	public static void criarHabilidadesInimigos(InimigoBO inimigoBO, HabilidadesBO habilidadesBO,
			InimigoHabilidadeBO inimigoHabBO, StatusBO statusBO) {

		System.out.println(">> Criando habilidades dos inimigos...");

		// ==============================
		// STATUS PARA INIMIGOS
		// ==============================
		Status furia = criarOuBuscarStatus(statusBO, "Fúria", 0, 0.5, 2);
		Status mordidaProfunda = criarOuBuscarStatus(statusBO, "Mordida Profunda", 8, 0.0, 3);
		Status veneno = criarOuBuscarStatus(statusBO, "Veneno", 6, -0.1, 4);
		Status sangramento = criarOuBuscarStatus(statusBO, "Sangramento", 5, 0.0, 3);
		Status atordoamento = criarOuBuscarStatus(statusBO, "Atordoamento", 0, 0.0, 1);
		Status fraqueza = criarOuBuscarStatus(statusBO, "Fraqueza", 0, -0.3, 2);

		// ==============================
		// HABILIDADES DE INIMIGOS
		// ==============================
		Map<String, Habilidade> habilidadesCriadas = new HashMap<>();

		// Habilidades para Bestas
		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Arranhão Selvagem", 5, 1.2, "OFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Mordida Brutal", 12, 1.8, "OFENSIVA", mordidaProfunda.getId()));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Fúria Animal", 0, 0, "BUFF", furia.getId()));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas, new Habilidade("Investida", 10, 2.0, "OFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Garras Afiadas", 8, 1.5, "OFENSIVA", sangramento.getId()));

		// Habilidades para Ladrões
		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Dardo Venenoso", 8, 1.4, "OFENSIVA", veneno.getId()));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Golpe Traiçoeiro", 15, 2.2, "OFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas, new Habilidade("Esquiva", 0, 0, "DEFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Ataque Surpresa", 20, 2.5, "OFENSIVA"));

		// Habilidades para Magos
		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas, new Habilidade("Bola de Fogo", 15, 2.0, "OFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Raio Paralisante", 10, 1.0, "OFENSIVA", atordoamento.getId()));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas, new Habilidade("Escudo Arcano", 0, 0, "DEFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Maldição de Fraqueza", 5, 0.0, "DEBUFF", fraqueza.getId()));

		// Habilidades para Chefes
		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Rugido Aterrorizante", 0, 0, "DEBUFF"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas, new Habilidade("Devastação", 25, 3.0, "OFENSIVA"));

		criarOuBuscarHabilidade(habilidadesBO, habilidadesCriadas,
				new Habilidade("Regeneração Monstruosa", 0, 0, "CURA"));

		// ==============================
		// ATRIBUIR HABILIDADES AOS INIMIGOS
		// ==============================

		List<Inimigo> inimigos = inimigoBO.pesquisarTodos();
		System.out.println(">> Linkando habilidades aos inimigos...");

		for (Inimigo i : inimigos) {
			String nomeInimigo = i.getNome();
			String classeInimigo = i.getClass().getSimpleName();

			System.out.println("\n🔗 Configurando " + nomeInimigo + " (" + classeInimigo + "):");

			switch (classeInimigo) {
			case "Besta":
				if (nomeInimigo.contains("Urso")) {
					linkar(inimigoHabBO, i, habilidadesCriadas, "Arranhão Selvagem", 40);
					linkar(inimigoHabBO, i, habilidadesCriadas, "Mordida Brutal", 30);
					linkar(inimigoHabBO, i, habilidadesCriadas, "Investida", 20);
				} else if (nomeInimigo.contains("Lobo")) {
					linkar(inimigoHabBO, i, habilidadesCriadas, "Arranhão Selvagem", 50);
					linkar(inimigoHabBO, i, habilidadesCriadas, "Garras Afiadas", 35);
				}
				break;

			case "Ladrao":
				linkar(inimigoHabBO, i, habilidadesCriadas, "Dardo Venenoso", 30);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Golpe Traiçoeiro", 25);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Esquiva", 20);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Ataque Surpresa", 15);
				break;

			case "InimigoMagico":
				linkar(inimigoHabBO, i, habilidadesCriadas, "Bola de Fogo", 35);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Raio Paralisante", 25);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Escudo Arcano", 20);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Maldição de Fraqueza", 15);
				break;

			case "Chefe":
				linkar(inimigoHabBO, i, habilidadesCriadas, "Fúria Animal", 15);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Mordida Brutal", 25);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Rugido Aterrorizante", 10);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Devastação", 20);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Regeneração Monstruosa", 10);
				break;

			default:
				linkar(inimigoHabBO, i, habilidadesCriadas, "Arranhão Selvagem", 60);
				linkar(inimigoHabBO, i, habilidadesCriadas, "Investida", 30);
				System.out.println(
						"⚠ Tipo de inimigo desconhecido: " + classeInimigo + ". Usando habilidades genéricas.");
			}
		}

		System.out.println("\n✔ Habilidades dos inimigos criadas e vinculadas!");
	}

	// =============================================================
	// HELPERS
	// =============================================================

	private static Status criarOuBuscarStatus(StatusBO statusBO, String nome, int danoTurno, double modificadorDefesa,
			int duracaoTurnos) {

		Status st = new Status(nome, danoTurno, modificadorDefesa, duracaoTurnos);

		if (!statusBO.existe(st)) {
			statusBO.inserir(st);
			System.out.println("   + Status criado: " + nome);
			st = statusBO.procurarPorNome(st);
		} else {
			st = statusBO.procurarPorNome(st);
		}

		return st;
	}

	private static void criarOuBuscarHabilidade(HabilidadesBO habBO, Map<String, Habilidade> map, Habilidade hab) {

		if (!habBO.existe(hab)) {
			habBO.inserir(hab);
			System.out.println("   + Habilidade criada: " + hab.getNome());
		}

		hab = habBO.procurarPorNome(hab);

		if (hab != null)
			map.put(hab.getNome(), hab);
	}

	private static void linkar(InimigoHabilidadeBO ihBO, Inimigo inimigo, Map<String, Habilidade> map, String nomeHab,
			int chanceUso) {

		Habilidade hab = map.get(nomeHab);

		if (hab == null) {
			System.out.println("⚠ Habilidade '" + nomeHab + "' não encontrada para " + inimigo.getNome());
			return;
		}

		// Usando o método adicionarHabilidade do seu BO
		InimigoHabilidade inimigoHab = new InimigoHabilidade();
		inimigoHab.setIdInimigo(inimigo.getId());
		inimigoHab.setIdHabilidade(hab.getId());
		inimigoHab.setChance_uso(chanceUso);

		if (ihBO.adicionarHabilidade(inimigoHab)) {
			System.out.println("   ✅ " + nomeHab + " → " + inimigo.getNome() + " (" + chanceUso + "% chance)");
		} else {
			System.out.println("   ❌ Falha ao atribuir " + nomeHab + " a " + inimigo.getNome());
		}
	}


	// Método para popular inimigos de exemplo (opcional)
	public static void popularInimigosExemplo(InimigoBO inimigoBO) {
		System.out.println(">> Criando inimigos de exemplo...");

		// Limpa inimigos existentes
		List<Inimigo> existentes = inimigoBO.pesquisarTodos();
		for (Inimigo i : existentes) {
			inimigoBO.excluir(i);
		}

		// Cria novos inimigos
		Inimigo urso = InimigoFactory.criarInimigo("Besta");
		urso.setNome("Urso Selvagem");
		urso.setHpMax(100);
		urso.setHp(100);
		urso.setAtaque(15);
		urso.setDefesa(10);
		urso.setRecompensaOuro(20);
		inimigoBO.inserir(urso);
		System.out.println("   🐻 Urso Selvagem criado");

		Inimigo lobo = InimigoFactory.criarInimigo("Besta");
		lobo.setNome("Lobo Feroz");
		lobo.setHpMax(70);
		lobo.setHp(70);
		lobo.setAtaque(12);
		lobo.setDefesa(8);
		lobo.setRecompensaOuro(15);
		inimigoBO.inserir(lobo);
		System.out.println("   🐺 Lobo Feroz criado");

		Inimigo ladrao = InimigoFactory.criarInimigo("Ladrao");
		ladrao.setNome("Assaltante das Sombras");
		ladrao.setHpMax(80);
		ladrao.setHp(80);
		ladrao.setAtaque(12);
		ladrao.setDefesa(5);
		ladrao.setRecompensaOuro(15);
		inimigoBO.inserir(ladrao);
		System.out.println("   🗡️ Assaltante das Sombras criado");

		Inimigo magico = InimigoFactory.criarInimigo("InimigoMagico");
		magico.setNome("Slime Arcano");
		magico.setHpMax(60);
		magico.setHp(60);
		magico.setAtaque(18);
		magico.setDefesa(3);
		magico.setRecompensaOuro(25);
		inimigoBO.inserir(magico);
		System.out.println("   🔮 Slime Arcano criado");

		Inimigo chefe = InimigoFactory.criarInimigo("Chefe");
		chefe.setNome("Lobo Alfa Supremo");
		chefe.setHpMax(200);
		chefe.setHp(200);
		chefe.setAtaque(25);
		chefe.setDefesa(15);
		chefe.setRecompensaOuro(50);
		chefe.setTipoIA(com.classes.Enums.TipoIA.CHEFE);
		inimigoBO.inserir(chefe);
		System.out.println("   👑 Lobo Alfa Supremo criado");

		System.out.println("✔ Inimigos de exemplo criados!");
	}

	// Método para testar o sistema de habilidades
	public static void testarHabilidades(InimigoHabilidadeBO inimigoHabBO, InimigoBO inimigoBO) {
		System.out.println("\n>> Testando sistema de habilidades...");

		for (Inimigo inimigo : inimigoBO.pesquisarTodos()) {
			System.out.println("\n🎯 " + inimigo.getNome() + ":");

			// Lista habilidades configuradas
			List<InimigoHabilidade> habilidades = inimigoHabBO.listarHabilidades(inimigo.getId());
			if (habilidades.isEmpty()) {
				System.out.println("   Nenhuma habilidade configurada");
				continue;
			}

			for (InimigoHabilidade ih : habilidades) {
				System.out.println(
						"   - Habilidade ID: " + ih.getIdHabilidade() + " (Chance: " + ih.getChance_uso() + "%)");
			}

			// Testa escolha aleatória
			System.out.println("\n   🎲 Simulando 10 escolhas de habilidades:");
			Map<Integer, Integer> contagem = new HashMap<>();
			for (int i = 0; i < 10; i++) {
				InimigoHabilidade escolhida = inimigoHabBO.escolherHabilidade(inimigo);
				if (escolhida != null) {
					int idHabilidade = escolhida.getIdHabilidade();
					contagem.put(idHabilidade, contagem.getOrDefault(idHabilidade, 0) + 1);
				} else {
					System.out.println("   Turno " + (i + 1) + ": Ataque básico");
				}
			}

			// Mostra estatísticas
			for (Map.Entry<Integer, Integer> entry : contagem.entrySet()) {
				System.out.println("   Habilidade ID " + entry.getKey() + ": " + entry.getValue() + " vezes");
			}
		}

		System.out.println("\n✔ Teste de habilidades concluído!");
	}
}