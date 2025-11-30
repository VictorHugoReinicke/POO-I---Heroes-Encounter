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
    private boolean minhaVez = false;
    
    // Componentes da interface
    private JLabel lblStatus;
    private JLabel lblJogadorLocal;
    private JLabel lblJogadorRemoto;
    private JTextArea txtLogMultiplayer;
    private JButton btnIniciarHost;
    private JButton btnConectar;
    private JButton btnIniciarAventura;
    
    public TelaAventuraMultiplayer(TelaAventura pai, Jogador jogador) {
        super(pai, "Modo Multijogador", true);
        this.telaAventura = pai;
        this.jogadorLocal = jogador;
        this.networkManager = new NetworkManager();
        initializeTela();
    }
    
    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
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
        
        // Painel de log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log do Multijogador"));
        
        txtLogMultiplayer = new JTextArea(15, 50);
        txtLogMultiplayer.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLogMultiplayer.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLogMultiplayer);
        logPanel.add(scrollLog, BorderLayout.CENTER);
        
        // Painel de controles
        JPanel controlesPanel = new JPanel(new FlowLayout());
        btnIniciarHost = criarBotao("🏠 INICIAR COMO HOST", new Color(0, 100, 200));
        btnConectar = criarBotao("🔗 CONECTAR COMO CLIENTE", new Color(0, 150, 0));
        btnIniciarAventura = criarBotao("🚀 INICIAR AVENTURA", new Color(200, 100, 0));
        btnIniciarAventura.setEnabled(false);
        
        btnIniciarHost.addActionListener(e -> iniciarComoHost());
        btnConectar.addActionListener(e -> conectarComoCliente());
        btnIniciarAventura.addActionListener(e -> iniciarAventuraCooperativa());
        
        controlesPanel.add(btnIniciarHost);
        controlesPanel.add(btnConectar);
        controlesPanel.add(btnIniciarAventura);
        
        add(tituloPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
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
        botao.setPreferredSize(new Dimension(200, 40));
        return botao;
    }
    
    private void iniciarComoHost() {
        if (networkManager.startAsHost()) {
            lblStatus.setText("🟡 Aguardando jogador 2...");
            btnIniciarHost.setEnabled(false);
            btnConectar.setEnabled(false);
            adicionarLog("✅ Você é o HOST (Jogador 1)");
            adicionarLog("⏳ Aguardando jogador 2 conectar...");
        }
    }
    
    private void conectarComoCliente() {
        String hostIP = JOptionPane.showInputDialog(this, 
            "Digite o IP do Host:", "192.168.1.");
        
        if (hostIP != null && !hostIP.trim().isEmpty()) {
            if (networkManager.connectAsClient(hostIP.trim())) {
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
            }
        }
    }
    
    private void iniciarAventuraCooperativa() {
        if (networkManager.isConnected()) {
            adicionarLog("🚀 Iniciando aventura cooperativa...");
            // Aqui você iniciaria a aventura com ambos os jogadores
            JOptionPane.showMessageDialog(this, 
                "Aventura Cooperativa Iniciada!\n\n" +
                "Jogador 1: " + jogadorLocal.getNome() + "\n" +
                "Jogador 2: " + (jogadorRemoto != null ? jogadorRemoto.getNome() : "Conectando..."),
                "Aventura Iniciada", JOptionPane.INFORMATION_MESSAGE);
            
            // Fechar esta tela e voltar para aventura principal
            dispose();
        }
    }
    
    private void processarMensagensRede() {
        while (true) {
            try {
                if (networkManager.hasMessages()) {
                    GameMessage message = networkManager.getNextMessage();
                    processarMensagem(message);
                }
                Thread.sleep(100); // Pequena pausa para não consumir muita CPU
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
                    lblJogadorRemoto.setText("Jogador Remoto: " + jogadorRemoto.getNome());
                    lblStatus.setText("🟢 Conectado - Pronto!");
                    btnIniciarAventura.setEnabled(true);
                    adicionarLog("👋 " + jogadorRemoto.getNome() + " entrou na aventura!");
                    break;
                    
                case PLAYER_ACTION:
                    adicionarLog("🎯 Ação recebida de " + message.getPlayerId());
                    // Processar ação do outro jogador
                    break;
                    
                case CHAT_MESSAGE:
                    String chatMsg = (String) message.getData();
                    adicionarLog("💬 " + chatMsg);
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
            telaAventura, 
            jogadorLocal, 
            jogadorRemoto, 
            inimigo, 
            networkManager
        );
        
        telaCombate.setVisible(true);
        
        // ✅ INICIAR THREAD PARA PROCESSAR MENSAGENS DE COMBATE
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