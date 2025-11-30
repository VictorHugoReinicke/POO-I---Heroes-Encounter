package com.classes.DTO;

public class InimigoFactory {

    public static Inimigo criarInimigo(String tipoClasse) {
        
        if (tipoClasse == null || tipoClasse.isEmpty()) {
            return null;
        }

        switch (tipoClasse) {
            case "Besta":
                return new Besta();
            case "Chefe":
                return new Chefe();
            case "Ladrao":
                return new Ladrao();
            case "InimigoMagico":
                return new InimigoMagico();
            default:
                System.out.println("Erro na Factory: Tipo de Inimigo não reconhecido: " + tipoClasse);
                return null;
        }
    }
}