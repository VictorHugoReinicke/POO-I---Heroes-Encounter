package com.classes.DTO;

import com.classes.Enums.TipoIA;
import com.classes.main.*;

public abstract class Inimigo extends SerVivo {

	private int recompensaOuro;
	private int ataque;
	private int defesa;
	private TipoIA tipoIA;

	public Inimigo() {
		super();
		this.tipoIA = TipoIA.BALANCEADO;
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
		String acaoEscolhida = InimigoIA.decidirAcao(this, jogador);
		
		InimigoIA.executarAcao(this, jogador, acaoEscolhida);
	}

	public String getDescricaoCompleta() {
		return String.format("%s\n HP: %d/%d\n Ataque: %d\n Defesa: %d\n Recompensa: %d\n%s",
				getNome(),
				getHp(), getHpMax(), 
				getAtaque(), getDefesa(), 
				getRecompensaOuro(),
				InimigoIA.getDescricaoIA(getTipoIA()));
	}
}