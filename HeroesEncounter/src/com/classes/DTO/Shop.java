package com.classes.DTO;

public class Shop {

	private int id;
	private String nome;

	public Shop(String nome) {
		this.nome = nome;
	}

	public Shop() {
		super();
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

}
