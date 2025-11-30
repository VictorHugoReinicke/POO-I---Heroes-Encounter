package com.classes.BO;

import com.classes.DAO.StatusDAO;
import com.classes.DTO.Status;
import java.util.List;

public class StatusBO {

	private StatusDAO statusDAO;

	public StatusBO() {
		this.statusDAO = new StatusDAO();
	}
	
    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) ---
    // ------------------------------------------------------------------

	public boolean inserir(Status status) {
        // Regra de Negócio 1: Garante que o Status não seja duplicado
        if (statusDAO.existe(status)) {
            System.out.println("⚠️ Status '" + status.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        
        // Regra de Negócio 2: Duração mínima deve ser 1 turno (a menos que seja um efeito permanente, mas vamos assumir que não)
        if (status.getDuracaoTurnos() < 1) {
            System.out.println("⚠️ Duração em turnos deve ser no mínimo 1. Inserção cancelada.");
            return false;
        }

		return statusDAO.inserir(status);
	}
	
	public boolean alterar(Status status) {
		return statusDAO.alterar(status);
	}
	
	public boolean excluir(Status status) {
		// Regra de Negócio: Antes de excluir, checaria se alguma Habilidade, JogadorStatus ou InimigoStatus
        // depende deste Status (FKs). Se depender, a exclusão seria impedida.
		return statusDAO.excluir(status);
	}
	
    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA ---
    // ------------------------------------------------------------------

	public Status procurarPorCodigo(int id) {
		return statusDAO.procurarPorCodigo(id);
	}
	
	public Status procurarPorNome(Status status) {
		return statusDAO.procurarPorNome(status);
	}
	
	public boolean existe(Status status) {
		return statusDAO.existe(status);
	}
	
	public List<Status> pesquisarTodos() {
		return statusDAO.pesquisarTodos();
	}
}