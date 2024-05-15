package fr.android.devmobproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecetteDAO {

    public String[] getRecette(String ville) throws SQLException {
        String[] recette = new String[2];

        try (Connection connection = MySQLDatabase.getConnection()) {
            String query = "SELECT r.nom_plat, r.instructions " +
                    "FROM recette AS r " +
                    "INNER JOIN ville AS v ON v.id_ville = r.id_ville " +
                    "WHERE v.nom_ville = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ville);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                recette[0] = resultSet.getString("nom_plat");
                recette[1] = resultSet.getString("instructions");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recette;
    }

}
