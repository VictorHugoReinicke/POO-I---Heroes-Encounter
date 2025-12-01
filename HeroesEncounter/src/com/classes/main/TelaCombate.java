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

	// Componentes da interface
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

		// Painel de título
		JPanel tituloPanel = new JPanel();
		tituloPanel.setBackground(new Color(30, 30, 70));
		JLabel titulo = new JLabel("COMBATE - " + inimigo.getNome().toUpperCase());
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		titulo.setForeground(Color.WHITE);
		tituloPanel.add(titulo);

		// Painel de status
		JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		statusPanel.setBackground(Color.WHITE);

		// Status do Jogador
		JPanel jogadorPanel = new JPanel(new FlowLayout());
		jogadorPanel.setBackground(new Color(220, 240, 255));
		jogadorPanel.setBorder(BorderFactory.createTitledBorder(jogador.getNome()));

		lblJogadorHP = new JLabel("❤️ HP: " + jogador.getHp() + "/" + jogador.getHpMax());
		lblJogadorHP.setFont(new Font("Arial", Font.BOLD, 12));

		lblJogadorMana = new JLabel("🔵 Mana: " + jogador.getMana() + "/" + jogador.getManaMax());
		lblJogadorMana.setFont(new Font("Arial", Font.BOLD, 12));

		// Mostrar arma equipada
		String armaEquipada = CalculadoraCombate.getInfoArmaEquipada(jogador);
		JLabel lblArma = new JLabel("⚔️ " + armaEquipada);
		lblArma.setFont(new Font("Arial", Font.ITALIC, 10));
		lblArma.setForeground(Color.DARK_GRAY);

		jogadorPanel.add(lblJogadorHP);
		jogadorPanel.add(lblJogadorMana);
		jogadorPanel.add(lblArma);

		// Status do Inimigo
		JPanel inimigoPanel = new JPanel(new FlowLayout());
		inimigoPanel.setBackground(new Color(255, 220, 220));
		inimigoPanel.setBorder(BorderFactory.createTitledBorder(inimigo.getNome()));

		lblInimigoHP = new JLabel("💀 HP: " + inimigo.getHp() + "/" + inimigo.getHpMax());
		lblInimigoHP.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel lblInimigoAtaque = new JLabel("⚔️ Ataque: " + inimigo.getAtaque());
		lblInimigoAtaque.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel lblInimigoDefesa = new JLabel("🛡️ Defesa: " + inimigo.getDefesa());
		lblInimigoDefesa.setFont(new Font("Arial", Font.BOLD, 12));

		inimigoPanel.add(lblInimigoHP);
		inimigoPanel.add(lblInimigoAtaque);
		inimigoPanel.add(lblInimigoDefesa);

		statusPanel.add(jogadorPanel);
		statusPanel.add(inimigoPanel);

		// Painel de log
		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.setBorder(BorderFactory.createTitledBorder("Log do Combate"));

		txtLog = new JTextArea(15, 50);
		txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtLog.setEditable(false);
		txtLog.setBackground(Color.BLACK);
		txtLog.setForeground(Color.WHITE);

		JScrollPane scrollLog = new JScrollPane(txtLog);
		logPanel.add(scrollLog, BorderLayout.CENTER);

		// Painel de ações
		JPanel acoesPanel = new JPanel(new GridLayout(2, 3, 5, 5));
		acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações"));
		acoesPanel.setBackground(new Color(240, 240, 240));

		btnAtaqueNormal = criarBotaoCombate("⚔️ ATAQUE NORMAL", new Color(220, 60, 60));
		btnHabilidades = criarBotaoCombate("✨ HABILIDADES", new Color(60, 120, 220));
		btnItens = criarBotaoCombate("🎒 USAR ITEM", new Color(60, 180, 120));
		btnDefender = criarBotaoCombate("🛡️ DEFENDER", new Color(200, 150, 0));
		btnFugir = criarBotaoCombate("🏃 FUGIR", new Color(100, 100, 100));

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
		
		
		JLabel lblTipoIA = new JLabel("🧠 IA: " + InimigoIA.getDescricaoIA(inimigo.getTipoIA()));
        lblTipoIA.setFont(new Font("Arial", Font.ITALIC, 10));
        lblTipoIA.setForeground(Color.DARK_GRAY);
        inimigoPanel.add(lblTipoIA);
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
		// ✅ ATUALIZAR INVENTÁRIO ANTES DO COMBATE
		atualizarInventarioJogador();

		adicionarLog("⚔️ COMBATE INICIADO!");
		adicionarLog(jogador.getNome() + " vs " + inimigo.getNome());

		// DEBUG: MOSTRAR STATUS COMPLETO
		adicionarLog("🎯 STATUS DA BATALHA:");
		adicionarLog("Jogador Ataque: " + jogador.getAtaque());
		adicionarLog("Inimigo Defesa: " + inimigo.getDefesa());
		adicionarLog("Arma Equipada: " + CalculadoraCombate.getInfoArmaEquipada(jogador));

		// MOSTRAR INVENTÁRIO ATUAL
		if (jogador.getInventario() != null) {
			adicionarLog("🎒 Itens no inventário: " + jogador.getInventario().size());
			// Debug: mostrar itens específicos
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

		// MOSTRAR ARMA EQUIPADA
		String infoArma = CalculadoraCombate.getInfoArmaEquipada(jogador);
		adicionarLog(infoArma);

		// USAR CALCULADORA DE COMBATE - CORRETO!
		ResultadoAtaque resultado = CalculadoraCombate.calcularAtaqueFisico(jogador, inimigo);
		int dano = resultado.getDano();

		inimigo.setHp(inimigo.getHp() - dano);

		// MENSAGEM COM CRÍTICO
		if (resultado.isCritico()) {
			adicionarLog("💥 **CRÍTICO!** " + jogador.getNome() + " ataca causando " + dano + " de dano!");
		} else {
			adicionarLog("⚔️ " + jogador.getNome() + " ataca causando " + dano + " de dano!");
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
			adicionarLog("❌ Você não possui habilidades!");
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

			// USAR NOVO SISTEMA DE HABILIDADES
			ResultadoHabilidade resultado = GerenciadorHabilidades.executarHabilidade(jogador, inimigo, habilidade);

			if (!resultado.isSucesso()) {
				adicionarLog("❌ " + resultado.getMensagem());
				habilitarBotoes();
				return;
			}

			adicionarLog("✨ " + jogador.getNome() + " usa " + resultado.getHabilidadeUsada() + "!");

			// Mostrar resultados
			if (resultado.getDanoCausado() > 0) {
				if (resultado.isCritico()) {
					adicionarLog("💥 **CRÍTICO!** Causa " + resultado.getDanoCausado() + " de dano!");
				} else {
					adicionarLog("💫 Causa " + resultado.getDanoCausado() + " de dano!");
				}
			}

			if (resultado.getCuraAplicada() > 0) {
				adicionarLog("💖 Cura " + resultado.getCuraAplicada() + " de HP!");
			}

			if (resultado.getStatusAplicado() != null) {
				adicionarLog("⚡ Aplica " + resultado.getStatusAplicado() + "!");
			}

			adicionarLog("🔵 Gasto de mana: -" + habilidade.getCustoMana());

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
			adicionarLog("❌ Seu inventário está vazio!");
			habilitarBotoes();
			return;
		}

		// Filtrar itens consumíveis
		java.util.List<JogadorItem> itensConsumiveis = new java.util.ArrayList<>();
		for (JogadorItem ji : inventario) {
			if (ji.getItem() instanceof ItemConsumivel && ji.getQuantidade() > 0) {
				itensConsumiveis.add(ji);
			}
		}

		if (itensConsumiveis.isEmpty()) {
			adicionarLog("❌ Nenhum item consumível no inventário!");
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

			// Aplicar efeito do item
			if (item.getCura() > 0) {
				int cura = Math.min(jogador.getHpMax() - jogador.getHp(), item.getCura());
				jogador.setHp(jogador.getHp() + cura);
				adicionarLog("💖 Usou " + item.getNome() + "! +" + cura + " HP");
			}
			if (item.getMana() > 0) {
				int mana = Math.min(jogador.getManaMax() - jogador.getMana(), item.getMana());
				jogador.setMana(jogador.getMana() + mana);
				adicionarLog("🔵 Usou " + item.getNome() + "! +" + mana + " Mana");
			}

			// ✅ CORREÇÃO: REMOVER ITEM PERMANENTEMENTE DO BANCO
			boolean itemRemovido = jogadorItemBO.usarItem(jogador.getId(), item.getId());

			if (itemRemovido) {
				// ✅ ATUALIZAR INVENTÁRIO LOCAL
				itemSelecionado.setQuantidade(itemSelecionado.getQuantidade() - 1);

				// Se quantidade chegou a zero, remover da lista local
				if (itemSelecionado.getQuantidade() <= 0) {
					jogador.getInventario().remove(itemSelecionado);
					adicionarLog("🎒 " + item.getNome() + " esgotado!");
				} else {
					adicionarLog("🎒 Restam " + itemSelecionado.getQuantidade() + " " + item.getNome());
				}
			} else {
				adicionarLog("⚠️ Erro ao remover item do inventário");
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
		adicionarLog("🛡️ " + jogador.getNome() + " assume posição defensiva!");
		adicionarLog("🎯 Próximo ataque inimigo será reduzido em 50%!");

		turnoInimigo();
		atualizarStatus();
	}

	protected void tentarFugir() {
		desabilitarBotoes();

		double chanceFuga = 0.6; // 60% de chance de fugir
		if (Math.random() < chanceFuga) {
			adicionarLog("🏃 " + jogador.getNome() + " fugiu do combate!");
			JOptionPane.showMessageDialog(this, "Fuga bem-sucedida!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			adicionarLog("❌ " + jogador.getNome() + " falhou ao tentar fugir!");
			turnoInimigo();
			atualizarStatus();
		}
	}

	protected void turnoInimigo() {
        if (inimigo.getHp() <= 0) return;
        
        // ✅ VERIFICAR ESQUIVA (ILUSÃO)
        boolean esquivou = GerenciadorStatus.verificarEsquiva(jogador);
        
        if (esquivou) {
            adicionarLog("🎭 **ILUSÃO ATIVA!** " + jogador.getNome() + " se esquivou completamente do ataque!");
            habilitarBotoes();
            return;
        }
        
        // PROCESSAR DOTs no inimigo
        adicionarLog("⚡ Processando efeitos de status no inimigo...");
        int danoDOT = GerenciadorHabilidades.processarInicioTurnoInimigo(inimigo);
        
        if (danoDOT > 0) {
            adicionarLog("🔥 " + inimigo.getNome() + " sofre " + danoDOT + " de dano por efeitos!");
        }
        
        if (inimigo.getHp() <= 0) {
            adicionarLog("💀 " + inimigo.getNome() + " sucumbiu aos efeitos!");
            vitoria();
            return;
        }
        
        // ✅ DECISÃO POR IA BASEADA NO TIPO
        adicionarLog("\n🤖 " + inimigo.getNome() + " está pensando... (" + inimigo.getTipoIA() + ")");
        
        // Pequeno delay para dramatização
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Obter decisão da IA
        String acaoEscolhida = inimigo.decidirAcao(jogador);
        executarAcaoInimigoIA(acaoEscolhida);
        
        // PROCESSAR FIM DE TURNO
        GerenciadorHabilidades.processarFimDeTurno(jogador, inimigo);
        
        if (jogador.getHp() <= 0) {
            derrota();
        } else {
            habilitarBotoes();
        }
        
        atualizarStatus();
    }
	
	 private void executarAcaoInimigoIA(String acao) {
	        adicionarLog("🎯 " + inimigo.getNome() + " escolheu: " + traduzirAcao(acao));
	        
	        switch (acao) {
	            case "ATAQUE_NORMAL":
	                executarAtaqueNormal();
	                break;
	            case "ATAQUE_PODEROSO":
	                executarAtaquePoderoso();
	                break;
	            case "DEFENDER":
	                executarDefesaInimigo();
	                break;
	            case "GRITAR":
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
	            default:
	                executarAtaqueNormal(); // Fallback
	        }
	    }
	 
	 private void executarAtaqueNormal() {
	        // ... implementação existente do ataque normal ...
	    }
	 
	   private String traduzirAcao(String acao) {
	        switch (acao) {
	            case "ATAQUE_NORMAL": return "Ataque Normal";
	            case "ATAQUE_PODEROSO": return "Ataque Poderoso ⚔️";
	            case "DEFENDER": return "Defesa 🛡️";
	            case "GRITAR": return "Grito de Batalha 🗣️";
	            case "GRITAR_GUERRA": return "Grito de Guerra 👑";
	            case "ATAQUE_ESPECIAL_CHEFE": return "Ataque Especial do Chefe 💀";
	            case "CURAR": return "Cura 💚";
	            case "REGENERAR": return "Regeneração ✨";
	            case "BUFF_DEFESA": return "Aumentar Defesa 🔼";
	            case "DEFESA_TOTAL": return "Defesa Total 🛡️🛡️";
	            case "DEBUFF_JOGADOR": return "Enfraquecer Jogador ⬇️";
	            case "FUGIR": return "Fuga 🏃";
	            default: return acao.replace("_", " ").toLowerCase();
	        }
	    }
	    
	    private void executarAtaquePoderoso() {
	        ResultadoAtaque resultado = CalculadoraCombate.calcularAtaqueInimigo(inimigo, jogador);
	        int danoBase = resultado.getDano();
	        int danoExtra = (int)(danoBase * 1.5); // 50% mais dano
	        
	        if (defesaAtiva) {
	            danoExtra = Math.max(1, danoExtra / 2);
	            adicionarLog("🛡️ Sua defesa reduz parte do dano poderoso!");
	            defesaAtiva = false;
	        }
	        
	        jogador.setHp(jogador.getHp() - danoExtra);
	        
	        if (resultado.isCritico()) {
	            adicionarLog("💥💥 **ATAQUE PODEROSO CRÍTICO!** " + inimigo.getNome() + 
	                " causa " + danoExtra + " de dano devastador!");
	        } else {
	            adicionarLog("💥 " + inimigo.getNome() + " desfere um ataque poderoso causando " + 
	                danoExtra + " de dano!");
	        }
	    }
	    
	    private void executarDefesaInimigo() {
	        adicionarLog("🛡️ " + inimigo.getNome() + " assume posição defensiva!");
	        adicionarLog("🎯 Próximo ataque do jogador será reduzido!");
	        
	        // Aumenta defesa temporariamente para o próximo turno
	        inimigo.setDefesa(inimigo.getDefesa() + 10);
	        adicionarLog("⬆️ Defesa aumentada em 10 pontos!");
	    }
	    
	    private void executarGrito() {
	        adicionarLog("🗣️💥 " + inimigo.getNome() + " solta um grito ensurdecedor!");
	        adicionarLog("😵 Jogador fica atordoado! Ataque reduzido no próximo turno!");
	        
	        // Reduz ataque do jogador temporariamente
	        jogador.setAtaque(jogador.getAtaque() - 5);
	        adicionarLog("⬇️ Seu ataque foi reduzido em 5 pontos!");
	    }
	    
	    private void executarAtaqueEspecialChefe() {
	        adicionarLog("👑🔥 " + inimigo.getNome() + " usa ATAQUE ESPECIAL DO CHEFE!");
	        
	        int dano = inimigo.getAtaque() * 2; // Dano dobrado
	        if (defesaAtiva) {
	            dano = Math.max(1, dano / 3); // Defesa reduz mais
	            adicionarLog("🛡️ Sua defesa reduz significativamente o dano especial!");
	            defesaAtiva = false;
	        }
	        
	        jogador.setHp(jogador.getHp() - dano);
	        adicionarLog("💀💀 DANO COLOSSAL: " + dano + "!");
	        
	        // Efeito adicional para chefes
	        adicionarLog("⚡ Você ficou atordoado pelo poder do chefe!");
	    }

	    private void executarCuraInimigo() {
	        int cura = (int)(inimigo.getHpMax() * 0.2); // Cura 20% do HP máximo
	        int novoHP = Math.min(inimigo.getHpMax(), inimigo.getHp() + cura);
	        inimigo.setHp(novoHP);
	        
	        adicionarLog("💚 " + inimigo.getNome() + " se cura em " + cura + " HP!");
	        adicionarLog("❤️ HP atual: " + inimigo.getHp() + "/" + inimigo.getHpMax());
	    }
	    
	    private void executarBuffDefesa() {
	        int aumentoDefesa = 15;
	        inimigo.setDefesa(inimigo.getDefesa() + aumentoDefesa);
	        
	        adicionarLog("✨ " + inimigo.getNome() + " fortalece sua defesa!");
	        adicionarLog("🛡️ Defesa aumentada em " + aumentoDefesa + " pontos!");
	    }
	    
	    private void executarDebuffJogador() {
	        adicionarLog("⚠️ " + inimigo.getNome() + " enfraquece você!");
	        adicionarLog("⬇️ Seus atributos foram reduzidos!");
	        
	        // Reduz ataque e defesa do jogador
	        jogador.setAtaque(Math.max(1, jogador.getAtaque() - 8));
	        if (jogador.getDefesa() > 0) {
	            jogador.setDefesa(Math.max(0, jogador.getDefesa() - 5));
	        }
	    }
	    
	    private void executarFugaInimigo() {
	        adicionarLog("🏃 " + inimigo.getNome() + " tenta fugir!");
	        if (new Random().nextDouble() < 0.4) { // 40% de chance
	            adicionarLog("✅ " + inimigo.getNome() + " fugiu do combate!");
	            dispose();
	            if (telaAventura != null) {
	                telaAventura.adicionarLog("🏃 " + inimigo.getNome() + " fugiu do combate!");
	            }
	        } else {
	            adicionarLog("❌ " + inimigo.getNome() + " falhou em fugir!");
	        }
	    }
	    
	protected void vitoria() {
		adicionarLog("🎉 " + inimigo.getNome() + " foi derrotado!");
		adicionarLog("💰 Recompensa: +" + inimigo.getRecompensaOuro() + " de ouro!");

		jogador.setOuro(jogador.getOuro() + inimigo.getRecompensaOuro());

		// Ganhar experiência
		int expGanha = 25;
		// Aqui você pode adicionar lógica de level up se tiver sistema de experiência

		adicionarLog("⭐ Experiência ganha: +" + expGanha + " XP");

		JOptionPane.showMessageDialog(this,
				"🎉 VITÓRIA!\n\n" + "Você derrotou " + inimigo.getNome() + "!\n" + "Recompensa: +"
						+ inimigo.getRecompensaOuro() + " de ouro!\n" + "Experiência: +" + expGanha + " XP",
				"Vitória", JOptionPane.INFORMATION_MESSAGE);

		dispose();
		telaAventura.batalhaVencida();
	}

	protected void derrota() {
		adicionarLog("💀 " + jogador.getNome() + " foi derrotado...");

		JOptionPane.showMessageDialog(this,
				"💀 DERROTA!\n\n" + "Você foi derrotado por " + inimigo.getNome() + "...\n" + "A jornada termina aqui.",
				"Derrota", JOptionPane.ERROR_MESSAGE);

		dispose();
		telaAventura.batalhaPerdida();
	}

	private void atualizarInventarioJogador() {
		try {
			JogadorItemBO jogadorItemBO = new JogadorItemBO();
			List<JogadorItem> inventarioAtual = jogadorItemBO.listarItensPorJogador(jogador.getId());
			jogador.setInventario(inventarioAtual);
			adicionarLog("🎒 Inventário atualizado!");
		} catch (Exception e) {
			adicionarLog("⚠️ Erro ao atualizar inventário: " + e.getMessage());
		}
	}

	protected void atualizarStatus() {
		lblJogadorHP.setText("❤️ HP: " + jogador.getHp() + "/" + jogador.getHpMax());
		lblJogadorMana.setText("🔵 Mana: " + jogador.getMana() + "/" + jogador.getManaMax());
		lblInimigoHP.setText("💀 HP: " + Math.max(0, inimigo.getHp()) + "/" + inimigo.getHpMax());

		// Atualizar cor do HP do inimigo baseado na vida restante
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