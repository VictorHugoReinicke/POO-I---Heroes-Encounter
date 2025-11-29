package com.classes.DTO;

public class ControladorCombate {

	private SerVivo turnoAtual;

	public SerVivo getTurnoAtual() {
		return turnoAtual;
	}

	public void setTurnoAtual(SerVivo turnoAtual) {
		this.turnoAtual = turnoAtual;
	}

	public void iniciarCombate(Jogador jogador, Inimigo inimigo) {
		
	}
	
	public void proximoTurno() {}
	
	public boolean acaoExecutada(SerVivo servivo) {
		return true;
	}
	
}
