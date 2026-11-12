
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CollectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        FrameLayout navHome = findViewById(R.id.navHome);
        FrameLayout navSearch = findViewById(R.id.navSearch);
        FrameLayout navNews = findViewById(R.id.navNews);
        FrameLayout navGuides = findViewById(R.id.navGuides);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(
                    CollectionsActivity.this,
                    HomeActivity.class
            ));
            finish();
        });

        navSearch.setOnClickListener(v -> {
            startActivity(new Intent(
                    CollectionsActivity.this,
                    SearchActivity.class
            ));
            finish();
        });

        navNews.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Раздел новостей добавим позже",
                        Toast.LENGTH_SHORT
                ).show()
        );

        navGuides.setOnClickListener(v -> {
            startActivity(new Intent(
                    CollectionsActivity.this,
                    GuidesActivity.class
            ));
            finish();
        });

        findViewById(R.id.collectionsBackBottom)
                .setOnClickListener(v -> finish());

        findViewById(R.id.collectionSouls).setOnClickListener(v ->
                openCollection("souls"));
        findViewById(R.id.collectionArmored).setOnClickListener(v ->
                openCollection("armored"));

        findViewById(R.id.collectionTenchu).setOnClickListener(v ->
                openCollection("tenchu"));

        findViewById(R.id.collectionForgotten).setOnClickListener(v ->
                openCollection("forgotten"));
    }

    private void openCollection(String collectionId) {
        Intent intent = new Intent(
                CollectionsActivity.this,
                CollectionGamesActivity.class
        );

        intent.putExtra("collection", collectionId);
        startActivity(intent);
    }
}
