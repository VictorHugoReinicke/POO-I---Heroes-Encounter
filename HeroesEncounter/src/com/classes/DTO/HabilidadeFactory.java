package com.classes.DTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.classes.BO.*;

public class HabilidadeFactory {

	public static void criarHabilidadesIniciais(HabilidadesBO habBO) {
		if (habBO == null)
			throw new IllegalArgumentException("HabilidadesBO é nulo");

		ClasseHabilidadeBO chBO = new ClasseHabilidadeBO();
		ClasseBO classeBO = new ClasseBO();
		StatusBO statusBO = new StatusBO();

		System.out.println(">> Criando Status para Habilidades...");

		Status queimadura = criarOuBuscarStatus(statusBO, "Queimadura", 10, 0.0, 3);
		Status sangramento = criarOuBuscarStatus(statusBO, "Sangramento", 11, 0.0, 4);
		Status congelamento = criarOuBuscarStatus(statusBO, "Congelamento", 13, -0.3, 2);

		Status protecaoDivina = criarOuBuscarStatus(statusBO, "Proteção Divina", 0, 0.9, 2);
		Status ilusao = criarOuBuscarStatus(statusBO, "Ilusão", 0, 0.0, 1);

		ilusao.setChanceEsquiva(0.8);
		try {
			statusBO.alterar(ilusao);
			System.out.println(
					"Status Ilusão configurado com chance de esquiva: " + (ilusao.getChanceEsquiva() * 100) + "%");
		} catch (Exception e) {
			System.err.println("Erro ao configurar chance de esquiva para Ilusão: " + e.getMessage());
		}

		List<Habilidade> paladino = Arrays.asList(
				new Habilidade("Defesa Divina", 10, 0, "DEFENSIVA", protecaoDivina.getId()),
				new Habilidade("Expurgar", 18, 1.5, "OFENSIVA"),
				new Habilidade("Benção Divina", 20, 0.6, "CURA")
		);

		List<Habilidade> mago = Arrays.asList(new Habilidade("Ilusão Arcana", 4, 0, "DEFENSIVA", ilusao.getId()),

				new Habilidade("Bola de Fogo", 12, 2.2, "OFENSIVA", queimadura.getId()),
				new Habilidade("Estaca de Gelo", 10, 3.0, "OFENSIVA", congelamento.getId())
		);

		List<Habilidade> guerreiro = Arrays.asList(new Habilidade("Golpe Forte", 13, 2.0, "OFENSIVA"),
				new Habilidade("Jogo de Pés", 8, 0, "DEFENSIVA", ilusao.getId()),
				new Habilidade("Sangramento", 7, 1.0, "OFENSIVA", sangramento.getId()));

		Map<String, Habilidade> habilidadesMap = new HashMap<>();

		System.out.println(">> Criando Habilidades...");
		paladino.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));
		mago.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));
		guerreiro.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));

		Classe paladinoClasse = new Classe("Paladino", 0, 0, 0);
		Classe magoClasse = new Classe("Mago", 0, 0, 0);
		Classe guerreiroClasse = new Classe("Guerreiro", 0, 0, 0);

		if (!classeBO.existe(paladinoClasse))
			classeBO.inserir(paladinoClasse);
		if (!classeBO.existe(magoClasse))
			classeBO.inserir(magoClasse);
		if (!classeBO.existe(guerreiroClasse))
			classeBO.inserir(guerreiroClasse);

		paladinoClasse = classeBO.procurarPorNome(paladinoClasse);
		magoClasse = classeBO.procurarPorNome(magoClasse);
		guerreiroClasse = classeBO.procurarPorNome(guerreiroClasse);

		if (paladinoClasse == null || magoClasse == null || guerreiroClasse == null) {
			System.out.println("Erro: Não foi possível garantir as classes no banco. Abortando linkagem.");
			return;
		}

		System.out.println(">> Linkando Habilidades às Classes...");

		atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Defesa Divina");
		atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Expurgar");
		atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Benção Divina");

		atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Ilusão Arcana");
		atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Bola de Fogo");
		atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Estaca de Gelo");

		atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Golpe Forte");
		atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Jogo de Pés");
		atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Sangramento");

		System.out.println("Habilidades criadas e linkadas às classes com sucesso!");
	}

	private static Status criarOuBuscarStatus(StatusBO statusBO, String nome, int danoTurno, double modificadorDefesa,
			int duracaoTurnos) {
		Status status = new Status(nome, danoTurno, modificadorDefesa, duracaoTurnos);

		if (!statusBO.existe(status)) {
			statusBO.inserir(status);
			System.out.println("   + Status criado: " + nome);
			status = statusBO.procurarPorNome(status);
		} else {
			status = statusBO.procurarPorNome(status);
		}

		return status;
	}

	private static void salvarOuBuscar(HabilidadesBO habBO, Habilidade h, Map<String, Habilidade> map) {
		try {
			if (!habBO.existe(h)) {
				habBO.inserir(h);
				System.out.println("   + Habilidade criada: " + h.getNome());
			} else {
				Habilidade encontrado = habBO.procurarPorNome(h);
				if (encontrado != null) {
					h.setId(encontrado.getId());
					System.out.println("   ✔ Habilidade encontrada: " + h.getNome() + " (ID: " + h.getId() + ")");
				}
			}
			Habilidade doDB = habBO.procurarPorNome(h);
			if (doDB != null) {
				map.put(doDB.getNome(), doDB);
			} else {
				System.out.println("Falha ao garantir habilidade: " + h.getNome());
			}
		} catch (Exception e) {
			System.err.println("Erro ao processar habilidade " + h.getNome() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void atribuirSeExistir(ClasseHabilidadeBO chBO, int idClasse, Map<String, Habilidade> map,
			String nomeHabilidade) {
		Habilidade hab = map.get(nomeHabilidade);
		if (hab == null) {
			System.out.println("Habilidade '" + nomeHabilidade + "' não encontrada — não foi atribuída à classe ID "
					+ idClasse);
			return;
		}
		chBO.atribuirHabilidade(idClasse, hab.getId());
		System.out.println("Link " + nomeHabilidade + " → Classe ID " + idClasse);
	}
}