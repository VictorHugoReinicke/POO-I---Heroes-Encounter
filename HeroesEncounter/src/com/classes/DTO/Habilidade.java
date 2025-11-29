package com.classes.DTO;

public class Habilidade {

	private String nome;
	private int custoMana;
	private int dano;
	private String tipo;
	private Status efeitoStatus;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCustoMana() {
		return custoMana;
	}

	public void setCustoMana(int custoMana) {
		this.custoMana = custoMana;
	}

	public int getDano() {
		return dano;
	}

	public void setDano(int dano) {
		this.dano = dano;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Status getEfeitoStatus() {
		return efeitoStatus;
	}

	public void setEfeitoStatus(Status efeitoStatus) {
		this.efeitoStatus = efeitoStatus;
	}

}
