package com.classes.DTO;

public class ShopItem {

	private int idShop;
	private int idItem;

	private int precoVenda;
	private int quantidade;

	private Item item;
	private Shop shop;

	public ShopItem(int idShop, int idItem, int precoVenda, int quantidade) {
		this.idShop = idShop;
		this.idItem = idItem;
		this.precoVenda = precoVenda;
		this.quantidade = quantidade;
	}

	public ShopItem() {
	}

	public int getIdShop() {
		return idShop;
	}

	public void setIdShop(int idShop) {
		this.idShop = idShop;
	}

	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public int getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(int precoVenda) {
		this.precoVenda = precoVenda;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
