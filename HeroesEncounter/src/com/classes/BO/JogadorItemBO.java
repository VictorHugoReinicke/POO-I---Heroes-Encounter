package com.classes.BO;

import com.classes.DAO.JogadorItemDAO;
import com.classes.DTO.JogadorItem;
import com.classes.DTO.ItemArma;       // Necessário para lógica de equipar
import com.classes.DTO.Item;

import java.util.List;

public class JogadorItemBO {

	private JogadorItemDAO jogadorItemDAO;
	

	public JogadorItemBO() {
		this.jogadorItemDAO = new JogadorItemDAO();
	}
	

    public boolean adicionarItem(int idJogador, Item item, int quantidade) {
        
        JogadorItem registroExistente = jogadorItemDAO.procurarRegistro(idJogador, item.getId());
        
        if (registroExistente != null) {
            int novaQuantidade = registroExistente.getQuantidade() + quantidade;
            registroExistente.setQuantidade(novaQuantidade);
            
            System.out.printf("Inventário: Adicionado %d de '%s'. Nova quantidade: %d.\n",
                quantidade, item.getNome(), novaQuantidade);
            
            return jogadorItemDAO.alterar(registroExistente);

        } else {
            JogadorItem novoRegistro = new JogadorItem(idJogador, item.getId(), quantidade, false);
            
            System.out.printf("Inventário: Inserido novo item '%s' (x%d).\n",
                item.getNome(), quantidade);
            
            return jogadorItemDAO.inserir(novoRegistro);
        }
    }

    public boolean removerItem(int idJogador, Item item, int quantidade) {
        
        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());
        
        if (registro == null) {
            System.out.println("Inventário: Jogador não possui o item " + item.getNome() + ".");
            return false;
        }

        int novaQuantidade = registro.getQuantidade() - quantidade;

        if (novaQuantidade <= 0) {
            System.out.println("Inventário: Item " + item.getNome() + " esgotado. Registro excluído.");
            return jogadorItemDAO.excluir(registro);
        } else {
            registro.setQuantidade(novaQuantidade);
            System.out.printf("Inventário: Removido %d de '%s'. Nova quantidade: %d.\n",
                quantidade, item.getNome(), novaQuantidade);
            return jogadorItemDAO.alterar(registro);
        }
    }

    public boolean equiparItem(int idJogador, Item item) {

        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());

        if (registro == null) {
             System.out.println("Erro: Item não encontrado no inventário.");
             return false;
        }
        
        if (registro.isEquipado()) {
            System.out.println("Item já está equipado.");
            return true; 
        }

        if (item instanceof ItemArma) {
            this.desequiparItemPorTipo(idJogador, ItemArma.class);
        } 

        registro.setEquipado(true);
        System.out.printf("Item '%s' equipado com sucesso.\n", item.getNome());
        return jogadorItemDAO.alterar(registro);
    }
    

    private void desequiparItemPorTipo(int idJogador, Class<? extends Item> tipoClasse) {
        List<JogadorItem> inventario = jogadorItemDAO.listarItensPorJogador(idJogador);
        
        for (JogadorItem ji : inventario) {
            // Verifica se o item está equipado e se é do tipo que queremos desequipar
            if (ji.isEquipado() && tipoClasse.isInstance(ji.getItem())) {
                ji.setEquipado(false);
                jogadorItemDAO.alterar(ji);
                System.out.printf("  -> Desequipado o item anterior: %s\n", ji.getItem().getNome());
            }
        }
    }

    public boolean desequiparItem(int idJogador, Item item) {
        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());

        if (registro == null || !registro.isEquipado()) {
             System.out.println("Item não estava equipado ou não existe no inventário.");
             return false;
        }

        registro.setEquipado(false);
        System.out.printf("Item '%s' desequipado com sucesso.\n", item.getNome());
        return jogadorItemDAO.alterar(registro);
    }


    public boolean usarItem(int idJogador, int idItem) {
        try {
            JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, idItem);
            
            if (registro == null) {
                System.out.println("Item não encontrado no inventário do jogador");
                return false;
            }
            
            if (registro.getQuantidade() <= 0) {
                System.out.println("Item esgotado no inventário");
                return false;
            }
            
            int novaQuantidade = registro.getQuantidade() - 1;
            
            if (novaQuantidade <= 0) {
                System.out.println("Item esgotado e removido do inventário");
                return jogadorItemDAO.excluir(registro);
            } else {
                registro.setQuantidade(novaQuantidade);
                System.out.printf("Item usado. Nova quantidade: %d\n", novaQuantidade);
                return jogadorItemDAO.alterar(registro);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao usar item: " + e.getMessage());
            return false;
        }
    }

    public JogadorItem procurarRegistro(int idJogador, int idItem) {
        return jogadorItemDAO.procurarRegistro(idJogador, idItem);
    }

    public List<JogadorItem> listarItensPorJogador(int idJogador) {
        return jogadorItemDAO.listarItensPorJogador(idJogador);
    }
    
    public boolean alterar(JogadorItem jogadorItem) {
        return jogadorItemDAO.alterar(jogadorItem);
    }
    
    public boolean existe(int idJogador, int idItem) {
        return jogadorItemDAO.existe(idJogador, idItem);
    }
}