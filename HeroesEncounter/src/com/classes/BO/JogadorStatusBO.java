package com.classes.BO;

import com.classes.DAO.JogadorStatusDAO;
import com.classes.DTO.JogadorStatus;
import com.classes.DTO.Status;

import java.util.List;

public class JogadorStatusBO {

	private JogadorStatusDAO jogadorStatusDAO;
	private StatusBO statusBO;
	public JogadorStatusBO() {
		this.jogadorStatusDAO = new JogadorStatusDAO();
		this.statusBO = new StatusBO();
	}
	
	public boolean aplicarStatus(int idJogador, int idStatus) {
        
        Status statusDetalhe = statusBO.procurarPorCodigo(idStatus);
        
        if (statusDetalhe == null) {
            System.out.println("Erro: Status ID " + idStatus + " não encontrado.");
            return false;
        }

        JogadorStatus registroExistente = jogadorStatusDAO.procurarRegistro(idJogador, idStatus);
        
        if (registroExistente != null) {
            registroExistente.setTurnosRestantes(statusDetalhe.getDuracaoTurnos());
            System.out.printf("Status %s renovado para o Jogador ID %d. Turnos: %d.\n",
                statusDetalhe.getNome(), idJogador, statusDetalhe.getDuracaoTurnos());
            
            return jogadorStatusDAO.alterar(registroExistente);

        } else {
            JogadorStatus novoRegistro = new JogadorStatus(
                idJogador, 
                idStatus, 
                statusDetalhe.getDuracaoTurnos()
            );
            
            System.out.printf("Status %s aplicado ao Jogador ID %d. Turnos: %d.\n",
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

	public void processarFimDeTurno(int idJogador) {
	    
	    List<JogadorStatus> statusAtivos = jogadorStatusDAO.listarStatusPorJogador(idJogador);
	    
	    for (JogadorStatus js : statusAtivos) {
	        
	        int turnos = js.getTurnosRestantes();
	        
	        if (turnos > 1) {
	            js.setTurnosRestantes(turnos - 1);
	            jogadorStatusDAO.alterar(js);
	            
	        } else {
	            jogadorStatusDAO.excluir(js);
	        }
	    }
	}

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