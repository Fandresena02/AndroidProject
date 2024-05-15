package fr.android.devmobproject;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import android.os.StrictMode;

public class MySQLDatabase {

    private static final String URL = "jdbc:mysql://10.0.2.2:3306/devmobile";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static Connection getConnection() {
        Connection connection = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
