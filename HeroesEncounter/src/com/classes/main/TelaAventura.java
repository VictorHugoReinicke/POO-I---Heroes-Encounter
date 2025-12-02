package com.classes.main;

import com.classes.DTO.*;
import com.classes.BO.InimigoBO;
import com.classes.BO.ShopBO;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class TelaAventura extends JFrame {
	private Jogador jogador;
	private int batalhasVencidas;
	private boolean jornadaAtiva;
	private InimigoBO inimigoBO;
	private ShopBO shopBO;
	private Random random;

	private JLabel lblStatus;
	private JLabel lblProgresso;
	private JTextArea txtLog;
	private JButton btnIniciarJornada;
	private JButton btnProximaBatalha;
	private JButton btnStatus;
	private JButton btnShop;
	private JButton btnVoltarMenu;

	public TelaAventura(Jogador jogador) {
		this.jogador = jogador;
		this.batalhasVencidas = 0;
		this.jornadaAtiva = false;
		this.inimigoBO = new InimigoBO();
		this.shopBO = new ShopBO();
		this.random = new Random();
		initializeTela();
	}

	private void initializeTela() {
		setTitle("Heroes Encounter - Aventura");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));
		setResizable(false);

		JPanel tituloPanel = new JPanel();
		tituloPanel.setBackground(new Color(30, 30, 70));
		JLabel titulo = new JLabel("AVENTURA - " + jogador.getNome().toUpperCase());
		titulo.setFont(new Font("Arial", Font.BOLD, 20));
		titulo.setForeground(Color.WHITE);
		tituloPanel.add(titulo);

		JPanel statusPanel = new JPanel(new GridLayout(2, 1));
		statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		statusPanel.setBackground(Color.WHITE);

		lblStatus = new JLabel("Status: Preparado para aventura");
		lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

		lblProgresso = new JLabel("Progresso: Jornada não iniciada");
		lblProgresso.setFont(new Font("Arial", Font.BOLD, 12));
		lblProgresso.setForeground(Color.BLUE);

		statusPanel.add(lblStatus);
		statusPanel.add(lblProgresso);

		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.setBorder(BorderFactory.createTitledBorder("Log da Aventura"));

		txtLog = new JTextArea(15, 50);
		txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtLog.setEditable(false);
		txtLog.setBackground(Color.BLACK);
		txtLog.setForeground(Color.WHITE);

		JScrollPane scrollLog = new JScrollPane(txtLog);
		logPanel.add(scrollLog, BorderLayout.CENTER);

		JPanel botoesPanel = new JPanel(new GridLayout(2, 3, 5, 5));
		botoesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		botoesPanel.setBackground(new Color(240, 240, 240));
		
		btnIniciarJornada = criarBotaoAventura("INICIAR JORNADA", new Color(220, 60, 60));
		btnProximaBatalha = criarBotaoAventura("PRÓXIMA BATALHA", new Color(60, 120, 220));
		btnShop = criarBotaoAventura("VISITAR SHOP", new Color(255, 165, 0));
		btnStatus = criarBotaoAventura("VER STATUS", new Color(60, 180, 120));
		btnVoltarMenu = criarBotaoAventura("VOLTAR AO MENU", new Color(100, 100, 100));

		btnIniciarJornada.addActionListener(e -> iniciarJornada());
		btnProximaBatalha.addActionListener(e -> proximaBatalha());
		btnShop.addActionListener(e -> visitarShop());
		btnStatus.addActionListener(e -> mostrarStatus());
		btnVoltarMenu.addActionListener(e -> voltarAoMenu());

		botoesPanel.add(btnIniciarJornada);
		botoesPanel.add(btnProximaBatalha);
		botoesPanel.add(btnShop);
		botoesPanel.add(btnStatus);
		botoesPanel.add(btnVoltarMenu);

		btnProximaBatalha.setEnabled(false);
		btnShop.setEnabled(false);

		add(tituloPanel, BorderLayout.NORTH);
		add(statusPanel, BorderLayout.CENTER);
		add(logPanel, BorderLayout.EAST);
		add(botoesPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);

		adicionarLog("Bem-vindo à aventura, " + jogador.getNome() + "!");
		adicionarLog("Classe: " + determinarClasse(jogador));
		adicionarLog("HP: " + jogador.getHp() + "/" + jogador.getHpMax());
		if (jogador.getManaMax() > 0) {
			adicionarLog("Mana: " + jogador.getMana() + "/" + jogador.getManaMax());
		}
		adicionarLog("Ouro: " + jogador.getOuro());
		adicionarLog("");
		adicionarLog("Clique em 'INICIAR JORNADA' para começar sua aventura épica!");
	}

	private JButton criarBotaoAventura(String texto, Color cor) {
		JButton botao = new JButton(texto);
		botao.setBackground(cor);
		botao.setForeground(Color.WHITE);
		botao.setFont(new Font("Arial", Font.BOLD, 11));
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createRaisedBevelBorder());
		botao.setPreferredSize(new Dimension(140, 40));

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

	private void iniciarJornada() {
		if (!jornadaAtiva) {
			jornadaAtiva = true;
			batalhasVencidas = 0;

			adicionarLog("==========================================");
			adicionarLog(" JORNADA INICIADA! ");
			adicionarLog("==========================================");
			adicionarLog("Sua missão: Vencer 3 batalhas épicas!");
			adicionarLog("• Batalha 1: Inimigo Aleatório");
			adicionarLog("• Batalha 2: Inimigo Aleatório");
			adicionarLog("• Batalha Final: CHEFE SUPREMO!");
			adicionarLog("==========================================");
			adicionarLog("Entre as batalhas, visite o SHOP para se preparar!");
			adicionarLog("Boa sorte, " + jogador.getNome() + "!");

			lblStatus.setText("Status: Jornada em Andamento");
			lblProgresso.setText("Progresso: " + batalhasVencidas + "/3 batalhas vencidas");

			btnIniciarJornada.setEnabled(false);
			btnProximaBatalha.setEnabled(true);
			btnShop.setEnabled(true);

			JOptionPane.showMessageDialog(this,
					"JORNADA INICIADA! \n\n" + "Sua missão é vencer 3 batalhas:\n" + "• 2 inimigos aleatórios\n"
							+ "• 1 chefe supremo\n\n" + "Entre as batalhas, visite o SHOP para comprar itens!\n\n"
							+ "Boa sorte, " + jogador.getNome() + "!",
					"Jornada Iniciada", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected void proximaBatalha() {
		if (!jornadaAtiva) {
			JOptionPane.showMessageDialog(this, "Você precisa iniciar a jornada primeiro!", "Jornada Não Iniciada",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			Inimigo inimigo = obterInimigoAleatorio(batalhasVencidas == 2);

			adicionarLog("");
			adicionarLog("INICIANDO BATALHA " + (batalhasVencidas + 1));
			adicionarLog("Inimigo: " + inimigo.getNome());
			adicionarLog("HP: " + inimigo.getHp() + " | Ataque: " + inimigo.getAtaque());

			TelaCombate telaCombate = new TelaCombate(this, jogador, inimigo);
			telaCombate.setVisible(true);

		} catch (Exception e) {
			adicionarLog("Erro ao iniciar batalha: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Erro ao iniciar batalha: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void batalhaVencida() {
		batalhasVencidas++;
		adicionarLog("Vitória! Batalhas vencidas: " + batalhasVencidas + "/3");
		lblProgresso.setText("Progresso: " + batalhasVencidas + "/3 batalhas vencidas");

		if (batalhasVencidas >= 3) {
			jornadaCompleta();
		} else {
			jogador.setHp(Math.min(jogador.getHpMax(), jogador.getHp() + 20));
			if (jogador.getManaMax() > 0) {
				jogador.setMana(jogador.getManaMax());
			}
			adicionarLog("HP restaurado em 20 pontos!");
			adicionarLog("Visite o SHOP para se preparar!");
		}
	}

	
	public void batalhaPerdida() {
		jornadaAtiva = false;
		adicionarLog("Derrota! A jornada termina aqui...");
		lblStatus.setText("Status: Derrotado");
		lblProgresso.setText("Progresso: Jornada Falhou");

		btnProximaBatalha.setEnabled(false);
		btnShop.setEnabled(false);
	}

	private Inimigo obterInimigoAleatorio(boolean isChefe) {
		try {
			List<Inimigo> todosInimigos = inimigoBO.pesquisarTodos();

			if (todosInimigos == null || todosInimigos.isEmpty()) {
				throw new Exception("Nenhum inimigo encontrado no banco de dados");
			}

			List<Inimigo> inimigosFiltrados = new java.util.ArrayList<>();
			for (Inimigo inimigo : todosInimigos) {
				String nomeClasse = inimigo.getClass().getSimpleName();
				if (isChefe && nomeClasse.equalsIgnoreCase("Chefe")) {
					inimigosFiltrados.add(inimigo);
				} else if (!isChefe && !nomeClasse.equalsIgnoreCase("Chefe")) {
					inimigosFiltrados.add(inimigo);
				}
			}

			if (inimigosFiltrados.isEmpty()) {
				adicionarLog("Nenhum " + (isChefe ? "chefe" : "inimigo normal")
						+ " encontrado. Usando qualquer inimigo disponível.");
				inimigosFiltrados = todosInimigos;
			}

			if (inimigosFiltrados.isEmpty()) {
				throw new Exception("Nenhum inimigo disponível no banco de dados");
			}

			int indexAleatorio = random.nextInt(inimigosFiltrados.size());
			Inimigo inimigoSelecionado = inimigosFiltrados.get(indexAleatorio);

			inimigoSelecionado.setHp(inimigoSelecionado.getHpMax());

			adicionarLog("Inimigo selecionado: " + inimigoSelecionado.getNome() + " (Classe: "
					+ inimigoSelecionado.getClass().getSimpleName() + ")");

			return inimigoSelecionado;

		} catch (Exception e) {
			adicionarLog("Erro ao carregar inimigo do banco: " + e.getMessage());

			if (isChefe) {
				Inimigo chefe = InimigoFactory.criarInimigo("Chefe");
				if (chefe != null) {
					chefe.setHp(chefe.getHpMax());
					return chefe;
				}
			} else {
				String[] tiposNormais = { "Besta", "Ladrao", "InimigoMagico" };
				String tipoAleatorio = tiposNormais[random.nextInt(tiposNormais.length)];
				Inimigo inimigo = InimigoFactory.criarInimigo(tipoAleatorio);
				if (inimigo != null) {
					inimigo.setHp(inimigo.getHpMax());
					return inimigo;
				}
			}

			adicionarLog("ERRO CRÍTICO: Não foi possível criar nenhum inimigo");
			return null;
		}
	}

	
	private void visitarShop() {
	    if (!jornadaAtiva) {
	        JOptionPane.showMessageDialog(this, "Você precisa iniciar a jornada primeiro!", "Jornada Não Iniciada",
	                JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    adicionarLog("");
	    adicionarLog("VISITANDO O SHOP...");
	    adicionarLog("Seu ouro atual: " + jogador.getOuro());

	    TelaShop telaShop = new TelaShop(this, jogador);
	    telaShop.setVisible(true);
	    
	    adicionarLog("Voltando da loja...");
	    adicionarLog("Ouro atual: " + jogador.getOuro());
	}

	private void jornadaCompleta() {
		jornadaAtiva = false;

		adicionarLog("");
		adicionarLog("==========================================");
		adicionarLog(" * * * JORNADA COMPLETA! * * * ");
		adicionarLog("==========================================");
		adicionarLog("Parabéns, " + jogador.getNome() + "!");
		adicionarLog("Você venceu todas as 3 batalhas!");
		adicionarLog("O reino está salvo graças aos seus esforços!");
		adicionarLog("Você se tornou uma lenda!");
		adicionarLog("==========================================");

		lblStatus.setText("Status: Jornada Completa!");
		lblProgresso.setText("Progresso: MISSÃO CUMPRIDA!");

		btnProximaBatalha.setEnabled(false);
		btnShop.setEnabled(false);

		JOptionPane.showMessageDialog(this,
				" JORNADA COMPLETA! \n\n" + "Parabéns, herói!\n"
						+ "Você venceu todas as 3 batalhas e completou sua jornada!\n\n"
						+ "O reino está salvo graças aos seus esforços!\n" + "Você se tornou uma lenda!",
				"Jornada Vitoriosa", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostrarStatus() {
		StringBuilder status = new StringBuilder();
		status.append("=== STATUS DO PERSONAGEM ===\n\n");
		status.append("Nome: ").append(jogador.getNome()).append("\n");
		status.append("Classe: ").append(determinarClasse(jogador)).append("\n");
		status.append("HP: ").append(jogador.getHp()).append("/").append(jogador.getHpMax()).append("\n");
		status.append("Mana: ").append(jogador.getMana()).append("/").append(jogador.getManaMax()).append("\n");
		status.append("Ataque: ").append(jogador.getAtaque()).append("\n");
		status.append("Ouro: ").append(jogador.getOuro()).append("\n\n");

		status.append("Progresso da Jornada: ").append(batalhasVencidas).append("/3 batalhas\n");

		JOptionPane.showMessageDialog(this, status.toString(), "Status do Personagem", JOptionPane.INFORMATION_MESSAGE);
	}

	private void voltarAoMenu() {
		int resposta = JOptionPane.showConfirmDialog(this,
				"Deseja voltar ao menu principal?\n" + "Seu progresso atual será perdido.", "Voltar ao Menu",
				JOptionPane.YES_NO_OPTION);

		if (resposta == JOptionPane.YES_OPTION) {
			dispose();
			new TelaSelecao().setVisible(true);
		}
	}

	public void adicionarLog(String mensagem) {
		txtLog.append(mensagem + "\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

	protected String determinarClasse(Jogador jogador) {
		if (jogador instanceof Guerreiro) {
			return "Guerreiro";
		} else if (jogador instanceof Mago) {
			return "Mago";
		} else if (jogador instanceof Paladino) {
			return "Paladino";
		}
		return "Desconhecida";
	}
}