package com.classes.Conexao;

import java.sql.DriverManager;
import java.sql.Connection;

public class Conexao {

	final static String NOME_DO_BANCO = "poo";
    public static Connection conectar() {
    	try {
            
    		Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            
            String url = "jdbc:mysql://127.0.0.1/" + NOME_DO_BANCO;
            
            
            return DriverManager.getConnection(url, "root", "root");
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
}

