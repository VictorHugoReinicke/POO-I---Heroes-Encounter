package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classes.Conexao.Conexao;
import com.classes.DTO.Inimigo;
import com.classes.DTO.InimigoStatus;

public class InimigoStatusDAO {

    final String NOMEDATABELA = "InimigoStatus";
    
    private InimigoDAO inimigoDAO;

    public InimigoStatusDAO() {
        this.inimigoDAO = new InimigoDAO();
    }

    public boolean inserir(InimigoStatus inimigoStatus) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "INSERT INTO " + NOMEDATABELA
                    + " (idInimigo, idStatus, turno_restante) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, inimigoStatus.getIdInimigo());
            ps.setInt(2, inimigoStatus.getIdStatus());
            ps.setInt(3, inimigoStatus.getTurnoRestante()); 

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                inimigoStatus.setId(rs.getInt(1));
            }

            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(InimigoStatus inimigoStatus) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "UPDATE " + NOMEDATABELA
                    + " SET turno_restante = ? WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, inimigoStatus.getTurnoRestante());
            ps.setInt(2, inimigoStatus.getId()); 

            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(InimigoStatus inimigoStatus) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "DELETE FROM " + NOMEDATABELA + " WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inimigoStatus.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<InimigoStatus> procurarPorInimigo(int idInimigo) {
        try {
            Connection conn = Conexao.conectar();
            String sql = "SELECT * FROM " + NOMEDATABELA + " WHERE idInimigo = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idInimigo);
            ResultSet rs = ps.executeQuery();

            List<InimigoStatus> lista = montarLista(rs);
            
            ps.close();
            rs.close();
            conn.close();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private InimigoStatus montarObjeto(ResultSet rs) throws Exception {
        InimigoStatus is = new InimigoStatus();
        
        is.setId(rs.getInt("id"));
        is.setIdInimigo(rs.getInt("idInimigo"));
        is.setIdStatus(rs.getInt("idStatus"));
        is.setTurnoRestante(rs.getInt("turno_restante"));
        

        Inimigo inimigo = inimigoDAO.procurarPorCodigo(is.getIdInimigo());
        is.setInimigo(inimigo);
        

        return is;
    }

    private List<InimigoStatus> montarLista(ResultSet rs) {
        List<InimigoStatus> listObj = new ArrayList<>();
        try {
            while (rs.next()) {
                InimigoStatus obj = montarObjeto(rs);
                listObj.add(obj);
            }
            return listObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}