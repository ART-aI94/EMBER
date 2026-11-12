
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginInput;
    private EditText passwordInput;

    private Button loginButton;
    private Button registerButton;

    private TextView forgotPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        forgotPassword = findViewById(R.id.forgotPassword);

        // Восстановление пароля пока не предусмотрено
        forgotPassword.setVisibility(View.GONE);

        // Поле входа используется для Email
        loginInput.setHint("Электронная почта");

        loginButton.setOnClickListener(v -> loginUser());

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (email.isEmpty()) {
            loginInput.setError("Введите электронную почту");
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Введите пароль");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Выполняется вход...");

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (!task.isSuccessful()) {

                        loginButton.setEnabled(true);
                        loginButton.setText("Войти");

                        Toast.makeText(
                                this,
                                "Неверная почта или пароль",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    FirebaseUser user = auth.getCurrentUser();

                    if (user == null) {
                        showLoginError();
                        return;
                    }

                    user.reload().addOnCompleteListener(this, reloadTask -> {

                        FirebaseUser refreshedUser = auth.getCurrentUser();

                        if (!reloadTask.isSuccessful() || refreshedUser == null) {
                            showLoginError();
                            return;
                        }

                        if (!refreshedUser.isEmailVerified()) {

                            auth.signOut();

                            loginButton.setEnabled(true);
                            loginButton.setText("Войти");

                            Toast.makeText(
                                    this,
                                    "Сначала подтверди электронную почту",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        openHome();
                    });
                });
    }

    private void openHome() {

        Intent intent = new Intent(
                LoginActivity.this,
                HomeActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    private void showLoginError() {

        loginButton.setEnabled(true);
        loginButton.setText("Войти");

        Toast.makeText(
                this,
                "Не удалось проверить аккаунт. Проверь подключение к интернету.",
                Toast.LENGTH_LONG
        ).show();
    }
}
