package com.classes.main;

import com.classes.BO.*;
import com.classes.DTO.*;

public class IniciarMain {

    public static void main(String[] args) {

        System.out.println("\n======== INICIANDO SETUP DO JOGO ========\n");

        ClasseBO classeBO = new ClasseBO();
        InimigoBO inimigoBO = new InimigoBO();
        HabilidadesBO habBO = new HabilidadesBO();
        ItemBO itemBO = new ItemBO();
        ShopBO shopBO = new ShopBO();
        ShopItemBO shopItemBO = new ShopItemBO();
        StatusBO statusBO = new StatusBO(); // ← NOVO: Adicionado
        InimigoHabilidadeBO inimigoHabBO = new InimigoHabilidadeBO(); // ← NOVO: Adicionado

        popularClasses(classeBO);
        popularInimigos(inimigoBO);
        popularHabilidades(habBO);
        popularItens(itemBO);
        popularShop(shopBO, shopItemBO, itemBO);
        
        // ← NOVA SEÇÃO: Criar habilidades dos inimigos
        criarHabilidadesInimigos(inimigoBO, habBO, inimigoHabBO, statusBO);

        System.out.println("\n======== SETUP FINALIZADO COM SUCESSO ========\n");
    }

    // ====================================================
    // 1. CLASSES
    // ====================================================
    private static void popularClasses(ClasseBO classeBO) {
        System.out.println(">> Populando Classes...");

        adicionarClasse(classeBO, new Classe("Guerreiro", 5, 0, 3));
        adicionarClasse(classeBO, new Classe("Mago", 0, 5, 1));
        adicionarClasse(classeBO, new Classe("Paladino", 3, 3, 3));

        System.out.println("✔ Classes OK\n");
    }

    // ====================================================
    // 3. INIMIGOS
    // ====================================================
    private static void popularInimigos(InimigoBO inimigoBO) {
        System.out.println(">> Populando Inimigos...");

        // Limpa inimigos existentes (opcional - para garantir limpeza)
        limparInimigosExistente(inimigoBO);

        // Besta
        Inimigo besta = InimigoFactory.criarInimigo("Besta");
        if (besta != null) {
            besta.setNome("Urso Selvagem");
            besta.setHpMax(70);
            besta.setHp(besta.getHpMax()); // ← CORREÇÃO: Inicializa HP atual
            besta.setAtaque(20);
            besta.setDefesa(10);
            besta.setRecompensaOuro(20); // ← NOVO: Adicionado recompensa
            besta.setTipoIA(com.classes.Enums.TipoIA.BALANCEADO);
            adicionarInimigo(inimigoBO, besta);
        }

        // Ladrão
        Inimigo ladrao = InimigoFactory.criarInimigo("Ladrao");
        if (ladrao != null) {
            ladrao.setNome("Assaltante das Sombras");
            ladrao.setHpMax(60);
            ladrao.setHp(ladrao.getHpMax()); // ← CORREÇÃO
            ladrao.setAtaque(25);
            ladrao.setDefesa(10);
            ladrao.setRecompensaOuro(15); // ← NOVO
            ladrao.setTipoIA(com.classes.Enums.TipoIA.AGRESSIVO);
            adicionarInimigo(inimigoBO, ladrao);
        }

        // Mágico
        Inimigo magico = InimigoFactory.criarInimigo("InimigoMagico");
        if (magico != null) {
            magico.setNome("Slime Arcano");
            magico.setHpMax(40);
            magico.setHp(magico.getHpMax()); // ← CORREÇÃO
            magico.setAtaque(30);
            magico.setDefesa(7);
            magico.setRecompensaOuro(25); // ← NOVO
            magico.setTipoIA(com.classes.Enums.TipoIA.ESTRATEGICA);
            adicionarInimigo(inimigoBO, magico);
        }

        // Chefe
        Inimigo chefe = InimigoFactory.criarInimigo("Chefe");
        if (chefe != null) {
            chefe.setNome("Lobo Alfa Supremo");
            chefe.setHpMax(200);
            chefe.setHp(chefe.getHpMax()); // ← CORREÇÃO
            chefe.setAtaque(30);
            chefe.setDefesa(50);
            chefe.setRecompensaOuro(50); // ← NOVO
            chefe.setTipoIA(com.classes.Enums.TipoIA.CHEFE);
            adicionarInimigo(inimigoBO, chefe);
        }

        // ← NOVO: Adicionar mais inimigos para variedade
        Inimigo lobo = InimigoFactory.criarInimigo("Besta");
        if (lobo != null) {
            lobo.setNome("Lobo Feroz");
            lobo.setHpMax(50);
            lobo.setHp(lobo.getHpMax());
            lobo.setAtaque(18);
            lobo.setDefesa(8);
            lobo.setRecompensaOuro(12);
            lobo.setTipoIA(com.classes.Enums.TipoIA.AGRESSIVO);
            adicionarInimigo(inimigoBO, lobo);
        }

        System.out.println("✔ Inimigos OK\n");
    }

    // ====================================================
    // 4. HABILIDADES
    // ====================================================
    private static void popularHabilidades(HabilidadesBO habBO) {
        System.out.println(">> Populando Habilidades...");

        HabilidadeFactory.criarHabilidadesIniciais(habBO);

        System.out.println("✔ Habilidades OK\n");
    }

    // ====================================================
    // 5. ITENS
    // ====================================================
    private static void popularItens(ItemBO itemBO) {
        System.out.println(">> Populando Itens...");

        ItemArma espadaLonga = new ItemArma("Espada Longa", 10, 0, 5);
        ItemArma cajadoArcano = new ItemArma("Cajado Arcano", 4, 6, 8);
        ItemDefesa escudoMadeira = new ItemDefesa("Escudo de Madeira", 0, 5);

        ItemConsumivel pocaoVida = new ItemConsumivel("Poção de Vida", 50, 0);
        ItemConsumivel pocaoMana = new ItemConsumivel("Poção de Mana", 0, 30);

        adicionarItem(itemBO, espadaLonga);
        adicionarItem(itemBO, cajadoArcano);
        adicionarItem(itemBO, escudoMadeira);

        adicionarItem(itemBO, pocaoVida);
        adicionarItem(itemBO, pocaoMana);

        System.out.println("✔ Itens OK\n");
    }

    // ====================================================
    // 6. SHOP
    // ====================================================
    private static void popularShop(ShopBO shopBO, ShopItemBO siBO, ItemBO itemBO) {
        System.out.println(">> Populando Shop...");

        Shop loja = new Shop("Loja Central");

        if (!shopBO.inserir(loja)) {
            loja = shopBO.procurarPorNome(loja);
        }

        System.out.println("✔ Loja criada: " + loja.getNome());

        // Itens do Shop
        adicionarShopItem(siBO, loja, itemBO.procurarPorNome("Espada Longa"), 100, 5);
        adicionarShopItem(siBO, loja, itemBO.procurarPorNome("Cajado Arcano"), 150, 3);
        adicionarShopItem(siBO, loja, itemBO.procurarPorNome("Escudo de Madeira"), 80, 4);
        adicionarShopItem(siBO, loja, itemBO.procurarPorNome("Poção de Vida"), 25, 10);
        adicionarShopItem(siBO, loja, itemBO.procurarPorNome("Poção de Mana"), 30, 8);

        System.out.println("✔ Shop OK\n");
    }

    // ====================================================
    // 7. HABILIDADES DOS INIMIGOS (NOVO MÉTODO)
    // ====================================================
    private static void criarHabilidadesInimigos(
            InimigoBO inimigoBO,
            HabilidadesBO habBO,
            InimigoHabilidadeBO inimigoHabBO,
            StatusBO statusBO) {
        
        System.out.println(">> Criando Habilidades dos Inimigos...");
        
        try {
            // Usa o factory que você criou
            InimigoHabilidadeFactory.criarHabilidadesInimigos(
                inimigoBO, 
                habBO, 
                inimigoHabBO, 
                statusBO
            );
            
            // Opcional: Testar o sistema
            System.out.println("\n>> Testando sistema de habilidades...");
            testarHabilidadesInimigos(inimigoBO, inimigoHabBO);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar habilidades dos inimigos: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("✔ Habilidades dos Inimigos OK\n");
    }
    
    // ====================================================
    // MÉTODO AUXILIAR PARA TESTAR HABILIDADES
    // ====================================================
    private static void testarHabilidadesInimigos(InimigoBO inimigoBO, InimigoHabilidadeBO inimigoHabBO) {
        System.out.println("🧪 Testando escolha de habilidades...");
        
        for (Inimigo inimigo : inimigoBO.pesquisarTodos()) {
            System.out.println("\n🎯 " + inimigo.getNome() + " (" + inimigo.getClass().getSimpleName() + "):");
            
            // Lista habilidades configuradas
            java.util.List<InimigoHabilidade> habilidades = inimigoHabBO.listarHabilidades(inimigo.getId());
            
            if (habilidades.isEmpty()) {
                System.out.println("   ⚠ Nenhuma habilidade configurada");
                continue;
            }
            
            // Mostra habilidades disponíveis
            int totalChances = 0;
            for (InimigoHabilidade ih : habilidades) {
                System.out.println("   - Habilidade ID: " + ih.getIdHabilidade() + 
                                 " (Chance: " + ih.getChance_uso() + "%)");
                totalChances += ih.getChance_uso();
            }
            
            // Testa 5 escolhas aleatórias
            System.out.println("   🎲 Total chances: " + totalChances + "%");
            System.out.println("   Simulando 5 turnos:");
            
            java.util.Map<Integer, Integer> contagem = new java.util.HashMap<>();
            for (int i = 1; i <= 5; i++) {
                InimigoHabilidade escolhida = inimigoHabBO.escolherHabilidade(inimigo);
                if (escolhida != null) {
                    int idHabilidade = escolhida.getIdHabilidade();
                    contagem.put(idHabilidade, contagem.getOrDefault(idHabilidade, 0) + 1);
                    System.out.println("     Turno " + i + ": Habilidade ID " + idHabilidade);
                } else {
                    System.out.println("     Turno " + i + ": Ataque básico");
                }
            }
            
            // Mostra estatísticas
            if (!contagem.isEmpty()) {
                System.out.println("   📊 Estatísticas:");
                for (java.util.Map.Entry<Integer, Integer> entry : contagem.entrySet()) {
                    System.out.println("     Habilidade ID " + entry.getKey() + ": " + 
                                     entry.getValue() + " vezes");
                }
            }
        }
    }

    // ====================================================
    // HELPERS
    // ====================================================
    private static void limparInimigosExistente(InimigoBO inimigoBO) {
        // Opcional: Limpa inimigos existentes para começar do zero
        java.util.List<Inimigo> existentes = inimigoBO.pesquisarTodos();
        if (!existentes.isEmpty()) {
            System.out.println("   🧹 Limpando " + existentes.size() + " inimigos existentes...");
            for (Inimigo i : existentes) {
                inimigoBO.excluir(i);
            }
        }
    }

    private static void adicionarClasse(ClasseBO bo, Classe c) {
        if (!bo.existe(c)) {
            bo.inserir(c);
            System.out.println("   + Classe criada: " + c.getNome());
        }
    }

    private static void adicionarInimigo(InimigoBO bo, Inimigo i) {
        if (!bo.existe(i)) {
            bo.inserir(i);
            System.out.println("   + Inimigo criado: " + i.getNome());
        }
    }

    private static void adicionarItem(ItemBO bo, Item item) {
        if (!bo.existe(item)) {
            bo.inserir(item);
            System.out.println("   + Item criado: " + item.getNome());
        }
    }

    private static void adicionarShopItem(ShopItemBO bo, Shop shop, Item item, int preco, int quantidade) {
        if (item == null) {
            System.out.println("   ⚠️ Item não encontrado para adicionar ao shop");
            return;
        }

        ShopItem si = new ShopItem(shop.getId(), item.getId(), preco, quantidade);

        if (!bo.inserir(si)) {
            bo.alterar(si);
            System.out.println("   ↻ Item atualizado no shop: " + item.getNome() + " (Preço: " + preco + ", Qtd: " + quantidade + ")");
        } else {
            System.out.println("   + Item adicionado ao shop: " + item.getNome() + " (Preço: " + preco + ", Qtd: " + quantidade + ")");
        }
    }
}