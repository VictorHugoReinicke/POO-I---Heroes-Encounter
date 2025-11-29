package com.classes.BO;

import com.classes.DTO.Jogador;
import com.classes.DAO.JogadorDAO;
import java.util.List;

public class JogadorBO {

	public boolean inserir(Jogador jogador) {
		if (existe(jogador) != true) {
			JogadorDAO marcasDAO = new JogadorDAO();
			return marcasDAO.inserir(jogador);
		}
		return false;
	}

	public boolean alterar(Jogador jogador) {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.alterar(jogador);
	}

	public boolean excluir(Jogador jogador) {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.excluir(jogador);
	}

	public Jogador procurarPorCodigo(Jogador jogador) {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.procurarPorCodigo(jogador);
	}

	public Jogador procurarPorNome(Jogador jogador) {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.procurarPorNome(jogador);
	}

	public boolean existe(Jogador jogador) {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.existe(jogador);
	}

	public List<Jogador> pesquisarTodos() {
		JogadorDAO jogadoresDAO = new JogadorDAO();
		return jogadoresDAO.pesquisarTodos();
	}
}
