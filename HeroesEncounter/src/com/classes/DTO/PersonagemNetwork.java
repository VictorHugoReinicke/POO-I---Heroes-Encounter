package com.classes.DTO;

import java.io.Serializable;

public class PersonagemNetwork implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private String classe;
	private int vida;
	private int ataque;
	private int defesa;
	private int mana;

	public PersonagemNetwork(int id, String nome, String classe, int vida, int ataque, int defesa, int mana) {
		this.id = id;
		this.nome = nome;
		this.classe = classe;
		this.vida = vida;
		this.ataque = ataque;
		this.defesa = defesa;
		this.mana = mana;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getClasse() {
		return classe;
	}

	public int getVida() {
		return vida;
	}

	public int getAtaque() {
		return ataque;
	}

	public int getDefesa() {
		return defesa;
	}

	public int getMana() {
		return mana;
	}

	@Override
	public String toString() {
		return nome + " - " + classe + " (Vida: " + vida + ", Ataque: " + ataque + ")";
	}
}