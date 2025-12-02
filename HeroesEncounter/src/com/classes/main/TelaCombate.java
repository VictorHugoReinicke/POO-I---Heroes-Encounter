package com.classes.main;

import com.classes.BO.*;
import com.classes.DTO.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class TelaCombate extends JDialog {
	private Jogador jogador;
	private Inimigo inimigo;
	private TelaAventura telaAventura;
	private JogadorItemBO jogadorItemBO;

	private JLabel lblJogadorHP;
	private JLabel lblJogadorMana;
	private JLabel lblInimigoHP;
	private JTextArea txtLog;
	private JButton btnAtaqueNormal;
	private JButton btnHabilidades;
	private JButton btnItens;
	private JButton btnFugir;
	private JButton btnDefender;

	private boolean defesaAtiva = false;

	public TelaCombate(TelaAventura pai, Jogador jogador, Inimigo inimigo) {
		super(pai, "Combate - " + inimigo.getNome(), true);
		this.telaAventura = pai;
		this.jogador = jogador;
		this.inimigo = inimigo;

		this.jogadorItemBO = new JogadorItemBO();

		initializeTela();
		iniciarCombate();
	}

	private void initializeTela() {
		setLayout(new BorderLayout(10, 10));
		setSize(700, 550);
		setLocationRelativeTo(getParent());
		setResizable(false);

		JPanel tituloPanel = new JPanel();
		tituloPanel.setBackground(new Color(30, 30, 70));
		JLabel titulo = new JLabel("COMBATE - " + inimigo.getNome().toUpperCase());
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		titulo.setForeground(Color.WHITE);
		tituloPanel.add(titulo);

		JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		statusPanel.setBackground(Color.WHITE);

		JPanel jogadorPanel = new JPanel(new FlowLayout());
		jogadorPanel.setBackground(new Color(220, 240, 255));
		jogadorPanel.setBorder(BorderFactory.createTitledBorder(jogador.getNome()));

		lblJogadorHP = new JLabel("HP: " + jogador.getHp() + "/" + jogador.getHpMax());
		lblJogadorHP.setFont(new Font("Arial", Font.BOLD, 12));

		lblJogadorMana = new JLabel("Mana: " + jogador.getMana() + "/" + jogador.getManaMax());
		lblJogadorMana.setFont(new Font("Arial", Font.BOLD, 12));

		String armaEquipada = CalculadoraCombate.getInfoArmaEquipada(jogador);
		JLabel lblArma = new JLabel(" - " + armaEquipada);
		lblArma.setFont(new Font("Arial", Font.ITALIC, 10));
		lblArma.setForeground(Color.DARK_GRAY);

		jogadorPanel.add(lblJogadorHP);
		jogadorPanel.add(lblJogadorMana);
		jogadorPanel.add(lblArma);

		JPanel inimigoPanel = new JPanel(new FlowLayout());
		inimigoPanel.setBackground(new Color(255, 220, 220));
		inimigoPanel.setBorder(BorderFactory.createTitledBorder(inimigo.getNome()));

		lblInimigoHP = new JLabel("HP: " + inimigo.getHp() + "/" + inimigo.getHpMax());
		lblInimigoHP.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel lblInimigoAtaque = new JLabel("Ataque: " + inimigo.getAtaque());
		lblInimigoAtaque.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel lblInimigoDefesa = new JLabel("Defesa: " + inimigo.getDefesa());
		lblInimigoDefesa.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel lblTipoIA = new JLabel("IA: " + getDescricaoIA(inimigo.getTipoIA()));
		lblTipoIA.setFont(new Font("Arial", Font.ITALIC, 10));
		lblTipoIA.setForeground(Color.DARK_GRAY);

		inimigoPanel.add(lblInimigoHP);
		inimigoPanel.add(lblInimigoAtaque);
		inimigoPanel.add(lblInimigoDefesa);
		inimigoPanel.add(lblTipoIA);

		statusPanel.add(jogadorPanel);
		statusPanel.add(inimigoPanel);

		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.setBorder(BorderFactory.createTitledBorder("Log do Combate"));

		txtLog = new JTextArea(15, 50);
		txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtLog.setEditable(false);
		txtLog.setBackground(Color.BLACK);
		txtLog.setForeground(Color.WHITE);

		JScrollPane scrollLog = new JScrollPane(txtLog);
		logPanel.add(scrollLog, BorderLayout.CENTER);

		JPanel acoesPanel = new JPanel(new GridLayout(2, 3, 5, 5));
		acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações"));
		acoesPanel.setBackground(new Color(240, 240, 240));

		btnAtaqueNormal = criarBotaoCombate("️ATAQUE NORMAL", new Color(220, 60, 60));
		btnHabilidades = criarBotaoCombate("HABILIDADES", new Color(60, 120, 220));
		btnItens = criarBotaoCombate("USAR ITEM", new Color(60, 180, 120));
		btnDefender = criarBotaoCombate("️DEFENDER", new Color(200, 150, 0));
		btnFugir = criarBotaoCombate("FUGIR", new Color(100, 100, 100));

		btnAtaqueNormal.addActionListener(e -> ataqueNormal());
		btnHabilidades.addActionListener(e -> usarHabilidade());
		btnItens.addActionListener(e -> usarItem());
		btnDefender.addActionListener(e -> defender());
		btnFugir.addActionListener(e -> tentarFugir());

		acoesPanel.add(btnAtaqueNormal);
		acoesPanel.add(btnHabilidades);
		acoesPanel.add(btnItens);
		acoesPanel.add(btnDefender);
		acoesPanel.add(btnFugir);

		add(tituloPanel, BorderLayout.NORTH);
		add(statusPanel, BorderLayout.CENTER);
		add(logPanel, BorderLayout.EAST);
		add(acoesPanel, BorderLayout.SOUTH);
	}

	private String getDescricaoIA(com.classes.Enums.TipoIA tipo) {
		if (tipo == null) return "Desconhecida";
		
		switch (tipo) {
			case AGRESSIVO: return "️Agressivo";
			case DEFENSIVA: return "️Defensivo";
			case ESTRATEGICA: return "Estratégico";
			case BALANCEADO: return "️Balanceado";
			case ALEATORIA: return "Aleatório";
			case CHEFE: return "Chefe";
			default: return "Desconhecida";
		}
	}

	private JButton criarBotaoCombate(String texto, Color cor) {
		JButton botao = new JButton(texto);
		botao.setBackground(cor);
		botao.setForeground(Color.WHITE);
		botao.setFont(new Font("Arial", Font.BOLD, 12));
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createRaisedBevelBorder());
		botao.setPreferredSize(new Dimension(160, 50));

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

	private void iniciarCombate() {
		atualizarInventarioJogador();

		adicionarLog("COMBATE INICIADO!");
		adicionarLog(jogador.getNome() + " vs " + inimigo.getNome());
		adicionarLog("IA do inimigo: " + getDescricaoIA(inimigo.getTipoIA()));

		adicionarLog("STATUS DA BATALHA:");
		adicionarLog("Jogador Ataque: " + jogador.getAtaque());
		adicionarLog("Inimigo Defesa: " + inimigo.getDefesa());
		adicionarLog("Arma Equipada: " + CalculadoraCombate.getInfoArmaEquipada(jogador));

		// MOSTRAR INVENTÁRIO ATUAL
		if (jogador.getInventario() != null) {
			adicionarLog("Itens no inventário: " + jogador.getInventario().size());
			for (JogadorItem ji : jogador.getInventario()) {
				if (ji.getItem() instanceof ItemConsumivel) {
					adicionarLog("   - " + ji.getItem().getNome() + " (x" + ji.getQuantidade() + ")");
				}
			}
		}

		adicionarLog("==================================");
		atualizarStatus();
	}

	protected void ataqueNormal() {
		desabilitarBotoes();

		String infoArma = CalculadoraCombate.getInfoArmaEquipada(jogador);
		adicionarLog(infoArma);

		ResultadoAtaque resultado = CalculadoraCombate.calcularAtaqueFisico(jogador, inimigo);
		int dano = resultado.getDano();

		inimigo.setHp(inimigo.getHp() - dano);

		if (resultado.isCritico()) {
			adicionarLog("**CRÍTICO!** " + jogador.getNome() + " ataca causando " + dano + " de dano!");
		} else {
			adicionarLog(" - " + jogador.getNome() + " ataca causando " + dano + " de dano!");
		}

		if (inimigo.getHp() <= 0) {
			vitoria();
		} else {
			turnoInimigo();
		}

		atualizarStatus();
	}

	protected void usarHabilidade() {
		desabilitarBotoes();

		List<Habilidade> habilidades = jogador.getHabilidades();
		if (habilidades == null || habilidades.isEmpty()) {
			adicionarLog("Você não possui habilidades!");
			habilitarBotoes();
			return;
		}

		String[] opcoesHabilidades = new String[habilidades.size()];
		for (int i = 0; i < habilidades.size(); i++) {
			Habilidade h = habilidades.get(i);
			opcoesHabilidades[i] = h.getNome() + " (Mana: " + h.getCustoMana() + ")";
		}

		int escolha = JOptionPane.showOptionDialog(this, "Escolha uma habilidade:",
				"Habilidades - " + jogador.getNome(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				opcoesHabilidades, opcoesHabilidades[0]);

		if (escolha >= 0 && escolha < habilidades.size()) {
			Habilidade habilidade = habilidades.get(escolha);

			ResultadoHabilidade resultado = GerenciadorHabilidades.executarHabilidade(jogador, inimigo, habilidade);

			if (!resultado.isSucesso()) {
				adicionarLog(" - " + resultado.getMensagem());
				habilitarBotoes();
				return;
			}

			adicionarLog(" - " + jogador.getNome() + " usa " + resultado.getHabilidadeUsada() + "!");

			// Mostrar resultados
			if (resultado.getDanoCausado() > 0) {
				if (resultado.isCritico()) {
					adicionarLog("**CRÍTICO!** Causa " + resultado.getDanoCausado() + " de dano!");
				} else {
					adicionarLog("Causa " + resultado.getDanoCausado() + " de dano!");
				}
			}

			if (resultado.getCuraAplicada() > 0) {
				adicionarLog("Cura " + resultado.getCuraAplicada() + " de HP!");
			}

			if (resultado.getStatusAplicado() != null) {
				adicionarLog("Aplica " + resultado.getStatusAplicado() + "!");
			}

			adicionarLog("Gasto de mana: -" + habilidade.getCustoMana());

			if (inimigo.getHp() <= 0) {
				vitoria();
			} else {
				turnoInimigo();
			}
		} else {
			habilitarBotoes();
		}

		atualizarStatus();
	}

	protected void usarItem() {
		desabilitarBotoes();

		List<JogadorItem> inventario = jogador.getInventario();
		if (inventario == null || inventario.isEmpty()) {
			adicionarLog("Seu inventário está vazio!");
			habilitarBotoes();
			return;
		}

		java.util.List<JogadorItem> itensConsumiveis = new java.util.ArrayList<>();
		for (JogadorItem ji : inventario) {
			if (ji.getItem() instanceof ItemConsumivel && ji.getQuantidade() > 0) {
				itensConsumiveis.add(ji);
			}
		}

		if (itensConsumiveis.isEmpty()) {
			adicionarLog("Nenhum item consumível no inventário!");
			habilitarBotoes();
			return;
		}

		String[] opcoesItens = new String[itensConsumiveis.size()];
		for (int i = 0; i < itensConsumiveis.size(); i++) {
			JogadorItem ji = itensConsumiveis.get(i);
			opcoesItens[i] = ji.getItem().getNome() + " (x" + ji.getQuantidade() + ")";
		}

		int escolha = JOptionPane.showOptionDialog(this, "Escolha um item para usar:",
				"Inventário - " + jogador.getNome(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				opcoesItens, opcoesItens[0]);

		if (escolha >= 0 && escolha < itensConsumiveis.size()) {
			JogadorItem itemSelecionado = itensConsumiveis.get(escolha);
			ItemConsumivel item = (ItemConsumivel) itemSelecionado.getItem();

			if (item.getCura() > 0) {
				int cura = Math.min(jogador.getHpMax() - jogador.getHp(), item.getCura());
				jogador.setHp(jogador.getHp() + cura);
				adicionarLog("Usou " + item.getNome() + "! +" + cura + " HP");
			}
			if (item.getMana() > 0) {
				int mana = Math.min(jogador.getManaMax() - jogador.getMana(), item.getMana());
				jogador.setMana(jogador.getMana() + mana);
				adicionarLog("Usou " + item.getNome() + "! +" + mana + " Mana");
			}

			boolean itemRemovido = jogadorItemBO.usarItem(jogador.getId(), item.getId());

			if (itemRemovido) {
				itemSelecionado.setQuantidade(itemSelecionado.getQuantidade() - 1);

				if (itemSelecionado.getQuantidade() <= 0) {
					jogador.getInventario().remove(itemSelecionado);
					adicionarLog(" - " + item.getNome() + " esgotado!");
				} else {
					adicionarLog("Restam " + itemSelecionado.getQuantidade() + " " + item.getNome());
				}
			} else {
				adicionarLog("Erro ao remover item do inventário");
			}

			turnoInimigo();
		} else {
			habilitarBotoes();
		}

		atualizarStatus();
	}

	protected void defender() {
		desabilitarBotoes();

		defesaAtiva = true;
		adicionarLog(" - " + jogador.getNome() + " assume posição defensiva!");
		adicionarLog("Próximo ataque inimigo será reduzido em 50%!");

		turnoInimigo();
		atualizarStatus();
	}

	protected void tentarFugir() {
		desabilitarBotoes();

		double chanceFuga = 0.6; // 60% de chance de fugir
		if (Math.random() < chanceFuga) {
			adicionarLog(" - " + jogador.getNome() + " fugiu do combate!");
			JOptionPane.showMessageDialog(this, "Fuga bem-sucedida!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			adicionarLog(" - " + jogador.getNome() + " falhou ao tentar fugir!");
			turnoInimigo();
			atualizarStatus();
		}
	}

	protected void turnoInimigo() {
		if (inimigo.getHp() <= 0) return;
		
		boolean esquivou = GerenciadorStatus.verificarEsquiva(jogador);
		
		if (esquivou) {
			adicionarLog("*ILUSÃO ATIVA!** " + jogador.getNome() + " se esquivou completamente do ataque!");
			habilitarBotoes();
			return;
		}
		
		// PROCESSAR DOTs no inimigo
		adicionarLog("Processando efeitos de status no inimigo...");
		int danoDOT = GerenciadorHabilidades.processarInicioTurnoInimigo(inimigo);
		
		if (danoDOT > 0) {
			adicionarLog(" - " + inimigo.getNome() + " sofre " + danoDOT + " de dano por efeitos!");
		}
		
		if (inimigo.getHp() <= 0) {
			adicionarLog(" - " + inimigo.getNome() + " sucumbiu aos efeitos!");
			vitoria();
			return;
		}
		
		adicionarLog("\n- " + inimigo.getNome() + " está pensando... (" + inimigo.getTipoIA() + ")");
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		try {
			String acaoEscolhida = GeminiAI.decidirAcao(inimigo, jogador);
			String acaoTraduzida = traduzirAcao(acaoEscolhida);
			adicionarLog(" - " + inimigo.getNome() + " escolheu: " + acaoTraduzida);
			executarAcaoInimigoIA(acaoEscolhida);
		} catch (Exception e) {
			adicionarLog("Erro na IA Gemini: " + e.getMessage());
			adicionarLog("Usando ataque normal como fallback");
			executarAtaqueNormalInimigo();
		}
		
		GerenciadorHabilidades.processarFimDeTurno(jogador, inimigo);
		
		if (jogador.getHp() <= 0) {
			derrota();
		} else {
			habilitarBotoes();
		}
		
		atualizarStatus();
	}
	
	private void executarAcaoInimigoIA(String acao) {
		switch (acao) {
			case "ATAQUE_NORMAL":
				executarAtaqueNormalInimigo();
				break;
			case "ATAQUE_PODEROSO":
			case "ATAQUE_FEROZ":
				executarAtaquePoderoso();
				break;
			case "DEFENDER":
				executarDefesaInimigo();
				break;
			case "GRITAR":
			case "RUGIDO":
			case "GRITAR_GUERRA":
				executarGrito();
				break;
			case "ATAQUE_ESPECIAL_CHEFE":
				executarAtaqueEspecialChefe();
				break;
			case "CURAR":
			case "REGENERAR":
				executarCuraInimigo();
				break;
			case "BUFF_DEFESA":
			case "DEFESA_TOTAL":
				executarBuffDefesa();
				break;
			case "DEBUFF_JOGADOR":
				executarDebuffJogador();
				break;
			case "FUGIR":
				executarFugaInimigo();
				break;
			case "FEITICO_MAGICO":
				executarFeiticoMagico();
				break;
			case "ATAQUE_RAPIDO":
				executarAtaqueRapido();
				break;
			default:
				executarAtaqueNormalInimigo();
		}
	}
	
	private void executarAtaqueNormalInimigo() {
		ResultadoAtaque resultado = CalculadoraCombate.calcularAtaqueInimigo(inimigo, jogador);
		int danoInimigo = resultado.getDano();
		
		if (defesaAtiva) {
			int danoOriginal = danoInimigo;
			danoInimigo = Math.max(1, danoInimigo / 2);
			adicionarLog("Sua defesa reduz o dano de " + danoOriginal + " para " + danoInimigo + "!");
			defesaAtiva = false;
		}
		
		jogador.setHp(jogador.getHp() - danoInimigo);
		
		if (resultado.isCritico()) {
			adicionarLog("**CRÍTICO INIMIGO!** " + inimigo.getNome() + " ataca causando " + danoInimigo + " de dano!");
		} else {
			adicionarLog(" - " + inimigo.getNome() + " ataca causando " + danoInimigo + " de dano!");
		}
	}
	
	private void executarAtaquePoderoso() {
		ResultadoAtaque resultado = CalculadoraCombate.calcularAtaqueInimigo(inimigo, jogador);
		int danoBase = resultado.getDano();
		int danoExtra = (int)(danoBase * 1.5); // 50% mais dano
		
		if (defesaAtiva) {
			danoExtra = Math.max(1, danoExtra / 2);
			adicionarLog("Sua defesa reduz parte do dano poderoso!");
			defesaAtiva = false;
		}
		
		jogador.setHp(jogador.getHp() - danoExtra);
		
		if (resultado.isCritico()) {
			adicionarLog("**ATAQUE PODEROSO CRÍTICO!** Causa " + danoExtra + " de dano devastador!");
		} else {
			adicionarLog("Causa " + danoExtra + " de dano!");
		}
	}
	
	private void executarAtaqueRapido() {
		adicionarLog(" - " + inimigo.getNome() + " ataca rapidamente!");
		for (int i = 0; i < 2; i++) {
			int dano = inimigo.getAtaque() / 2;
			jogador.setHp(jogador.getHp() - dano);
			adicionarLog("Ataque " + (i+1) + ": " + dano + " de dano!");
		}
	}
	
	private void executarFeiticoMagico() {
		adicionarLog(" - " + inimigo.getNome() + " lança um feitiço mágico!");
		int danoMagico = inimigo.getAtaque() * 2;
		
		if (defesaAtiva) {
			danoMagico = Math.max(1, danoMagico / 2);
			adicionarLog("Sua defesa reduz parte do dano mágico!");
			defesaAtiva = false;
		}
		
		jogador.setHp(jogador.getHp() - danoMagico);
		adicionarLog("Dano mágico: " + danoMagico + "!");
	}
	
	private void executarDefesaInimigo() {
		adicionarLog(" - " + inimigo.getNome() + " assume posição defensiva!");
		adicionarLog("Próximo ataque do jogador será reduzido!");
		
		// Aumenta defesa temporariamente para o próximo turno
		inimigo.setDefesa(inimigo.getDefesa() + 10);
		adicionarLog("Defesa aumentada em 10 pontos!");
	}
	
	private void executarGrito() {
		adicionarLog(" - " + inimigo.getNome() + " solta um grito ensurdecedor!");
		adicionarLog("Jogador fica atordoado! Ataque reduzido no próximo turno!");
		
		// Reduz ataque do jogador temporariamente
		jogador.setAtaque(Math.max(1, jogador.getAtaque() - 5));
		adicionarLog("Seu ataque foi reduzido em 5 pontos!");
	}
	
	private void executarAtaqueEspecialChefe() {
		adicionarLog(" - " + inimigo.getNome() + " usa ATAQUE ESPECIAL DO CHEFE!");
		
		int dano = inimigo.getAtaque() * 2; // Dano dobrado
		if (defesaAtiva) {
			dano = Math.max(1, dano / 3);
			adicionarLog("Sua defesa reduz significativamente o dano especial!");
			defesaAtiva = false;
		}
		
		jogador.setHp(jogador.getHp() - dano);
		adicionarLog("DANO COLOSSAL: " + dano + "!");
		
		// Efeito adicional
		jogador.setAtaque(Math.max(1, jogador.getAtaque() - 3));
		adicionarLog("Você ficou atordoado! Ataque reduzido!");
	}
	
	private void executarCuraInimigo() {
		int cura = (int)(inimigo.getHpMax() * 0.2); // Cura 20% do HP máximo
		int novoHP = Math.min(inimigo.getHpMax(), inimigo.getHp() + cura);
		int hpCura = novoHP - inimigo.getHp();
		inimigo.setHp(novoHP);
		
		adicionarLog(" - " + inimigo.getNome() + " se cura em " + hpCura + " HP!");
		adicionarLog("HP atual: " + inimigo.getHp() + "/" + inimigo.getHpMax());
	}
	
	private void executarBuffDefesa() {
		int aumentoDefesa = 15;
		inimigo.setDefesa(inimigo.getDefesa() + aumentoDefesa);
		
		adicionarLog(" - " + inimigo.getNome() + " fortalece sua defesa!");
		adicionarLog("Defesa aumentada em " + aumentoDefesa + " pontos!");
	}
	
	private void executarDebuffJogador() {
		adicionarLog(" - " + inimigo.getNome() + " enfraquece você!");
		
		int ataqueAnterior = jogador.getAtaque();
		jogador.setAtaque(Math.max(1, jogador.getAtaque() - 8));
		int reducaoAtaque = ataqueAnterior - jogador.getAtaque();
		
		adicionarLog("Ataque reduzido em " + reducaoAtaque + " pontos!");
		
		try {
			java.lang.reflect.Method getDefesaMethod = jogador.getClass().getMethod("getDefesa");
			java.lang.reflect.Method setDefesaMethod = jogador.getClass().getMethod("setDefesa", int.class);
			
			int defesaAtual = (int) getDefesaMethod.invoke(jogador);
			if (defesaAtual > 0) {
				int novaDefesa = Math.max(0, defesaAtual - 5);
				setDefesaMethod.invoke(jogador, novaDefesa);
				adicionarLog("Defesa reduzida em " + (defesaAtual - novaDefesa) + " pontos!");
			}
		} catch (Exception e) {
		}
	}
	
	private void executarFugaInimigo() {
		adicionarLog(" - " + inimigo.getNome() + " tenta fugir!");
		if (new Random().nextDouble() < 0.4) { // 40% de chance
			adicionarLog(" - " + inimigo.getNome() + " fugiu do combate!");
			dispose();
			if (telaAventura != null) {
				telaAventura.adicionarLog(" - " + inimigo.getNome() + " fugiu do combate!");
			}
		} else {
			adicionarLog(" - " + inimigo.getNome() + " falhou em fugir!");
		}
	}
	
	private String traduzirAcao(String acao) {
		switch (acao) {
			case "ATAQUE_NORMAL": return "Ataque Normal";
			case "ATAQUE_PODEROSO": return "Ataque Poderoso";
			case "ATAQUE_FEROZ": return "Ataque Feroz";
			case "ATAQUE_RAPIDO": return "Ataque Rápido";
			case "DEFENDER": return "Defesa";
			case "GRITAR": return "Grito de Batalha";
			case "RUGIDO": return "Rugido";
			case "GRITAR_GUERRA": return "Grito de Guerra";
			case "ATAQUE_ESPECIAL_CHEFE": return "Ataque Especial do Chefe";
			case "CURAR": return "Cura";
			case "REGENERAR": return "Regeneração";
			case "BUFF_DEFESA": return "Aumentar Defesa";
			case "DEFESA_TOTAL": return "Defesa Total";
			case "DEBUFF_JOGADOR": return "Enfraquecer Jogador";
			case "FUGIR": return "Fuga";
			case "FEITICO_MAGICO": return "Feitiço Mágico";
			default: return acao.replace("_", " ").toLowerCase();
		}
	}

	protected void vitoria() {
		adicionarLog(" - " + inimigo.getNome() + " foi derrotado!");
		adicionarLog(" - Recompensa: +" + inimigo.getRecompensaOuro() + " de ouro!");

		jogador.setOuro(jogador.getOuro() + inimigo.getRecompensaOuro());

		int expGanha = 25;

		adicionarLog("Experiência ganha: +" + expGanha + " XP");

		JOptionPane.showMessageDialog(this,
				"VITÓRIA!\n\n" + "Você derrotou " + inimigo.getNome() + "!\n" + "Recompensa: +"
						+ inimigo.getRecompensaOuro() + " de ouro!\n" + "Experiência: +" + expGanha + " XP",
				"Vitória", JOptionPane.INFORMATION_MESSAGE);

		dispose();
		telaAventura.batalhaVencida();
	}

	protected void derrota() {
		adicionarLog(" - " + jogador.getNome() + " foi derrotado...");

		JOptionPane.showMessageDialog(this,
				"DERROTA!\n\n" + "Você foi derrotado por " + inimigo.getNome() + "...\n" + "A jornada termina aqui.",
				"Derrota", JOptionPane.ERROR_MESSAGE);

		dispose();
		telaAventura.batalhaPerdida();
	}

	private void atualizarInventarioJogador() {
		try {
			JogadorItemBO jogadorItemBO = new JogadorItemBO();
			List<JogadorItem> inventarioAtual = jogadorItemBO.listarItensPorJogador(jogador.getId());
			jogador.setInventario(inventarioAtual);
			adicionarLog("Inventário atualizado!");
		} catch (Exception e) {
			adicionarLog("Erro ao atualizar inventário: " + e.getMessage());
		}
	}

	protected void atualizarStatus() {
		lblJogadorHP.setText("HP: " + jogador.getHp() + "/" + jogador.getHpMax());
		lblJogadorMana.setText("Mana: " + jogador.getMana() + "/" + jogador.getManaMax());
		lblInimigoHP.setText("HP: " + Math.max(0, inimigo.getHp()) + "/" + inimigo.getHpMax());

		double percentualVida = (double) inimigo.getHp() / inimigo.getHpMax();
		if (percentualVida <= 0.25) {
			lblInimigoHP.setForeground(Color.RED);
		} else if (percentualVida <= 0.5) {
			lblInimigoHP.setForeground(Color.ORANGE);
		} else {
			lblInimigoHP.setForeground(Color.BLACK);
		}
	}

	protected void adicionarLog(String mensagem) {
		txtLog.append(mensagem + "\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

	protected void desabilitarBotoes() {
		btnAtaqueNormal.setEnabled(false);
		btnHabilidades.setEnabled(false);
		btnItens.setEnabled(false);
		btnDefender.setEnabled(false);
		btnFugir.setEnabled(false);
	}

	protected void habilitarBotoes() {
		btnAtaqueNormal.setEnabled(true);
		btnHabilidades.setEnabled(jogador.getHabilidades() != null && !jogador.getHabilidades().isEmpty());
		btnItens.setEnabled(jogador.getInventario() != null && !jogador.getInventario().isEmpty());
		btnDefender.setEnabled(true);
		btnFugir.setEnabled(true);
	}

	protected Jogador getJogador() {
		return jogador;
	}

	protected Inimigo getInimigo() {
		return inimigo;
	}

	protected boolean isDefesaAtiva() {
		return defesaAtiva;
	}

	protected void setDefesaAtiva(boolean defesaAtiva) {
		this.defesaAtiva = defesaAtiva;
	}
}