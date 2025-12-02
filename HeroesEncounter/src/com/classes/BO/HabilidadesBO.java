package com.classes.BO;

import com.classes.DAO.HabilidadesDAO;
import com.classes.DTO.Habilidade;
import java.util.List;

public class HabilidadesBO {

	private HabilidadesDAO habilidadesDAO;

	public HabilidadesBO() {
		this.habilidadesDAO = new HabilidadesDAO();
	}
	

	public boolean inserir(Habilidade habilidade) {
        if (habilidadesDAO.existe(habilidade)) {
            System.out.println("Habilidade '" + habilidade.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        String tipo = habilidade.getTipo().toUpperCase();
        
        if (habilidade.getFatorDano() <= 0 && 
            (tipo.equals("FÍSICO") || tipo.equals("MÁGICO")) ) {
             
             System.out.println("Fator de dano deve ser maior que zero para habilidades de ataque. Inserção cancelada.");
             return false;
        }

		return habilidadesDAO.inserir(habilidade);
	}
	
	public boolean alterar(Habilidade habilidade) {
		return habilidadesDAO.alterar(habilidade);
	}
	
	public boolean excluir(Habilidade habilidade) {
		return habilidadesDAO.excluir(habilidade);
	}
	

	public Habilidade procurarPorCodigo(int id) {
		return habilidadesDAO.procurarPorCodigo(id);
	}
	
	public Habilidade procurarPorNome(Habilidade habilidade) {
		return habilidadesDAO.procurarPorNome(habilidade);
	}
	
	public boolean existe(Habilidade habilidade) {
		return habilidadesDAO.existe(habilidade);
	}
	
	public List<Habilidade> pesquisarTodos() {
		return habilidadesDAO.pesquisarTodos();
	}
}