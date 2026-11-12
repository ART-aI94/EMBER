
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BloodborneWeaponDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "weapon_title";
    public static final String EXTRA_ENGLISH = "weapon_english";
    public static final String EXTRA_IMAGE = "weapon_image";
    public static final String EXTRA_SUBTITLE = "weapon_subtitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodborne_weapon_details);

        setupBottomNavigation();

        TextView title = findViewById(R.id.weaponTitle);
        TextView english = findViewById(R.id.weaponEnglish);
        TextView subtitle = findViewById(R.id.weaponSubtitle);

        ImageView image = findViewById(R.id.weaponImage);

        TextView description = findViewById(R.id.weaponDescription);
        TextView usage = findViewById(R.id.weaponUsage);
        TextView location = findViewById(R.id.weaponLocation);

        String imageName = getIntent().getStringExtra(EXTRA_IMAGE);
        String weaponSubtitle = getIntent().getStringExtra(EXTRA_SUBTITLE);

        BloodborneWeaponRepository.WeaponInfo info =
                BloodborneWeaponRepository.getWeapon(imageName);

        if (info != null) {
            title.setText(info.title);
            english.setText(info.english);
            description.setText(info.description);
            usage.setText(info.usage);
            location.setText(info.location);
        } else {
            title.setText(getIntent().getStringExtra(EXTRA_TITLE));
            english.setText(getIntent().getStringExtra(EXTRA_ENGLISH));

            description.setText("Информация об оружии пока отсутствует.");
            usage.setText("Информация об использовании пока отсутствует.");
            location.setText("Информация о местонахождении пока отсутствует.");
        }

        subtitle.setText(
                weaponSubtitle != null ? weaponSubtitle : ""
        );

        if (imageName != null) {
            int imageId = getResources().getIdentifier(
                    imageName,
                    "drawable",
                    getPackageName()
            );

            if (imageId != 0) {
                image.setImageResource(imageId);
            }
        }

        // Возвращение к списку оружия
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void setupBottomNavigation() {

        findViewById(R.id.navHome).setOnClickListener(v ->
                navigateTo(HomeActivity.class)
        );

        findViewById(R.id.navSearch).setOnClickListener(v ->
                navigateTo(SearchActivity.class)
        );

        findViewById(R.id.navNews).setOnClickListener(v ->
                navigateTo(NewsActivity.class)
        );

        findViewById(R.id.navGuides).setOnClickListener(v ->
                navigateTo(GuidesActivity.class)
        );
    }

    private void navigateTo(Class<?> destination) {

        Intent intent = new Intent(
                BloodborneWeaponDetailsActivity.this,
                destination
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }
}
