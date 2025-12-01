package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.classes.DTO.Shop;
import com.classes.Conexao.Conexao;

public class ShopDAO {

	final String NOMEDATABELA = "Shop";
	
	public boolean inserir(Shop shop) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "INSERT INTO " + NOMEDATABELA + " (nome) VALUES (?);";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, shop.getNome());

            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                shop.setId(rs.getInt(1));
            }

            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public Shop procurarPorCodigo(int id) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT id, nome FROM " + NOMEDATABELA + " WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Shop obj = montarObjeto(rs);
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
	
	public Shop procurarPorNome(Shop shop) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT id, nome FROM " + NOMEDATABELA + " WHERE nome = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, shop.getNome());
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Shop obj = montarObjeto(rs);
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
	
	public boolean existe(Shop shop) {
        return procurarPorNome(shop) != null;
    }
	
	private Shop montarObjeto(ResultSet rs) throws Exception {
        Shop obj = new Shop();
        obj.setId(rs.getInt("id"));
        obj.setNome(rs.getString("nome"));
        return obj;
    }
}
