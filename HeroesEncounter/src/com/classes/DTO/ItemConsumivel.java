package com.classes.DTO;

public class ItemConsumivel extends Item{

	private int cura;
	private int mana;

	public ItemConsumivel(String nome, int cura, int mana) {
        super(nome, "Consumivel"); 
        this.cura = cura;
        this.mana = mana;
    }
	
	public ItemConsumivel() {

    }
	
	public int getCura() {
		return cura;
	}

	public void setCura(int cura) {
		this.cura = cura;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

}
