
package com.example.ember;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileAvatar;
    private TextView usernameText;
    private TextView emailText;
    private TextView registrationDateText;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileAvatar = findViewById(R.id.profileAvatar);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        registrationDateText = findViewById(R.id.registrationDateText);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.logoutButton).setOnClickListener(v -> confirmLogout());

        loadProfile();
    }


    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из аккаунта")
                .setMessage("Ты действительно хочешь выйти из своего аккаунта?")
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Выйти", (dialog, which) -> {
                    auth.signOut();

                    android.content.Intent intent =
                            new android.content.Intent(ProfileActivity.this, LoginActivity.class);

                    intent.setFlags(
                            android.content.Intent.FLAG_ACTIVITY_NEW_TASK |
                                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);
                    finish();
                })
                .show();
    }


    private void loadProfile() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        emailText.setText(user.getEmail() != null ? user.getEmail() : "Не указана");

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (!document.exists()) {
                        usernameText.setText("Пользователь");
                        return;
                    }

                    String username = document.getString("username");
                    String avatarId = document.getString("avatarId");

                    usernameText.setText(
                            username != null ? "@" + username : "Пользователь"
                    );

                    if (avatarId != null) {
                        int avatarResource = getResources().getIdentifier(
                                avatarId,
                                "drawable",
                                getPackageName()
                        );

                        if (avatarResource != 0) {
                            profileAvatar.setImageResource(avatarResource);
                        }
                    }

                    Object createdAt = document.get("createdAt");

                    if (createdAt instanceof com.google.firebase.Timestamp) {
                        Date date = ((com.google.firebase.Timestamp) createdAt).toDate();

                        SimpleDateFormat format = new SimpleDateFormat(
                                "dd.MM.yyyy",
                                Locale.getDefault()
                        );

                        registrationDateText.setText(format.format(date));
                    } else {
                        registrationDateText.setText("Неизвестно");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Не удалось загрузить профиль",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}
