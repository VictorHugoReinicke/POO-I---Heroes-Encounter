package com.classes.Conexao;

import java.sql.DriverManager;
import java.sql.Connection;

public class Conexao {

	final static String NOME_DO_BANCO = "poo";
    public static Connection conectar() {
    	try {
            // USAR O DRIVER MODERNO: com.mysql.cj.jdbc.Driver
    		Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // USAR OS PARÂMETROS NECESSÁRIOS: timezone, allowPublicKeyRetrieval e useSSL
            String url = "jdbc:mysql://127.0.0.1/" + NOME_DO_BANCO;
            
            // Verificar se o usuário e senha estão corretos
            return DriverManager.getConnection(url, "root", "root");
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
}

