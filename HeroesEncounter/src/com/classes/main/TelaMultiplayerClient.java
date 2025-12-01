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

        // Painel de conexão
        JPanel conexaoPanel = new JPanel(new FlowLayout());
        conexaoPanel.setBorder(BorderFactory.createTitledBorder("Conexão com Host"));
        
        conexaoPanel.add(new JLabel("IP do Host:"));
        txtIP = new JTextField(15);
        txtIP.setText("127.0.0.1"); // Default para teste local
        conexaoPanel.add(txtIP);
        
        btnConectar = criarBotao("🔗 CONECTAR", new Color(60, 120, 220));
        btnConectar.addActionListener(e -> conectar());
        conexaoPanel.add(btnConectar);

        // Status
        lblStatus = new JLabel("Digite o IP do host e conecte");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));

        // Lista de personagens (inicialmente invisível)
        JPanel listaPanel = new JPanel(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createTitledBorder("Selecione seu Personagem"));
        
        listModel = new DefaultListModel<>();
        listaPersonagens = new JList<>(listModel);
        JScrollPane scrollLista = new JScrollPane(listaPersonagens);
        listaPanel.add(scrollLista, BorderLayout.CENTER);
        listaPanel.setVisible(false);

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout());
        btnSelecionar = criarBotao("✅ SELECIONAR PERSONAGEM", new Color(50, 150, 50));
        btnVoltar = criarBotao("↩️ VOLTAR", new Color(200, 50, 50));
        
        btnSelecionar.setEnabled(false);
        btnSelecionar.addActionListener(e -> selecionarPersonagem());
        btnVoltar.addActionListener(e -> voltar());
        
        botoesPanel.add(btnSelecionar);
        botoesPanel.add(btnVoltar);

        add(conexaoPanel, BorderLayout.NORTH);
        add(lblStatus, BorderLayout.CENTER);
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
        lblStatus.setText("Conectando...");
        lblStatus.setForeground(Color.BLUE);

        new Thread(() -> {
            if (networkManager.connectAsClient(ip)) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Conectado! Aguardando personagens...");
                    receberPersonagens();
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Falha na conexão!");
                    lblStatus.setForeground(Color.RED);
                    btnConectar.setEnabled(true);
                });
            }
        }).start();
    }

    private void receberPersonagens() {
        new Thread(() -> {
            try {
                Object obj = networkManager.receiveObject();
                if (obj instanceof List) {
                    jogadoresDisponiveis = (List<Jogador>) obj;
                    SwingUtilities.invokeLater(this::exibirPersonagens);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void exibirPersonagens() {
        listModel.clear();
        for (Jogador jogador : jogadoresDisponiveis) {
            String info = jogador.getNome() + " - " + determinarClasse(jogador);
            listModel.addElement(info);
        }
        
        // Mostrar painel de lista
        ((JPanel)getContentPane().getComponent(2)).setVisible(true);
        lblStatus.setText("Selecione seu personagem:");
        btnSelecionar.setEnabled(true);
        
        listaPersonagens.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = listaPersonagens.getSelectedIndex();
                if (index >= 0) {
                    jogadorSelecionado = jogadoresDisponiveis.get(index);
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
        
        lblStatus.setText("Personagem selecionado! Aguardando host...");
        btnSelecionar.setEnabled(false);
        
        // Aguardar início do jogo
        new Thread(this::aguardarInicio).start();
    }

    private void aguardarInicio() {
        try {
            Object obj = networkManager.receiveObject();
            if ("INICIAR".equals(obj)) {
                SwingUtilities.invokeLater(() -> {
                    // Iniciar aventura como cliente
                    TelaAventuraMultiplayer telaAventura = new TelaAventuraMultiplayer(jogadorSelecionado, null, networkManager, false);
                    telaAventura.setVisible(true);
                    dispose();
                    telaPai.setVisible(false);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void voltar() {
        networkManager.close();
        dispose();
        telaPai.setVisible(true);
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
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(200, 40));
        return botao;
    }
}