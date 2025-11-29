package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

public class Mago extends Jogador{

	private static final int ID_CLASSE_MAGO = 2;
	private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("Cajado");
	private int danoMagico;
	
	
	
	public Mago() {
		this.setIdClasse(ID_CLASSE_MAGO);
		this.setHpMax(70);
		this.setHp(70);
		this.setAtaque(5);
		this.setOuro(100);
		this.setIdStatus(1);
		this.setManaMax(150);
        this.setMana(150);
		this.setOuro(100);
		this.setIdStatus(1);
		this.setNivel(1);
		this.setDanoMagico(25);
	}
	
	public int getDanoMagico() {
		return danoMagico;
	}

	public void setDanoMagico(int danoMagico) {
		this.danoMagico = danoMagico;
	}

	@Override
    public List<String> getTiposArmasPermitidas() {
        return TIPOS_PERMITIDOS;
    }
	
	
	public void ataqueMagico(SerVivo alvo) {
	    if (this.getMana() > 0) {
	        
	        int danoTotal = this.getAtaque() + this.getDanoMagico(); 
	        
	        System.out.println(this.getNome() + " conjura uma magia, causando " + danoTotal + " de dano!");
	        alvo.receberDano(danoTotal);
	        
	        
	        this.setMana(this.getMana() - 5);
	    } else {
	        System.out.println(this.getNome() + " tenta conjurar, mas está sem Mana!");
	    }
	}
	
}
