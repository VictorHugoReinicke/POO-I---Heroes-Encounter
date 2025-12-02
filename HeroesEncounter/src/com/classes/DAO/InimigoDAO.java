package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Inimigo;
import com.classes.Enums.TipoIA;
import com.classes.DTO.InimigoFactory;

public class InimigoDAO {

	final String NOMEDATABELA = "Inimigo";

	public boolean inserir(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, tipo, vida_total, dano, gold_recompensa, ia_tipo) VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, inimigo.getNome());
			ps.setString(2, inimigo.getClass().getSimpleName());
			ps.setInt(3, inimigo.getHpMax());
			ps.setInt(4, inimigo.getAtaque());
			ps.setInt(5, inimigo.getRecompensaOuro());
			ps.setString(6, inimigo.getTipoIA().name());
			

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				inimigo.setId(rs.getInt(1));
			}

			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA + " SET nome = ?, tipo = ?, vida_total = ?, dano = ?, gold_recompensa = ?, ia_tipo = ? WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, inimigo.getNome());
			ps.setString(2, inimigo.getClass().getSimpleName());
			ps.setInt(3, inimigo.getHpMax());
			ps.setInt(4, inimigo.getAtaque());
			ps.setInt(5, inimigo.getRecompensaOuro());
			ps.setString(6, inimigo.getTipoIA().name());
			ps.setInt(7, inimigo.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, inimigo.getId());
			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Inimigo procurarPorCodigo(int id) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Inimigo obj = montarObjeto(rs);
				ps.close();
				rs.close();
				conn.close();
				return obj;
			}

			ps.close();
			rs.close();
			conn.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Inimigo> pesquisarTodos() {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Inimigo> listObj = montarLista(rs);

			ps.close();
			rs.close();
			conn.close();

			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Inimigo montarObjeto(ResultSet rs) throws Exception {

		String tipoClasse = rs.getString("tipo");

		Inimigo i = InimigoFactory.criarInimigo(tipoClasse);

		if (i == null) {
			throw new Exception("Falha ao criar instância de Inimigo do tipo: " + tipoClasse);
		}

		i.setId(rs.getInt("id"));
		i.setNome(rs.getString("nome"));
		i.setHpMax(rs.getInt("vida_total"));

		i.setAtaque(rs.getInt("dano"));
		i.setRecompensaOuro(rs.getInt("gold_recompensa"));
		i.setTipoIA(TipoIA.valueOf(rs.getString("ia_tipo")));

		return i;
	}

	private List<Inimigo> montarLista(ResultSet rs) {
		List<Inimigo> listObj = new ArrayList<Inimigo>();
		try {
			while (rs.next()) {
				Inimigo obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Inimigo procurarPorNome(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, inimigo.getNome());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Inimigo obj = montarObjeto(rs);
				ps.close();
				rs.close();
				conn.close();
				return obj;
			}

			ps.close();
			rs.close();
			conn.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean existe(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, inimigo.getNome());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				ps.close();
				rs.close();
				conn.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}