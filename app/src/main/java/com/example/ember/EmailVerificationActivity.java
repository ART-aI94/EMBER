
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmailVerificationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView emailText;
    private TextView statusText;

    private Button checkButton;
    private Button resendButton;

    private String username;
    private String avatarId;

    private boolean isSending = false;
    private boolean isSaving = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable enableResend = () -> {
        if (!isFinishing() && !isDestroyed()) {
            resendButton.setEnabled(true);
            resendButton.setText("Отправить письмо повторно");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailText = findViewById(R.id.verificationEmail);
        statusText = findViewById(R.id.verificationStatus);

        checkButton = findViewById(R.id.checkVerificationButton);
        resendButton = findViewById(R.id.resendVerificationButton);

        username = getIntent().getStringExtra("username");
        avatarId = getIntent().getStringExtra("avatarId");

        FirebaseUser user = auth.getCurrentUser();

        if (user == null || username == null || avatarId == null) {
            Toast.makeText(
                    this,
                    "Не удалось получить данные регистрации",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        emailText.setText(user.getEmail());

        checkButton.setOnClickListener(v -> checkVerification());

        resendButton.setOnClickListener(v -> resendEmail());
    }

    private void checkVerification() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null || isSaving) {
            return;
        }

        checkButton.setEnabled(false);
        statusText.setText("Проверяем подтверждение...");

        user.reload().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                checkButton.setEnabled(true);
                statusText.setText(
                        "Не удалось проверить почту. Попробуй ещё раз."
                );
                return;
            }

            FirebaseUser refreshedUser = auth.getCurrentUser();

            if (refreshedUser == null || !refreshedUser.isEmailVerified()) {
                checkButton.setEnabled(true);
                statusText.setText(
                        "Пока не видим подтверждения. Проверь почту."
                );
                return;
            }

            statusText.setText("Почта подтверждена! Создаём профиль...");

            isSaving = true;

            // Обновляем токен, чтобы Firestore получил
            // актуальный признак подтверждённой почты.
            refreshedUser.getIdToken(true)
                    .addOnSuccessListener(token -> createUserProfile())
                    .addOnFailureListener(error -> {
                        isSaving = false;
                        checkButton.setEnabled(true);
                        statusText.setText(
                                "Не удалось обновить данные авторизации."
                        );
                    });
        });
    }

    private void createUserProfile() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            isSaving = false;
            checkButton.setEnabled(true);
            return;
        }

        if (!username.matches("^[a-z0-9_]{3,20}$")) {
            isSaving = false;
            checkButton.setEnabled(true);
            statusText.setText("Некорректный никнейм.");
            return;
        }

        WriteBatch batch = db.batch();

        // Публичный профиль
        Map<String, Object> publicProfile = new HashMap<>();
        publicProfile.put("username", username);
        publicProfile.put("avatarId", avatarId);
        publicProfile.put("createdAt", FieldValue.serverTimestamp());

        // Приватные данные
        Map<String, Object> privateProfile = new HashMap<>();
        privateProfile.put("email", user.getEmail());
        privateProfile.put("createdAt", FieldValue.serverTimestamp());

        // Резервирование уникального никнейма
        Map<String, Object> usernameData = new HashMap<>();
        usernameData.put("uid", user.getUid());
        usernameData.put("username", username);

        batch.set(
                db.collection("users").document(user.getUid()),
                publicProfile
        );

        batch.set(
                db.collection("privateUsers").document(user.getUid()),
                privateProfile
        );

        batch.set(
                db.collection("usernames").document(username),
                usernameData
        );

        batch.commit().addOnCompleteListener(task -> {

            isSaving = false;

            if (!task.isSuccessful()) {

                checkButton.setEnabled(true);

                statusText.setText(
                        "Не удалось сохранить профиль. "
                                + "Возможно, никнейм уже занят."
                );

                showUsernameDialog();
                return;
            }

            statusText.setText("Профиль успешно создан!");

            Toast.makeText(
                    this,
                    "Добро пожаловать в EMBER!",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(
                    EmailVerificationActivity.this,
                    HomeActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }

    private void showUsernameDialog() {

        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(username);
        input.setSelection(input.getText().length());
        input.setHint("Новый никнейм");

        new AlertDialog.Builder(this)
                .setTitle("Не удалось занять никнейм")
                .setMessage(
                        "Возможно, это имя уже использует другой пользователь. "
                                + "Попробуй другое."
                )
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("Повторить", (dialog, which) -> {

                    String newUsername = input.getText()
                            .toString()
                            .trim()
                            .toLowerCase(Locale.ROOT);

                    if (!newUsername.matches("^[a-z0-9_]{3,20}$")) {
                        Toast.makeText(
                                this,
                                "Никнейм должен содержать 3–20 символов: "
                                        + "английские буквы, цифры или _",
                                Toast.LENGTH_LONG
                        ).show();

                        showUsernameDialog();
                        return;
                    }

                    username = newUsername;
                    isSaving = true;

                    createUserProfile();
                })
                .setNegativeButton("Позже", (dialog, which) -> {
                    checkButton.setEnabled(true);
                    statusText.setText(
                            "Почта подтверждена. Профиль пока не создан."
                    );
                })
                .show();
    }

    private void resendEmail() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null || isSending) {
            return;
        }

        isSending = true;

        resendButton.setEnabled(false);
        resendButton.setText("Подождите 60 секунд");

        handler.postDelayed(enableResend, 60000);

        auth.setLanguageCode("ru");

        user.sendEmailVerification().addOnCompleteListener(task -> {

            isSending = false;

            if (task.isSuccessful()) {
                statusText.setText("Письмо отправлено повторно.");
            } else {
                statusText.setText(
                        "Не удалось отправить письмо. Попробуй позже."
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(enableResend);
        super.onDestroy();
    }
}
