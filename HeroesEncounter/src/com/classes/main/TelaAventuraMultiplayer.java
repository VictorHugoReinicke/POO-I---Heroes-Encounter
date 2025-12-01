package com.classes.main;

import com.classes.networking.*;
import com.classes.BO.*;
import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaAventuraMultiplayer extends JDialog {
    private Jogador jogadorLocal;
    private Jogador jogadorRemoto;
    private NetworkManager networkManager;
    private TelaAventura telaAventura;
    private SessaoMultiplayer sessao;

    // Componentes da interface
    private JLabel lblStatus;
    private JLabel lblJogadorLocal;
    private JLabel lblJogadorRemoto;
    private JTextArea txtLogMultiplayer;
    private JButton btnIniciarHost;
    private JButton btnConectar;
    private JButton btnIniciarAventura;
    private JTextField txtIP;

    public TelaAventuraMultiplayer(TelaAventura pai, Jogador jogador) {
        super(pai, "Modo Multijogador - LAN", true);
        this.telaAventura = pai;
        this.jogadorLocal = jogador;
        this.networkManager = new NetworkManager();
        this.sessao = SessaoMultiplayer.getInstancia();
        initializeTela();
    }

    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
        setLocationRelativeTo(getParent());

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("🎮 MODO MULTIJOGADOR - LAN", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // Painel de status
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status da Conexão"));

        lblStatus = new JLabel("🔴 Desconectado", JLabel.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

        lblJogadorLocal = new JLabel("Jogador Local: " + jogadorLocal.getNome(), JLabel.CENTER);
        lblJogadorRemoto = new JLabel("Jogador Remoto: Aguardando...", JLabel.CENTER);

        statusPanel.add(lblJogadorLocal);
        statusPanel.add(lblJogadorRemoto);
        statusPanel.add(lblStatus);

        // Painel de conexão
        JPanel conexaoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        conexaoPanel.setBorder(BorderFactory.createTitledBorder("Conexão"));

        // Painel do Host
        JPanel hostPanel = new JPanel(new FlowLayout());
        hostPanel.setBorder(BorderFactory.createTitledBorder("Ser o Host"));
        btnIniciarHost = criarBotao("🏠 INICIAR COMO HOST", new Color(0, 100, 200));
        btnIniciarHost.addActionListener(e -> iniciarComoHost());
        hostPanel.add(btnIniciarHost);

        // Painel do Cliente
        JPanel clientPanel = new JPanel(new FlowLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Conectar a um Host"));
        
        clientPanel.add(new JLabel("IP do Host:"));
        txtIP = new JTextField(12);
        txtIP.setText("192.168.1.");
        clientPanel.add(txtIP);
        
        btnConectar = criarBotao("🔗 CONECTAR", new Color(0, 150, 0));
        btnConectar.addActionListener(e -> conectarComoCliente());
        clientPanel.add(btnConectar);

        conexaoPanel.add(hostPanel);
        conexaoPanel.add(clientPanel);

        // Painel de log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log do Multijogador"));

        txtLogMultiplayer = new JTextArea(12, 50);
        txtLogMultiplayer.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLogMultiplayer.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLogMultiplayer);
        logPanel.add(scrollLog, BorderLayout.CENTER);

        // Painel de controles
        JPanel controlesPanel = new JPanel(new FlowLayout());
        btnIniciarAventura = criarBotao("🚀 INICIAR AVENTURA", new Color(200, 100, 0));
        btnIniciarAventura.setEnabled(false);
        btnIniciarAventura.addActionListener(e -> iniciarAventuraCooperativa());

        JButton btnFechar = criarBotao("❌ FECHAR", new Color(150, 0, 0));
        btnFechar.addActionListener(e -> dispose());

        controlesPanel.add(btnIniciarAventura);
        controlesPanel.add(btnFechar);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(conexaoPanel, BorderLayout.CENTER);

        add(tituloPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(controlesPanel, BorderLayout.SOUTH);

        // Thread para processar mensagens de rede
        new Thread(this::processarMensagensRede).start();
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
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

    private void iniciarComoHost() {
        if (networkManager.startAsHost()) {
            sessao.adicionarJogador(jogadorLocal);
            
            lblStatus.setText("🟡 Aguardando jogador 2...");
            btnIniciarHost.setEnabled(false);
            btnConectar.setEnabled(false);
            adicionarLog("✅ Você é o HOST (Jogador 1)");
            adicionarLog("⏳ Aguardando jogador 2 conectar...");
            adicionarLog("💡 Compartilhe seu IP com o outro jogador!");
        } else {
            adicionarLog("❌ Erro: Não foi possível iniciar como host");
            adicionarLog("💡 Verifique se o firewall está bloqueando a conexão");
        }
    }

    private void conectarComoCliente() {
        String hostIP = txtIP.getText().trim();
        
        if (hostIP.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Digite o IP do computador HOST!\n\n" +
                "Exemplos:\n" +
                "• 192.168.1.100\n" + 
                "• 192.168.0.150", 
                "IP Necessário", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        adicionarLog("🔗 Tentando conectar ao HOST: " + hostIP);
        
        if (networkManager.connectAsClient(hostIP)) {
            sessao.adicionarJogador(jogadorLocal);
            
            lblStatus.setText("🟢 Conectado como Cliente");
            btnIniciarHost.setEnabled(false);
            btnConectar.setEnabled(false);
            adicionarLog("✅ Conectado ao HOST: " + hostIP);

            // Enviar mensagem de join
            GameMessage joinMsg = new GameMessage(
                GameMessage.MessageType.PLAYER_JOIN, 
                jogadorLocal, 
                jogadorLocal.getId()
            );
            networkManager.sendMessage(joinMsg);
        } else {
            adicionarLog("❌ Falha ao conectar ao HOST: " + hostIP);
            adicionarLog("💡 Verifique:");
            adicionarLog("   • O IP está correto?");
            adicionarLog("   • O HOST já iniciou o servidor?");
            adicionarLog("   • Firewall está bloqueando?");
        }
    }

    private void iniciarAventuraCooperativa() {
        if (networkManager.isConnected()) {
            adicionarLog("🚀 Iniciando aventura cooperativa...");
            
            if (sessao.getQuantidadeJogadores() >= 2) {
                sessao.iniciarSessao();
                adicionarLog("🤝 " + jogadorLocal.getNome() + " e " + jogadorRemoto.getNome() + " unem forças!");
                
                JOptionPane.showMessageDialog(this,
                    "🚀 AVENTURA COOPERATIVA INICIADA!\n\n" +
                    "Jogador 1: " + jogadorLocal.getNome() + "\n" +
                    "Jogador 2: " + jogadorRemoto.getNome() + "\n\n" +
                    "Agora vocês enfrentarão os desafios juntos!",
                    "Aventura Cooperativa", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                telaAventura.adicionarLog("🎮 Modo cooperativo ativo com " + jogadorRemoto.getNome());
            } else {
                adicionarLog("❌ Ainda não há jogadores suficientes conectados");
            }
        } else {
            adicionarLog("❌ Não há conexão ativa");
        }
    }

    private void processarMensagensRede() {
        while (true) {
            try {
                if (networkManager.hasMessages()) {
                    GameMessage message = networkManager.getNextMessage();
                    processarMensagem(message);
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void processarMensagem(GameMessage message) {
        SwingUtilities.invokeLater(() -> {
            switch (message.getType()) {
                case PLAYER_JOIN:
                    jogadorRemoto = (Jogador) message.getData();
                    sessao.adicionarJogador(jogadorRemoto);
                    
                    lblJogadorRemoto.setText("Jogador Remoto: " + jogadorRemoto.getNome());
                    lblStatus.setText("🟢 Conectado - Pronto!");
                    btnIniciarAventura.setEnabled(true);
                    adicionarLog("👋 " + jogadorRemoto.getNome() + " entrou na aventura!");
                    adicionarLog("✅ Conexão estabelecida com sucesso!");
                    
                    if (networkManager.isHost() && sessao.getQuantidadeJogadores() >= 2) {
                        adicionarLog("🚀 Dois jogadores conectados - Sessão pronta!");
                    }
                    break;

                case PLAYER_ACTION:
                    adicionarLog("🎯 Ação recebida de " + message.getPlayerId());
                    break;

                case CHAT_MESSAGE:
                    String chatMsg = (String) message.getData();
                    adicionarLog("💬 " + chatMsg);
                    break;
                    
                case PLAYER_SYNC: // ✅ AGORA ESTÁ DEFINIDO
                    Jogador jogadorAtualizado = (Jogador) message.getData();
                    sessao.adicionarJogador(jogadorAtualizado);
                    adicionarLog("📊 Dados sincronizados: " + jogadorAtualizado.getNome());
                    break;
                    
                case INVENTORY_SYNC: // ✅ AGORA ESTÁ DEFINIDO
                    adicionarLog("🎒 Inventário sincronizado");
                    break;
                    
                case PLAYER_READY: // ✅ AGORA ESTÁ DEFINIDO
                    adicionarLog("✅ Jogador " + message.getPlayerId() + " está pronto");
                    break;

                default:
                    adicionarLog("📨 Mensagem: " + message.getType());
            }
        });
    }

    private void adicionarLog(String mensagem) {
        txtLogMultiplayer.append(mensagem + "\n");
        txtLogMultiplayer.setCaretPosition(txtLogMultiplayer.getDocument().getLength());
    }

    public void iniciarCombateMultiplayer(Inimigo inimigo) {
        TelaCombateMultiplayer telaCombate = new TelaCombateMultiplayer(
            telaAventura, jogadorLocal, jogadorRemoto, inimigo, networkManager
        );

        telaCombate.setVisible(true);

        new Thread(() -> {
            while (telaCombate.isVisible() && networkManager.isConnected()) {
                try {
                    if (networkManager.hasMessages()) {
                        GameMessage message = networkManager.getNextMessage();
                        if (message.getType() == GameMessage.MessageType.COMBAT_ACTION ||
                            message.getType() == GameMessage.MessageType.COMBAT_END ||
                            message.getType() == GameMessage.MessageType.PLAYER_STATS) {
                            
                            SwingUtilities.invokeLater(() -> {
                                telaCombate.processarMensagemCombate(message);
                            });
                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}