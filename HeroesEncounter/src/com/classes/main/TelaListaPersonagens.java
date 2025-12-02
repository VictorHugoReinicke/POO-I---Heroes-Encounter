package com.classes.main;

import com.classes.DTO.*;
import com.classes.BO.JogadorBO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaListaPersonagens extends JDialog {
	private TelaSelecao telaPai;
	private JList<String> listaPersonagens;
	private DefaultListModel<String> listModel;
	private JButton btnSelecionar;
	private JButton btnDeletar;
	private JButton btnVoltar;
	private JTextArea txtDetalhes;
	private List<Jogador> jogadores;
	private JogadorBO jogadorBO;
	private boolean modoMultiplayer = false;

	public TelaListaPersonagens(TelaSelecao pai) {
		super(pai, "Selecionar Personagem Existente", true);
		this.telaPai = pai;
		this.jogadorBO = new JogadorBO();
		initializeTela();
		carregarPersonagens();
	}

	 public void setModoMultiplayer(boolean modoMultiplayer) {
	        this.modoMultiplayer = modoMultiplayer;
	        if (modoMultiplayer) {
	            setTitle("Selecionar Personagem para Multijogador");
	        }
	    }
	
	protected void onPersonagemSelecionado(Jogador jogador) {}

	private void initializeTela() {
		setLayout(new BorderLayout(10, 10));
		setSize(700, 500);
		setLocationRelativeTo(getParent());
		setResizable(false);

		JPanel tituloPanel = new JPanel();
		tituloPanel.setBackground(new Color(30, 30, 70));
		JLabel titulo = new JLabel("SELECIONAR PERSONAGEM PARA AVENTURA");
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		titulo.setForeground(Color.WHITE);
		tituloPanel.add(titulo);

		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel listaPanel = new JPanel(new BorderLayout());
		listaPanel.setBorder(BorderFactory.createTitledBorder("Lista de Personagens"));

		listModel = new DefaultListModel<>();
		listaPersonagens = new JList<>(listModel);
		listaPersonagens.setFont(new Font("Arial", Font.PLAIN, 14));
		listaPersonagens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollLista = new JScrollPane(listaPersonagens);
		listaPanel.add(scrollLista, BorderLayout.CENTER);

		JPanel detalhesPanel = new JPanel(new BorderLayout());
		detalhesPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Personagem"));

		txtDetalhes = new JTextArea(10, 30);
		txtDetalhes.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtDetalhes.setEditable(false);
		txtDetalhes.setBackground(new Color(240, 240, 240));
		txtDetalhes.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
		detalhesPanel.add(scrollDetalhes, BorderLayout.CENTER);

		mainPanel.add(listaPanel);
		mainPanel.add(detalhesPanel);

		JPanel botoesPanel = new JPanel(new FlowLayout());
		btnSelecionar = criarBotao("INICIAR AVENTURA", new Color(50, 150, 50));
		btnDeletar = criarBotao("DELETAR PERSONAGEM", new Color(200, 50, 50));
		btnVoltar = criarBotao("VOLTAR", new Color(200, 150, 0));

		btnSelecionar.addActionListener(e -> iniciarAventura());
		btnDeletar.addActionListener(e -> deletarPersonagem());
		btnVoltar.addActionListener(e -> voltar());

		botoesPanel.add(btnSelecionar);
		botoesPanel.add(btnDeletar);
		botoesPanel.add(btnVoltar);

		listaPersonagens.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				exibirDetalhesPersonagem();
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

	private void carregarPersonagens() {
		try {
			jogadores = jogadorBO.pesquisarTodos();

			if (jogadores == null || jogadores.isEmpty()) {
				listModel.addElement("Nenhum personagem encontrado");
				btnSelecionar.setEnabled(false);
				btnDeletar.setEnabled(false);
				return;
			}

			for (Jogador jogador : jogadores) {
				String tipoClasse = determinarClasse(jogador);
				String info = String.format("%s  - %s", jogador.getNome(), tipoClasse);
				listModel.addElement(info);
			}

			if (!jogadores.isEmpty()) {
				listaPersonagens.setSelectedIndex(0);
			}

		} catch (Exception e) {
			listModel.addElement("Erro ao carregar personagens");
			btnSelecionar.setEnabled(false);
			btnDeletar.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Erro ao carregar personagens: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void exibirDetalhesPersonagem() {
		int selectedIndex = listaPersonagens.getSelectedIndex();
		if (selectedIndex >= 0 && selectedIndex < jogadores.size()) {
			Jogador jogador = jogadores.get(selectedIndex);

			StringBuilder detalhes = new StringBuilder();
			detalhes.append("Nome: ").append(jogador.getNome()).append("\n");
			detalhes.append("Classe: ").append(determinarClasse(jogador)).append("\n");
			detalhes.append("ID: ").append(jogador.getId()).append("\n\n");

			detalhes.append("HP: ").append(jogador.getHp()).append("/").append(jogador.getHpMax()).append("\n");
			detalhes.append("Mana: ").append(jogador.getMana()).append("/").append(jogador.getManaMax()).append("\n");
			detalhes.append("Ataque: ").append(jogador.getAtaque()).append("\n");
			detalhes.append("Ouro: ").append(jogador.getOuro()).append("\n\n");

			detalhes.append("Inventário:\n");
			if (jogador.getInventario() != null && !jogador.getInventario().isEmpty()) {
				for (JogadorItem ji : jogador.getInventario()) {
					String equipado = ji.isEquipado() ? " [EQUIPADO]" : "";
					detalhes.append("• ").append(ji.getItem().getNome()).append(" (x").append(ji.getQuantidade())
							.append(")").append(equipado).append("\n");
				}
			} else {
				detalhes.append("• Vazio\n");
			}

			detalhes.append("\nHabilidades:\n");
			if (jogador.getHabilidades() != null && !jogador.getHabilidades().isEmpty()) {
				for (Habilidade h : jogador.getHabilidades()) {
					detalhes.append("• ").append(h.getNome()).append(" (Dano: ").append(h.getFatorDano())
							.append(", Mana: ").append(h.getCustoMana()).append(")\n");
				}
			} else {
				detalhes.append("• Nenhuma\n");
			}

			txtDetalhes.setText(detalhes.toString());
			txtDetalhes.setCaretPosition(0);
			btnSelecionar.setEnabled(true);
			btnDeletar.setEnabled(true);
		}
	}

	private void deletarPersonagem() {
		int selectedIndex = listaPersonagens.getSelectedIndex();
		if (selectedIndex < 0 || selectedIndex >= jogadores.size()) {
			JOptionPane.showMessageDialog(this, "Por favor, selecione um personagem para deletar!",
					"Seleção Necessária", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Jogador jogadorSelecionado = jogadores.get(selectedIndex);
		String nomePersonagem = jogadorSelecionado.getNome();
		String classePersonagem = determinarClasse(jogadorSelecionado);

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"ATENÇÃO: Esta ação é PERMANENTE!\n\n" + "Tem certeza que deseja deletar o personagem?\n" + "Nome: "
						+ nomePersonagem + "\n" + "Classe: " + classePersonagem + "\n\n"
						+ "Todos os dados serão perdidos para sempre!",
				"CONFIRMAR DELEÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacao == JOptionPane.YES_OPTION) {
			try {
				boolean deletado = jogadorBO.excluir(jogadorSelecionado);

				if (deletado) {
					JOptionPane
							.showMessageDialog(this,
									"Personagem deletado com sucesso!\n" + "Nome: " + nomePersonagem + "\n"
											+ "Classe: " + classePersonagem,
									"Deleção Bem-sucedida", JOptionPane.INFORMATION_MESSAGE);

					atualizarListaAposDelecao(selectedIndex);

				} else {
					throw new Exception("Falha ao deletar personagem do banco de dados");
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Erro ao deletar personagem:\n" + e.getMessage(),
						"Erro na Deleção", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private void atualizarListaAposDelecao(int indiceDeletado) {
		try {
			jogadores.remove(indiceDeletado);

			listModel.remove(indiceDeletado);

			jogadores = jogadorBO.pesquisarTodos();

			listModel.clear();

			if (jogadores == null || jogadores.isEmpty()) {
				listModel.addElement("Nenhum personagem encontrado");
				btnSelecionar.setEnabled(false);
				btnDeletar.setEnabled(false);

				txtDetalhes.setText("");
			} else {
				for (Jogador jogador : jogadores) {
					String tipoClasse = determinarClasse(jogador);
					String info = String.format("%s  - %s", jogador.getNome(), tipoClasse);
					listModel.addElement(info);
				}

				int novoIndice = Math.min(indiceDeletado, listModel.getSize() - 1);
				if (novoIndice >= 0) {
					listaPersonagens.setSelectedIndex(novoIndice);
				} else {
					btnSelecionar.setEnabled(false);
					btnDeletar.setEnabled(false);
					txtDetalhes.setText("");
				}
			}

		} catch (Exception e) {
			System.err.println("Erro ao atualizar lista após deleção: " + e.getMessage());
			e.printStackTrace();

			carregarPersonagens();
		}
	}

	private void iniciarAventura() {
	    int selectedIndex = listaPersonagens.getSelectedIndex();
	    if (selectedIndex >= 0 && selectedIndex < jogadores.size()) {
	        Jogador jogadorSelecionado = jogadores.get(selectedIndex);
	        
	        dispose();
	        
	        onPersonagemSelecionado(jogadorSelecionado);
	                        
	        TelaAventura telaAventura = new TelaAventura(jogadorSelecionado);
	        telaAventura.setVisible(true);
	    }
	}

	private void voltar() {
		dispose();
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
}