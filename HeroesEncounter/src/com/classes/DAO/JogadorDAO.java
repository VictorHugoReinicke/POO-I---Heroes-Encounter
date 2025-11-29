package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.classes.DTO.Guerreiro;
import com.classes.DTO.Jogador;
import com.classes.DTO.Mago;
import com.classes.DTO.Paladino;
import com.classes.Conexao.Conexao;

public class JogadorDAO {

	final String NOMEDATABELA = "Jogador";

	public boolean inserir(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, gold, vida_atual, idClasse, mana_atual) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, jogador.getNome()); // nome
			ps.setInt(2, jogador.getOuro()); // gold (de Jogador)
			ps.setInt(3, jogador.getHp());// vida_atual (de SerVivo)
			ps.setInt(4, jogador.getIdClasse()); // idClasse (de Jogador)
			ps.setInt(5, jogador.getMana()); // mana_atual (de SerVivo)

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, gold = ?, idClasse = ?, vida_atual = ?, mana_atual = ? " + "WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, jogador.getNome());
			ps.setInt(2, jogador.getOuro());
			ps.setInt(3, jogador.getIdClasse());
			ps.setInt(4, jogador.getHp());
			ps.setInt(5, jogador.getMana());

			ps.setInt(6, jogador.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, jogador.getId());
			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Jogador procurarPorCodigo(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, jogador.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Jogador obj = montarObjeto(rs);

				ps.close();
				rs.close();
				conn.close();
				return obj;
			} else {
				ps.close();
				rs.close();
				conn.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Jogador procurarPorNome(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, jogador.getNome());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Jogador obj = montarObjeto(rs);
				ps.close();
				rs.close();
				conn.close();
				return obj;
			} else {
				ps.close();
				rs.close();
				conn.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean existe(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, jogador.getNome());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ps.close();
				rs.close();
				conn.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public List<Jogador> pesquisarTodos() {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Jogador> listObj = montarLista(rs);
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Jogador montarObjeto(ResultSet rs) throws Exception {
		int idClasse = rs.getInt("idClasse");
		Jogador obj = null;

		switch (idClasse) {
		case 1:
			obj = new Guerreiro();
			break;
		case 2:
			obj = new Mago();
			break;
		case 3:
			obj = new Paladino();
			break;
		default:
			throw new IllegalArgumentException(
					"ID da Classe (" + idClasse + ") inválido encontrado no banco de dados.");
		}

		obj.setId(rs.getInt("id"));
		obj.setNome(rs.getString("nome"));
		obj.setHp(rs.getInt("vida_atual"));
		obj.setMana(rs.getInt("mana_atual"));
		obj.setOuro(rs.getInt("gold"));

		obj.setIdClasse(idClasse);

		return obj;
	}

	public List<Jogador> montarLista(ResultSet rs) {
		List<Jogador> listObj = new ArrayList<Jogador>();
		try {
			while (rs.next()) {
				Jogador obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
