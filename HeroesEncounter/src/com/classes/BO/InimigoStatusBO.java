package com.classes.BO;

import java.util.List;

import com.classes.DAO.InimigoStatusDAO;
import com.classes.DTO.InimigoStatus;

public class InimigoStatusBO {

    private InimigoStatusDAO inimigoStatusDAO;
    // O StatusBO pode ser necessário para checar a compatibilidade de status
    // private StatusBO statusBO; 

    public InimigoStatusBO() {
        this.inimigoStatusDAO = new InimigoStatusDAO();
        // this.statusBO = new StatusBO(); 
    }

    // ------------------------------------------------------------------
    // --- 1. LÓGICA DE APLICAÇÃO E REMOÇÃO (COMBATE) ---
    // ------------------------------------------------------------------

    /**
     * Aplica um novo status a um inimigo, verificando se ele já existe.
     * @param novoStatus O DTO InimigoStatus a ser inserido.
     * @return true se o status foi aplicado (inserido ou atualizado), false caso contrário.
     */
    public boolean aplicarStatus(InimigoStatus novoStatus) {
        
        if (novoStatus.getTurnoRestante() <= 0) {
            System.out.println("⚠️ Status não aplicado: Duração deve ser positiva.");
            return false;
        }

        // 1. Procura se o mesmo status já está ativo no inimigo
        List<InimigoStatus> statusAtivos = inimigoStatusDAO.procurarPorInimigo(novoStatus.getIdInimigo());
        
        for (InimigoStatus is : statusAtivos) {
            // Regra de Negócio: Se o status já existe, atualiza apenas o contador de turnos (reforça a duração)
            if (is.getIdStatus() == novoStatus.getIdStatus()) {
                
                // Ex: Se o novo status tem duração 3 e o ativo tem 1, o novo será 3.
                is.setTurnoRestante(novoStatus.getTurnoRestante()); 
                
                System.out.println("🔄 Status ID " + is.getIdStatus() + " reforçado no Inimigo ID " + is.getIdInimigo());
                return inimigoStatusDAO.alterar(is);
            }
        }
        
        // 2. Se o status não existe, insere um novo registro
        System.out.println("✨ Novo Status ID " + novoStatus.getIdStatus() + " aplicado no Inimigo ID " + novoStatus.getIdInimigo());
        return inimigoStatusDAO.inserir(novoStatus);
    }
    
    /**
     * Remove um status específico do banco de dados (geralmente porque a duração chegou a zero).
     */
    public boolean removerStatus(InimigoStatus inimigoStatus) {
        if (inimigoStatus.getId() <= 0) {
            System.out.println("⚠️ Não é possível remover: ID do InimigoStatus inválido.");
            return false;
        }
        System.out.println("❌ Status ID " + inimigoStatus.getIdStatus() + " removido do Inimigo ID " + inimigoStatus.getIdInimigo());
        return inimigoStatusDAO.excluir(inimigoStatus);
    }
    
    /**
     * Gerencia a duração de todos os status ativos de um inimigo. 
     * Chamado no final do turno do inimigo.
     */
    public void gerenciarTurno(int idInimigo) {
        List<InimigoStatus> statusAtivos = inimigoStatusDAO.procurarPorInimigo(idInimigo);
        
        for (InimigoStatus is : statusAtivos) {
            
            is.decrementarTurno();
            
            if (is.getTurnoRestante() <= 0) {
                // Se o turno chegou a zero, remove o status
                removerStatus(is);
            } else {
                // Se ainda sobram turnos, atualiza no banco
                inimigoStatusDAO.alterar(is);
                System.out.println("⏳ Status ID " + is.getIdStatus() + " do Inimigo ID " + is.getIdInimigo() + 
                                   " decrementado. Restam: " + is.getTurnoRestante() + " turnos.");
            }
        }
    }
    
    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA E UTILIDADE ---
    // ------------------------------------------------------------------

    public List<InimigoStatus> pesquisarStatusPorInimigo(int idInimigo) {
        return inimigoStatusDAO.procurarPorInimigo(idInimigo);
    }

    // Outros métodos de busca por ID seriam implementados aqui se necessário.
}