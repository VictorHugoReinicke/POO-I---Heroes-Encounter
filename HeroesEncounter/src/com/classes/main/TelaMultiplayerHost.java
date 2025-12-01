package com.classes.main;

import com.classes.DTO.*;
import com.classes.BO.JogadorBO;
import com.classes.network.NetworkManager;
import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class TelaMultiplayerHost extends JDialog {
	private TelaSelecao telaPai;
	private NetworkManager networkManager;
	private JogadorBO jogadorBO;
	private List<Jogador> jogadores;
	private Jogador jogadorHost;
	private Jogador jogadorConvidado;

	private JLabel lblStatus;
	private JLabel lblIP;
	private JList<String> listaPersonagensHost;
	private JList<String> listaPersonagensConvidado;
	private DefaultListModel<String> listModelHost;
	private DefaultListModel<String> listModelConvidado;
	private JButton btnIniciar;
	private JButton btnVoltar;

	public TelaMultiplayerHost(TelaSelecao pai) {
		super(pai, "Host Multiplayer", true);
		this.telaPai = pai;
		this.networkManager = new NetworkManager();
		this.jogadorBO = new JogadorBO();
		initializeTela();
		iniciarHost();
	}

	private void initializeTela() {
		setLayout(new BorderLayout(10, 10));
		setSize(800, 600);
		setLocationRelativeTo(getParent());
		setResizable(false);

		// Painel de título
		JPanel tituloPanel = new JPanel();
		tituloPanel.setBackground(new Color(30, 30, 70));
		JLabel titulo = new JLabel("🎮 HOST MULTIPLAYER - AGUARDANDO JOGADOR 2");
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		titulo.setForeground(Color.WHITE);
		tituloPanel.add(titulo);

		// Painel de status
		JPanel statusPanel = new JPanel(new FlowLayout());
		statusPanel.setBackground(Color.WHITE);

		lblIP = new JLabel();
		lblIP.setFont(new Font("Arial", Font.BOLD, 12));

		lblStatus = new JLabel("Aguardando conexão...");
		lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
		lblStatus.setForeground(Color.BLUE);

		statusPanel.add(new JLabel("IP do Host: "));
		statusPanel.add(lblIP);
		statusPanel.add(Box.createHorizontalStrut(20));
		statusPanel.add(lblStatus);

		// Painel principal com seleção de personagens
		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Painel do Host
		JPanel hostPanel = new JPanel(new BorderLayout());
		hostPanel.setBorder(BorderFactory.createTitledBorder("Seu Personagem (Host)"));

		listModelHost = new DefaultListModel<>();
		listaPersonagensHost = new JList<>(listModelHost);
		JScrollPane scrollHost = new JScrollPane(listaPersonagensHost);
		hostPanel.add(scrollHost, BorderLayout.CENTER);

		// Painel do Convidado
		JPanel convidadoPanel = new JPanel(new BorderLayout());
		convidadoPanel.setBorder(BorderFactory.createTitledBorder("Personagem do Jogador 2"));

		listModelConvidado = new DefaultListModel<>();
		listaPersonagensConvidado = new JList<>(listModelConvidado);
		listaPersonagensConvidado.setEnabled(false);
		JScrollPane scrollConvidado = new JScrollPane(listaPersonagensConvidado);
		convidadoPanel.add(scrollConvidado, BorderLayout.CENTER);

		mainPanel.add(hostPanel);
		mainPanel.add(convidadoPanel);

		// Painel de botões
		JPanel botoesPanel = new JPanel(new FlowLayout());
		btnIniciar = criarBotao("🚀 INICIAR JOGO", new Color(50, 150, 50));
		btnVoltar = criarBotao("↩️ VOLTAR", new Color(200, 50, 50));

		btnIniciar.setEnabled(false);
		btnIniciar.addActionListener(e -> iniciarJogo());
		btnVoltar.addActionListener(e -> voltar());

		botoesPanel.add(btnIniciar);
		botoesPanel.add(btnVoltar);

		add(tituloPanel, BorderLayout.NORTH);
		add(statusPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
		add(botoesPanel, BorderLayout.SOUTH);

		carregarPersonagens();
	}

	private void iniciarHost() {
		// Mostrar que está iniciando
		lblStatus.setText("Iniciando servidor...");
		lblStatus.setForeground(Color.BLUE);

		// Executar em thread separada para não travar a interface
		new Thread(() -> {
			if (networkManager.startAsHost()) {
				SwingUtilities.invokeLater(() -> {
					lblIP.setText(getLocalIP() + " (Porta: " + networkManager.getCurrentPort() + ")");
					lblStatus.setText("Jogador 2 conectado! Aguardando seleção...");
					lblStatus.setForeground(Color.GREEN);
					enviarListaPersonagens();
					aguardarSelecaoConvidado();
				});
			} else {
				SwingUtilities.invokeLater(() -> {
					lblStatus.setText("Erro: Todas as portas estão ocupadas!");
					lblStatus.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this,
							"Não foi possível iniciar o servidor.\nTodas as portas estão ocupadas.\nFeche outros programas e tente novamente.",
							"Erro ao Iniciar Servidor", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private void enviarListaPersonagens() {
		try {
			networkManager.sendObject(jogadores);
			System.out.println("Lista de personagens enviada para o cliente");
		} catch (Exception e) {
			System.err.println("Erro ao enviar lista de personagens: " + e.getMessage());
		}
	}

	private void aguardarSelecaoConvidado() {
		new Thread(() -> {
			try {
				Object obj = networkManager.receiveObject();
				if (obj instanceof Integer) {
					int index = (Integer) obj;
					SwingUtilities.invokeLater(() -> {
						selecionarPersonagemConvidado(index);
						btnIniciar.setEnabled(true);
						lblStatus.setText("Pronto para iniciar! Clique em INICIAR JOGO");
					});
				}
			} catch (Exception e) {
				System.err.println("Erro ao aguardar seleção: " + e.getMessage());
				SwingUtilities.invokeLater(() -> {
					lblStatus.setText("Erro na comunicação: " + e.getMessage());
					lblStatus.setForeground(Color.RED);
				});
			}
		}).start();
	}

	private void selecionarPersonagemConvidado(int index) {
		if (index >= 0 && index < jogadores.size()) {
			jogadorConvidado = jogadores.get(index);
			listModelConvidado.clear();
			listModelConvidado.addElement(jogadorConvidado.getNome() + " - " + determinarClasse(jogadorConvidado));
			lblStatus.setText("Jogador 2 selecionou: " + jogadorConvidado.getNome());
		}
	}

	private void carregarPersonagens() {
		try {
			jogadores = jogadorBO.pesquisarTodos();
			for (Jogador jogador : jogadores) {
				String info = jogador.getNome() + " - " + determinarClasse(jogador);
				listModelHost.addElement(info);
			}

			listaPersonagensHost.addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int index = listaPersonagensHost.getSelectedIndex();
					if (index >= 0) {
						jogadorHost = jogadores.get(index);
					}
				}
			});

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar personagens!", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void iniciarJogo() {
		if (jogadorHost == null || jogadorConvidado == null) {
			JOptionPane.showMessageDialog(this, "Selecione ambos os personagens!", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Enviar confirmação para o cliente
		networkManager.sendObjectSafe("INICIAR");

		// Iniciar aventura multiplayer
		TelaAventuraMultiplayer telaAventura = new TelaAventuraMultiplayer(jogadorHost, jogadorConvidado,
				networkManager, true);

		telaAventura.setVisible(true);
		dispose();
		telaPai.setVisible(false);
	}

	private void voltar() {
		networkManager.stop();
		dispose();
		telaPai.setVisible(true);
	}

	private String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "127.0.0.1";
		}
	}

	private String determinarClasse(Jogador jogador) {
		if (jogador instanceof Guerreiro)
			return "Guerreiro";
		if (jogador instanceof Mago)
			return "Mago";
		if (jogador instanceof Paladino)
			return "Paladino";
		return "Desconhecida";
	}

	private JButton criarBotao(String texto, Color cor) {
		JButton botao = new JButton(texto);
		botao.setBackground(cor);
		botao.setForeground(Color.WHITE);
		botao.setFont(new Font("Arial", Font.BOLD, 14));
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createRaisedBevelBorder());
		botao.setPreferredSize(new Dimension(180, 40));
		return botao;
	}
}