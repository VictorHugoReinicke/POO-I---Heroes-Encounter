package com.classes.DTO;

import java.util.Arrays;
import java.util.List;

import com.classes.BO.HabilidadesBO;
import com.classes.BO.ItemBO;

public class Mago extends Jogador {

    private static final int ID_CLASSE_MAGO = 2;
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("Cajado");
    private int danoMagico;

    public Mago() {
        this.setIdClasse(ID_CLASSE_MAGO);
        this.setHpMax(70);
        this.setHp(70);
        this.setAtaque(10);
        this.setOuro(100);
        this.setIdStatus(1);
        this.setManaMax(150);
        this.setMana(150);
        this.setOuro(100);
        this.setDanoMagico(25);
    }

    public Mago(String nome) {
        this();
        this.setNome(nome);
    }
    
    public int getDanoMagico() {
        return danoMagico;
    }

    public void setDanoMagico(int danoMagico) {
        this.danoMagico = danoMagico;
    }

    public void inicializarKit(ItemBO itemBO, HabilidadesBO habBO) {
        // Mago deve usar Cajado (ID 2)
        ItemArma cajado = (ItemArma) itemBO.procurarPorCodigo(2);
        this.setArmaEquipada(cajado);
        this.adicionarItem(cajado, 1, true);

        // Mago usa Poção de Mana (ID 5)
        ItemConsumivel mana = (ItemConsumivel) itemBO.procurarPorCodigo(5);
        this.adicionarItem(mana, 2, false);
        ItemConsumivel cura = (ItemConsumivel) itemBO.procurarPorCodigo(4);
        this.adicionarItem(cura, 1, false);
        

        // Mago deve ter habilidades de Mago (IDs 4,5,6)
        this.getHabilidades().add(habBO.procurarPorCodigo(4));
        this.getHabilidades().add(habBO.procurarPorCodigo(5));
        this.getHabilidades().add(habBO.procurarPorCodigo(6));
    }

    @Override
    public List<String> getTiposArmasPermitidas() {
        return TIPOS_PERMITIDOS;
    }
}