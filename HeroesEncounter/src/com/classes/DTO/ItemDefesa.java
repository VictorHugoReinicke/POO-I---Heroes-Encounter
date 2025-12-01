package com.classes.DTO;

public class ItemDefesa extends Item{

	private int bonusDefesa;
	private int bonusEsquiva;

	public ItemDefesa(String nome, int bonusDefesa, int bonusEsquiva) {
        super(nome, "Defesa"); 
        this.bonusDefesa= bonusDefesa;
        this.bonusEsquiva = bonusEsquiva;
    }
	
	public int getBonusDefesa() {
		return bonusDefesa;
	}

	public void setBonusDefesa(int bonusDefesa) {
		this.bonusDefesa = bonusDefesa;
	}

	public int getBonusEsquiva() {
		return bonusEsquiva;
	}

	public void setBonusEsquiva(int bonusEsquiva) {
		this.bonusEsquiva = bonusEsquiva;
	}

}
