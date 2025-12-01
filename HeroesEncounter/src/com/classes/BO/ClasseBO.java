package com.classes.BO;

import com.classes.DTO.Classe;
import com.classes.DAO.ClasseDAO;

public class ClasseBO {

    private ClasseDAO getDAO() {
        return new ClasseDAO();
    }

    public boolean inserir(Classe classe) {

        if (!existe(classe)) {
            ClasseDAO dao = getDAO();
            if (dao.inserir(classe)) {

                Classe objNoDB = dao.procurarPorNome(classe);
                if (objNoDB != null) {
                    classe.setId(objNoDB.getId());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean alterar(Classe classe) {
        return getDAO().alterar(classe);
    }

    public boolean excluir(Classe classe) {
        return getDAO().excluir(classe);
    }

    public Classe procurarPorCodigo(int id) {
        return getDAO().procurarPorCodigo(id);
    }

    public Classe procurarPorNome(Classe classe) {
        return getDAO().procurarPorNome(classe);
    }

    public boolean existe(Classe classe) {
        return getDAO().existe(classe);
    }
}