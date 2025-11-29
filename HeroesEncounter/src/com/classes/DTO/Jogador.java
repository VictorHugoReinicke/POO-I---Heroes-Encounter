package com.classes.DTO;

import java.util.List;

public abstract class Jogador extends SerVivo {

	private List<Item> inventario;
	private int limiteInventario;
	private int ouro;
	private List<Habilidade> habilidades;
	private ItemArma armaEquipada;
	private ItemDefesa armaduraEquipada;
	private int idClasse;

	public List<Item> getInventario() {
		return inventario;
	}

	public void setInventario(List<Item> inventario) {
		this.inventario = inventario;
	}

	public int getLimiteInventario() {
		return limiteInventario;
	}

	public void setLimiteInventario(int limiteInventario) {
		this.limiteInventario = limiteInventario;
	}

	public int getOuro() {
		return ouro;
	}

	public void setOuro(int ouro) {
		this.ouro = ouro;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public ItemArma getArmaEquipada() {
		return armaEquipada;
	}

	public void setArmaEquipada(ItemArma armaEquipada) {
		this.armaEquipada = armaEquipada;
	}

	public ItemDefesa getArmaduraEquipada() {
		return armaduraEquipada;
	}

	public void setArmaduraEquipada(ItemDefesa armaduraEquipada) {
		this.armaduraEquipada = armaduraEquipada;
	}
	
	public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }
	
	public void usarHabilidade(Habilidade habilidade) {
	}

	public void usarItem(Item item) {
	}

	public boolean comprarItem(Item item) {

		return true;

	}
	
	public abstract List<String> getTiposArmasPermitidas();
}
