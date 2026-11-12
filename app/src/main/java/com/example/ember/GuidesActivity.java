
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuidesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);

        FrameLayout navHome = findViewById(R.id.navHome);
        FrameLayout navSearch = findViewById(R.id.navSearch);
        FrameLayout navNews = findViewById(R.id.navNews);
        FrameLayout navGuides = findViewById(R.id.navGuides);

        // Переход на главную страницу
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(
                    GuidesActivity.this,
                    HomeActivity.class
            );
            startActivity(intent);
            finish();
        });

        // Переход к поиску
        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(
                    GuidesActivity.this,
                    SearchActivity.class
            );
            startActivity(intent);
            finish();
        });

        // Раздел новостей пока не реализуем
        navNews.setOnClickListener(v -> {
            Intent intent = new Intent(GuidesActivity.this, NewsActivity.class);
            startActivity(intent);
            finish();
        });


        // Пользователь уже находится в гайдах
        navGuides.setOnClickListener(v ->
                Toast.makeText(
                        GuidesActivity.this,
                        "Вы уже в разделе гайдов",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // Гайд по Bloodborne
        findViewById(R.id.guideBloodborne).setOnClickListener(v -> {
            Intent intent = new Intent(
                    GuidesActivity.this,
                    BloodborneWeaponsActivity.class
            );
            startActivity(intent);
        });

        // Гайд по Elden Ring
        findViewById(R.id.guideEldenRing).setOnClickListener(v -> {
            Intent intent = new Intent(
                    GuidesActivity.this,
                    GuideDetailActivity.class
            );
            intent.putExtra(GuideDetailActivity.EXTRA_GAME, "elden");
            startActivity(intent);
        });

        // Гайд по Sekiro
        findViewById(R.id.guideSekiro).setOnClickListener(v -> {
            Intent intent = new Intent(
                    GuidesActivity.this,
                    GuideDetailActivity.class
            );
            intent.putExtra(GuideDetailActivity.EXTRA_GAME, "sekiro");
            startActivity(intent);
        });
    }
}
