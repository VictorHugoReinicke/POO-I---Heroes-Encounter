package com.classes.DTO;

import com.classes.Enums.TipoIA;

public class Chefe extends Inimigo {

    public Chefe() {
        super();
        
         
        this.setHpMax(300);
        this.setHp(this.getHpMax());
        this.setNome("Lorde do Abismo");
        
        
        this.setAtaque(35); 
        this.setDefesa(25); 
        this.setRecompensaOuro(100);
        this.setTipoIA(TipoIA.CHEFE); 
    }
}