package com.classes.BO;

import java.util.List;
import java.util.Random;

import com.classes.DAO.InimigoHabilidadeDAO;
import com.classes.DTO.Inimigo;
import com.classes.DTO.InimigoHabilidade;

public class InimigoHabilidadeBO {

    private InimigoHabilidadeDAO inimigoHabilidadeDAO;

    public InimigoHabilidadeBO() {
        this.inimigoHabilidadeDAO = new InimigoHabilidadeDAO();
    }

    public boolean adicionarHabilidade(InimigoHabilidade ligacao) {

        if (ligacao.getChance_uso() < 0 || ligacao.getChance_uso() > 100) {
            System.out.println("Falha: A Chance de Uso deve ser entre 0 e 100.");
            return false;
        }

        if (inimigoHabilidadeDAO.existe(ligacao)) {
            System.out.println("Falha: Esta habilidade já está associada a este inimigo.");
            return false;
        }

        System.out.println("Habilidade ID " + ligacao.getIdHabilidade() + " adicionada ao Inimigo ID " + ligacao.getIdInimigo() + " com " + ligacao.getChance_uso() + "% de chance.");
        return inimigoHabilidadeDAO.inserir(ligacao);
    }

    public boolean alterarChanceUso(InimigoHabilidade ligacao) {
        
        if (ligacao.getChance_uso() < 0 || ligacao.getChance_uso() > 100) {
            System.out.println("Falha: A Chance de Uso deve ser entre 0 e 100.");
            return false;
        }
        
        return inimigoHabilidadeDAO.alterar(ligacao);
    }

    public boolean removerHabilidade(InimigoHabilidade ligacao) {
        System.out.println("Habilidade ID " + ligacao.getIdHabilidade() + " removida do Inimigo ID " + ligacao.getIdInimigo() + ".");
        return inimigoHabilidadeDAO.excluir(ligacao);
    }

    public InimigoHabilidade escolherHabilidade(Inimigo inimigo) {
        
        List<InimigoHabilidade> habilidadesDoInimigo = inimigoHabilidadeDAO.procurarPorInimigo(inimigo.getId());
        
        if (habilidadesDoInimigo == null || habilidadesDoInimigo.isEmpty()) {
            System.out.println("Inimigo " + inimigo.getNome() + " não tem habilidades cadastradas.");
            return null; // O inimigo usaria um ataque básico neste caso
        }

        int totalChances = 0;
        for (InimigoHabilidade ih : habilidadesDoInimigo) {
            totalChances += ih.getChance_uso();
        }
        
        if (totalChances == 0) {
            System.out.println("Inimigo " + inimigo.getNome() + ": Nenhuma habilidade com chance > 0.");
            return null;
        }

        Random random = new Random();
        int valorSorteado = random.nextInt(totalChances); 
        
        int acumuladorDeChances = 0;
        for (InimigoHabilidade ih : habilidadesDoInimigo) {
            acumuladorDeChances += ih.getChance_uso();
            
            if (valorSorteado < acumuladorDeChances) {
                System.out.println("Inimigo " + inimigo.getNome() + " sorteou " + valorSorteado +
                                   " (Limite: " + acumuladorDeChances + "). Habilidade selecionada.");
                return ih;
            }
        }
        
        return null;
    }

    public List<InimigoHabilidade> listarHabilidades(int idInimigo) {
        return inimigoHabilidadeDAO.procurarPorInimigo(idInimigo);
    }
}