package com.classes.DTO;

import com.classes.BO.HabilidadesBO;
import com.classes.BO.ItemBO;
import com.classes.BO.JogadorBO;
import com.classes.BO.JogadorItemBO;
import com.classes.DAO.ClasseDAO;

public class JogadorFactory {

    private static final String PACOTE_DTO = "com.classes.DTO.";
    private static final ClasseDAO classeDAO = new ClasseDAO();
    private static final JogadorBO jogadorBO = new JogadorBO();
    private static final JogadorItemBO jogadorItemBO = new JogadorItemBO();

    public static Jogador criarJogador(int idClasse) {
        return criarJogador(idClasse, null, false);
    }

    public static Jogador criarJogador(int idClasse, String nome) {
        return criarJogador(idClasse, nome, true);
    }


    
    private static Jogador criarJogador(int idClasse, String nome, boolean salvarNoBanco) {
        Classe classe = classeDAO.procurarPorCodigo(idClasse);

        if (classe == null)
            throw new IllegalArgumentException("ID de Classe não encontrado: " + idClasse);

        String nomeCompleto = PACOTE_DTO + classe.getNome();

        try {
            Class<?> clazz = Class.forName(nomeCompleto);

            Jogador jogador = (Jogador) clazz.getConstructor().newInstance();
            jogador.setNome(nome);

            ItemBO itemBO = new ItemBO();
            HabilidadesBO habBO = new HabilidadesBO();

            clazz.getMethod("inicializarKit", ItemBO.class, HabilidadesBO.class)
                 .invoke(jogador, itemBO, habBO);

            if (salvarNoBanco) {
                boolean salvouJogador = jogadorBO.inserir(jogador);
                if (salvouJogador) {
                    System.out.println("Jogador criado e salvo no banco de dados com sucesso!");
                    
                    boolean itensSalvos = salvarItensInventario(jogador);
                    if (itensSalvos) {
                        System.out.println("Itens do inventário salvos com sucesso!");
                        
                        boolean armaEquipada = equiparArmaInicial(jogador);
                        if (armaEquipada) {
                            System.out.println("Arma inicial equipada com sucesso!");
                        }
                    } else {
                        System.out.println("Erro ao salvar itens do inventário!");
                    }
                } else {
                    System.out.println("Erro ao salvar jogador no banco de dados!");
                }
            }

            return jogador;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar jogador!", e);
        }
    }


    private static boolean salvarItensInventario(Jogador jogador) {
        try {
            for (JogadorItem jogadorItem : jogador.getInventario()) {
                jogadorItem.setIdJogador(jogador.getId());
                
                boolean itemSalvo = jogadorItemBO.adicionarItem(
                    jogador.getId(), 
                    jogadorItem.getItem(), 
                    jogadorItem.getQuantidade()
                );
                
                if (!itemSalvo) {
                    System.out.println("Erro ao salvar item: " + jogadorItem.getItem().getNome());
                    return false;
                }
                
                if (jogadorItem.isEquipado()) {
                    boolean equipado = jogadorItemBO.equiparItem(jogador.getId(), jogadorItem.getItem());
                    if (equipado) {
                        System.out.println(" Item equipado: " + jogadorItem.getItem().getNome());
                    }
                }
                
                System.out.println(" Item salvo no inventário: " + jogadorItem.getItem().getNome() + " (x" + jogadorItem.getQuantidade() + ")");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar itens do inventário: " + e.getMessage());
            return false;
        }
    }

    private static boolean equiparArmaInicial(Jogador jogador) {
        try {
            if (jogador.getArmaEquipada() != null) {
                // Usa o método equiparItem do JogadorItemBO
                boolean equipado = jogadorItemBO.equiparItem(jogador.getId(), jogador.getArmaEquipada());
                if (equipado) {
                    System.out.println(" Arma equipada: " + jogador.getArmaEquipada().getNome());
                }
                return equipado;
            }
            return true; // Se não tem arma para equipar, considera sucesso
        } catch (Exception e) {
            System.out.println("Erro ao equipar arma inicial: " + e.getMessage());
            return false;
        }
    }
}