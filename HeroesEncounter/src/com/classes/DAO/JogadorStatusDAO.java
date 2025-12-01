package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.JogadorStatus;
import com.classes.DTO.Status;

public class JogadorStatusDAO {

	final String NOMEDATABELA = "JogadorStatus";
    
    // DAO necessário para carregar o DTO Status completo
	private StatusDAO statusDAO; 
    
    public JogadorStatusDAO() {
        // Assume que StatusDAO já existe e está funcional
        this.statusDAO = new StatusDAO(); 
    }

    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

    /**
     * Aplica um novo status ao jogador.
     */
	public boolean inserir(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
            // idJogador e idStatus formam a chave composta
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (idJogador, idStatus, turnos_restantes) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, js.getIdJogador());
			ps.setInt(2, js.getIdStatus());
            ps.setInt(3, js.getTurnosRestantes());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * Atualiza os turnos restantes de um status ativo.
     * Usado a cada turno de combate.
     */
	public boolean alterar(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET turnos_restantes = ? " 
                    + "WHERE idJogador = ? AND idStatus = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, js.getTurnosRestantes());
            
            // Chaves compostas
			ps.setInt(2, js.getIdJogador());
			ps.setInt(3, js.getIdStatus());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * Remove um status do jogador (usado quando turnosRestantes chega a zero).
     */
	public boolean excluir(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA 
                    + " WHERE idJogador = ? AND idStatus = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, js.getIdJogador());
			ps.setInt(2, js.getIdStatus());
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
     * Procura um registro específico de status para um jogador (chave composta).
     */
	public JogadorStatus procurarRegistro(int idJogador, int idStatus) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idJogador, idStatus, turnos_restantes FROM " + NOMEDATABELA 
					+ " WHERE idJogador = ? AND idStatus = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idJogador);
            ps.setInt(2, idStatus);
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				JogadorStatus obj = montarObjeto(rs);

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
     * Lista todos os status ativos que um jogador possui.
     */
	public List<JogadorStatus> listarStatusPorJogador(int idJogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idJogador, idStatus, turnos_restantes FROM " + NOMEDATABELA 
					+ " WHERE idJogador = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idJogador);
			ResultSet rs = ps.executeQuery();
			
			List<JogadorStatus> listObj = montarLista(rs);
			
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

	private JogadorStatus montarObjeto(ResultSet rs) throws Exception {
		
		int idStatus = rs.getInt("idStatus");
        
		JogadorStatus js = new JogadorStatus();
        
        // Atributos de JogadorStatus
        js.setIdJogador(rs.getInt("idJogador"));
        js.setIdStatus(idStatus);
        js.setTurnosRestantes(rs.getInt("turnos_restantes"));

        // ❗️ CARREGAMENTO DO STATUS COMPLETO
        // Usa o StatusDAO para carregar os detalhes do efeito (dano/modificador)
        Status statusCompleto = statusDAO.procurarPorCodigo(idStatus);
        js.setStatus(statusCompleto);
        
        // NOTE: O DTO Jogador não é carregado aqui para manter a performance.
        
		return js;
	}

	private List<JogadorStatus> montarLista(ResultSet rs) {
		List<JogadorStatus> listObj = new ArrayList<JogadorStatus>();
		try {
			while (rs.next()) {
				JogadorStatus obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * Verifica se a combinação Jogador/Status já existe.
     */
	public boolean existe(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE idJogador = ? AND idStatus = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, js.getIdJogador());
            ps.setInt(2, js.getIdStatus());
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
                ps.close();
				rs.close();
				conn.close();
				return true;
			}
            ps.close();
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}