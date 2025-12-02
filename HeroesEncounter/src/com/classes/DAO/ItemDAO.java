package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.classes.DTO.*;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Item;

public class ItemDAO {

	final String NOMEDATABELA = "Item";

	public boolean inserir(Item item) {

		Connection conn = null;
		PreparedStatement psItem = null;
		PreparedStatement psSubtipo = null;
		ResultSet rs = null;

		try {
			conn = Conexao.conectar();

			conn.setAutoCommit(false);

			String sqlItem = "INSERT INTO Item (nome, preco) VALUES (?, ?);";

			psItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
			psItem.setString(1, item.getNome());
			psItem.setInt(2, 0);

			int affectedRows = psItem.executeUpdate();
			if (affectedRows == 0) {
				throw new Exception("Falha ao inserir Item, nenhuma linha afetada.");
			}

			rs = psItem.getGeneratedKeys();
			if (rs.next()) {
				item.setId(rs.getInt(1));
			} else {
				throw new Exception("Falha ao obter ID do Item após inserção.");
			}

			String sqlSubtipo = "";

			if (item instanceof ItemArma) {
				ItemArma arma = (ItemArma) item;
				sqlSubtipo = "INSERT INTO Arma (idItem, bonusDano, bonusMagico, bonusCritico) VALUES (?, ?, ?, ?);";
				psSubtipo = conn.prepareStatement(sqlSubtipo);
				psSubtipo.setInt(1, item.getId());
				psSubtipo.setInt(2, arma.getBonusDano());
				psSubtipo.setInt(3, arma.getBonusMagico());
				psSubtipo.setInt(4, arma.getBonusCritico());

			} else if (item instanceof ItemDefesa) {
				ItemDefesa defesa = (ItemDefesa) item;
				sqlSubtipo = "INSERT INTO Defesa (idItem, bonusDefesa, bonusEsquiva) VALUES (?, ?, ?);";
				psSubtipo = conn.prepareStatement(sqlSubtipo);
				psSubtipo.setInt(1, item.getId());
				psSubtipo.setInt(2, defesa.getBonusDefesa());
				psSubtipo.setInt(3, defesa.getBonusEsquiva());

			} else if (item instanceof ItemConsumivel) {
				ItemConsumivel consumivel = (ItemConsumivel) item;
				sqlSubtipo = "INSERT INTO Consumivel (idItem, cura, mana) VALUES (?, ?, ?);";
				psSubtipo = conn.prepareStatement(sqlSubtipo);
				psSubtipo.setInt(1, item.getId());
				psSubtipo.setInt(2, consumivel.getCura());
				psSubtipo.setInt(3, consumivel.getMana());

			} else {
				throw new IllegalArgumentException("Tipo de Item inválido: " + item.getTipoItem());
			}

			psSubtipo.executeUpdate();

			conn.commit();
			return true;

		} catch (Exception e) {

			try {
				if (conn != null)
					conn.rollback();
			} catch (Exception rb) {
				rb.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (psItem != null)
					psItem.close();
				if (psSubtipo != null)
					psSubtipo.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
	}


	public boolean alterar(Item item) {
	    Connection conn = null;
	    PreparedStatement psItem = null;
	    PreparedStatement psSubtipo = null;

	    try {
	        conn = Conexao.conectar();
	        conn.setAutoCommit(false); 

	  
	        String sqlItem = "UPDATE Item SET nome = ?, preco = ? WHERE id = ?;";
	        psItem = conn.prepareStatement(sqlItem);
	        psItem.setString(1, item.getNome());
	        psItem.setInt(2, 0); 
	        psItem.setInt(3, item.getId());
	        psItem.executeUpdate();

	      
	        
	        if (item instanceof ItemArma) {
	            ItemArma arma = (ItemArma) item;
	            String sqlArma = "UPDATE Arma SET bonusDano = ?, bonusMagico = ?, bonusCritico = ? WHERE idItem = ?;";
	            psSubtipo = conn.prepareStatement(sqlArma);
	            psSubtipo.setInt(1, arma.getBonusDano());
	            psSubtipo.setInt(2, arma.getBonusMagico());
	            psSubtipo.setInt(3, arma.getBonusCritico());
	            psSubtipo.setInt(4, item.getId());
	            
	        } else if (item instanceof ItemDefesa) {
	            ItemDefesa defesa = (ItemDefesa) item;
	            String sqlDefesa = "UPDATE Defesa SET bonusDefesa = ?, bonusEsquiva = ? WHERE idItem = ?;";
	            psSubtipo = conn.prepareStatement(sqlDefesa);
	            psSubtipo.setInt(1, defesa.getBonusDefesa());
	            psSubtipo.setInt(2, defesa.getBonusEsquiva());
	            psSubtipo.setInt(3, item.getId());
	            
	        } else if (item instanceof ItemConsumivel) {
	            ItemConsumivel consumivel = (ItemConsumivel) item;
	            String sqlConsumivel = "UPDATE Consumivel SET cura = ?, mana = ? WHERE idItem = ?;";
	            psSubtipo = conn.prepareStatement(sqlConsumivel);
	            psSubtipo.setInt(1, consumivel.getCura());
	            psSubtipo.setInt(2, consumivel.getMana());
	            psSubtipo.setInt(3, item.getId());
	            
	        } else {
	            conn.rollback(); 
	            throw new IllegalArgumentException("Tipo de Item inválido para alteração: " + item.getClass().getSimpleName());
	        }

	        psSubtipo.executeUpdate();


	        conn.commit();
	        return true;

	    } catch (Exception e) {
	        
	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception rb) {
	            rb.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {

	        try {
	            if (psItem != null) psItem.close();
	            if (psSubtipo != null) psSubtipo.close();
	            if (conn != null) conn.setAutoCommit(true);
	            if (conn != null) conn.close();
	        } catch (Exception f) {
	            f.printStackTrace();
	        }
	    }
	}
	
	
	public boolean excluir(Item item) {
	    Connection conn = null;
	    PreparedStatement psItem = null;
	    PreparedStatement psSubtipo = null;
	    
	    try {
	        conn = Conexao.conectar();
	        conn.setAutoCommit(false); // Inicia a Transação

	        String sqlSubtipo = "";
	        
	        if (item instanceof ItemArma) {
	            sqlSubtipo = "DELETE FROM Arma WHERE idItem = ?;";
	        } else if (item instanceof ItemDefesa) {
	            sqlSubtipo = "DELETE FROM Defesa WHERE idItem = ?;";
	        } else if (item instanceof ItemConsumivel) {
	            sqlSubtipo = "DELETE FROM Consumivel WHERE idItem = ?;";
	        } else {
	            throw new IllegalArgumentException("Tipo de Item inválido para exclusão: " + item.getClass().getSimpleName());
	        }

	        psSubtipo = conn.prepareStatement(sqlSubtipo);
	        psSubtipo.setInt(1, item.getId());
	        psSubtipo.executeUpdate();
	        psSubtipo.close();

	        String sqlItem = "DELETE FROM Item WHERE id = ?;";
	        psItem = conn.prepareStatement(sqlItem);
	        psItem.setInt(1, item.getId());
	        psItem.executeUpdate();

	        conn.commit();
	        return true;

	    } catch (Exception e) {

	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception rb) {
	            rb.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {

	        try {
	            if (psItem != null) psItem.close();
	            if (psSubtipo != null) psSubtipo.close();
	            if (conn != null) conn.setAutoCommit(true);
	            if (conn != null) conn.close();
	        } catch (Exception f) {
	            f.printStackTrace();
	        }
	    }
	}

	public Item procurarPorCodigo(int id) {
		Connection conn = null;
	    PreparedStatement psItem = null;
	    PreparedStatement psSubtipo = null;
	    ResultSet rsItem = null;
	    ResultSet rsSubtipo = null;
	    Item item = null;
	    String tipo = null;
	    
		try {
			conn = Conexao.conectar();
			String sqlItem = "SELECT nome FROM Item WHERE id = ?;";
	        psItem = conn.prepareStatement(sqlItem);
	        psItem.setInt(1, id);
	        rsItem = psItem.executeQuery();
	        
	        if (!rsItem.next()) {
	            return null;
	        }

	        String nomeItem = rsItem.getString("nome");


	        String sqlArma = "SELECT bonusDano, bonusMagico, bonusCritico FROM Arma WHERE idItem = ?;";
	        psSubtipo = conn.prepareStatement(sqlArma);
	        psSubtipo.setInt(1, id);
	        rsSubtipo = psSubtipo.executeQuery();
	        if (rsSubtipo.next()) {
	            tipo = "Arma";

	            ItemArma arma = new ItemArma(nomeItem, rsSubtipo.getInt("bonusDano"), rsSubtipo.getInt("bonusMagico"), rsSubtipo.getInt("bonusCritico"));
	            arma.setId(id);
	            item = arma;
	        }
	        rsSubtipo.close();
	        
	        if (item == null) {

	            String sqlDefesa = "SELECT bonusDefesa, bonusEsquiva FROM Defesa WHERE idItem = ?;";
	            psSubtipo = conn.prepareStatement(sqlDefesa);
	            psSubtipo.setInt(1, id);
	            rsSubtipo = psSubtipo.executeQuery();
	            if (rsSubtipo.next()) {
	                tipo = "Defesa";

	                ItemDefesa defesa = new ItemDefesa(nomeItem, rsSubtipo.getInt("bonusDefesa"), rsSubtipo.getInt("bonusEsquiva"));
	                defesa.setId(id);
	                item = defesa;
	            }
	            rsSubtipo.close();
	        }

	        if (item == null) {

	            String sqlConsumivel = "SELECT cura, mana FROM Consumivel WHERE idItem = ?;";
	            psSubtipo = conn.prepareStatement(sqlConsumivel);
	            psSubtipo.setInt(1, id);
	            rsSubtipo = psSubtipo.executeQuery();
	            if (rsSubtipo.next()) {
	                tipo = "Consumivel";

	                ItemConsumivel consumivel = new ItemConsumivel(nomeItem, rsSubtipo.getInt("cura"), rsSubtipo.getInt("mana"));
	                consumivel.setId(id);
	                item = consumivel;
	            }
	            rsSubtipo.close();
	        }


	        return item;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            if (rsItem != null) rsItem.close();
	            if (rsSubtipo != null) rsSubtipo.close();
	            if (psItem != null) psItem.close();
	            if (psSubtipo != null) psSubtipo.close();
	            if (conn != null) conn.close();
	        } catch (Exception f) {
	            f.printStackTrace();
	        }
	    }
	}
	
	

	public Item procurarPorNome(Item item) {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        conn = Conexao.conectar();
	        String sql = "SELECT id FROM Item WHERE nome = ?;";
	        ps = conn.prepareStatement(sql);
	        ps.setString(1, item.getNome());
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            int idEncontrado = rs.getInt("id");

	            return procurarPorCodigo(idEncontrado); 
	        } else {
	            return null;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {

	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (Exception f) {
	            f.printStackTrace();
	        }
	    }
	}

	public boolean existe(Item item) {
		return procurarPorNome(item) != null;
	}
	
	public Item procurarPorNome(String nome) {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        conn = Conexao.conectar();
	        String sql = "SELECT id FROM Item WHERE nome = ?;";
	        ps = conn.prepareStatement(sql);
	        ps.setString(1, nome);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            int id = rs.getInt("id");
	            return procurarPorCodigo(id);
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
}
