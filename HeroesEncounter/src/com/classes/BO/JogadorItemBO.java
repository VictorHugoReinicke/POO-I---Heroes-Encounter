package com.classes.BO;

import com.classes.DAO.JogadorItemDAO;
import com.classes.DTO.JogadorItem;
import com.classes.DTO.ItemArma;       // Necessário para lógica de equipar
import com.classes.DTO.Item;

import java.util.List;

public class JogadorItemBO {

	private JogadorItemDAO jogadorItemDAO;
	
    // Note: Em cenários reais, o JogadorBO e ItemBO seriam injetados aqui.
    // Para simplificar, focaremos apenas na lógica de inventário.

	public JogadorItemBO() {
		this.jogadorItemDAO = new JogadorItemDAO();
	}
	
    // ------------------------------------------------------------------
    // --- 1. LÓGICA DE NEGÓCIO PRINCIPAL: ADICIONAR E REMOVER ---
    // ------------------------------------------------------------------

    /**
     * Adiciona um item ao inventário.
     * Se o jogador já possui o item, a quantidade é atualizada.
     * Se não possui, um novo registro é inserido.
     */
    public boolean adicionarItem(int idJogador, Item item, int quantidade) {
        
        // 1. Tenta encontrar um registro existente (DTO que representa a linha no DB)
        JogadorItem registroExistente = jogadorItemDAO.procurarRegistro(idJogador, item.getId());
        
        if (registroExistente != null) {
            // O item já existe, apenas atualiza a quantidade
            int novaQuantidade = registroExistente.getQuantidade() + quantidade;
            registroExistente.setQuantidade(novaQuantidade);
            
            System.out.printf("✅ Inventário: Adicionado %d de '%s'. Nova quantidade: %d.\n", 
                quantidade, item.getNome(), novaQuantidade);
            
            return jogadorItemDAO.alterar(registroExistente);

        } else {
            // Item novo, insere um novo registro
            // Item novo nunca começa equipado, a menos que seja forçado
            JogadorItem novoRegistro = new JogadorItem(idJogador, item.getId(), quantidade, false);
            
            System.out.printf("✅ Inventário: Inserido novo item '%s' (x%d).\n", 
                item.getNome(), quantidade);
            
            return jogadorItemDAO.inserir(novoRegistro);
        }
    }

    /**
     * Remove uma quantidade de item do inventário.
     * Se a quantidade chegar a zero ou menos, o registro é excluído.
     */
    public boolean removerItem(int idJogador, Item item, int quantidade) {
        
        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());
        
        if (registro == null) {
            System.out.println("⚠️ Inventário: Jogador não possui o item " + item.getNome() + ".");
            return false;
        }

        int novaQuantidade = registro.getQuantidade() - quantidade;

        if (novaQuantidade <= 0) {
            // Exclui o registro se a quantidade for esgotada
            System.out.println("✅ Inventário: Item " + item.getNome() + " esgotado. Registro excluído.");
            return jogadorItemDAO.excluir(registro);
        } else {
            // Atualiza a quantidade
            registro.setQuantidade(novaQuantidade);
            System.out.printf("✅ Inventário: Removido %d de '%s'. Nova quantidade: %d.\n", 
                quantidade, item.getNome(), novaQuantidade);
            return jogadorItemDAO.alterar(registro);
        }
    }


    // ------------------------------------------------------------------
    // --- 2. LÓGICA DE NEGÓCIO: EQUIPAR/DESEQUIPAR ---
    // ------------------------------------------------------------------

    /**
     * Equipa um item no jogador. 
     * Implementa a regra: só pode haver 1 item equipado por "slot" (ex: 1 Arma).
     */
    public boolean equiparItem(int idJogador, Item item) {
        
        // 1. Busca o registro do item no inventário
        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());

        if (registro == null) {
             System.out.println("❌ Erro: Item não encontrado no inventário.");
             return false;
        }
        
        // 2. Verifica se o item já está equipado
        if (registro.isEquipado()) {
            System.out.println("⚠️ Item já está equipado.");
            return true; 
        }

        // 3. LÓGICA DE SLOT: Se for uma Arma ou Defesa, precisa desequipar o antigo
        if (item instanceof ItemArma) {
            // Desequipa qualquer outra arma antes de equipar a nova
            this.desequiparItemPorTipo(idJogador, ItemArma.class);
        } 
        // Você adicionaria lógica semelhante para ItemDefesa, etc.
        
        // 4. Equipa o item e salva no DB
        registro.setEquipado(true);
        System.out.printf("✅ Item '%s' equipado com sucesso.\n", item.getNome());
        return jogadorItemDAO.alterar(registro);
    }
    
    /**
     * Método auxiliar para desequipar um item de um tipo específico (ex: todas as Armas).
     */
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
    
    /**
     * Desequipa um item específico.
     */
    public boolean desequiparItem(int idJogador, Item item) {
        JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, item.getId());

        if (registro == null || !registro.isEquipado()) {
             System.out.println("⚠️ Item não estava equipado ou não existe no inventário.");
             return false;
        }

        registro.setEquipado(false);
        System.out.printf("✅ Item '%s' desequipado com sucesso.\n", item.getNome());
        return jogadorItemDAO.alterar(registro);
    }


    public boolean usarItem(int idJogador, int idItem) {
        try {
            JogadorItem registro = jogadorItemDAO.procurarRegistro(idJogador, idItem);
            
            if (registro == null) {
                System.out.println("❌ Item não encontrado no inventário do jogador");
                return false;
            }
            
            if (registro.getQuantidade() <= 0) {
                System.out.println("❌ Item esgotado no inventário");
                return false;
            }
            
            // Remove 1 unidade
            int novaQuantidade = registro.getQuantidade() - 1;
            
            if (novaQuantidade <= 0) {
                // Remove o registro se acabou
                System.out.println("✅ Item esgotado e removido do inventário");
                return jogadorItemDAO.excluir(registro);
            } else {
                // Atualiza a quantidade
                registro.setQuantidade(novaQuantidade);
                System.out.printf("✅ Item usado. Nova quantidade: %d\n", novaQuantidade);
                return jogadorItemDAO.alterar(registro);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao usar item: " + e.getMessage());
            return false;
        }
    }
    
    // ------------------------------------------------------------------
    // --- 3. MÉTODOS DE BUSCA E DELEGAÇÃO ---
    // ------------------------------------------------------------------

    public JogadorItem procurarRegistro(int idJogador, int idItem) {
        return jogadorItemDAO.procurarRegistro(idJogador, idItem);
    }

    public List<JogadorItem> listarItensPorJogador(int idJogador) {
        return jogadorItemDAO.listarItensPorJogador(idJogador);
    }
    
    // Delegações simples (usar com cautela, a lógica acima é preferível)
    public boolean alterar(JogadorItem jogadorItem) {
        return jogadorItemDAO.alterar(jogadorItem);
    }
    
    public boolean existe(int idJogador, int idItem) {
        return jogadorItemDAO.existe(idJogador, idItem);
    }
}