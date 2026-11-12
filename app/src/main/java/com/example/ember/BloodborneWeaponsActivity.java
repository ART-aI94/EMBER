
package com.example.ember;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class BloodborneWeaponsActivity extends AppCompatActivity {

    private final int COLOR_BACKGROUND = Color.rgb(244, 223, 202);
    private final int COLOR_CARD = Color.rgb(255, 245, 233);
    private final int COLOR_TEXT = Color.rgb(33, 24, 18);
    private final int COLOR_MUTED = Color.rgb(128, 111, 96);
    private final int COLOR_ACCENT = Color.rgb(184, 79, 19);
    private final int COLOR_INACTIVE = Color.rgb(232, 211, 190);

    private LinearLayout listContainer;
    private Button rightHandButton;
    private Button leftHandButton;
    private TextView countText;

    private static class WeaponItem {
        String title;
        String englishTitle;
        String image;
        String hand;
        String subtitle;

        WeaponItem(String title, String englishTitle, String image,
                   String hand, String subtitle) {
            this.title = title;
            this.englishTitle = englishTitle;
            this.image = image;
            this.hand = hand;
            this.subtitle = subtitle;
        }
    }

    private final List<WeaponItem> weapons = Arrays.asList(

            new WeaponItem("Секира охотника", "Hunter Axe",
                    "w1", "right", "Тяжёлые размашистые атаки"),

            new WeaponItem("Трость-хлыст", "Threaded Cane",
                    "w3", "right", "Два режима и увеличенная дальность"),

            new WeaponItem("Острый лук Саймона", "Simon's Bowblade",
                    "w5", "right", "Клинок и лук в одном оружии"),

            new WeaponItem("Пила-штык", "Saw Cleaver",
                    "w9", "right", "Быстрое оружие с трансформацией"),

            new WeaponItem("Молот-меч", "Kirkhammer",
                    "w11", "right", "Меч и мощный каменный молот"),

            new WeaponItem("Коготь чудовища", "Beast Claw",
                    "w13", "right", "Накопление чудовищной силы"),

            new WeaponItem("Клинок Милосердия", "Blade of Mercy",
                    "w14", "right", "Быстрые серии атак"),

            new WeaponItem("Клинок Погребения", "Burial Blade",
                    "w16", "right", "Коса с большим радиусом атак"),

            new WeaponItem("Чикаге", "Chikage",
                    "w18", "right", "Клинок с кровавой трансформацией"),

            new WeaponItem("Колесо Логариуса", "Logarius' Wheel",
                    "w20", "right", "Тяжёлые удары и усиление"),

            new WeaponItem("Священный Клинок Людвига", "Ludwig's Holy Blade",
                    "w23", "right", "Меч и двуручный клинок"),

            new WeaponItem("Сабля всадника", "Reiterpallasch",
                    "w27", "right", "Рапира со встроенным выстрелом"),

            new WeaponItem("Ружьё-штык", "Rifle Spear",
                    "w28", "right", "Копьё с огнестрельным механизмом"),

            new WeaponItem("Забиватель кольев", "Stake Driver",
                    "w31", "right", "Мощный заряженный удар"),

            new WeaponItem("Тонитрус", "Tonitrus",
                    "w33", "right", "Оружие с электрическим усилением"),

            // ЛЕВАЯ РУКА

            new WeaponItem("Мушкетон охотника", "Hunter Blunderbuss",
                    "w7", "left", "Широкий разброс на близкой дистанции"),

            new WeaponItem("Пистолет охотника", "Hunter Pistol",
                    "w8", "left", "Быстрый выстрел для парирования"),

            new WeaponItem("Эвелина", "Evelyn",
                    "w22", "left", "Пистолет рыцарей Кейнхёрста"),

            new WeaponItem("Ружьё Людвига", "Ludwig's Rifle",
                    "w25", "left", "Длинноствольное огнестрельное оружие"),

            new WeaponItem("Огнемёт", "Flamesprayer",
                    "w26", "left", "Огненная атака на близкой дистанции"),

            new WeaponItem("Пушка", "Cannon",
                    "w35", "left", "Мощный выстрел с большим расходом пуль"),

            new WeaponItem("Магазинный пистолет", "Repeating Pistol",
                    "w37", "left", "Два выстрела подряд"),

            new WeaponItem("Розмарин", "Rosmarinus",
                    "w38", "left", "Атака тайным туманом"),

            new WeaponItem("Факел", "Torch",
                    "w39", "left", "Освещает тёмные места"),

            new WeaponItem("Деревянный щит", "Wooden Shield",
                    "w40", "left", "Защита от некоторых атак")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodborne_weapons);

        listContainer = findViewById(R.id.weaponListContainer);
        rightHandButton = findViewById(R.id.rightHandButton);
        leftHandButton = findViewById(R.id.leftHandButton);
        countText = findViewById(R.id.weaponCountText);

        setupBottomNavigation();

        rightHandButton.setOnClickListener(v -> showCategory("right"));
        leftHandButton.setOnClickListener(v -> showCategory("left"));

        showCategory("right");
    }

    private void showCategory(String hand) {

        boolean rightSelected = hand.equals("right");

        styleCategoryButton(rightHandButton, rightSelected);
        styleCategoryButton(leftHandButton, !rightSelected);

        listContainer.removeAllViews();

        int count = 0;

        for (WeaponItem weapon : weapons) {

            if (weapon.hand.equals(hand)) {
                listContainer.addView(createWeaponRow(weapon));
                count++;
            }
        }

        countText.setText(count +
                (rightSelected
                        ? " предметов для правой руки"
                        : " предметов для левой руки"));
    }

    private void styleCategoryButton(Button button, boolean selected) {

        button.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        selected ? COLOR_ACCENT : COLOR_INACTIVE
                )
        );

        button.setTextColor(selected ? Color.WHITE : COLOR_TEXT);
    }

    private View createWeaponRow(WeaponItem weapon) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(8), dp(10), dp(8));
        row.setBackgroundColor(COLOR_CARD);

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(94)
                );

        rowParams.bottomMargin = dp(8);
        row.setLayoutParams(rowParams);

        ImageView image = new ImageView(this);

        int imageId = getResources().getIdentifier(
                weapon.image,
                "drawable",
                getPackageName()
        );

        if (imageId != 0) {
            image.setImageResource(imageId);
        }

        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setContentDescription(weapon.title);

        row.addView(image,
                new LinearLayout.LayoutParams(dp(76), dp(76)));

        LinearLayout textBlock = new LinearLayout(this);
        textBlock.setOrientation(LinearLayout.VERTICAL);
        textBlock.setPadding(dp(12), 0, 0, 0);

        row.addView(textBlock,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

        TextView title = new TextView(this);
        title.setText(weapon.title);
        title.setTextColor(COLOR_TEXT);
        title.setTextSize(17);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        textBlock.addView(title);

        TextView english = new TextView(this);
        english.setText(weapon.englishTitle);
        english.setTextColor(COLOR_MUTED);
        english.setTextSize(12);

        textBlock.addView(english);

        TextView subtitle = new TextView(this);
        subtitle.setText(weapon.subtitle);
        subtitle.setTextColor(COLOR_MUTED);
        subtitle.setTextSize(12);
        subtitle.setMaxLines(2);

        textBlock.addView(subtitle);

        TextView arrow = new TextView(this);
        arrow.setText("›");
        arrow.setTextColor(COLOR_ACCENT);
        arrow.setTextSize(30);

        row.addView(arrow);


        row.setClickable(true);
        row.setFocusable(true);

        row.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    BloodborneWeaponsActivity.this,
                    BloodborneWeaponDetailsActivity.class
            );

            intent.putExtra(
                    BloodborneWeaponDetailsActivity.EXTRA_TITLE,
                    weapon.title
            );

            intent.putExtra(
                    BloodborneWeaponDetailsActivity.EXTRA_ENGLISH,
                    weapon.englishTitle
            );

            intent.putExtra(
                    BloodborneWeaponDetailsActivity.EXTRA_IMAGE,
                    weapon.image
            );

            intent.putExtra(
                    BloodborneWeaponDetailsActivity.EXTRA_SUBTITLE,
                    weapon.subtitle
            );

            startActivity(intent);
        });


        return row;
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

        findViewById(R.id.navGuides).setOnClickListener(v -> {
            // Уже на странице гайда.
        });
    }

    private void navigateTo(Class<?> destination) {

        Intent intent = new Intent(
                BloodborneWeaponsActivity.this,
                destination
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }


    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density + 0.5f);
    }
}
