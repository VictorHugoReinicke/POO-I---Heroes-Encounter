package com.classes.BO;

import java.util.List;
import com.classes.DAO.ShopItemDAO;
import com.classes.DTO.ShopItem;

public class ShopItemBO {

	private ShopItemDAO shopItemDAO;

	public ShopItemBO() {
		this.shopItemDAO = new ShopItemDAO();
	}

	public boolean inserir(ShopItem shopItem) {
		if (shopItemDAO.existe(shopItem)) {
			System.out.println("⚠️ Item (ID: " + shopItem.getIdItem() + ") já está registrado na Loja (ID: "
					+ shopItem.getIdShop() + ").");
			return false;
		}
		return shopItemDAO.inserir(shopItem);
	}

	public boolean alterar(ShopItem shopItem) {
		return shopItemDAO.alterar(shopItem);
	}

	public boolean excluir(ShopItem shopItem) {
		return shopItemDAO.excluir(shopItem);
	}

	public ShopItem procurarRegistro(int idShop, int idItem) {
		return shopItemDAO.procurarRegistro(idShop, idItem);
	}

	public List<ShopItem> listarItensPorShop(int idShop) {
		return shopItemDAO.listarItensPorShop(idShop);
	}

}
