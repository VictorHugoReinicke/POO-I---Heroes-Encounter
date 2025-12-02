package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Habilidade;

public class HabilidadesDAO {

	final String NOMEDATABELA = "Habilidades";

	public boolean inserir(Habilidade habilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (nome, custo_mana, fator_dano, tipo, idStatus) VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, habilidade.getNome());
			ps.setInt(2, habilidade.getCustoMana());
			ps.setDouble(3, habilidade.getFatorDano());
			ps.setString(4, habilidade.getTipo());
            
            if (habilidade.getIdStatus() == 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, habilidade.getIdStatus());
            }

			ps.executeUpdate();
			
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                habilidade.setId(rs.getInt(1)); 
            }
            
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(Habilidade habilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, custo_mana = ?, fator_dano = ?, tipo = ?, idStatus = ? " 
                    + "WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, habilidade.getNome());
			ps.setInt(2, habilidade.getCustoMana());
			ps.setDouble(3, habilidade.getFatorDano());
			ps.setString(4, habilidade.getTipo());
            
            if (habilidade.getIdStatus() == 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, habilidade.getIdStatus());
            }

			ps.setInt(6, habilidade.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Habilidade habilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, habilidade.getId());
			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Habilidade procurarPorCodigo(int id) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, custo_mana, fator_dano, tipo, idStatus FROM " + NOMEDATABELA
					+ " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				Habilidade obj = montarObjeto(rs);
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

	public Habilidade procurarPorNome(Habilidade habilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, custo_mana, fator_dano, tipo, idStatus FROM " + NOMEDATABELA
					+ " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, habilidade.getNome());
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				Habilidade obj = montarObjeto(rs);
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
    
    public boolean existe(Habilidade habilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, habilidade.getNome());
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

	public List<Habilidade> pesquisarTodos() {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, custo_mana, fator_dano, tipo, idStatus FROM " + NOMEDATABELA + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Habilidade> listObj = montarLista(rs);
			
            ps.close();
			rs.close();
			conn.close();
            
            return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Habilidade montarObjeto(ResultSet rs) throws Exception {
		
		Habilidade h = new Habilidade();
        
        h.setId(rs.getInt("id"));
        h.setNome(rs.getString("nome"));
        h.setCustoMana(rs.getInt("custo_mana"));
        h.setFatorDano(rs.getDouble("fator_dano"));
        h.setTipo(rs.getString("tipo"));
        
        h.setIdStatus(rs.getInt("idStatus"));
        
		return h;
	}

	private List<Habilidade> montarLista(ResultSet rs) {
		List<Habilidade> listObj = new ArrayList<Habilidade>();
		try {
			while (rs.next()) {
				Habilidade obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}