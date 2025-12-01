package com.classes.DTO;

public class Classe {
	private int id;
	private String nome;
	private int bonus_forca;
	private int bonus_magia;
	private int bonus_esquiva;

	public Classe(String nome, int bonus_forca, int bonus_magia, int bonus_esquiva) {
		this.nome = nome;
		this.bonus_forca = bonus_forca;
		this.bonus_magia = bonus_magia;
		this.bonus_esquiva = bonus_esquiva;
	}

	public Classe() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getBonus_forca() {
		return bonus_forca;
	}

	public void setBonus_forca(int bonus_forca) {
		this.bonus_forca = bonus_forca;
	}

	public int getBonus_magia() {
		return bonus_magia;
	}

	public void setBonus_magia(int bonus_magia) {
		this.bonus_magia = bonus_magia;
	}

	public int getBonus_esquiva() {
		return bonus_esquiva;
	}

	public void setBonus_esquiva(int bonus_esquiva) {
		this.bonus_esquiva = bonus_esquiva;
	}
}