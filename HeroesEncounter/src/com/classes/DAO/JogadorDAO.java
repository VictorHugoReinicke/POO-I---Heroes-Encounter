package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classes.DTO.Jogador;
import com.classes.DTO.JogadorFactory;
import com.classes.Conexao.Conexao;

public class JogadorDAO {

	final String NOMEDATABELA = "Jogador";

	public boolean inserir(Jogador jogador) {
	    try {
	        Connection conn = Conexao.conectar();
	        String sql = "INSERT INTO " + NOMEDATABELA + " (nome, gold, vida_atual, idClasse, mana_atual) VALUES (?, ?, ?, ?, ?)";
	        // Use RETURN_GENERATED_KEYS para obter o ID gerado
	        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

	        ps.setString(1, jogador.getNome());
	        ps.setInt(2, jogador.getOuro());
	        ps.setInt(3, jogador.getHp());
	        ps.setInt(4, jogador.getIdClasse());
	        ps.setInt(5, jogador.getMana());

	        int linhasAfetadas = ps.executeUpdate();
	        
	        if (linhasAfetadas > 0) {
	            // Obter o ID gerado
	            ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int idGerado = generatedKeys.getInt(1);
	                jogador.setId(idGerado); // Define o ID no objeto jogador
	            }
	            generatedKeys.close();
	        }
	        
	        ps.close();
	        conn.close();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean alterar(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA
					+ " SET nome = ?, gold = ?, idClasse = ?, vida_atual = ?, mana_atual = ? " + "WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, jogador.getNome());
			ps.setInt(2, jogador.getOuro());
			ps.setInt(3, jogador.getIdClasse());
			ps.setInt(4, jogador.getHp());
			ps.setInt(5, jogador.getMana());

			ps.setInt(6, jogador.getId());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(Jogador jogador) {
        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // ✅ INICIA TRANSACTION
            
            int idJogador = jogador.getId();
            System.out.println("🗑️ Iniciando deleção em cascata do jogador ID: " + idJogador);
            
            // ✅ 1. PRIMEIRO DELETA OS ITENS DO JOGADOR
            System.out.println("📦 Deletando itens do jogador...");
            String sqlItens = "DELETE FROM jogadoritem WHERE idJogador = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlItens)) {
                ps.setInt(1, idJogador);
                int itensDeletados = ps.executeUpdate();
                System.out.println("✅ Itens deletados: " + itensDeletados);
            }
            
            // ✅ 2. DELETA OS STATUS DO JOGADOR
            System.out.println("⚡ Deletando status do jogador...");
            String sqlStatus = "DELETE FROM jogadorstatus WHERE idJogador = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStatus)) {
                ps.setInt(1, idJogador);
                int statusDeletados = ps.executeUpdate();
                System.out.println("✅ Status deletados: " + statusDeletados);
            }
                     
                  
            // ✅ 5. FINALMENTE DELETA O JOGADOR
            System.out.println("👤 Deletando jogador...");
            String sqlJogador = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlJogador)) {
                ps.setInt(1, idJogador);
                int jogadorDeletado = ps.executeUpdate();
                
                if (jogadorDeletado > 0) {
                    conn.commit(); // ✅ CONFIRMA TODAS AS DELEÇÕES
                    System.out.println("✅ Jogador deletado com sucesso!");
                    return true;
                } else {
                    conn.rollback(); // ✅ CANCELA TUDO SE FALHAR
                    System.out.println("❌ Nenhum jogador foi deletado");
                    return false;
                }
            }
            
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback(); // ✅ CANCELA EM CASO DE ERRO
                }
            } catch (SQLException ex) {
                System.err.println("❌ Erro no rollback: " + ex.getMessage());
            }
            System.err.println("❌ Erro ao deletar jogador: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // ✅ RESTAURA AUTO-COMMIT
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

	public Jogador procurarPorCodigo(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA
					+ " WHERE id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, jogador.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Jogador obj = montarObjeto(rs);

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

	public Jogador procurarPorNome(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA
					+ " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, jogador.getNome());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Jogador obj = montarObjeto(rs);
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

	public boolean existe(Jogador jogador) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE nome = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, jogador.getNome());
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

	public List<Jogador> pesquisarTodos() {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT id, nome, gold, idClasse, vida_atual, mana_atual FROM " + NOMEDATABELA + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Jogador> listObj = montarLista(rs);
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Jogador montarObjeto(ResultSet rs) throws Exception {

		int idClasse = rs.getInt("idClasse");

		Jogador jogador = JogadorFactory.criarJogador(idClasse);

		jogador.setId(rs.getInt("id"));
		jogador.setNome(rs.getString("nome"));
		jogador.setHp(rs.getInt("vida_atual"));
		jogador.setMana(rs.getInt("mana_atual"));
		jogador.setOuro(rs.getInt("gold"));

		jogador.setIdClasse(idClasse);

		return jogador;
	}

	public List<Jogador> montarLista(ResultSet rs) {
		List<Jogador> listObj = new ArrayList<Jogador>();
		try {
			while (rs.next()) {
				Jogador obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
