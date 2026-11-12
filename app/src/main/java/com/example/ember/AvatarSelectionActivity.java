
package com.example.ember;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvatarSelectionActivity extends AppCompatActivity {

    private ImageView[] avatars;
    private String selectedAvatar = null;

    private final int[] avatarIds = {
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5,
            R.drawable.avatar6
    };

    private final String[] avatarNames = {
            "avatar1",
            "avatar2",
            "avatar3",
            "avatar4",
            "avatar5",
            "avatar6"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_selection);

        avatars = new ImageView[]{
                findViewById(R.id.avatar1),
                findViewById(R.id.avatar2),
                findViewById(R.id.avatar3),
                findViewById(R.id.avatar4),
                findViewById(R.id.avatar5),
                findViewById(R.id.avatar6)
        };

        Button confirmButton = findViewById(R.id.confirmAvatarButton);

        for (int i = 0; i < avatars.length; i++) {
            final int position = i;

            avatars[i].setOnClickListener(v -> selectAvatar(position));
        }

        confirmButton.setOnClickListener(v -> confirmSelection());
    }

    private void selectAvatar(int position) {

        selectedAvatar = avatarNames[position];

        for (int i = 0; i < avatars.length; i++) {
            GradientDrawable border = new GradientDrawable();
            border.setColor(Color.parseColor("#F4CBAA"));
            border.setCornerRadius(12f);

            if (i == position) {
                border.setStroke(7, Color.parseColor("#B84F13"));
            } else {
                border.setStroke(2, Color.parseColor("#D9B89C"));
            }

            avatars[i].setBackground(border);
        }
    }

    private void confirmSelection() {

        if (selectedAvatar == null) {
            Toast.makeText(
                    this,
                    "Сначала выбери аватар",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        getIntent().putExtra("selected_avatar", selectedAvatar);

        setResult(RESULT_OK, getIntent());
        finish();
    }
}
