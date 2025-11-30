package com.classes.DTO;

import com.classes.BO.*;

import java.util.List;
import java.util.Random;

public class GerenciadorHabilidades {
	 private static final Random random = new Random();
	    private static JogadorStatusBO jogadorStatusBO = new JogadorStatusBO();
	    private static InimigoStatusBO inimigoStatusBO = new InimigoStatusBO();
	    private static StatusBO statusBO = new StatusBO();
	    
	    public static ResultadoHabilidade executarHabilidade(Jogador jogador, Inimigo inimigo, Habilidade habilidade) {
	        ResultadoHabilidade resultado = new ResultadoHabilidade();
	        resultado.setHabilidadeUsada(habilidade.getNome());
	        
	        // Verificar mana
	        if (jogador.getMana() < habilidade.getCustoMana()) {
	            resultado.setSucesso(false);
	            resultado.setMensagem("Mana insuficiente!");
	            return resultado;
	        }
	        
	        // Aplicar custo de mana
	        jogador.setMana(jogador.getMana() - habilidade.getCustoMana());
	        
	        // Processar baseado no tipo
	        switch (habilidade.getTipo()) {
	            case "OFENSIVA":
	                processarOfensiva(jogador, inimigo, habilidade, resultado);
	                break;
	            case "DEFENSIVA":
	                processarDefensiva(jogador, inimigo, habilidade, resultado);
	                break;
	            case "CURA":
	                processarCura(jogador, habilidade, resultado);
	                break;
	        }
	        
	        // Aplicar status se houver
	        if (habilidade.getIdStatus() > 0) {
	            aplicarStatus(habilidade, jogador, inimigo, resultado);
	        }
	        
	        resultado.setSucesso(true);
	        return resultado;
	    }
	    
	    private static void processarOfensiva(Jogador jogador, Inimigo inimigo, Habilidade habilidade, ResultadoHabilidade resultado) {
	        ResultadoAtaque resultadoAtaque;
	        
	        // ✅ USAR CALCULADORA ESPECÍFICA PARA CADA HABILIDADE
	        switch (habilidade.getNome()) {
	            case "Golpe Forte":
	                resultadoAtaque = CalculadoraCombate.calcularGolpeForte(jogador, inimigo);
	                break;
	            case "Bola de Fogo":
	            case "Estaca de Gelo":
	                resultadoAtaque = CalculadoraCombate.calcularAtaqueMagico(jogador, inimigo, habilidade);
	                break;
	            default:
	                resultadoAtaque = CalculadoraCombate.calcularAtaqueFisico(jogador, inimigo);
	                break;
	        }
	        
	        int danoFinal = resultadoAtaque.getDano();
	        
	        // Aplicar dano
	        inimigo.setHp(inimigo.getHp() - danoFinal);
	        resultado.setDanoCausado(danoFinal);
	        resultado.setCritico(resultadoAtaque.isCritico());
	        
	        resultado.setMensagem((resultado.getMensagem() != null ? resultado.getMensagem() : "") + 
	                             "Causou " + danoFinal + " de dano!");
	    }
	    
	    private static void processarDefensiva(Jogador jogador, Inimigo inimigo, Habilidade habilidade, ResultadoHabilidade resultado) {
	        resultado.setMensagem("Defesa ativada!");
	        
	        // Habilidades defensivas geralmente aplicam status no jogador
	        if (habilidade.getIdStatus() > 0) {
	            resultado.setEfeitoAplicado("Buff defensivo aplicado");
	        }
	    }
	    
	    private static void processarCura(Jogador jogador, Habilidade habilidade, ResultadoHabilidade resultado) {
	        // CORREÇÃO: Garantir que a cura seja positiva
	        double percentualCura = Math.abs(habilidade.getFatorDano()); // Usar valor absoluto
	        int cura = (int)(jogador.getHpMax() * percentualCura);
	        
	        int novaVida = Math.min(jogador.getHpMax(), jogador.getHp() + cura);
	        int curaReal = novaVida - jogador.getHp();
	        
	        jogador.setHp(novaVida);
	        resultado.setCuraAplicada(curaReal);
	        resultado.setMensagem("Cura aplicada: +" + curaReal + " HP");
	    }
	    
	    private static void aplicarStatus(Habilidade habilidade, Jogador jogador, Inimigo inimigo, ResultadoHabilidade resultado) {
	        try {
	            Status status = statusBO.procurarPorCodigo(habilidade.getIdStatus());
	            if (status == null) {
	                return;
	            }
	            
	            boolean statusAplicado = false;
	            String tipoHabilidade = habilidade.getTipo();
	            
	            // Habilidades ofensivas aplicam status no inimigo
	            if ("OFENSIVA".equals(tipoHabilidade)) {
	                InimigoStatus inimigoStatus = new InimigoStatus(inimigo.getId(), status.getId(), status.getDuracaoTurnos());
	                statusAplicado = inimigoStatusBO.aplicarStatus(inimigoStatus);
	                
	                if (statusAplicado) {
	                    resultado.setStatusAplicado(status.getNome());
	                    resultado.setMensagem((resultado.getMensagem() != null ? resultado.getMensagem() + " " : "") + 
	                                        "Aplicou " + status.getNome() + " no inimigo!");
	                }
	            }
	            // Habilidades defensivas aplicam status no jogador
	            else if ("DEFENSIVA".equals(tipoHabilidade)) {
	                statusAplicado = jogadorStatusBO.aplicarStatus(jogador.getId(), status.getId());
	                
	                if (statusAplicado) {
	                    resultado.setStatusAplicado(status.getNome());
	                    resultado.setMensagem((resultado.getMensagem() != null ? resultado.getMensagem() + " " : "") + 
	                                        "Aplicou " + status.getNome() + " em si mesmo!");
	                }
	            }
	            
	        } catch (Exception e) {
	            System.err.println("Erro ao aplicar status: " + e.getMessage());
	        }
	    }
	    
	    // ✅ NOVO: Processar início de turno do inimigo (DOTs)
	    public static int processarInicioTurnoInimigo(Inimigo inimigo) {
	        return GerenciadorStatus.processarDOTInimigo(inimigo);
	    }
	    
	    // ✅ ATUALIZADO: Usar GerenciadorStatus para processar fim de turno
	    public static void processarFimDeTurno(Jogador jogador, Inimigo inimigo) {
	        GerenciadorStatus.processarFimDeTurno(jogador, inimigo);
	    }
	}