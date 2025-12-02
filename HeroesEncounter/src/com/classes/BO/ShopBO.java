package com.classes.BO;

import com.classes.DTO.Shop;
import com.classes.DTO.Item;
import com.classes.DTO.Jogador;
import com.classes.DTO.ShopItem; // Necessário para a lógica de estoque
import com.classes.DAO.ShopDAO;

import java.util.List;

public class ShopBO {

	private ShopDAO shopDAO;
	private ShopItemBO shopItemBO;

	public ShopBO() {
		super();
		this.shopDAO = new ShopDAO();
		this.shopItemBO = new ShopItemBO();
	}
	
	public boolean inserir(Shop shop) {
        if (shopDAO.existe(shop)) {
            System.out.println("Loja '" + shop.getNome() + "' já existe. Inserção cancelada.");
            return false;
        }
        return shopDAO.inserir(shop);
    }
	
	public Shop procurarPorCodigo(int id) {
        return shopDAO.procurarPorCodigo(id);
    }
	
	public Shop procurarPorNome(Shop shop) {
        return shopDAO.procurarPorNome(shop);
    }

	public void listarItens(Shop shop) {
        System.out.println("--- ESTOQUE DA LOJA: " + shop.getNome().toUpperCase() + " ---");
        
        List<ShopItem> estoque = shopItemBO.listarItensPorShop(shop.getId());

        if (estoque.isEmpty()) {
            System.out.println("Nenhum item em estoque nesta loja.");
            return;
        }

        for (ShopItem si : estoque) {
            Item item = si.getItem();
            
            if (item != null) {
                String tipo = item.getClass().getSimpleName().replace("Item", ""); // Ex: ItemArma -> Arma
                
                System.out.printf("  [ID: %d] %-20s | Tipo: %-10s | Preço: %-4d GOLD | Estoque: %d\n", 
                    item.getId(), 
                    item.getNome(), 
                    tipo,
                    si.getPrecoVenda(), 
                    si.getQuantidade()
                );
            }
        }
        System.out.println("----------------------------------------------");
    }
	
	public boolean existe(Shop shop) {
        return shopDAO.existe(shop); 
    }

	public boolean venderParaJogador(Jogador jogador, Item item, Shop shop) {
        
		
        ShopItem registro = shopItemBO.procurarRegistro(shop.getId(), item.getId());
        
        if (registro == null || registro.getQuantidade() <= 0) {
            System.out.println(" - " + item.getNome() + " indisponível ou esgotado na loja " + shop.getNome() + ".");
            return false;
        }

        
        if (jogador.getOuro() < registro.getPrecoVenda()) {
            System.out.printf("Gold insuficiente. Você tem %d, mas o preço é %d.\n", jogador.getOuro(), registro.getPrecoVenda());
            return false;
        }
        registro.setQuantidade(registro.getQuantidade() - 1);
        boolean sucessoUpdateEstoque = shopItemBO.alterar(registro);
        
        if (sucessoUpdateEstoque) {
            System.out.printf("Venda de %s para %s concluída! Preço: %d GOLD. Novo estoque: %d.\n",
                item.getNome(), jogador.getNome(), registro.getPrecoVenda(), registro.getQuantidade());
            return true;
        } else {
            
            System.out.println("Falha na atualização do estoque (erro no DB). Venda cancelada.");
            return false;
        }
    }
	
}