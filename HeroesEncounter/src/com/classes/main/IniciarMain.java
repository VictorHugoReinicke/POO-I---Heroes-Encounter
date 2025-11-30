package com.classes.main;

import com.classes.BO.*;
import com.classes.DTO.*;

public class IniciarMain {

    public static void main(String[] args) {

        System.out.println("\n======== INICIANDO SETUP DO JOGO ========\n");

        ClasseBO classeBO     = new ClasseBO();
        StatusBO statusBO     = new StatusBO();
        InimigoBO inimigoBO   = new InimigoBO();
        HabilidadesBO habBO   = new HabilidadesBO();
        ItemBO itemBO         = new ItemBO();
        ShopBO shopBO         = new ShopBO();
        ShopItemBO shopItemBO = new ShopItemBO();

        popularClasses(classeBO);
        popularStatus(statusBO);
        popularInimigos(inimigoBO);
        popularHabilidades(habBO);
        popularItens(itemBO);
        popularShop(shopBO, shopItemBO, itemBO);

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
    // 2. STATUS
    // ====================================================
    private static void popularStatus(StatusBO statusBO) {
        System.out.println(">> Populando Status...");

        adicionarStatus(statusBO, new Status("Veneno", 10, 0.0, 3));
        adicionarStatus(statusBO, new Status("Atordoamento", 0, 0.5, 2));
        adicionarStatus(statusBO, new Status("Sangramento", 8, 0.0, 4));
        adicionarStatus(statusBO, new Status("Fraqueza", 0, 0.3, 3));

        System.out.println("✔ Status OK\n");
    }

    // ====================================================
    // 3. INIMIGOS
    // ====================================================
    private static void popularInimigos(InimigoBO inimigoBO) {
        System.out.println(">> Populando Inimigos...");

        // Besta
        Inimigo besta = InimigoFactory.criarInimigo("Besta");
        if (besta != null) {
            besta.setNome("Urso Selvagem");
            besta.setHpMax(40);
            besta.setAtaque(5);
            besta.setDefesa(2);
            adicionarInimigo(inimigoBO, besta);
        }

        // Ladrão
        Inimigo ladrao = InimigoFactory.criarInimigo("Ladrao");
        if (ladrao != null) {
            ladrao.setNome("Assaltante das Sombras");
            ladrao.setHpMax(30);
            ladrao.setAtaque(6);
            ladrao.setDefesa(2);
            adicionarInimigo(inimigoBO, ladrao);
        }

        // Mágico
        Inimigo magico = InimigoFactory.criarInimigo("InimigoMagico");
        if (magico != null) {
            magico.setNome("Slime Arcano");
            magico.setHpMax(25);
            magico.setAtaque(3);
            magico.setDefesa(1);
            adicionarInimigo(inimigoBO, magico);
        }

        // Chefe
        Inimigo chefe = InimigoFactory.criarInimigo("Chefe");
        if (chefe != null) {
            chefe.setNome("Lobo Alfa Supremo");
            chefe.setHpMax(60);
            chefe.setAtaque(10);
            chefe.setDefesa(4);
            adicionarInimigo(inimigoBO, chefe);
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

        ItemArma espadaLonga   = new ItemArma("Espada Longa", 10, 0, 5);
        ItemArma cajadoArcano  = new ItemArma("Cajado Arcano", 4, 6, 8);
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
    // HELPERS
    // ====================================================
    private static void adicionarClasse(ClasseBO bo, Classe c) {
        if (!bo.existe(c)) {
            bo.inserir(c);
            System.out.println("   + Classe criada: " + c.getNome());
        }
    }

    private static void adicionarStatus(StatusBO bo, Status s) {
        if (!bo.existe(s)) {
            bo.inserir(s);
            System.out.println("   + Status criado: " + s.getNome());
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
        if (item == null) return;

        ShopItem si = new ShopItem(shop.getId(), item.getId(), preco, quantidade);

        if (!bo.inserir(si)) {
            bo.alterar(si);
        }

        System.out.println("   + Item adicionado ao shop: " + item.getNome());
    }
}
