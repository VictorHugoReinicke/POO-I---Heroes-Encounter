package com.classes.DTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.classes.BO.ClasseBO;
import com.classes.BO.ClasseHabilidadeBO;
import com.classes.BO.HabilidadesBO;


public class HabilidadeFactory {

   
    public static void criarHabilidadesIniciais(HabilidadesBO habBO) {
        if (habBO == null) throw new IllegalArgumentException("HabilidadesBO é nulo");

        ClasseHabilidadeBO chBO = new ClasseHabilidadeBO();
        ClasseBO classeBO = new ClasseBO();

        // =================================
        // PALADINO
        // =================================
        List<Habilidade> paladino = Arrays.asList(
            new Habilidade("Defesa Divina", 4, 0, "DEFENSIVA"),
            new Habilidade("Expurgar", 3, 1.5, "OFENSIVA"),
            new Habilidade("Benção Divina", 5, -1.2, "CURA")
        );

        // =================================
        // MAGO
        // =================================
        List<Habilidade> mago = Arrays.asList(
            new Habilidade("Ilusão Arcana", 4, 0, "DEFENSIVA"),
            new Habilidade("Bola de Fogo", 6, 2.2, "OFENSIVA"),
            new Habilidade("Estaca de Gelo", 5, 3.0, "OFENSIVA")
        );

        // =================================
        // GUERREIRO
        // =================================
        List<Habilidade> guerreiro = Arrays.asList(
            new Habilidade("Golpe Forte", 2, 0, "OFENSIVA"),
            new Habilidade("Jogo de Pés", 2, 0, "DEFENSIVA"),
            new Habilidade("Sangramento", 3, 1.0, "OFENSIVA", 1) // ajuste idStatus conforme seu DB
        );

        // Salva todas as habilidades (garante existência) e monta um mapa nome->obj
        Map<String, Habilidade> habilidadesMap = new HashMap<>();

        paladino.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));
        mago.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));
        guerreiro.forEach(h -> salvarOuBuscar(habBO, h, habilidadesMap));

        // Recupera classes (assume que as classes já foram criadas no DB; se não, cria)
        Classe paladinoClasse = new Classe("Paladino", 0, 0, 0);
        Classe magoClasse = new Classe("Mago", 0, 0, 0);
        Classe guerreiroClasse = new Classe("Guerreiro", 0, 0, 0);

        if (!classeBO.existe(paladinoClasse)) classeBO.inserir(paladinoClasse);
        if (!classeBO.existe(magoClasse)) classeBO.inserir(magoClasse);
        if (!classeBO.existe(guerreiroClasse)) classeBO.inserir(guerreiroClasse);

        paladinoClasse = classeBO.procurarPorNome(paladinoClasse);
        magoClasse = classeBO.procurarPorNome(magoClasse);
        guerreiroClasse = classeBO.procurarPorNome(guerreiroClasse);

        if (paladinoClasse == null || magoClasse == null || guerreiroClasse == null) {
            System.out.println("Erro: Não foi possível garantir as classes no banco. Abortando linkagem.");
            return;
        }

        // ===============================
        // Fazer a atribuição: Classe -> Habilidade
        // ===============================
        // Paladino
        atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Defesa Divina");
        atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Expurgar");
        atribuirSeExistir(chBO, paladinoClasse.getId(), habilidadesMap, "Benção Divina");

        // Mago
        atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Ilusão Arcana");
        atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Bola de Fogo");
        atribuirSeExistir(chBO, magoClasse.getId(), habilidadesMap, "Estaca de Gelo");

        // Guerreiro
        atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Golpe Forte");
        atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Jogo de Pés");
        atribuirSeExistir(chBO, guerreiroClasse.getId(), habilidadesMap, "Sangramento");

        System.out.println("Habilidades criadas e linkadas às classes.");
    }

    // ------ helpers ------

    private static void salvarOuBuscar(HabilidadesBO habBO, Habilidade h, Map<String, Habilidade> map) {
        try {
            if (!habBO.existe(h)) {
                habBO.inserir(h);
                // após inserir o id deverá estar no DTO (se seu DAO preencher generated keys)
            } else {
                Habilidade encontrado = habBO.procurarPorNome(h);
                if (encontrado != null) {
                    // atualiza referência para ter o id correto
                    h.setId(encontrado.getId());
                }
            }
            // garante que temos o objeto do DB (com id) no mapa
            Habilidade doDB = habBO.procurarPorNome(h);
            if (doDB != null) {
                map.put(doDB.getNome(), doDB);
                System.out.println("✔ Habilidade pronta: " + doDB.getNome() + " (ID: " + doDB.getId() + ")");
            } else {
                System.out.println("⚠️ Falha ao garantir habilidade: " + h.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atribuirSeExistir(ClasseHabilidadeBO chBO, int idClasse, Map<String, Habilidade> map, String nomeHabilidade) {
        Habilidade hab = map.get(nomeHabilidade);
        if (hab == null) {
            System.out.println("⚠️ Habilidade '" + nomeHabilidade + "' não encontrada — não foi atribuída à classe ID " + idClasse);
            return;
        }
        chBO.atribuirHabilidade(idClasse, hab.getId());
    }
}
