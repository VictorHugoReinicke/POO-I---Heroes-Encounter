package com.classes.BO;

import com.classes.DAO.ClasseHabilidadeDAO;
import com.classes.DTO.ClasseHabilidade;
import java.util.List;

public class ClasseHabilidadeBO {

	private ClasseHabilidadeDAO classeHabilidadeDAO;
	// Outros BOs que podem ser necessários:
    // private ClasseBO classeBO;
    // private HabilidadesBO habilidadesBO;

	public ClasseHabilidadeBO() {
		this.classeHabilidadeDAO = new ClasseHabilidadeDAO();
        // this.classeBO = new ClasseBO();
        // this.habilidadesBO = new HabilidadesBO();
	}
	
    // ------------------------------------------------------------------
    // --- 1. LÓGICA DE NEGÓCIO PRINCIPAL: ATRIBUIÇÃO ---
    // ------------------------------------------------------------------

    /**
     * Atribui uma Habilidade a uma Classe, inserindo o registro M-N se ele não existir.
     */
	public boolean atribuirHabilidade(int idClasse, int idHabilidade) {
        
        ClasseHabilidade ch = new ClasseHabilidade(idClasse, idHabilidade);
        
        // Regra de Negócio: Evita duplicidade
        if (classeHabilidadeDAO.existe(ch)) {
            System.out.printf("⚠️ Habilidade ID %d já está atribuída à Classe ID %d. Atribuição cancelada.\n",
                idHabilidade, idClasse);
            return false;
        }

        // Regra de Negócio: (Idealmente checaria se a Classe e a Habilidade existem)
        // if (classeBO.procurarPorCodigo(idClasse) == null) return false;
        // if (habilidadesBO.procurarPorCodigo(idHabilidade) == null) return false;

		return classeHabilidadeDAO.inserir(ch);
	}
	
    /**
     * Remove a atribuição de uma Habilidade de uma Classe.
     */
	public boolean removerAtribuicao(int idClasse, int idHabilidade) {
        
        ClasseHabilidade ch = new ClasseHabilidade(idClasse, idHabilidade);
        
        // Regra de Negócio: Deve checar se o registro existe antes de tentar excluir
        if (!classeHabilidadeDAO.existe(ch)) {
             System.out.printf("⚠️ Atribuição Habilidade ID %d para Classe ID %d não existe. Remoção cancelada.\n",
                idHabilidade, idClasse);
            return false;
        }
        
		return classeHabilidadeDAO.excluir(ch);
	}
	
    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA ---
    // ------------------------------------------------------------------

	public List<ClasseHabilidade> listarHabilidadesPorClasse(int idClasse) {
		return classeHabilidadeDAO.listarHabilidadesPorClasse(idClasse);
	}
	
	public boolean existe(ClasseHabilidade ch) {
		return classeHabilidadeDAO.existe(ch);
	}
}