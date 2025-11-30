package com.classes.DTO;

public class InimigoHabilidade {

	private int idInimigo;
	private int idHabilidade;
	private int chanceUso;

	private Inimigo inimigo;
	private Habilidade habilidade;

	public InimigoHabilidade() {
	}

	public InimigoHabilidade(int idInimigo, int idHabilidade, int chanceUso) {
		this.idInimigo = idInimigo;
		this.idHabilidade = idHabilidade;
		this.chanceUso = chanceUso;
	}

	public int getIdInimigo() {
		return idInimigo;
	}

	public void setIdInimigo(int idInimigo) {
		this.idInimigo = idInimigo;
	}

	public int getIdHabilidade() {
		return idHabilidade;
	}

	public void setIdHabilidade(int idHabilidade) {
		this.idHabilidade = idHabilidade;
	}

	public int getChanceUso() {
		return chanceUso;
	}

	public void setChanceUso(int chanceUso) {
		this.chanceUso = chanceUso;
	}

	public Inimigo getInimigo() {
		return inimigo;
	}

	public void setInimigo(Inimigo inimigo) {
		this.inimigo = inimigo;
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}
}