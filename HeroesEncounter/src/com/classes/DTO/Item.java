package com.classes.DTO;

public abstract class Item {
	
	private int id;
	private String nome;
	private String raridade;
	private String tipoItem;
	
	public Item() {
		super();
	}

	public Item(String nome, String tipoItem) {
        this.nome = nome;
        this.tipoItem = tipoItem;
    }

	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
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

	public String getRaridade() {
		return raridade;
	}

	public void setRaridade(String raridade) {
		this.raridade = raridade;
	}

}
