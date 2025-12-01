package com.classes.BO;

import com.classes.DAO.HabilidadesDAO;
import com.classes.DTO.Habilidade;
import java.util.List;

public class HabilidadesBO {

	private HabilidadesDAO habilidadesDAO;
	// private StatusBO statusBO; // Seria usado para buscar o objeto Status completo

	public HabilidadesBO() {
		this.habilidadesDAO = new HabilidadesDAO();
		// this.statusBO = new StatusBO(); 
	}
	
    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

	public boolean inserir(Habilidade habilidade) {
        // Regra de Negócio: Garante que a Habilidade não seja duplicada
        if (habilidadesDAO.existe(habilidade)) {
            System.out.println("⚠️ Habilidade '" + habilidade.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        String tipo = habilidade.getTipo().toUpperCase();
        
        if (habilidade.getFatorDano() <= 0 && 
            (tipo.equals("FÍSICO") || tipo.equals("MÁGICO")) ) {
             
             System.out.println("⚠️ Fator de dano deve ser maior que zero para habilidades de ataque. Inserção cancelada.");
             return false;
        }

		return habilidadesDAO.inserir(habilidade);
	}
	
	public boolean alterar(Habilidade habilidade) {
		return habilidadesDAO.alterar(habilidade);
	}
	
	public boolean excluir(Habilidade habilidade) {
		// Regra de Negócio: Antes de excluir, você checaria se alguma ClasseHabilidade
        // ou InimigoHabilidade depende desta habilidade. Se sim, a exclusão seria impedida.
		return habilidadesDAO.excluir(habilidade);
	}
	
    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA ---
    // ------------------------------------------------------------------

	public Habilidade procurarPorCodigo(int id) {
        // Se a busca for bem-sucedida, você usaria o StatusBO para preencher o objeto DTO Status completo
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