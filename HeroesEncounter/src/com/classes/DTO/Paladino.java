package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

public class Paladino extends Jogador {

	private static final int ID_CLASSE_PALADINO = 3;
	private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("Espada Leve");

	public Paladino() {
        this.setIdClasse(ID_CLASSE_PALADINO); 
        
        this.setHpMax(120);
        this.setHp(120);
        this.setManaMax(80);
        this.setMana(80);
        this.setAtaque(15);
        this.setNivel(1);
        this.setIdStatus(1);
        this.setOuro(100);
    }
	
	@Override
	public List<String> getTiposArmasPermitidas() {
		return TIPOS_PERMITIDOS;
	}

	public void aumentarEsquiva() {
	}

}
