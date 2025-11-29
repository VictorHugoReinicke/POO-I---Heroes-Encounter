package com.classes.DTO;

import java.util.List;

public class Shop {

	private List<Item> itensVenda;
	private int precoMinimo;
	private int precoMaximo;

	public List<Item> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<Item> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public int getPrecoMinimo() {
		return precoMinimo;
	}

	public void setPrecoMinimo(int precoMinimo) {
		this.precoMinimo = precoMinimo;
	}

	public int getPrecoMaximo() {
		return precoMaximo;
	}

	public void setPrecoMaximo(int precoMaximo) {
		this.precoMaximo = precoMaximo;
	}

	public void listarItens() {
	}

	public boolean venderParaJogador(Jogador jogador, Item item) {

		return true;

	}

}
