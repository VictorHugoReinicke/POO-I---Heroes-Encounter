package com.classes.DTO;

public class InimigoHabilidade {

	private int idInimigo;
	private int idHabilidade;
	private int chance_uso;

	private Inimigo inimigo;
	private Habilidade habilidade;

	public InimigoHabilidade() {
	}

	public InimigoHabilidade(int idInimigo, int idHabilidade, int chance_uso) {
		this.idInimigo = idInimigo;
		this.idHabilidade = idHabilidade;
		this.chance_uso = chance_uso;
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


	public int getChance_uso() {
		return chance_uso;
	}

	public void setChance_uso(int chance_uso) {
		this.chance_uso = chance_uso;
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