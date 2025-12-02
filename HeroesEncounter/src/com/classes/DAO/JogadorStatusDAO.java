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

	private StatusDAO statusDAO; 
    
    public JogadorStatusDAO() {
        this.statusDAO = new StatusDAO(); 
    }

	public boolean inserir(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
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

	public boolean alterar(JogadorStatus js) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET turnos_restantes = ? " 
                    + "WHERE idJogador = ? AND idStatus = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, js.getTurnosRestantes());
            
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

	private JogadorStatus montarObjeto(ResultSet rs) throws Exception {
		
		int idStatus = rs.getInt("idStatus");
        
		JogadorStatus js = new JogadorStatus();

        js.setIdJogador(rs.getInt("idJogador"));
        js.setIdStatus(idStatus);
        js.setTurnosRestantes(rs.getInt("turnos_restantes"));

        Status statusCompleto = statusDAO.procurarPorCodigo(idStatus);
        js.setStatus(statusCompleto);

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