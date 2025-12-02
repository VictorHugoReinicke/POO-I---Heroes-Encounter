package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

import com.classes.BO.HabilidadesBO;
import com.classes.BO.ItemBO;

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
		this.setOuro(100);
	}

	public Paladino(String nome) {
        this();
        this.setNome(nome);
    }
	
	public void inicializarKit(ItemBO itemBO, HabilidadesBO habBO) {
	    ItemArma espadaLeve = (ItemArma) itemBO.procurarPorCodigo(1);
	    this.setArmaEquipada(espadaLeve);
	    this.adicionarItem(espadaLeve, 1, true);

	    ItemConsumivel cura = (ItemConsumivel) itemBO.procurarPorCodigo(4);
	    this.adicionarItem(cura, 2, false);
	    ItemConsumivel mana = (ItemConsumivel) itemBO.procurarPorCodigo(5);
	    this.adicionarItem(mana, 1, false);

	    this.getHabilidades().add(habBO.procurarPorCodigo(1));
	    this.getHabilidades().add(habBO.procurarPorCodigo(2));
	    this.getHabilidades().add(habBO.procurarPorCodigo(3));
	}

	@Override
	public List<String> getTiposArmasPermitidas() {
		return TIPOS_PERMITIDOS;
	}
}