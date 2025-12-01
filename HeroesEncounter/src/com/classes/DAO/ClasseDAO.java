package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.classes.DTO.Classe;
import com.classes.Conexao.Conexao;

public class ClasseDAO {

	final String NOMEDATABELA = "Classe";

	public boolean inserir(Classe classe) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, bonus_forca, bonus_magia, bonus_esquiva) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, classe.getNome());
			ps.setInt(2, classe.getBonus_forca());
			ps.setInt(3, classe.getBonus_magia());
			ps.setInt(4, classe.getBonus_esquiva());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(Classe classe) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, bonus_forca = ?, bonus_magia = ?, bonus_esquiva = ? " + "WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, classe.getNome());
			ps.setInt(2, classe.getBonus_forca());
			ps.setInt(3, classe.getBonus_magia());
			ps.setInt(4, classe.getBonus_esquiva());
			ps.setInt(5, classe.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Classe classe) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, classe.getId());
			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Classe procurarPorCodigo(int id) {
	    try {
	        Connection conn = Conexao.conectar();
	        String sql = "SELECT id, nome, bonus_forca, bonus_magia, bonus_esquiva FROM " + NOMEDATABELA
	                + " WHERE id = ?;";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        

	        ps.setInt(1, id);
	        
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {

	            Classe obj = new Classe();
	            obj.setId(rs.getInt("id"));
	            obj.setNome(rs.getString("nome"));
	            obj.setBonus_forca(rs.getInt("bonus_forca"));
	            obj.setBonus_magia(rs.getInt("bonus_magia"));
	            obj.setBonus_esquiva(rs.getInt("bonus_esquiva"));
	            
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

	public Classe procurarPorNome(Classe classe) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, bonus_forca, bonus_magia, bonus_esquiva FROM " + NOMEDATABELA
					+ " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, classe.getNome());
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
			
				Classe obj = new Classe();
				obj.setId(rs.getInt("id"));
				obj.setNome(rs.getString("nome"));
				obj.setBonus_forca(rs.getInt("bonus_forca"));
				obj.setBonus_magia(rs.getInt("bonus_magia"));
				obj.setBonus_esquiva(rs.getInt("bonus_esquiva"));
				
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
	
	public boolean existe(Classe classe) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, classe.getNome());
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

}
