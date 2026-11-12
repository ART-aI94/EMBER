
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(this::checkUserSession, 2000);
    }

    private void checkUserSession() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            openLogin();
            return;
        }

        user.reload().addOnCompleteListener(this, task -> {

            if (isFinishing() || isDestroyed()) {
                return;
            }

            FirebaseUser refreshedUser = auth.getCurrentUser();

            if (!task.isSuccessful() || refreshedUser == null) {
                auth.signOut();
                openLogin();
                return;
            }

            if (refreshedUser.isEmailVerified()) {
                openHome();
            } else {
                auth.signOut();
                openLogin();
            }
        });
    }

    private void openLogin() {

        Intent intent = new Intent(
                MainActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    private void openHome() {

        Intent intent = new Intent(
                MainActivity.this,
                HomeActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
