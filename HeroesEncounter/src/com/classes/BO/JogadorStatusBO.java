package com.classes.BO;

import com.classes.DAO.JogadorStatusDAO;
import com.classes.DTO.JogadorStatus;
import com.classes.DTO.Status;

import java.util.List;

public class JogadorStatusBO {

	private JogadorStatusDAO jogadorStatusDAO;
	// Precisamos de StatusBO para buscar o objeto Status completo
	private StatusBO statusBO; 
	// Precisamos de JogadorBO para garantir que o jogador existe (em um sistema completo)
	// private JogadorBO jogadorBO;

	public JogadorStatusBO() {
		this.jogadorStatusDAO = new JogadorStatusDAO();
		this.statusBO = new StatusBO(); // Assume que StatusBO já existe
		// this.jogadorBO = new JogadorBO();
	}
	
    // ------------------------------------------------------------------
    // --- 1. LÓGICA DE NEGÓCIO PRINCIPAL: APLICAR E DECAIR ---
    // ------------------------------------------------------------------

    /**
     * Aplica um Status a um Jogador. 
     * Lógica de Negócio: Se o status já existe, a duração é renovada/substituída.
     */
	public boolean aplicarStatus(int idJogador, int idStatus) {
        
        // 1. Busca os detalhes do Status (para saber a duração base)
        Status statusDetalhe = statusBO.procurarPorCodigo(idStatus);
        
        if (statusDetalhe == null) {
            System.out.println("❌ Erro: Status ID " + idStatus + " não encontrado.");
            return false;
        }

        // 2. Tenta encontrar um registro existente (Duplicidade)
        JogadorStatus registroExistente = jogadorStatusDAO.procurarRegistro(idJogador, idStatus);
        
        if (registroExistente != null) {
            // Lógica de Renovação/Acúmulo: Renova a duração para a base do status
            registroExistente.setTurnosRestantes(statusDetalhe.getDuracaoTurnos());
            System.out.printf("✅ Status %s renovado para o Jogador ID %d. Turnos: %d.\n", 
                statusDetalhe.getNome(), idJogador, statusDetalhe.getDuracaoTurnos());
            
            return jogadorStatusDAO.alterar(registroExistente);

        } else {
            // Aplica um status novo
            JogadorStatus novoRegistro = new JogadorStatus(
                idJogador, 
                idStatus, 
                statusDetalhe.getDuracaoTurnos()
            );
            
            System.out.printf("✅ Status %s aplicado ao Jogador ID %d. Turnos: %d.\n", 
                statusDetalhe.getNome(), idJogador, statusDetalhe.getDuracaoTurnos());
            
            return jogadorStatusDAO.inserir(novoRegistro);
        }
    }

	public boolean removerStatus(int idJogador, int idStatus) {
        try {
            JogadorStatus registro = jogadorStatusDAO.procurarRegistro(idJogador, idStatus);
            if (registro != null) {
                return jogadorStatusDAO.excluir(registro);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao remover status: " + e.getMessage());
            return false;
        }
    }
	
    /**
     * Lógica executada no final de cada turno de combate.
     * Decrementa a duração e remove status esgotados.
     */
	public void processarFimDeTurno(int idJogador) {
	    
	    List<JogadorStatus> statusAtivos = jogadorStatusDAO.listarStatusPorJogador(idJogador);
	    
	    for (JogadorStatus js : statusAtivos) {
	        
	        int turnos = js.getTurnosRestantes();
	        
	        if (turnos > 1) {
	            // Diminui o contador
	            js.setTurnosRestantes(turnos - 1);
	            jogadorStatusDAO.alterar(js);
	            
	        } else {
	            // Turno restante chegou a 0 ou 1, então remove (fim do efeito)
	            jogadorStatusDAO.excluir(js);
	        }
	    }
	}

    // ------------------------------------------------------------------
    // --- 2. MÉTODOS DE BUSCA E UTILIDADE ---
    // ------------------------------------------------------------------
    
    public List<JogadorStatus> listarStatusAtivos(int idJogador) {
        return jogadorStatusDAO.listarStatusPorJogador(idJogador);
    }
    
    public boolean jogadorPossuiStatus(int idJogador, int idStatus) {
        return jogadorStatusDAO.procurarRegistro(idJogador, idStatus) != null;
    }
    public JogadorStatus procurarRegistro(int idJogador, int idStatus) {
        return jogadorStatusDAO.procurarRegistro(idJogador, idStatus);
    }
}