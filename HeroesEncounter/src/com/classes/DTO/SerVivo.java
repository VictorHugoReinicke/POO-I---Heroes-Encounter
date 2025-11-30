package com.classes.DTO;
import java.util.List;

public abstract class SerVivo implements IAtacavel {
	
	private int id;
	private String nome;
	private int hp;
	private int hpMax;
	private int mana;
	private int manaMax;
	private int ataque;
	private int defesa;
	private double esquiva;
	private int idStatus;
	List<Status> statusAtivo;
	

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}
	
	public int getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getManaMax() {
		return manaMax;
	}

	public void setManaMax(int manaMax) {
		this.manaMax = manaMax;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefesa() {
		return defesa;
	}

	public void setDefesa(int defesa) {
		this.defesa = defesa;
	}

	public double getEsquiva() {
		return esquiva;
	}

	public void setEsquiva(double esquiva) {
		this.esquiva = esquiva;
	}

	public List<Status> getStatusAtivo() {
		return statusAtivo;
	}

	public void setStatusAtivo(List<Status> statusAtivo) {
		this.statusAtivo = statusAtivo;
	}
	
	public void receberDano(int dano) {
		
	}
	
	public boolean esquivar() {
		return true;
	}
	
	public void curar() {
		
	}
	
	public void aplicarStatus() {}
	
	public void removerStatus() {}
	
	public boolean estaVivo() {
		return true;
	}
	
	@Override
	public void atacar(SerVivo alvo) {
		
	}

}
