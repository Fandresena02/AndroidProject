package fr.android.devmobproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/devmobile";
    private static final String USER = "root";
    private static final String PASSWORD = "CliMysql.";

    // Ajoutez une méthode à votre classe MySQLDatabase pour récupérer la recette associée à une ville
    public String getRecette(String ville) {
        String recette = "";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT recette FROM ville WHERE nom = '" + ville + "'");
            if (resultSet.next()) {
                recette = resultSet.getString("recette");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recette;
    }
}
