package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

public class Guerreiro extends Jogador {

	private static final int ID_CLASSE_GUERREIRO = 1;
	private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("Espada Pesada", "Machado");

	public Guerreiro() {
		this.setIdClasse(ID_CLASSE_GUERREIRO);
		this.setHpMax(150);
		this.setHp(150);
		this.setManaMax(0);
        this.setMana(0);
		this.setAtaque(20);
		this.setOuro(100);
		this.setIdStatus(1);
		this.setNivel(1);
        
	}

	@Override
	public List<String> getTiposArmasPermitidas() {
		return TIPOS_PERMITIDOS;
	}

	public void HabilidadeForte() {
	}

}
