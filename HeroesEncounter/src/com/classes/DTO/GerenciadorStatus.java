package com.classes.DTO;

import com.classes.BO.*;
import java.util.List;

public class GerenciadorStatus {
// ficou mt top victor, dps olha oq fiz tbm
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
					System.out.println("DOT: " + detalhesStatus.getNome() + " causa " + detalhesStatus.getDanoTurno()
							+ " de dano");
				}
			}

			if (danoTotal > 0) {
				inimigo.setHp(inimigo.getHp() - danoTotal);
			}

		} catch (Exception e) {
			System.err.println("Erro ao processar DOT do inimigo: " + e.getMessage());
		}

		return danoTotal;
	}

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


	public static boolean verificarEsquiva(Jogador jogador) {
	    try {
	        List<JogadorStatus> statusAtivos = jogadorStatusBO.listarStatusAtivos(jogador.getId());
	        
	        for (JogadorStatus status : statusAtivos) {
	            if (status.getStatus() != null && "Ilusão".equals(status.getStatus().getNome())) {
	                double chanceEsquiva = status.getStatus().getChanceEsquiva();
	                
	                boolean esquivou = Math.random() < chanceEsquiva;
	                
	                jogadorStatusBO.removerStatus(jogador.getId(), status.getStatus().getId());
	                
	                return esquivou;
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Erro ao verificar esquiva: " + e.getMessage());
	    }
	    
	    return false;
	}


	private static double getModificadorDefesaDoStatus(Status status) {
		switch (status.getNome()) {
		case "Congelamento":
			return -0.3;
		case "Proteção Divina":
			return 0.5;
		default:
			return 0.0;
		}
	}

	private static double getModificadorAtaqueDoStatus(Status status) {
		switch (status.getNome()) {
		case "Precisão":
			return 0.2;
		default:
			return 0.0;
		}
	}

	private static double getChanceEsquivaDoStatus(Status status) {
		switch (status.getNome()) {
		case "Ilusão":
			return 0.8;
		default:
			return 0.0;
		}
	}

	public static void processarFimDeTurno(Jogador jogador, Inimigo inimigo) {
		jogadorStatusBO.processarFimDeTurno(jogador.getId());
		inimigoStatusBO.gerenciarTurno(inimigo.getId());
	}
}