package com.classes.BO;

import com.classes.DAO.InimigoDAO;
import com.classes.DTO.Inimigo;

import java.util.List;

public class InimigoBO {

	private InimigoDAO inimigoDAO;

	public InimigoBO() {
		this.inimigoDAO = new InimigoDAO();
	}
	
	public boolean inserir(Inimigo inimigo) {
        
        if (inimigoDAO.existe(inimigo)) {
            System.out.println("Inimigo '" + inimigo.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        
        if (inimigo.getHpMax() <= 0) {
            System.out.println("HP Máximo deve ser maior que zero. Inserção cancelada.");
            return false;
        }
        
        
        if (inimigo.getHp() > inimigo.getHpMax()) {
            inimigo.setHp(inimigo.getHpMax());
        }

		return inimigoDAO.inserir(inimigo);
	}
	
	public boolean alterar(Inimigo inimigo) {
		return inimigoDAO.alterar(inimigo);
	}
	
	public boolean excluir(Inimigo inimigo) {
		return inimigoDAO.excluir(inimigo);
	}
	

	public Inimigo procurarPorCodigo(int id) { // <-- Assinatura corrigida
		return inimigoDAO.procurarPorCodigo(id);
	}

	public Inimigo procurarPorNome(Inimigo inimigo) { // <-- Assinatura corrigida
		return inimigoDAO.procurarPorNome(inimigo);
	}
	
	public boolean existe(Inimigo inimigo) {
		return inimigoDAO.existe(inimigo);
	}
	
	public List<Inimigo> pesquisarTodos() {
		return inimigoDAO.pesquisarTodos();
	}
}