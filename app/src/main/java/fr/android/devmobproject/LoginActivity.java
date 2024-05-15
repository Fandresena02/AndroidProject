package fr.android.devmobproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editTextIdent = findViewById(R.id.editTextIdent);
        final EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        Button btnLogin = findViewById(R.id.buttonAuthen);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On récupère les input
                String username = editTextIdent.getText().toString();
                String password = editTextPassword.getText().toString();

                try {
                    if (checkUser(username, password)) {
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public boolean checkUser(String userName, String password) throws SQLException {

        try (Connection conn = MySQLDatabase.getConnection()) {

            String query = "SELECT * FROM user WHERE login = ? AND pwd = ?";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
