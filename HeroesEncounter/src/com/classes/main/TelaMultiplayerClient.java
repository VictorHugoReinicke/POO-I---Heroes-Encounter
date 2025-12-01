package com.classes.main;

import com.classes.DTO.*;
import com.classes.network.NetworkManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaMultiplayerClient extends JDialog {
    private TelaSelecao telaPai;
    private NetworkManager networkManager;
    private List<Jogador> jogadoresDisponiveis;
    private Jogador jogadorSelecionado;
    
    private JTextField txtIP;
    private JLabel lblStatus;
    private JList<String> listaPersonagens;
    private DefaultListModel<String> listModel;
    private JButton btnConectar;
    private JButton btnSelecionar;
    private JButton btnVoltar;
    private JPanel listaPanel;

    public TelaMultiplayerClient(TelaSelecao pai) {
        super(pai, "Conectar Multiplayer", true);
        this.telaPai = pai;
        this.networkManager = new NetworkManager();
        initializeTela();
    }

    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("🔗 CONECTAR COMO CLIENTE");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // Painel de conexão
        JPanel conexaoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        conexaoPanel.setBorder(BorderFactory.createTitledBorder("Conexão com Host"));
        conexaoPanel.setBackground(Color.WHITE);
        
        JPanel ipPanel = new JPanel(new FlowLayout());
        ipPanel.setBackground(Color.WHITE);
        ipPanel.add(new JLabel("IP do Host:"));
        txtIP = new JTextField(15);
        txtIP.setText("127.0.0.1"); // Default para teste local
        ipPanel.add(txtIP);
        
        btnConectar = criarBotao("🔗 CONECTAR", new Color(60, 120, 220));
        btnConectar.addActionListener(e -> conectar());
        ipPanel.add(btnConectar);
        
        // Status
        lblStatus = new JLabel("Digite o IP do host e clique em CONECTAR");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(lblStatus);

        conexaoPanel.add(ipPanel);
        conexaoPanel.add(statusPanel);

        // Lista de personagens (inicialmente invisível)
        listaPanel = new JPanel(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createTitledBorder("Selecione seu Personagem"));
        listaPanel.setBackground(Color.WHITE);
        
        listModel = new DefaultListModel<>();
        listaPersonagens = new JList<>(listModel);
        listaPersonagens.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollLista = new JScrollPane(listaPersonagens);
        listaPanel.add(scrollLista, BorderLayout.CENTER);
        listaPanel.setVisible(false);

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout());
        botoesPanel.setBackground(new Color(240, 240, 240));
        
        btnSelecionar = criarBotao("✅ SELECIONAR PERSONAGEM", new Color(50, 150, 50));
        btnVoltar = criarBotao("↩️ VOLTAR", new Color(200, 50, 50));
        
        btnSelecionar.setEnabled(false);
        btnSelecionar.addActionListener(e -> selecionarPersonagem());
        btnVoltar.addActionListener(e -> voltar());
        
        botoesPanel.add(btnSelecionar);
        botoesPanel.add(btnVoltar);

        add(tituloPanel, BorderLayout.NORTH);
        add(conexaoPanel, BorderLayout.CENTER);
        add(listaPanel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
    }

    private void conectar() {
        String ip = txtIP.getText().trim();
        if (ip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o IP do host!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnConectar.setEnabled(false);
        txtIP.setEnabled(false);
        lblStatus.setText("Conectando... Tentando portas 12345-12349");
        lblStatus.setForeground(Color.BLUE);

        // Executar em thread separada para não travar a interface
        new Thread(() -> {
            boolean conectado = networkManager.connectAsClient(ip);
            
            SwingUtilities.invokeLater(() -> {
                if (conectado) {
                    lblStatus.setText("Conectado! Aguardando personagens...");
                    lblStatus.setForeground(Color.GREEN);
                    receberPersonagens();
                } else {
                    lblStatus.setText("Falha na conexão em todas as portas!");
                    lblStatus.setForeground(Color.RED);
                    btnConectar.setEnabled(true);
                    txtIP.setEnabled(true);
                    
                    JOptionPane.showMessageDialog(this, 
                        "❌ Não foi possível conectar ao host!\n\n" +
                        "Verifique:\n" +
                        "• O IP está correto? (" + ip + ")\n" +
                        "• O host está executando?\n" +
                        "• Firewall está bloqueando a conexão?\n" +
                        "• Ambos estão na mesma rede?", 
                        "Falha na Conexão", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private void receberPersonagens() {
        new Thread(() -> {
            try {
                System.out.println("Aguardando lista de personagens...");
                Object obj = networkManager.receiveObject();
                
                SwingUtilities.invokeLater(() -> {
                    if (obj instanceof List) {
                        jogadoresDisponiveis = (List<Jogador>) obj;
                        System.out.println("Recebida lista com " + jogadoresDisponiveis.size() + " personagens");
                        exibirPersonagens();
                    } else {
                        lblStatus.setText("Erro: Dados recebidos são inválidos!");
                        lblStatus.setForeground(Color.RED);
                        btnConectar.setEnabled(true);
                        txtIP.setEnabled(true);
                    }
                });
                
            } catch (Exception e) {
                System.err.println("Erro ao receber personagens: " + e.getMessage());
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Erro na comunicação: " + e.getMessage());
                    lblStatus.setForeground(Color.RED);
                    btnConectar.setEnabled(true);
                    txtIP.setEnabled(true);
                });
            }
        }).start();
    }

    private void exibirPersonagens() {
        listModel.clear();
        
        if (jogadoresDisponiveis == null || jogadoresDisponiveis.isEmpty()) {
            listModel.addElement("❌ Nenhum personagem disponível");
            return;
        }
        
        for (Jogador jogador : jogadoresDisponiveis) {
            String info = jogador.getNome() + " - " + determinarClasse(jogador);
            listModel.addElement(info);
        }
        
        // Mostrar painel de lista
        listaPanel.setVisible(true);
        lblStatus.setText("Selecione seu personagem:");
        btnSelecionar.setEnabled(true);
        
        // Ajustar tamanho da janela
        pack();
        setLocationRelativeTo(getParent());
        
        listaPersonagens.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = listaPersonagens.getSelectedIndex();
                if (index >= 0 && index < jogadoresDisponiveis.size()) {
                    jogadorSelecionado = jogadoresDisponiveis.get(index);
                    System.out.println("Personagem selecionado: " + jogadorSelecionado.getNome());
                }
            }
        });
    }

    private void selecionarPersonagem() {
        if (jogadorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um personagem!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Enviar seleção para o host
        int index = jogadoresDisponiveis.indexOf(jogadorSelecionado);
        networkManager.sendObject(index);
        
        lblStatus.setText("Personagem selecionado! Aguardando host iniciar o jogo...");
        btnSelecionar.setEnabled(false);
        listaPersonagens.setEnabled(false);
        
        // Aguardar início do jogo
        new Thread(this::aguardarInicio).start();
    }

    private void aguardarInicio() {
        try {
            System.out.println("Aguardando sinal de início do host...");
            Object obj = networkManager.receiveObject();
            
            SwingUtilities.invokeLater(() -> {
                if ("INICIAR".equals(obj)) {
                    System.out.println("Recebido sinal para iniciar jogo!");
                    
                    // Iniciar aventura como cliente
                    TelaAventuraMultiplayer telaAventura = new TelaAventuraMultiplayer(
                        jogadorSelecionado, null, networkManager, false);
                    telaAventura.setVisible(true);
                    dispose();
                    telaPai.setVisible(false);
                    
                } else {
                    lblStatus.setText("Erro: Sinal de início inválido!");
                    lblStatus.setForeground(Color.RED);
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erro ao aguardar início: " + e.getMessage());
            SwingUtilities.invokeLater(() -> {
                lblStatus.setText("Erro na comunicação: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
            });
        }
    }

    private void voltar() {
        networkManager.close();
        dispose();
        telaPai.setVisible(true);
    }

    private String determinarClasse(Jogador jogador) {
        if (jogador instanceof Guerreiro) {
            return "Guerreiro";
        } else if (jogador instanceof Mago) {
            return "Mago";
        } else if (jogador instanceof Paladino) {
            return "Paladino";
        }
        return "Desconhecida";
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(200, 40));
        
        // Efeito hover
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
}