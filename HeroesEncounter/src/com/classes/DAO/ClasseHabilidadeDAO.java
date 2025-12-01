package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.ClasseHabilidade;
import com.classes.DTO.Habilidade;

public class ClasseHabilidadeDAO {

	final String NOMEDATABELA = "ClasseHabilidade";
    
    // DAO necessário para carregar o DTO Habilidade completo
	private HabilidadesDAO habilidadesDAO; 
    
    public ClasseHabilidadeDAO() {
        // Assume que HabilidadesDAO já existe e está funcional
        this.habilidadesDAO = new HabilidadesDAO(); 
    }

    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

    /**
     * Insere uma nova ligação entre Classe e Habilidade.
     */
	public boolean inserir(ClasseHabilidade ch) {
		try {
			Connection conn = Conexao.conectar();
            // idClasse e idHabilidade formam a chave composta
			String sql = "INSERT INTO " + NOMEDATABELA
					+ " (idClasse, idHabilidade) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, ch.getIdClasse());
			ps.setInt(2, ch.getIdHabilidade());

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
     * Remove uma Habilidade específica de uma Classe.
     */
	public boolean excluir(ClasseHabilidade ch) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA 
                    + " WHERE idClasse = ? AND idHabilidade = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ch.getIdClasse());
			ps.setInt(2, ch.getIdHabilidade());
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
     * Procura um registro específico (chave composta).
     */
	public ClasseHabilidade procurarRegistro(int idClasse, int idHabilidade) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idClasse, idHabilidade FROM " + NOMEDATABELA 
					+ " WHERE idClasse = ? AND idHabilidade = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idClasse);
            ps.setInt(2, idHabilidade);
			ResultSet rs = ps.executeQuery();
            
			if (rs.next()) {
				ClasseHabilidade obj = montarObjeto(rs);

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
     * Lista todas as Habilidades pertencentes a uma Classe.
     * Retorna uma lista de objetos ClasseHabilidade, cada um contendo o DTO Habilidade completo.
     */
	public List<ClasseHabilidade> listarHabilidadesPorClasse(int idClasse) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idClasse, idHabilidade FROM " + NOMEDATABELA 
					+ " WHERE idClasse = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idClasse);
			ResultSet rs = ps.executeQuery();
			
			List<ClasseHabilidade> listObj = montarLista(rs);
			
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

	private ClasseHabilidade montarObjeto(ResultSet rs) throws Exception {
		
		int idHabilidade = rs.getInt("idHabilidade");
        
		ClasseHabilidade ch = new ClasseHabilidade();
        
        // Atributos de ClasseHabilidade
        ch.setIdClasse(rs.getInt("idClasse"));
        ch.setIdHabilidade(idHabilidade);

        // ❗️ CARREGAMENTO DA HABILIDADE COMPLETA
        // Isso assume que o HabilidadesDAO.procurarPorCodigo(int id) existe e funciona.
        Habilidade habilidadeCompleta = habilidadesDAO.procurarPorCodigo(idHabilidade);
        ch.setHabilidade(habilidadeCompleta);
        
        // NOTE: O DTO Classe não é carregado aqui para evitar loop infinito de DAOs.
        
		return ch;
	}

	private List<ClasseHabilidade> montarLista(ResultSet rs) {
		List<ClasseHabilidade> listObj = new ArrayList<ClasseHabilidade>();
		try {
			while (rs.next()) {
				ClasseHabilidade obj = montarObjeto(rs);
				listObj.add(obj);
			}
			return listObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    public boolean existe(ClasseHabilidade ch) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE idClasse = ? AND idHabilidade = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ch.getIdClasse());
            ps.setInt(2, ch.getIdHabilidade());
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