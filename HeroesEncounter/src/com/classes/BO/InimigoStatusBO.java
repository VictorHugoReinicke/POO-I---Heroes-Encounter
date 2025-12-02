package com.classes.BO;

import java.util.List;

import com.classes.DAO.InimigoStatusDAO;
import com.classes.DTO.InimigoStatus;

public class InimigoStatusBO {

    private InimigoStatusDAO inimigoStatusDAO;

    public InimigoStatusBO() {
        this.inimigoStatusDAO = new InimigoStatusDAO();
    }


    public boolean aplicarStatus(InimigoStatus novoStatus) {
        
        if (novoStatus.getTurnoRestante() <= 0) {
            System.out.println("Status não aplicado: Duração deve ser positiva.");
            return false;
        }

        List<InimigoStatus> statusAtivos = inimigoStatusDAO.procurarPorInimigo(novoStatus.getIdInimigo());
        
        for (InimigoStatus is : statusAtivos) {
            if (is.getIdStatus() == novoStatus.getIdStatus()) {
                
                is.setTurnoRestante(novoStatus.getTurnoRestante());
                
                System.out.println("Status ID " + is.getIdStatus() + " reforçado no Inimigo ID " + is.getIdInimigo());
                return inimigoStatusDAO.alterar(is);
            }
        }
        
        System.out.println("Novo Status ID " + novoStatus.getIdStatus() + " aplicado no Inimigo ID " + novoStatus.getIdInimigo());
        return inimigoStatusDAO.inserir(novoStatus);
    }
    
    public boolean removerStatus(InimigoStatus inimigoStatus) {
        if (inimigoStatus.getId() <= 0) {
            System.out.println("Não é possível remover: ID do InimigoStatus inválido.");
            return false;
        }
        System.out.println("Status ID " + inimigoStatus.getIdStatus() + " removido do Inimigo ID " + inimigoStatus.getIdInimigo());
        return inimigoStatusDAO.excluir(inimigoStatus);
    }

    public void gerenciarTurno(int idInimigo) {
        List<InimigoStatus> statusAtivos = inimigoStatusDAO.procurarPorInimigo(idInimigo);
        
        for (InimigoStatus is : statusAtivos) {
            
            is.decrementarTurno();
            
            if (is.getTurnoRestante() <= 0) {
                removerStatus(is);
            } else {
                inimigoStatusDAO.alterar(is);
                System.out.println("Status ID " + is.getIdStatus() + " do Inimigo ID " + is.getIdInimigo() +
                                   " decrementado. Restam: " + is.getTurnoRestante() + " turnos.");
            }
        }
    }

    public List<InimigoStatus> pesquisarStatusPorInimigo(int idInimigo) {
        return inimigoStatusDAO.procurarPorInimigo(idInimigo);
    }

}