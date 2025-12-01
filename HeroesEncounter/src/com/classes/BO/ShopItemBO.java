package com.classes.BO;

import java.util.List;
import com.classes.DAO.ShopItemDAO;
import com.classes.DTO.Item;
import com.classes.DTO.JogadorItem;
import com.classes.DTO.ShopItem;

public class ShopItemBO {

    private ShopItemDAO shopItemDAO;
    private JogadorItemBO jogadorItemBO;
    private ItemBO itemBO;
    
    public ShopItemBO() {
        this.shopItemDAO = new ShopItemDAO();
        this.jogadorItemBO = new JogadorItemBO();
        this.itemBO = new ItemBO();
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

    /**
     * ✅ MODIFICADO: Comprar um item do shop (estoque individual por jogador)
     */
    public boolean comprarItem(int idShop, int idItem, int jogadorId) {
        try {
            // Buscar o item do shop
            ShopItem shopItem = shopItemDAO.procurarRegistro(idShop, idItem);
            if (shopItem == null) {
                System.out.println("❌ Item não encontrado no shop");
                return false;
            }

            // Buscar o objeto Item completo
            Item item = shopItem.getItem();
            if (item == null) {
                // Fallback: buscar pelo ItemBO caso shopItem.getItem() retorne null
                item = itemBO.procurarPorCodigo(idItem);
                if (item == null) {
                    System.out.println("❌ Item base não encontrado");
                    return false;
                }
            }

            // ✅ VERIFICAR LIMITE POR JOGADOR (individual)
            JogadorItem itemExistente = jogadorItemBO.procurarRegistro(jogadorId, idItem);
            int quantidadeAtual = (itemExistente != null) ? itemExistente.getQuantidade() : 0;
            int limitePorJogador = 10; // Máximo 10 do mesmo item por jogador
            
            if (quantidadeAtual >= limitePorJogador) {
                System.out.println("❌ Jogador atingiu o limite deste item: " + quantidadeAtual + "/" + limitePorJogador);
                return false;
            }

            // ✅ ADICIONAR AO INVENTÁRIO DO JOGADOR (sem diminuir estoque global)
            boolean itemAdicionado = jogadorItemBO.adicionarItem(jogadorId, item, 1);
            
            if (!itemAdicionado) {
                System.out.println("❌ Erro ao adicionar item ao jogador");
                return false;
            }

            System.out.println("✅ Compra realizada! Jogador agora tem " + (quantidadeAtual + 1) + " unidades");
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao comprar item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ NOVO: Verificar se jogador pode comprar mais deste item
     */
    public boolean jogadorPodeComprar(int jogadorId, int idItem) {
        try {
            JogadorItem itemExistente = jogadorItemBO.procurarRegistro(jogadorId, idItem);
            int quantidadeAtual = (itemExistente != null) ? itemExistente.getQuantidade() : 0;
            return quantidadeAtual < 10; // Limite de 10 por jogador
        } catch (Exception e) {
            System.err.println("Erro ao verificar limite do jogador: " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ NOVO: Obter quantidade atual do jogador
     */
    public int getQuantidadeJogador(int jogadorId, int idItem) {
        try {
            JogadorItem itemExistente = jogadorItemBO.procurarRegistro(jogadorId, idItem);
            return (itemExistente != null) ? itemExistente.getQuantidade() : 0;
        } catch (Exception e) {
            System.err.println("Erro ao obter quantidade do jogador: " + e.getMessage());
            return 0;
        }
    }
}