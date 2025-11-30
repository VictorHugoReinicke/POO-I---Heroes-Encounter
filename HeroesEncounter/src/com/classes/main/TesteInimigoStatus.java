package com.classes.main;

import com.classes.BO.InimigoStatusBO;
import com.classes.DTO.InimigoStatus;

public class TesteInimigoStatus {

    public static void main(String[] args) {
        
        InimigoStatusBO statusBO = new InimigoStatusBO();
        
        // 1. DADOS FICTÍCIOS
        // NOTE: Estes IDs devem existir no seu banco de dados!
        final int ID_INIMIGO_TESTE = 1; // ID de um inimigo Besta/Ladrao criado anteriormente
        final int ID_STATUS_VENENO = 5; // ID fictício de um status "Veneno"
        final int DURACAO_INICIAL = 3;  // Duração inicial do status
        
        // 2. CRIAÇÃO DO DTO DO STATUS A SER APLICADO
        System.out.println("--- 1. Preparando InimigoStatus ---");
        InimigoStatus veneno = new InimigoStatus();
        veneno.setIdInimigo(ID_INIMIGO_TESTE);
        veneno.setIdStatus(ID_STATUS_VENENO);
        veneno.setTurnoRestante(DURACAO_INICIAL);
        
        // 3. TESTE DE APLICAÇÃO (INSERÇÃO)
        System.out.println("\n--- 2. Aplicando Status (1ª Vez - INSERÇÃO) ---");
        if (statusBO.aplicarStatus(veneno)) {
            System.out.println("✅ Status aplicado com sucesso. ID do registro: " + veneno.getId());
        } else {
            System.out.println("❌ Falha na inserção do Status.");
        }
        
        // 4. TESTE DE REFORÇO (ATUALIZAÇÃO)
        // Aplica o mesmo status com uma duração maior para forçar a atualização (BO Regra 1)
        veneno.setTurnoRestante(5);
        System.out.println("\n--- 3. Aplicando Status (2ª Vez - REFORÇO) ---");
        if (statusBO.aplicarStatus(veneno)) {
            System.out.println("✅ Status reforçado com sucesso. Duração agora é 5.");
        } else {
            System.out.println("❌ Falha no reforço do Status.");
        }
        
        // 5. TESTE DE GERENCIAMENTO DE TURNO (DECREMENTO)
        System.out.println("\n--- 4. Simulação de 3 Turnos (DECREMENTO) ---");
        
        System.out.println("--- Turno 1 ---");
        statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Duração passa de 5 para 4
        
        System.out.println("--- Turno 2 ---");
        statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Duração passa de 4 para 3
        
        System.out.println("--- Turno 3 ---");
        statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Duração passa de 3 para 2
        
        // 6. TESTE DE REMOÇÃO (FIM DA DURAÇÃO)
        System.out.println("\n--- 5. Simulação de Remoção (Fim da Duração) ---");
        
        // Decrementa até o último turno
        statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Duração passa de 2 para 1
        statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Duração passa de 1 para 0
        

        //System.out.println("--- Turno Final (Status Deve Ser Removido) ---");
        //statusBO.gerenciarTurno(ID_INIMIGO_TESTE); // Tentativa de decremento para -1 -> BO remove!

        // 7. VERIFICAÇÃO FINAL
        System.out.println("\n--- 6. Verificação Final (Busca) ---");
        if (statusBO.pesquisarStatusPorInimigo(ID_INIMIGO_TESTE).isEmpty()) {
            System.out.println("✅ Sucesso! A lista de status ativos para o Inimigo ID " + ID_INIMIGO_TESTE + " está vazia.");
        } else {
            System.out.println("❌ Falha! O status deveria ter sido removido, mas ainda está ativo.");
        }
    }
}