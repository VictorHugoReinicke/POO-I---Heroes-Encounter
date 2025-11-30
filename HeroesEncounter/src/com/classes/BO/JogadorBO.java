package com.classes.BO;

import com.classes.DTO.Jogador;
import com.classes.DAO.JogadorDAO;
import java.util.List;

public class JogadorBO {

	private JogadorDAO jogadorDAO;
	
	public JogadorBO() {
        this.jogadorDAO = new JogadorDAO();
    }
	
	public boolean inserir(Jogador jogador) {
		if (this.existe(jogador) != true) { 
			return jogadorDAO.inserir(jogador); 
		}
		return false;
	}

	public boolean alterar(Jogador jogador) {
		return jogadorDAO.alterar(jogador);
	}

	public boolean excluir(Jogador jogador) {
		return jogadorDAO.excluir(jogador);
	}

	public Jogador procurarPorCodigo(Jogador jogador) {
		return jogadorDAO.procurarPorCodigo(jogador);
	}

	public Jogador procurarPorNome(Jogador jogador) {
		return jogadorDAO.procurarPorNome(jogador);
	}

	public boolean existe(Jogador jogador) {
		return jogadorDAO.existe(jogador);
	}

	public List<Jogador> pesquisarTodos() {
		return jogadorDAO.pesquisarTodos();
	}
	
	
}
