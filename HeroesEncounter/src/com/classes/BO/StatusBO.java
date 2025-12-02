package com.classes.BO;

import com.classes.DAO.StatusDAO;
import com.classes.DTO.Status;
import java.util.List;

public class StatusBO {

	private StatusDAO statusDAO;

	public StatusBO() {
		this.statusDAO = new StatusDAO();
	}

	public boolean inserir(Status status) {
        if (statusDAO.existe(status)) {
            System.out.println("Status '" + status.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        
        if (status.getDuracaoTurnos() < 1) {
            System.out.println("Duração em turnos deve ser no mínimo 1. Inserção cancelada.");
            return false;
        }

		return statusDAO.inserir(status);
	}
	
	public boolean alterar(Status status) {
		return statusDAO.alterar(status);
	}
	
	public boolean excluir(Status status) {
		return statusDAO.excluir(status);
	}

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