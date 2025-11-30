package com.classes.DTO;

public class ItemArma extends Item{

	private int bonusDano;
	private int bonusMagico;
	private int bonusCritico;

	public ItemArma(String nome, int bonusDano, int bonusMagico, int bonusCritico) {
        super(nome, "Arma"); 
        this.bonusDano = bonusDano;
        this.bonusMagico= bonusMagico;
        this.bonusCritico = bonusCritico;
    }
    
    public ItemArma() {
        this.setTipoItem("Arma"); 
    }
	

	public int getBonusDano() {
		return bonusDano;
	}

	public void setBonusDano(int bonusDano) {
		this.bonusDano = bonusDano;
	}

	public int getBonusMagico() {
		return bonusMagico;
	}

	public void setBonusMagico(int bonusMagico) {
		this.bonusMagico = bonusMagico;
	}

	public int getBonusCritico() {
		return bonusCritico;
	}

	public void setBonusCritico(int bonusCritico) {
		this.bonusCritico = bonusCritico;
	}

}
