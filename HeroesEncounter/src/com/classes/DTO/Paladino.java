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
		this.setNivel(1);
		this.setIdStatus(1);
		this.setOuro(100);
	}

	public void inicializarKit(ItemBO itemBO, HabilidadesBO habBO) {
	    // Paladino deve usar Espada Leve (ID 3), não Cajado (ID 2)
	    ItemArma espadaLeve = (ItemArma) itemBO.procurarPorCodigo(1);
	    this.setArmaEquipada(espadaLeve);
	    this.adicionarItem(espadaLeve, 1, true);

	    // Paladino usa Poção de Vida (ID 4), não Poção de Mana (ID 5)
	    ItemConsumivel cura = (ItemConsumivel) itemBO.procurarPorCodigo(4);
	    this.adicionarItem(cura, 2, false);
	    ItemConsumivel mana = (ItemConsumivel) itemBO.procurarPorCodigo(5);
	    this.adicionarItem(mana, 1, false);

	    // Paladino deve ter habilidades de Paladino (IDs 7,8,9)
	    this.getHabilidades().add(habBO.procurarPorCodigo(1));
	    this.getHabilidades().add(habBO.procurarPorCodigo(2));
	    this.getHabilidades().add(habBO.procurarPorCodigo(3));
	}

	@Override
	public List<String> getTiposArmasPermitidas() {
		return TIPOS_PERMITIDOS;
	}
}