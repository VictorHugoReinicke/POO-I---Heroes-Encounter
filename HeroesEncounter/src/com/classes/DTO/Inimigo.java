package com.classes.DTO;

import java.util.List;

public class Inimigo extends SerVivo {

	private String tipo;
	private int recompensaOuro;
	private String iaTipo;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getRecompensaOuro() {
		return recompensaOuro;
	}

	public void setRecompensaOuro(int recompensaOuro) {
		this.recompensaOuro = recompensaOuro;
	}

	public String getIaTipo() {
		return iaTipo;
	}

	public void setIaTipo(String iaTipo) {
		this.iaTipo = iaTipo;
	}
	
	public void decidirAcao(Jogador jogador) {}
	
}
