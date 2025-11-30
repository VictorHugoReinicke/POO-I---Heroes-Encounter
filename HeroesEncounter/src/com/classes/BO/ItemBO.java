package com.classes.BO;

import com.classes.DTO.Item;
import com.classes.DAO.ItemDAO;

public class ItemBO {

	private ItemDAO itemDAO;

	public ItemBO() {
		this.itemDAO = new ItemDAO();
	}

	public boolean inserir(Item item) {

		if (itemDAO.existe(item)) {
			System.out.println("⚠️ Item '" + item.getNome() + "' já existe. Inserção cancelada.");
			return false;
		}

		return itemDAO.inserir(item);
	}

	public boolean existe(Item item) {
		return itemDAO.existe(item);
	}

	public boolean alterar(Item item) {
		return itemDAO.alterar(item);
	}

	public boolean excluir(Item item) {
		return itemDAO.excluir(item);
	}

	public Item procurarPorCodigo(int id) {
		return itemDAO.procurarPorCodigo(id);
	}

	public Item procurarPorNome(Item item) {
		return itemDAO.procurarPorNome(item);
	}
	
	public Item procurarPorNome(String nome) {
	    return itemDAO.procurarPorNome(nome);
	}
}