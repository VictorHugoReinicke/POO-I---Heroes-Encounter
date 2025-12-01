package com.classes.DTO;

import com.classes.Enums.TipoIA;

public class InimigoMagico extends Inimigo {
//massa massa, bem isso que eu tava pensando
	private double resistenciaMagica;

	public InimigoMagico() {
		super();

		this.setHpMax(50);
		this.setHp(this.getHpMax());
		this.setNome("Inimigo Mágico");

		this.setAtaque(10);
		this.setDefesa(5);
		this.setRecompensaOuro(15);
		this.setTipoIA(TipoIA.AGRESSIVO);

		this.setResistenciaMagica(0.10);
	}

	public double getResistenciaMagica() {
		return resistenciaMagica;
	}

	public void setResistenciaMagica(double resistenciaMagica) {
		this.resistenciaMagica = resistenciaMagica;
	}
}