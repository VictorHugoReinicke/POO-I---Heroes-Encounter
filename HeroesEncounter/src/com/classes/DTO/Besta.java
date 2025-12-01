package com.classes.DTO;

import com.classes.Enums.TipoIA;

public class Besta extends Inimigo {

    public Besta() {
        super();
        
        this.setHpMax(70);      
        this.setHp(this.getHpMax());
        this.setNome("Besta Selvagem");
        
        this.setAtaque(8);
        this.setDefesa(15);
        
        this.setRecompensaOuro(10);
        this.setTipoIA(TipoIA.BALANCEADO); 
    }
}