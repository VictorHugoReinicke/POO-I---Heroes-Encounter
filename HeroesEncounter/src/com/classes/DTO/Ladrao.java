package com.classes.DTO;

import com.classes.Enums.TipoIA;

public class Ladrao extends Inimigo {

    public Ladrao() {
        super();
        
      
        this.setHpMax(40);
        this.setHp(this.getHpMax());
        this.setNome("Ladrão de Estrada");
        
        this.setAtaque(12);
        this.setDefesa(5);
        this.setRecompensaOuro(35);
        this.setTipoIA(TipoIA.AGRESSIVO); 
    }
}