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

    // ------------------------------------------------------------------
    // --- 1. MÉTODOS DE MANIPULAÇÃO E VALIDAÇÃO ---
    // ------------------------------------------------------------------

    /**
     * Adiciona uma habilidade a um inimigo com uma chance de uso.
     */
    public boolean adicionarHabilidade(InimigoHabilidade ligacao) {

        // Regra de Negócio 1: Chance de Uso deve ser entre 0 e 100
        if (ligacao.getChance_uso() < 0 || ligacao.getChance_uso() > 100) {
            System.out.println("⚠️ Falha: A Chance de Uso deve ser entre 0 e 100.");
            return false;
        }

        // Regra de Negócio 2: Não pode haver ligação duplicada (mesmo Inimigo e Habilidade)
        if (inimigoHabilidadeDAO.existe(ligacao)) {
            System.out.println("⚠️ Falha: Esta habilidade já está associada a este inimigo.");
            return false;
        }

        System.out.println("✅ Habilidade ID " + ligacao.getIdHabilidade() + " adicionada ao Inimigo ID " + ligacao.getIdInimigo() + " com " + ligacao.getChance_uso() + "% de chance.");
        return inimigoHabilidadeDAO.inserir(ligacao);
    }
    
    /**
     * Altera apenas a chance de uso de uma ligação existente.
     */
    public boolean alterarChanceUso(InimigoHabilidade ligacao) {
        
        // Regra de Negócio 1: Chance de Uso deve ser entre 0 e 100
        if (ligacao.getChance_uso() < 0 || ligacao.getChance_uso() > 100) {
            System.out.println("⚠️ Falha: A Chance de Uso deve ser entre 0 e 100.");
            return false;
        }
        
        return inimigoHabilidadeDAO.alterar(ligacao);
    }

    /**
     * Remove a ligação entre o inimigo e a habilidade.
     */
    public boolean removerHabilidade(InimigoHabilidade ligacao) {
        System.out.println("❌ Habilidade ID " + ligacao.getIdHabilidade() + " removida do Inimigo ID " + ligacao.getIdInimigo() + ".");
        return inimigoHabilidadeDAO.excluir(ligacao);
    }

    // ------------------------------------------------------------------
    // --- 2. LÓGICA DE INTELIGÊNCIA ARTIFICIAL (IA) ---
    // ------------------------------------------------------------------

    /**
     * Escolhe aleatoriamente uma habilidade que o inimigo deve usar no turno, 
     * baseando-se na chance de uso de cada habilidade.
     * * @param inimigo O DTO do inimigo que está agindo.
     * @return O DTO InimigoHabilidade selecionado, ou null se nenhuma habilidade for usada.
     */
    public InimigoHabilidade escolherHabilidade(Inimigo inimigo) {
        
        // 1. Busca todas as habilidades disponíveis para o inimigo
        List<InimigoHabilidade> habilidadesDoInimigo = inimigoHabilidadeDAO.procurarPorInimigo(inimigo.getId());
        
        if (habilidadesDoInimigo == null || habilidadesDoInimigo.isEmpty()) {
            System.out.println("⚠️ Inimigo " + inimigo.getNome() + " não tem habilidades cadastradas.");
            return null; // O inimigo usaria um ataque básico neste caso
        }

        // 2. Calcula o somatório total das chances de uso
        int totalChances = 0;
        for (InimigoHabilidade ih : habilidadesDoInimigo) {
            totalChances += ih.getChance_uso();
        }
        
        // Se a soma das chances for zero, ele não usa habilidade especial.
        if (totalChances == 0) {
            System.out.println("⚠️ Inimigo " + inimigo.getNome() + ": Nenhuma habilidade com chance > 0.");
            return null;
        }

        // 3. Seleciona um número aleatório entre 0 e (totalChances - 1)
        Random random = new Random();
        int valorSorteado = random.nextInt(totalChances); 
        
        // 4. Determina qual habilidade foi selecionada (Método da Roda da Fortuna)
        int acumuladorDeChances = 0;
        for (InimigoHabilidade ih : habilidadesDoInimigo) {
            acumuladorDeChances += ih.getChance_uso();
            
            if (valorSorteado < acumuladorDeChances) {
                // A habilidade atual cobre o valor sorteado.
                System.out.println("🎲 Inimigo " + inimigo.getNome() + " sorteou " + valorSorteado + 
                                   " (Limite: " + acumuladorDeChances + "). Habilidade selecionada.");
                return ih;
            }
        }
        
        // Deve ser inatingível se totalChances > 0, mas serve como fallback
        return null;
    }

    // ------------------------------------------------------------------
    // --- 3. MÉTODOS DE BUSCA E UTILIDADE ---
    // ------------------------------------------------------------------

    public List<InimigoHabilidade> listarHabilidades(int idInimigo) {
        return inimigoHabilidadeDAO.procurarPorInimigo(idInimigo);
    }
}