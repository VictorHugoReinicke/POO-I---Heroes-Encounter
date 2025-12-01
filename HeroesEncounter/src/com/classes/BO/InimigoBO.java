package com.classes.BO;

import com.classes.DAO.InimigoDAO;
import com.classes.DTO.Inimigo;

import java.util.List;

public class InimigoBO {

	private InimigoDAO inimigoDAO;

	public InimigoBO() {
		this.inimigoDAO = new InimigoDAO();
	}
	
    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO (CRUD) E VALIDAÇÃO ---
    // ------------------------------------------------------------------

	public boolean inserir(Inimigo inimigo) {
        
        // Regra de Negócio 1: Garante que o Inimigo não seja duplicado (pelo nome)
        if (inimigoDAO.existe(inimigo)) {
            System.out.println("⚠️ Inimigo '" + inimigo.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        
        // Regra de Negócio 2: HP Máximo deve ser positivo (usando o método getHpMax)
        if (inimigo.getHpMax() <= 0) { 
            System.out.println("⚠️ HP Máximo deve ser maior que zero. Inserção cancelada.");
            return false;
        }
        
        
        // Regra de Negócio 3: Garante que o HP atual não exceda o máximo
        if (inimigo.getHp() > inimigo.getHpMax()) {
            inimigo.setHp(inimigo.getHpMax());
        }

		return inimigoDAO.inserir(inimigo);
	}
	
	public boolean alterar(Inimigo inimigo) {
		// Adicionar regras de negócio de alteração aqui (Ex: verificar se o ID existe)
		return inimigoDAO.alterar(inimigo);
	}
	
	public boolean excluir(Inimigo inimigo) {
		// Adicionar regras de negócio de exclusão aqui (Ex: checar dependências em InimigoStatus)
		return inimigoDAO.excluir(inimigo);
	}
	
    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA E UTILIDADE ---
    // ------------------------------------------------------------------

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
		return inimigoDAO.pesquisarTodos(); // Agora o DAO tem o método pesquisarTodos
	}
}