package com.classes.DTO;

public class Status {

	private String nome;
	private int duracaoTurnos;
	
	private int efeitoAtaque;
	private int efeitoDefesa;
	private int efeitoCura;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getDuracaoTurnos() {
		return duracaoTurnos;
	}

	public void setDuracaoTurnos(int duracaoTurnos) {
		this.duracaoTurnos = duracaoTurnos;
	}

	public int getEfeitoAtaque() {
		return efeitoAtaque;
	}

	public void setEfeitoAtaque(int efeitoAtaque) {
		this.efeitoAtaque = efeitoAtaque;
	}

	public int getEfeitoDefesa() {
		return efeitoDefesa;
	}

	public void setEfeitoDefesa(int efeitoDefesa) {
		this.efeitoDefesa = efeitoDefesa;
	}

	public int getEfeitoCura() {
		return efeitoCura;
	}

	public void setEfeitoCura(int efeitoCura) {
		this.efeitoCura = efeitoCura;
	}

	public void aplicarEfeito(SerVivo alvo) {
		if (this.duracaoTurnos <= 0) {
            return; 
        }

        if (this.efeitoCura != 0) {
            alvo.setHp(alvo.getHp() + this.efeitoCura);
        }
        
        this.duracaoTurnos--;
    }
	
}
