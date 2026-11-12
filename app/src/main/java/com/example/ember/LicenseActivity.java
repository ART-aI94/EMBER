package com.example.ember;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LicenseActivity extends AppCompatActivity {

    private Button closeLicenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        closeLicenseButton =
                findViewById(R.id.closeLicenseButton);

        closeLicenseButton.setOnClickListener(v -> finish());
    }
}