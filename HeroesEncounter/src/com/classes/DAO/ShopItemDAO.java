package com.classes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.classes.Conexao.Conexao;
import com.classes.DTO.ShopItem;

public class ShopItemDAO {

	final String NOMEDATABELA = "ShopItem";
	private ItemDAO itemDAO = new ItemDAO();
	private ShopDAO shopDAO = new ShopDAO();

	public boolean inserir(ShopItem shopItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "INSERT INTO " + NOMEDATABELA + " (idShop, idItem, preco_venda, quantidade, quantidade_disponivel) VALUES (?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, shopItem.getIdShop());
			ps.setInt(2, shopItem.getIdItem());
			ps.setInt(3, shopItem.getPrecoVenda());
			ps.setInt(4, shopItem.getQuantidade());
			ps.setInt(5, shopItem.getQuantidadeDisponivel());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean excluir(ShopItem shopItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "DELETE FROM " + NOMEDATABELA + " WHERE idShop = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, shopItem.getIdShop());
			ps.setInt(2, shopItem.getIdItem());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean alterar(ShopItem shopItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "UPDATE " + NOMEDATABELA + " SET preco_venda = ?, quantidade = ?, quantidade_disponivel = ?" + " WHERE idShop = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, shopItem.getPrecoVenda());
			ps.setInt(2, shopItem.getQuantidade());
			ps.setInt(3, shopItem.getQuantidadeDisponivel());
			ps.setInt(4, shopItem.getIdShop());
			ps.setInt(5, shopItem.getIdItem());

			ps.executeUpdate();
			ps.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ShopItem procurarRegistro(int idShop, int idItem) {
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idShop, idItem, preco_venda, quantidade, quantidade_disponivel FROM " + NOMEDATABELA + " WHERE idShop = ? AND idItem = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idShop);
			ps.setInt(2, idItem);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				ShopItem obj = montarObjeto(rs);
				ps.close();
				rs.close();
				conn.close();
				return obj;
			} else {
				ps.close();
				rs.close();
				conn.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ShopItem> listarItensPorShop(int idShop) {
		List<ShopItem> lista = new ArrayList<>();
		try {
			Connection conn = Conexao.conectar();
			String sql = "SELECT idShop, idItem, preco_venda, quantidade, quantidade_disponivel FROM " + NOMEDATABELA + " WHERE idShop = ? AND quantidade_disponivel > 0 ORDER BY idItem;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idShop);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ShopItem obj = montarObjeto(rs);
				lista.add(obj);
			}

			ps.close();
			rs.close();
			conn.close();
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private ShopItem montarObjeto(ResultSet rs) throws Exception {
		ShopItem obj = new ShopItem();
		int idShop = rs.getInt("idShop");
		int idItem = rs.getInt("idItem");

		obj.setIdShop(idShop);
		obj.setIdItem(idItem);
		obj.setPrecoVenda(rs.getInt("preco_venda"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));

		obj.setShop(shopDAO.procurarPorCodigo(idShop));
		obj.setItem(itemDAO.procurarPorCodigo(idItem));

		return obj;
	}

	public boolean existe(ShopItem shopItem) {
		return procurarRegistro(shopItem.getIdShop(), shopItem.getIdItem()) != null;
	}

}
