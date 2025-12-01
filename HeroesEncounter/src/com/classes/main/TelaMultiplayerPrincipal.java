package com.classes.main;

import com.classes.networking.*;
import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.List;

public class TelaMultiplayerPrincipal extends JDialog {
    private TelaSelecao telaPai;
    private NetworkManager networkManager;
    private SessaoMultiplayer sessao;
    
    // Componentes
    private JLabel lblStatus;
    private JTextArea txtLog;
    private JButton btnHost;
    private JButton btnClient;
    private JButton btnVoltar;
    private JTextField txtIP;
    private JComboBox<String> cmbPersonagens;
    
    private List<Jogador> personagensLocais;
    private Jogador jogadorSelecionado;

    public TelaMultiplayerPrincipal(TelaSelecao pai) {
        super(pai, "Modo Multijogador", true);
        this.telaPai = pai;
        this.networkManager = new NetworkManager();
        this.sessao = SessaoMultiplayer.getInstancia();
        initializeTela();
        carregarPersonagensLocais();
    }

    private void initializeTela() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setLocationRelativeTo(getParent());

        // Painel de título
        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(new Color(30, 30, 70));
        JLabel titulo = new JLabel("🎮 MODO MULTIJOGADOR - LAN", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        tituloPanel.add(titulo);

        // Painel principal
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel de seleção de personagem (APENAS PARA HOST)
        JPanel personagemPanel = new JPanel(new BorderLayout());
        personagemPanel.setBorder(BorderFactory.createTitledBorder("Selecionar Personagem (Host)"));

        cmbPersonagens = new JComboBox<>();
        cmbPersonagens.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnDetalhes = new JButton("📊 Ver Detalhes");
        btnDetalhes.addActionListener(e -> mostrarDetalhesPersonagem());

        personagemPanel.add(cmbPersonagens, BorderLayout.CENTER);
        personagemPanel.add(btnDetalhes, BorderLayout.EAST);

        // Painel de conexão
        JPanel conexaoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        conexaoPanel.setBorder(BorderFactory.createTitledBorder("Conexão"));

        // Host
        JPanel hostPanel = new JPanel(new FlowLayout());
        hostPanel.setBorder(BorderFactory.createTitledBorder("Criar Sessão (Host)"));
        btnHost = criarBotao("🏠 CRIAR SESSÃO HOST", new Color(0, 100, 200));
        btnHost.addActionListener(e -> criarSessaoHost());
        hostPanel.add(btnHost);

        // Client
        JPanel clientPanel = new JPanel(new FlowLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Conectar a Sessão (Client)"));
        
        clientPanel.add(new JLabel("IP do Host:"));
        txtIP = new JTextField(15);
        txtIP.setText("192.168.1.");
        clientPanel.add(txtIP);
        
        btnClient = criarBotao("🔗 CONECTAR COMO CLIENT", new Color(0, 150, 0));
        btnClient.addActionListener(e -> conectarComoClient());
        clientPanel.add(btnClient);

        // Status
        JPanel statusPanel = new JPanel(new FlowLayout());
        lblStatus = new JLabel("🔴 Desconectado - Escolha uma opção");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(lblStatus);

        conexaoPanel.add(hostPanel);
        conexaoPanel.add(clientPanel);
        conexaoPanel.add(statusPanel);

        mainPanel.add(personagemPanel);
        mainPanel.add(conexaoPanel);

        // Painel de log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log da Conexão"));

        txtLog = new JTextArea(15, 60);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        logPanel.add(scrollLog, BorderLayout.CENTER);

        // Painel de controles
        JPanel controlesPanel = new JPanel(new FlowLayout());
        btnVoltar = criarBotao("↩️ VOLTAR AO MENU", new Color(150, 0, 0));
        btnVoltar.addActionListener(e -> voltarAoMenu());

        controlesPanel.add(btnVoltar);

        add(tituloPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);
        add(controlesPanel, BorderLayout.SOUTH);

        // Iniciar thread de processamento de mensagens
        new Thread(this::processarMensagensRede).start();
    }

    private void carregarPersonagensLocais() {
        try {
            // ✅ APENAS HOST PRECISA CARREGAR PERSONAGENS DO BANCO
            com.classes.BO.JogadorBO jogadorBO = new com.classes.BO.JogadorBO();
            personagensLocais = jogadorBO.pesquisarTodos();

            cmbPersonagens.removeAllItems();
            
            if (personagensLocais == null || personagensLocais.isEmpty()) {
                cmbPersonagens.addItem("Nenhum personagem encontrado");
                btnHost.setEnabled(false);
                adicionarLog("❌ Host: Crie um personagem primeiro!");
            } else {
                for (Jogador jogador : personagensLocais) {
                    String classe = determinarClasse(jogador);
                    cmbPersonagens.addItem(jogador.getNome() + " - " + classe);
                }
                jogadorSelecionado = personagensLocais.get(0);
                adicionarLog("✅ " + personagensLocais.size() + " personagem(s) carregado(s)");
            }
        } catch (Exception e) {
            adicionarLog("❌ Erro ao carregar personagens: " + e.getMessage());
            cmbPersonagens.addItem("Erro ao carregar personagens");
            btnHost.setEnabled(false);
        }
    }

    private void mostrarDetalhesPersonagem() {
        int selectedIndex = cmbPersonagens.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < personagensLocais.size()) {
            Jogador jogador = personagensLocais.get(selectedIndex);
            
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("=== DETALHES DO PERSONAGEM ===\n\n");
            detalhes.append("Nome: ").append(jogador.getNome()).append("\n");
            detalhes.append("Classe: ").append(determinarClasse(jogador)).append("\n");
            detalhes.append("HP: ").append(jogador.getHp()).append("/").append(jogador.getHpMax()).append("\n");
            detalhes.append("Mana: ").append(jogador.getMana()).append("/").append(jogador.getManaMax()).append("\n");
            detalhes.append("Ataque: ").append(jogador.getAtaque()).append("\n");
            detalhes.append("Ouro: ").append(jogador.getOuro()).append("\n");
            
            JOptionPane.showMessageDialog(this, detalhes.toString(), 
                "Detalhes do Personagem", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void criarSessaoHost() {
        int selectedIndex = cmbPersonagens.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= personagensLocais.size()) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um personagem válido para ser o Host!", 
                "Personagem Necessário", JOptionPane.WARNING_MESSAGE);
            return;
        }

        jogadorSelecionado = personagensLocais.get(selectedIndex);
        
        if (networkManager.startAsHost()) {
            sessao.adicionarJogador(jogadorSelecionado);
            
            lblStatus.setText("🟡 Host: Aguardando cliente...");
            btnHost.setEnabled(false);
            btnClient.setEnabled(false);
            cmbPersonagens.setEnabled(false);
            
            adicionarLog("✅ Sessão HOST criada!");
            adicionarLog("🎮 Personagem: " + jogadorSelecionado.getNome());
            adicionarLog("⏳ Aguardando jogador 2 conectar...");
            adicionarLog("💡 Seu IP: " + getLocalIP());
            adicionarLog("📢 Compartilhe este IP com o outro jogador!");
        } else {
            adicionarLog("❌ Falha ao criar sessão host");
        }
    }

    private void conectarComoClient() {
        String hostIP = txtIP.getText().trim();
        
        if (hostIP.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Digite o IP do computador HOST!", 
                "IP Necessário", JOptionPane.WARNING_MESSAGE);
            return;
        }

        adicionarLog("🔗 Tentando conectar ao HOST: " + hostIP);
        
        if (networkManager.connectAsClient(hostIP)) {
            // ✅ CLIENT NÃO PRECISA DE PERSONAGEM LOCAL - SERÁ CRIADO VIA REDE
            lblStatus.setText("🟢 Conectado como Client - Aguardando dados...");
            btnHost.setEnabled(false);
            btnClient.setEnabled(false);
            cmbPersonagens.setEnabled(false);
            
            adicionarLog("✅ Conectado ao HOST: " + hostIP);
            adicionarLog("⏳ Aguardando dados do host...");
        } else {
            adicionarLog("❌ Falha ao conectar ao HOST: " + hostIP);
        }
    }

    private void processarMensagensRede() {
        while (true) {
            try {
                if (networkManager.hasMessages()) {
                    GameMessage message = networkManager.getNextMessage();
                    if (message != null) {
                        SwingUtilities.invokeLater(() -> {
                            processarMensagem(message);
                        });
                    }
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void processarMensagem(GameMessage message) {
        switch (message.getType()) {
            case PLAYER_SYNC:
                // ✅ CLIENT RECEBE DADOS DO PERSONAGEM DO HOST
                if (message.getData() instanceof Jogador) {
                    Jogador jogadorRemoto = (Jogador) message.getData();
                    sessao.adicionarJogador(jogadorRemoto);
                    
                    if (!networkManager.isHost()) {
                        // ✅ CLIENT: Usa o personagem recebido do host
                        jogadorSelecionado = jogadorRemoto;
                        adicionarLog("✅ Recebido personagem: " + jogadorRemoto.getNome());
                        adicionarLog("🎮 Pronto para jogar!");
                        
                        // ✅ INICIAR AVENTURA PARA CLIENT
                        iniciarAventuraMultiplayer();
                    }
                }
                break;
                
            case PLAYER_READY:
                // ✅ HOST RECEBE CONFIRMAÇÃO QUE CLIENT ESTÁ PRONTO
                if (networkManager.isHost()) {
                    adicionarLog("✅ Cliente pronto - Iniciando aventura...");
                    iniciarAventuraMultiplayer();
                }
                break;
                
            default:
                adicionarLog("📨 Mensagem: " + message.getType());
        }
    }

    private void iniciarAventuraMultiplayer() {
        if (jogadorSelecionado != null && networkManager.isConnected()) {
            adicionarLog("🚀 Iniciando aventura multiplayer...");
            
            // ✅ FECHAR ESTA TELA
            dispose();
            
            // ✅ CORREÇÃO: CONSTRUTOR CORRETO
            TelaAventuraMultiplayer telaAventura = new TelaAventuraMultiplayer(
                jogadorSelecionado, 
                networkManager, 
                sessao
            );
            telaAventura.setVisible(true);
            
        } else {
            adicionarLog("❌ Erro: Dados insuficientes para iniciar aventura");
        }
    }

    private void voltarAoMenu() {
        networkManager.disconnect();
        dispose();
        telaPai.setVisible(true);
    }

    // ✅ MÉTODO PARA OBTER IP LOCAL
    private String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    private String determinarClasse(Jogador jogador) {
        if (jogador instanceof Guerreiro) return "Guerreiro";
        if (jogador instanceof Mago) return "Mago";
        if (jogador instanceof Paladino) return "Paladino";
        return "Desconhecida";
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(200, 40));
        
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

    private void adicionarLog(String mensagem) {
        txtLog.append(mensagem + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
}