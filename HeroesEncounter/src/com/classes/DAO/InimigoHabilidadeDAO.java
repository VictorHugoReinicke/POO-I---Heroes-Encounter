package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.InimigoHabilidade;
import com.classes.DTO.Inimigo;     // Necessário para carregar o objeto Inimigo

public class InimigoHabilidadeDAO {

    final String NOMEDATABELA = "InimigoHabilidade";
    
    // Dependências para carregar objetos relacionados
    private InimigoDAO inimigoDAO;

    public InimigoHabilidadeDAO() {
        this.inimigoDAO = new InimigoDAO();
        // this.habilidadeDAO = new HabilidadeDAO(); // Descomentar quando HabilidadeDAO existir
    }

    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

    public boolean inserir(InimigoHabilidade inimigoHabilidade) {
        try {
            Connection conn = Conexao.conectar();
            // A tabela de ligação tem chave composta, então não usamos Statement.RETURN_GENERATED_KEYS
            String sql = "INSERT INTO " + NOMEDATABELA
                    + " (idInimigo, idHabilidade, chance_uso) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, inimigoHabilidade.getIdInimigo());
            ps.setInt(2, inimigoHabilidade.getIdHabilidade());
            ps.setInt(3, inimigoHabilidade.getChance_uso()); 

            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            // Este erro geralmente indica que a ligação já existe (chave duplicada)
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(InimigoHabilidade inimigoHabilidade) {
        try {
            Connection conn = Conexao.conectar();
            // Só permitimos alterar a chance de uso, pois os IDs definem a ligação
            String sql = "UPDATE " + NOMEDATABELA
                    + " SET chance_uso = ? WHERE idInimigo = ? AND idHabilidade = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, inimigoHabilidade.getChance_uso());
            ps.setInt(2, inimigoHabilidade.getIdInimigo()); 
            ps.setInt(3, inimigoHabilidade.getIdHabilidade()); 

            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(InimigoHabilidade inimigoHabilidade) {
        try {
            Connection conn = Conexao.conectar();
            // Exclui a ligação pela chave composta
            String sql = "DELETE FROM " + NOMEDATABELA + " WHERE idInimigo = ? AND idHabilidade = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inimigoHabilidade.getIdInimigo());
            ps.setInt(2, inimigoHabilidade.getIdHabilidade());
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
     * Retorna todas as habilidades (e suas chances) de um inimigo específico.
     */
    public List<InimigoHabilidade> procurarPorInimigo(int idInimigo) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE idInimigo = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idInimigo);
            ResultSet rs = ps.executeQuery();

            List<InimigoHabilidade> lista = montarLista(rs);
            
            ps.close();
            rs.close();
            conn.close();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verifica se uma ligação específica já existe.
     */
    public boolean existe(InimigoHabilidade inimigoHabilidade) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT idInimigo FROM " + NOMEDATABELA + " WHERE idInimigo = ? AND idHabilidade = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inimigoHabilidade.getIdInimigo());
            ps.setInt(2, inimigoHabilidade.getIdHabilidade());
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


    // ------------------------------------------------------------------
    // --- 3. MÉTODOS AUXILIARES ---
    // ------------------------------------------------------------------
    
    private InimigoHabilidade montarObjeto(ResultSet rs) throws Exception {
        InimigoHabilidade ih = new InimigoHabilidade();
        
        // 1. Atributos da tabela de ligação
        ih.setIdInimigo(rs.getInt("idInimigo"));
        ih.setIdHabilidade(rs.getInt("idHabilidade"));
        ih.setChance_uso(rs.getInt("chance_uso"));
        
        // 2. Carrega os objetos relacionados (o "JOIN" feito no código Java)
        
        // Carrega o Inimigo (já usa a Factory)
        Inimigo inimigo = inimigoDAO.procurarPorCodigo(ih.getIdInimigo());
        ih.setInimigo(inimigo);
        
        // Carrega a Habilidade (Descomentar quando HabilidadeDAO existir)
        // Habilidade habilidade = habilidadeDAO.procurarPorCodigo(ih.getIdHabilidade());
        // ih.setHabilidade(habilidade);
        
        return ih;
    }

    private List<InimigoHabilidade> montarLista(ResultSet rs) {
        List<InimigoHabilidade> listObj = new ArrayList<>();
        try {
            while (rs.next()) {
                InimigoHabilidade obj = montarObjeto(rs);
                listObj.add(obj);
            }
            return listObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}