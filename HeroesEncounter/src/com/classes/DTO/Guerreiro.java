package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

import com.classes.BO.HabilidadesBO;
import com.classes.BO.ItemBO;

public class Guerreiro extends Jogador {

	private static final int ID_CLASSE_GUERREIRO = 1;
	private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("Espada Pesada", "Machado");

	public Guerreiro() {
		this.setIdClasse(ID_CLASSE_GUERREIRO);
		this.setHpMax(150);
		this.setHp(150);
		this.setManaMax(50);
		this.setMana(50);
		this.setAtaque(20);
		this.setOuro(100);
	}

	public Guerreiro(String nome) {
        this(); 
        this.setNome(nome);
    }
	
	public void inicializarKit(ItemBO itemBO, HabilidadesBO habBO) {
		ItemArma machado = (ItemArma) itemBO.procurarPorCodigo(1);
		this.setArmaEquipada(machado);
		this.adicionarItem(machado, 1, true);

		ItemConsumivel cura = (ItemConsumivel) itemBO.procurarPorCodigo(4);
		this.adicionarItem(cura, 1, false);

		this.getHabilidades().add(habBO.procurarPorCodigo(7));
		this.getHabilidades().add(habBO.procurarPorCodigo(8));
		this.getHabilidades().add(habBO.procurarPorCodigo(9));
	}

	@Override
	public List<String> getTiposArmasPermitidas() {
		return TIPOS_PERMITIDOS;
	}
}