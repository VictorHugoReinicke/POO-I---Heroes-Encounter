package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Status;

public class StatusDAO {

	final String NOMEDATABELA = "Status";

    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

	public boolean inserir(Status status) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, dano_turno, modificador_defesa, duracao_turnos) VALUES (?, ?, ?, ?)";
			
            // Usamos RETURN_GENERATED_KEYS para obter o ID após a inserção
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, status.getNome());
			ps.setInt(2, status.getDanoTurno());
            // Para double/DECIMAL, usamos setDouble
			ps.setDouble(3, status.getModificadorDefesa());
			ps.setInt(4, status.getDuracaoTurnos());

			ps.executeUpdate();
			
            // Busca o ID gerado e atualiza o DTO
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                status.setId(rs.getInt(1)); 
            }
            
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(Status status) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, dano_turno = ?, modificador_defesa = ?, duracao_turnos = ? " 
                    + "WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, status.getNome());
			ps.setInt(2, status.getDanoTurno());
			ps.setDouble(3, status.getModificadorDefesa());
			ps.setInt(4, status.getDuracaoTurnos());
			ps.setInt(5, status.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Status status) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status.getId());
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

	public Status procurarPorCodigo(int id) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, dano_turno, modificador_defesa, duracao_turnos FROM " + NOMEDATABELA
					+ " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				Status obj = montarObjeto(rs);
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

	public Status procurarPorNome(Status status) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, dano_turno, modificador_defesa, duracao_turnos FROM " + NOMEDATABELA
					+ " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status.getNome());
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				Status obj = montarObjeto(rs);
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
    
    public boolean existe(Status status) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status.getNome());
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

	public List<Status> pesquisarTodos() {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, dano_turno, modificador_defesa, duracao_turnos FROM " + NOMEDATABELA + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Status> listObj = montarLista(rs);
			
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

	private Status montarObjeto(ResultSet rs) throws Exception {
		
		Status s = new Status();
        
        s.setId(rs.getInt("id"));
        s.setNome(rs.getString("nome"));
        s.setDanoTurno(rs.getInt("dano_turno"));
        // Para o tipo DECIMAL/double, usamos getDouble
        s.setModificadorDefesa(rs.getDouble("modificador_defesa")); 
        s.setDuracaoTurnos(rs.getInt("duracao_turnos"));
        
		return s;
	}

	private List<Status> montarLista(ResultSet rs) {
		List<Status> listObj = new ArrayList<Status>();
		try {
			while (rs.next()) {
				Status obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}