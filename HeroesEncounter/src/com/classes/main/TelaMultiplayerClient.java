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

    private JPanel painelCentral;      // Painel com CardLayout
    private JPanel statusPanel;        // Card 1
    private JPanel listaPanel;         // Card 2
    private CardLayout cardLayout;

    public TelaMultiplayerClient(TelaSelecao pai) {
        super(pai, "Conectar Multiplayer", true);
        this.telaPai = pai;
        this.networkManager = new NetworkManager();
        initializeTela();
    }

    private void initializeTela() {
        setLayout(new BorderLayout());
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
        add(tituloPanel, BorderLayout.NORTH);

        // Painel de conexão (fica no STATUS)
        JPanel conexaoPanel = new JPanel(new GridLayout(2, 1));
        conexaoPanel.setBorder(BorderFactory.createTitledBorder("Conexão com Host"));
        conexaoPanel.setBackground(Color.WHITE);

        JPanel ipPanel = new JPanel(new FlowLayout());
        ipPanel.setBackground(Color.WHITE);
        ipPanel.add(new JLabel("IP do Host:"));

        txtIP = new JTextField(15);
        txtIP.setText("127.0.0.1");
        ipPanel.add(txtIP);

        btnConectar = criarBotao("🔗 CONECTAR", new Color(60, 120, 220));
        btnConectar.addActionListener(e -> conectar());
        ipPanel.add(btnConectar);

        lblStatus = new JLabel("Digite o IP do host e clique em CONECTAR");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel statusMsgPanel = new JPanel(new FlowLayout());
        statusMsgPanel.setBackground(Color.WHITE);
        statusMsgPanel.add(lblStatus);

        conexaoPanel.add(ipPanel);
        conexaoPanel.add(statusMsgPanel);

        // CARDLAYOUT CENTRAL
        cardLayout = new CardLayout();
        painelCentral = new JPanel(cardLayout);

        // CARD: STATUS
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(conexaoPanel, BorderLayout.CENTER);

        // CARD: LISTA DE PERSONAGENS
        listaPanel = new JPanel(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createTitledBorder("Selecione seu Personagem"));
        listaPanel.setBackground(Color.WHITE);

        listModel = new DefaultListModel<>();
        listaPersonagens = new JList<>(listModel);
        JScrollPane scrollLista = new JScrollPane(listaPersonagens);
        listaPanel.add(scrollLista, BorderLayout.CENTER);

        painelCentral.add(statusPanel, "STATUS");
        painelCentral.add(listaPanel, "LISTA");

        add(painelCentral, BorderLayout.CENTER);

        // Botões inferiores
        JPanel botoesPanel = new JPanel(new FlowLayout());
        botoesPanel.setBackground(new Color(240, 240, 240));

        btnSelecionar = criarBotao("✅ SELECIONAR PERSONAGEM", new Color(50, 150, 50));
        btnVoltar = criarBotao("↩️ VOLTAR", new Color(200, 50, 50));

        btnSelecionar.setEnabled(false);
        btnSelecionar.addActionListener(e -> selecionarPersonagem());
        btnVoltar.addActionListener(e -> voltar());

        botoesPanel.add(btnSelecionar);
        botoesPanel.add(btnVoltar);

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
        lblStatus.setText("Conectando...");
        lblStatus.setForeground(Color.BLUE);

        new Thread(() -> {
        	boolean conectado = networkManager.startAsClient(ip);

            SwingUtilities.invokeLater(() -> {
                if (conectado) {
                    lblStatus.setText("Conectado! Aguardando personagens...");
                    lblStatus.setForeground(Color.GREEN);
                    receberPersonagens();
                } else {
                    lblStatus.setText("Falha na conexão!");
                    lblStatus.setForeground(Color.RED);
                    btnConectar.setEnabled(true);
                    txtIP.setEnabled(true);
                }
            });
        }).start();
    }

    private void receberPersonagens() {
        new Thread(() -> {
            try {
                Object obj = networkManager.receiveObject();

                SwingUtilities.invokeLater(() -> {
                    if (obj instanceof List) {
                        jogadoresDisponiveis = (List<Jogador>) obj;
                        exibirPersonagens();
                    } else {
                        lblStatus.setText("Erro ao receber personagens!");
                        lblStatus.setForeground(Color.RED);
                    }
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Erro: " + e.getMessage());
                    lblStatus.setForeground(Color.RED);
                });
            }
        }).start();
    }

    private void exibirPersonagens() {
        listModel.clear();

        for (Jogador jogador : jogadoresDisponiveis) {
            listModel.addElement(jogador.getNome() + " - " + determinarClasse(jogador));
        }

        btnSelecionar.setEnabled(true);

        // MOSTRA LISTA
        cardLayout.show(painelCentral, "LISTA");

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

        int index = jogadoresDisponiveis.indexOf(jogadorSelecionado);
        networkManager.sendObjectSafe(index);

        lblStatus.setText("Aguardando host iniciar...");
        btnSelecionar.setEnabled(false);

        new Thread(this::aguardarInicio).start();
    }

    private void aguardarInicio() {
        try {
            Object obj = networkManager.receiveObject();

            SwingUtilities.invokeLater(() -> {
                if ("INICIAR".equals(obj)) {
                    TelaAventuraMultiplayer tela = new TelaAventuraMultiplayer(
                            jogadorSelecionado, null, networkManager, false
                    );
                    tela.setVisible(true);
                    dispose();
                    telaPai.setVisible(false);
                }
            });

        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                lblStatus.setText("Erro ao iniciar: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
            });
        }
    }

    private void voltar() {
    	networkManager.stop();
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
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(200, 40));
        return botao;
    }
}
