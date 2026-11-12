package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginInput;
    private EditText passwordInput;
    private EditText emailInput;

    private CheckBox licenseCheckBox;

    private Button continueButton;
    private Button selectAvatarButton;

    private TextView licenseText;
    private TextView backToLogin;

    private ImageView selectedAvatarPreview;

    private FirebaseAuth auth;

    private String selectedAvatar = null;

    private static final int REQUEST_AVATAR = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        loginInput = findViewById(R.id.registerLoginInput);
        passwordInput = findViewById(R.id.registerPasswordInput);
        emailInput = findViewById(R.id.registerEmailInput);

        licenseCheckBox = findViewById(R.id.licenseCheckBox);

        continueButton = findViewById(R.id.continueRegisterButton);
        selectAvatarButton = findViewById(R.id.selectAvatarButton);

        licenseText = findViewById(R.id.licenseText);
        backToLogin = findViewById(R.id.backToLogin);

        selectedAvatarPreview = findViewById(R.id.selectedAvatarPreview);

        selectAvatarButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    AvatarSelectionActivity.class
            );

            startActivityForResult(intent, REQUEST_AVATAR);
        });

        continueButton.setOnClickListener(v -> validateRegistration());

        licenseText.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LicenseActivity.class
            );

            startActivity(intent);
        });

        backToLogin.setOnClickListener(v -> finish());
    }

    private void validateRegistration() {

        String username = loginInput.getText().toString()
                .trim()
                .toLowerCase(Locale.ROOT);

        String password = passwordInput.getText().toString();

        String email = emailInput.getText().toString().trim();

        if (!username.matches("^[a-z0-9_]{3,20}$")) {
            loginInput.setError(
                    "3–20 символов: английские буквы, цифры или _"
            );
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError(
                    "Пароль должен содержать минимум 6 символов"
            );
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Введите корректный Email");
            return;
        }

        if (selectedAvatar == null) {
            Toast.makeText(
                    this,
                    "Сначала выбери аватар",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!licenseCheckBox.isChecked()) {
            Toast.makeText(
                    this,
                    "Необходимо принять соглашение",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        createFirebaseAccount(username, email, password);
    }

    private void createFirebaseAccount(
            String username,
            String email,
            String password
    ) {

        continueButton.setEnabled(false);
        continueButton.setText("Создаём аккаунт...");

        auth.setLanguageCode("ru");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (!task.isSuccessful()) {

                        continueButton.setEnabled(true);
                        continueButton.setText("Продолжить");

                        Exception exception = task.getException();

                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            emailInput.setError(
                                    "Этот Email уже зарегистрирован"
                            );
                        } else {
                            Toast.makeText(
                                    this,
                                    "Не удалось создать аккаунт: "
                                            + (exception != null
                                            ? exception.getLocalizedMessage()
                                            : "неизвестная ошибка"),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        return;
                    }

                    if (auth.getCurrentUser() == null) {

                        continueButton.setEnabled(true);
                        continueButton.setText("Продолжить");

                        Toast.makeText(
                                this,
                                "Аккаунт создан, но не удалось получить пользователя",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    sendVerificationEmail(username);
                });
    }

    private void sendVerificationEmail(String username) {

        auth.getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(this, task -> {

                    Intent intent = new Intent(
                            RegisterActivity.this,
                            EmailVerificationActivity.class
                    );

                    intent.putExtra("username", username);
                    intent.putExtra("avatarId", selectedAvatar);

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                this,
                                "Письмо с подтверждением отправлено!",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "Аккаунт создан, но письмо не отправлено. "
                                        + "Попробуй отправить его повторно на следующем экране.",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    startActivity(intent);
                    finish();
                });
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AVATAR
                && resultCode == RESULT_OK
                && data != null) {

            selectedAvatar = data.getStringExtra("selected_avatar");

            if (selectedAvatar != null) {

                int avatarResource = getResources().getIdentifier(
                        selectedAvatar,
                        "drawable",
                        getPackageName()
                );

                if (avatarResource != 0) {

                    selectedAvatarPreview.setImageResource(avatarResource);

                    selectedAvatarPreview.setVisibility(ImageView.VISIBLE);

                    selectAvatarButton.setText("Изменить аватар");

                } else {

                    selectedAvatar = null;

                    Toast.makeText(
                            this,
                            "Не удалось загрузить аватар",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }
}
