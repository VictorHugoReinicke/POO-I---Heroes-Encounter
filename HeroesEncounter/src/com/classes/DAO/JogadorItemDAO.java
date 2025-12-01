package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Item;
import com.classes.DTO.JogadorItem;

public class JogadorItemDAO {

	final String NOMEDATABELA = "JogadorItem";
    
	private ItemDAO itemDAO; 
    
    public JogadorItemDAO() {
        this.itemDAO = new ItemDAO();
    }


	public boolean inserir(JogadorItem jogadorItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (idJogador, idItem, quantidade, equipado) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, jogadorItem.getIdJogador());
			ps.setInt(2, jogadorItem.getIdItem());
			ps.setInt(3, jogadorItem.getQuantidade());
			ps.setInt(4, jogadorItem.getEquipadoDB()); 

			ps.executeUpdate();
			ps.close();
			conn.close();
            
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(JogadorItem jogadorItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET quantidade = ?, equipado = ? " 
                    + "WHERE idJogador = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, jogadorItem.getQuantidade());
			ps.setInt(2, jogadorItem.getEquipadoDB());
            
			ps.setInt(3, jogadorItem.getIdJogador());
			ps.setInt(4, jogadorItem.getIdItem());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(JogadorItem jogadorItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE idJogador = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, jogadorItem.getIdJogador());
			ps.setInt(2, jogadorItem.getIdItem());
			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public JogadorItem procurarRegistro(int idJogador, int idItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idJogador, idItem, quantidade, equipado FROM " + NOMEDATABELA 
					+ " WHERE idJogador = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idJogador);
            ps.setInt(2, idItem);
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				JogadorItem obj = montarObjeto(rs);

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

   
	public List<JogadorItem> listarItensPorJogador(int idJogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idJogador, idItem, quantidade, equipado FROM " + NOMEDATABELA 
					+ " WHERE idJogador = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idJogador);
			ResultSet rs = ps.executeQuery();
			
			List<JogadorItem> listObj = montarLista(rs);
			
            ps.close();
			rs.close();
			conn.close();
            
            return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private JogadorItem montarObjeto(ResultSet rs) throws Exception {
		
		int idItem = rs.getInt("idItem");
        
		JogadorItem ji = new JogadorItem();

        ji.setIdJogador(rs.getInt("idJogador"));
        ji.setIdItem(idItem);
        ji.setQuantidade(rs.getInt("quantidade"));
        ji.setEquipado(rs.getBoolean("equipado"));

        Item itemCompleto = itemDAO.procurarPorCodigo(idItem);
        ji.setItem(itemCompleto);
               
		return ji;
	}

	private List<JogadorItem> montarLista(ResultSet rs) {
		List<JogadorItem> listObj = new ArrayList<JogadorItem>();
		try {
			while (rs.next()) {
				JogadorItem obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean existe(int idJogador, int idItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE idJogador = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idJogador);
            ps.setInt(2, idItem);
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