package com.classes.BO;

import com.classes.DAO.ClasseHabilidadeDAO;
import com.classes.DTO.ClasseHabilidade;
import java.util.List;

public class ClasseHabilidadeBO {

	private ClasseHabilidadeDAO classeHabilidadeDAO;

	public ClasseHabilidadeBO() {
		this.classeHabilidadeDAO = new ClasseHabilidadeDAO();
	}
	
	public boolean atribuirHabilidade(int idClasse, int idHabilidade) {
        
        ClasseHabilidade ch = new ClasseHabilidade(idClasse, idHabilidade);
        
        if (classeHabilidadeDAO.existe(ch)) {
            System.out.printf("Habilidade ID %d já está atribuída à Classe ID %d. Atribuição cancelada.\n",
                idHabilidade, idClasse);
            return false;
        }

		return classeHabilidadeDAO.inserir(ch);
	}
	
	public boolean removerAtribuicao(int idClasse, int idHabilidade) {
        
        ClasseHabilidade ch = new ClasseHabilidade(idClasse, idHabilidade);
        
        if (!classeHabilidadeDAO.existe(ch)) {
             System.out.printf("Atribuição Habilidade ID %d para Classe ID %d não existe. Remoção cancelada.\n",
                idHabilidade, idClasse);
            return false;
        }
        
		return classeHabilidadeDAO.excluir(ch);
	}
	
	public List<ClasseHabilidade> listarHabilidadesPorClasse(int idClasse) {
		return classeHabilidadeDAO.listarHabilidadesPorClasse(idClasse);
	}
	
	public boolean existe(ClasseHabilidade ch) {
		return classeHabilidadeDAO.existe(ch);
	}
}