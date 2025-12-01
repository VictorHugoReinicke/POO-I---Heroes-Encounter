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

	// ------------------------------------------------------------------
	// --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
	// ------------------------------------------------------------------

	public boolean inserir(Inimigo inimigo) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, tipo, vida_total, dano, gold_recompensa, ia_tipo) VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// ATRIBUTOS
			ps.setString(1, inimigo.getNome());
			// Usamos o nome da classe DTO (Ex: "Besta") para o campo 'tipo' do banco.
			ps.setString(2, inimigo.getClass().getSimpleName());
			ps.setInt(3, inimigo.getHpMax()); // Mapeamento: vida_total <-> getHpMax()
			ps.setInt(4, inimigo.getAtaque());
			ps.setInt(5, inimigo.getRecompensaOuro()); // Mapeamento: gold_recompensa <-> getRecompensaOuro()
			ps.setString(6, inimigo.getTipoIA().name()); // Mapeamento: ia_tipo <-> TipoIA
			

			ps.executeUpdate();

			// Busca o ID gerado e atualiza o DTO (ID herdado de SerVivo)
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
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, tipo = ?, vida_total = ?, dano = ?, gold_recompensa = ?, ia_tipo = ? WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			// ATRIBUTOS
			ps.setString(1, inimigo.getNome());
			ps.setString(2, inimigo.getClass().getSimpleName());
			ps.setInt(3, inimigo.getHpMax()); // vida_total
			ps.setInt(4, inimigo.getAtaque());
			ps.setInt(5, inimigo.getRecompensaOuro()); // gold_recompensa
			ps.setString(6, inimigo.getTipoIA().name()); // ia_tipo
			ps.setInt(7, inimigo.getId()); // ID herdado

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

	// ------------------------------------------------------------------
	// --- 2. MÉTODOS DE BUSCA ---
	// ------------------------------------------------------------------

	/**
	 * Procura um inimigo por ID usando a Factory para instanciar o tipo correto.
	 */
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

	/**
	 * Retorna todos os inimigos, instanciando o tipo correto (Polimorfismo).
	 */
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

	// ------------------------------------------------------------------
	// --- 3. MÉTODOS AUXILIARES ---
	// ------------------------------------------------------------------

	private Inimigo montarObjeto(ResultSet rs) throws Exception {

		// LÊ O TIPO DA CLASSE (e.g., "Besta") SALVO na coluna 'tipoClasse'
		String tipoClasse = rs.getString("tipo");

		// USA A FACTORY PARA INSTANCIAR O OBJETO CORRETO
		Inimigo i = InimigoFactory.criarInimigo(tipoClasse);

		if (i == null) {
			throw new Exception("Falha ao criar instância de Inimigo do tipo: " + tipoClasse);
		}

		// 1. Atributos HERDADOS de SerVivo
		i.setId(rs.getInt("id"));
		i.setNome(rs.getString("nome"));
		i.setHpMax(rs.getInt("vida_total")); // Mapeamento: vida_total -> setHpMax()

		// 2. Atributos PRÓPRIOS de Inimigo
		// O campo 'tipo' do banco é ignorado, pois o tipo real já está na instância
		// 'i'.
		i.setAtaque(rs.getInt("dano")); // Mapeamento: dano -> setAtaque()
		i.setRecompensaOuro(rs.getInt("gold_recompensa")); // Mapeamento: gold_recompensa -> setRecompensaOuro()
		i.setTipoIA(TipoIA.valueOf(rs.getString("ia_tipo"))); // Mapeamento: ia_tipo -> setTipoIA()

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