package com.classes.DTO;

import com.classes.BO.*;
import java.util.List;

public class GerenciadorStatus {

	private static JogadorStatusBO jogadorStatusBO = new JogadorStatusBO();
	private static InimigoStatusBO inimigoStatusBO = new InimigoStatusBO();
	private static StatusBO statusBO = new StatusBO();

	public static int processarDOTInimigo(Inimigo inimigo) {
		int danoTotal = 0;

		try {
			List<InimigoStatus> statusAtivos = inimigoStatusBO.pesquisarStatusPorInimigo(inimigo.getId());

			for (InimigoStatus status : statusAtivos) {
				Status detalhesStatus = statusBO.procurarPorCodigo(status.getIdStatus());

				if (detalhesStatus != null && detalhesStatus.getDanoTurno() > 0) {
					danoTotal += detalhesStatus.getDanoTurno();
					System.out.println("🔥 DOT: " + detalhesStatus.getNome() + " causa " + detalhesStatus.getDanoTurno()
							+ " de dano");
				}
			}

			// Aplica o dano total
			if (danoTotal > 0) {
				inimigo.setHp(inimigo.getHp() - danoTotal);
			}

		} catch (Exception e) {
			System.err.println("Erro ao processar DOT do inimigo: " + e.getMessage());
		}

		return danoTotal;
	}

	/**
	 * Calcula modificadores de defesa do inimigo baseado nos status ativos
	 */
	public static double calcularModificadorDefesaInimigo(Inimigo inimigo) {
		double modificadorTotal = 0.0;

		try {
			List<InimigoStatus> statusAtivos = inimigoStatusBO.pesquisarStatusPorInimigo(inimigo.getId());

			for (InimigoStatus status : statusAtivos) {
				Status detalhesStatus = statusBO.procurarPorCodigo(status.getIdStatus());

				if (detalhesStatus != null) {
					modificadorTotal += getModificadorDefesaDoStatus(detalhesStatus);
				}
			}

		} catch (Exception e) {
			System.err.println("Erro ao calcular modificador de defesa: " + e.getMessage());
		}

		return modificadorTotal;
	}

	/**
	 * Calcula modificadores de ataque do jogador baseado nos status ativos
	 */
	public static double calcularModificadorAtaqueJogador(Jogador jogador) {
		double modificadorTotal = 0.0;

		try {
			List<JogadorStatus> statusAtivos = jogadorStatusBO.listarStatusAtivos(jogador.getId());

			for (JogadorStatus status : statusAtivos) {
				if (status.getStatus() != null) {
					modificadorTotal += getModificadorAtaqueDoStatus(status.getStatus());
				}
			}

		} catch (Exception e) {
			System.err.println("Erro ao calcular modificador de ataque: " + e.getMessage());
		}

		return modificadorTotal;
	}

	/**
	 * Calcula chance de esquiva do jogador baseado nos status ativos
	 */
	public static double calcularChanceEsquivaJogador(Jogador jogador) {
		double chanceEsquiva = 0.0;

		try {
			List<JogadorStatus> statusAtivos = jogadorStatusBO.listarStatusAtivos(jogador.getId());

			for (JogadorStatus status : statusAtivos) {
				if (status.getStatus() != null) {
					chanceEsquiva += getChanceEsquivaDoStatus(status.getStatus());
				}
			}

		} catch (Exception e) {
			System.err.println("Erro ao calcular chance de esquiva: " + e.getMessage());
		}

		return chanceEsquiva;
	}

	/**
	 * Verifica se o jogador deve esquivar do próximo ataque
	 */
	public static boolean verificarEsquiva(Jogador jogador) {
	    try {
	        List<JogadorStatus> statusAtivos = jogadorStatusBO.listarStatusAtivos(jogador.getId());
	        
	        for (JogadorStatus status : statusAtivos) {
	            if (status.getStatus() != null && "Ilusão".equals(status.getStatus().getNome())) {
	                double chanceEsquiva = status.getStatus().getChanceEsquiva();
	                
	                boolean esquivou = Math.random() < chanceEsquiva;
	                
	                // SEMPRE remover o status após verificação (dura apenas 1 uso)
	                jogadorStatusBO.removerStatus(jogador.getId(), status.getStatus().getId());
	                
	                return esquivou;
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Erro ao verificar esquiva: " + e.getMessage());
	    }
	    
	    return false;
	}

	/**
	 * Método auxiliar para extrair modificador de defesa do status
	 */
	private static double getModificadorDefesaDoStatus(Status status) {
		switch (status.getNome()) {
		case "Congelamento":
			return -0.3; // -30% defesa (DEBUFF - inimigo defende menos)
		case "Proteção Divina":
			return 0.5; // +50% defesa (BUFF - jogador defende mais)
		default:
			return 0.0;
		}
	}

	/**
	 * Método auxiliar para extrair modificador de ataque do status
	 */
	private static double getModificadorAtaqueDoStatus(Status status) {
		switch (status.getNome()) {
		case "Precisão":
			return 0.2; // +20% ataque
		default:
			return 0.0;
		}
	}

	/**
	 * Método auxiliar para extrair chance de esquiva do status
	 */
	private static double getChanceEsquivaDoStatus(Status status) {
		switch (status.getNome()) {
		case "Ilusão":
			return 0.8; // 80% de chance de esquiva (próximo ataque)
		default:
			return 0.0;
		}
	}

	/**
	 * Processa todos os status no fim do turno (usando métodos EXISTENTES dos BOs)
	 */
	public static void processarFimDeTurno(Jogador jogador, Inimigo inimigo) {
		// Usa os métodos JÁ EXISTENTES dos seus BOs
		jogadorStatusBO.processarFimDeTurno(jogador.getId());
		inimigoStatusBO.gerenciarTurno(inimigo.getId());
	}
}