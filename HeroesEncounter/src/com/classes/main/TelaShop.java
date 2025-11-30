package com.classes.main;

import com.classes.DTO.*;
import com.classes.BO.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaShop extends JDialog {
    private Jogador jogador;
    private TelaAventura telaAventura;
    private ShopBO shopBO;
    private ShopItemBO shopItemBO;
    private JogadorItemBO jogadorItemBO;
    private ItemBO itemBO;
    
    // Componentes da interface
    private JLabel lblOuro;
    private JTextArea txtDetalhes;
    private JButton btnComprar;
    private JButton btnVoltar;
    private JList<String> listaItens;
    private DefaultListModel<String> listModel;
    private List<ShopItem> itensShop;
    private Shop shop;
    
    public TelaShop(TelaAventura pai, Jogador jogador) {
        super(pai, "Shop - Loja Central", true);
        this.telaAventura = pai;
        this.jogador = jogador;
        this.shopBO = new ShopBO();
        this.shopItemBO = new ShopItemBO();
        this.jogadorItemBO = new JogadorItemBO();
        this.itemBO = new ItemBO();
        initializeTela();
        carregarItensShop();
    }
    
    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Painel de título
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(new Color(30, 30, 70));
        
        JLabel titulo = new JLabel("🛒 SHOP - LOJA CENTRAL", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo, BorderLayout.CENTER);
        
        // Painel de ouro
        JPanel ouroPanel = new JPanel();
        ouroPanel.setBackground(new Color(30, 30, 70));
        lblOuro = new JLabel("💰 Ouro: " + jogador.getOuro());
        lblOuro.setFont(new Font("Arial", Font.BOLD, 14));
        lblOuro.setForeground(Color.YELLOW);
        ouroPanel.add(lblOuro);
        tituloPanel.add(ouroPanel, BorderLayout.EAST);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel da lista de itens
        JPanel listaPanel = new JPanel(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createTitledBorder("Itens Disponíveis na Loja"));
        
        listModel = new DefaultListModel<>();
        listaItens = new JList<>(listModel);
        listaItens.setFont(new Font("Arial", Font.PLAIN, 12));
        listaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollLista = new JScrollPane(listaItens);
        listaPanel.add(scrollLista, BorderLayout.CENTER);
        
        // Painel de detalhes
        JPanel detalhesPanel = new JPanel(new BorderLayout());
        detalhesPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Item"));
        
        txtDetalhes = new JTextArea(10, 30);
        txtDetalhes.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtDetalhes.setEditable(false);
        txtDetalhes.setBackground(new Color(240, 240, 240));
        txtDetalhes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
        detalhesPanel.add(scrollDetalhes, BorderLayout.CENTER);
        
        mainPanel.add(listaPanel);
        mainPanel.add(detalhesPanel);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout());
        btnComprar = criarBotao("🛒 COMPRAR ITEM", new Color(50, 150, 50));
        btnVoltar = criarBotao("↩️ VOLTAR", new Color(100, 100, 100));
        
        btnComprar.addActionListener(e -> comprarItem());
        btnVoltar.addActionListener(e -> dispose());
        
        botoesPanel.add(btnComprar);
        botoesPanel.add(btnVoltar);
        
        // Listener para seleção na lista
        listaItens.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                exibirDetalhesItem();
            }
        });
        
        add(tituloPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(180, 40));
        
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
                botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
                botao.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return botao;
    }
    
    private void carregarItensShop() {
        try {
            // Buscar a loja principal
            shop = shopBO.procurarPorNome(new Shop("Loja Central"));
            if (shop == null) {
                JOptionPane.showMessageDialog(this, "Loja não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar itens da loja
            itensShop = shopItemBO.listarItensPorShop(shop.getId());
            
            if (itensShop == null || itensShop.isEmpty()) {
                listModel.addElement("🏪 Nenhum item disponível na loja");
                btnComprar.setEnabled(false);
                return;
            }
            
            // Carregar itens na lista
            for (ShopItem shopItem : itensShop) {
                Item itemCompleto = itemBO.procurarPorCodigo(shopItem.getIdItem());
                if (itemCompleto != null) {
                    shopItem.setItem(itemCompleto);
                    
                    // ✅ VERIFICAR QUANTIDADE DO JOGADOR
                    int quantidadeJogador = shopItemBO.getQuantidadeJogador(jogador.getId(), shopItem.getIdItem());
                    
                    String info = String.format("%s - %d ouro (Você tem: %d/10)", 
                        itemCompleto.getNome(), 
                        shopItem.getPrecoVenda(), 
                        quantidadeJogador);
                    listModel.addElement(info);
                }
            }
            
            if (!itensShop.isEmpty()) {
                listaItens.setSelectedIndex(0);
            }
            
        } catch (Exception e) {
            listModel.addElement("❌ Erro ao carregar itens da loja");
            btnComprar.setEnabled(false);
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar itens: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void exibirDetalhesItem() {
        int selectedIndex = listaItens.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < itensShop.size()) {
            ShopItem shopItem = itensShop.get(selectedIndex);
            Item item = shopItem.getItem();
            
            if (item == null) {
                txtDetalhes.setText("❌ Erro: Item não encontrado");
                btnComprar.setEnabled(false);
                return;
            }
            
            // ✅ OBTER QUANTIDADE ATUAL DO JOGADOR
            int quantidadeJogador = shopItemBO.getQuantidadeJogador(jogador.getId(), shopItem.getIdItem());
            boolean podeComprar = shopItemBO.jogadorPodeComprar(jogador.getId(), shopItem.getIdItem());
            
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("📦 NOME: ").append(item.getNome()).append("\n");
            detalhes.append("💰 PREÇO: ").append(shopItem.getPrecoVenda()).append(" ouro\n");
            detalhes.append("🎒 SUA QUANTIDADE: ").append(quantidadeJogador).append("/10 unidades\n");
            detalhes.append("✅ STATUS: ").append(podeComprar ? "Pode comprar" : "LIMITE ATINGIDO").append("\n");
            detalhes.append("📋 TIPO: ").append(item.getTipoItem()).append("\n\n");
            
            // Detalhes específicos por tipo de item
            if (item instanceof ItemArma) {
                ItemArma arma = (ItemArma) item;
                detalhes.append("⚔️ ARMA DE COMBATE\n");
                detalhes.append("• Bônus Dano: +").append(arma.getBonusDano()).append("\n");
                detalhes.append("• Bônus Mágico: +").append(arma.getBonusMagico()).append("\n");
                detalhes.append("• Chance Crítico: ").append(arma.getBonusCritico()).append("%\n");
                
            } else if (item instanceof ItemDefesa) {
                ItemDefesa defesa = (ItemDefesa) item;
                detalhes.append("🛡️ EQUIPAMENTO DE DEFESA\n");
                detalhes.append("• Bônus Defesa: +").append(defesa.getBonusDefesa()).append("\n");
                detalhes.append("• Bônus Esquiva: +").append(defesa.getBonusEsquiva()).append("%\n");
                
            } else if (item instanceof ItemConsumivel) {
                ItemConsumivel consumivel = (ItemConsumivel) item;
                detalhes.append("🧪 ITEM CONSUMÍVEL\n");
                if (consumivel.getCura() > 0) {
                    detalhes.append("• Cura: +").append(consumivel.getCura()).append(" HP\n");
                }
                if (consumivel.getMana() > 0) {
                    detalhes.append("• Mana: +").append(consumivel.getMana()).append(" MP\n");
                }
            }
            
            detalhes.append("\n💳 SEU OURO: ").append(jogador.getOuro());
            
            txtDetalhes.setText(detalhes.toString());
            txtDetalhes.setCaretPosition(0);
            btnComprar.setEnabled(podeComprar && jogador.getOuro() >= shopItem.getPrecoVenda());
        }
    }
    
    private void comprarItem() {
        int selectedIndex = listaItens.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= itensShop.size()) {
            JOptionPane.showMessageDialog(this, "Selecione um item para comprar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ShopItem shopItem = itensShop.get(selectedIndex);
        Item item = shopItem.getItem();
        
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Erro: Item não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ✅ VERIFICAR SE JOGADOR PODE COMPRAR MAIS
        int quantidadeAtual = shopItemBO.getQuantidadeJogador(jogador.getId(), shopItem.getIdItem());
        boolean podeComprar = shopItemBO.jogadorPodeComprar(jogador.getId(), shopItem.getIdItem());
        
        if (!podeComprar) {
            JOptionPane.showMessageDialog(this, 
                "Você já tem o máximo permitido deste item!\n" +
                "Quantidade atual: " + quantidadeAtual + "/10",
                "Limite Atingido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar ouro suficiente
        if (jogador.getOuro() < shopItem.getPrecoVenda()) {
            JOptionPane.showMessageDialog(this, 
                "Ouro insuficiente!\n\n" +
                "Preço: " + shopItem.getPrecoVenda() + " ouro\n" +
                "Seu ouro: " + jogador.getOuro(), 
                "Ouro Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirmar compra
        int confirm = JOptionPane.showConfirmDialog(this,
            "CONFIRMAR COMPRA\n\n" +
            "Item: " + item.getNome() + "\n" +
            "Preço: " + shopItem.getPrecoVenda() + " ouro\n" +
            "Quantidade atual: " + quantidadeAtual + "/10\n" +
            "Nova quantidade: " + (quantidadeAtual + 1) + "/10\n\n" +
            "Deseja continuar?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Processar compra usando o ShopItemBO
                boolean sucesso = shopItemBO.comprarItem(shop.getId(), shopItem.getIdItem(), jogador.getId());
                
                if (sucesso) {
                    // Debitar ouro do jogador
                    jogador.setOuro(jogador.getOuro() - shopItem.getPrecoVenda());
                    
                    // ✅ ATUALIZAR INVENTÁRIO LOCAL
                    List<JogadorItem> inventarioAtual = jogadorItemBO.listarItensPorJogador(jogador.getId());
                    jogador.setInventario(inventarioAtual);
                    
                    // Atualizar interface
                    atualizarInterface();
                    
                    // Mensagem de sucesso
                    JOptionPane.showMessageDialog(this,
                        "✅ COMPRA REALIZADA COM SUCESSO!\n\n" +
                        "Item: " + item.getNome() + "\n" +
                        "Custo: " + shopItem.getPrecoVenda() + " ouro\n" +
                        "Quantidade: " + (quantidadeAtual + 1) + "/10 unidades\n" +
                        "Ouro restante: " + jogador.getOuro(),
                        "Compra Bem-sucedida",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Atualizar log na aventura
                    if (telaAventura != null) {
                        telaAventura.adicionarLog("🛒 Comprou " + item.getNome() + " por " + shopItem.getPrecoVenda() + " ouro");
                    }
                } else {
                    throw new Exception("Falha ao processar compra no sistema");
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ ERRO NA COMPRA\n\n" +
                    "Não foi possível completar a compra.\n" +
                    "Erro: " + e.getMessage(),
                    "Erro na Compra",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void atualizarInterface() {
        // Atualizar ouro
        lblOuro.setText("💰 Ouro: " + jogador.getOuro());
        
        // Recarregar itens da loja
        listModel.clear();
        carregarItensShop();
        
        // Atualizar detalhes do item selecionado
        exibirDetalhesItem();
    }
}