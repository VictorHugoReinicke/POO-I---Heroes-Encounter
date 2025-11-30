package com.classes.DTO;

import com.classes.Enums.TipoIA;

public abstract class Inimigo extends SerVivo {

	private int recompensaOuro;
	private int ataque;
	private int defesa;
	private TipoIA tipoIA;

	public Inimigo() {
		super();
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefesa() {
		return defesa;
	}

	public void setDefesa(int defesa) {
		this.defesa = defesa;
	}

	public int getRecompensaOuro() {
		return recompensaOuro;
	}

	public void setRecompensaOuro(int recompensaOuro) {
		this.recompensaOuro = recompensaOuro;
	}

	public TipoIA getTipoIA() {
		return tipoIA;
	}

	public void setTipoIA(TipoIA tipoIA) {
		this.tipoIA = tipoIA;
	}

	public void decidirAcao(Jogador jogador) {
	}

}
