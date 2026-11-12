
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CollectionGamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_games);

        // Кнопки возврата
        findViewById(R.id.collectionGamesBackBottom)
                .setOnClickListener(v -> finish());

        // Заголовок и описание
        TextView title = findViewById(R.id.collectionGamesTitle);
        TextView description =
                findViewById(R.id.collectionGamesDescription);

        RecyclerView recyclerView =
                findViewById(R.id.collectionGamesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Game> games = new ArrayList<>();

        String collection = getIntent().getStringExtra("collection");

        if (collection == null) {
            collection = "souls";
        }

        switch (collection) {

            case "souls":
                title.setText("Наследие Souls");

                description.setText(
                        "Исследуй мрачные королевства, опасные подземелья " +
                                "и миры, полные тайн. Сражайся с могущественными " +
                                "боссами, изучай их атаки и совершенствуй свои навыки. " +
                                "Каждая победа требует терпения и точности. " +
                                "Открой игры, сформировавшие узнаваемый стиль FromSoftware."
                );

                games.add(new Game(
                        "Dark Souls",
                        R.drawable.dark_souls
                ));

                games.add(new Game(
                        "Dark Souls II",
                        R.drawable.dark_souls2
                ));

                games.add(new Game(
                        "Dark Souls III",
                        R.drawable.dark_souls3
                ));

                games.add(new Game(
                        "Elden Ring",
                        R.drawable.elden_ring
                ));

                games.add(new Game(
                        "Bloodborne",
                        R.drawable.bloodborne
                ));

                games.add(new Game(
                        "Sekiro: Shadows Die Twice",
                        R.drawable.sekiro
                ));

                break;

            case "armored":
                title.setText("Стальные титаны");

                description.setText(
                        "Управляй боевыми мехами в мире технологий и масштабных " +
                                "сражений. Собирай собственную машину, подбирай оружие " +
                                "и экспериментируй с боевыми стратегиями. " +
                                "Каждое решение влияет на возможности твоего меха. " +
                                "Найди свой стиль среди механических баталий FromSoftware."
                );

                games.add(new Game(
                        "Armored Core",
                        R.drawable.armored_core
                ));

                games.add(new Game(
                        "Armored Core 2",
                        R.drawable.armored_core2
                ));

                games.add(new Game(
                        "Armored Core 3",
                        R.drawable.armored_core3
                ));

                games.add(new Game(
                        "Armored Core 4",
                        R.drawable.armored_core_4
                ));

                break;

            case "tenchu":
                title.setText("Тени ниндзя");

                description.setText(
                        "Погрузись в атмосферу древней Японии и тайных миссий. " +
                                "Используй тени, обходи охрану и выбирай момент для атаки. " +
                                "Здесь внимательность и ловкость часто важнее силы. " +
                                "Пройди путь воина, который действует незаметно."
                );

                games.add(new Game(
                        "Tenchu: Fatal Shadows",
                        R.drawable.tenchu_fatal_shadows
                ));

                games.add(new Game(
                        "Tenchu: Shadow Assassins",
                        R.drawable.tenchu_shadow_assassins
                ));

                games.add(new Game(
                        "Tenchu: Time of the Assassins",
                        R.drawable.tenchu_time_of_the_assassins
                ));

                games.add(new Game(
                        "Tenchu: Wrath of Heaven",
                        R.drawable.tenchu_wrath_of_heaven
                ));

                break;

            case "forgotten":
                title.setText("Забытые миры");

                description.setText(
                        "Открой менее известные миры из истории FromSoftware. " +
                                "Исследуй древние подземелья, загадочные башни " +
                                "и места, скрывающие опасные секреты. " +
                                "Тебя ждут классические ролевые приключения, мистика " +
                                "и атмосферные истории из прошлого студии."
                );

                games.add(new Game(
                        "King's Field",
                        R.drawable.kings_field
                ));

                games.add(new Game(
                        "King's Field II",
                        R.drawable.kings_field_2
                ));

                games.add(new Game(
                        "King's Field III",
                        R.drawable.kings_field3
                ));

                games.add(new Game(
                        "King's Field IV",
                        R.drawable.kings_field_4
                ));

                games.add(new Game(
                        "Shadow Tower",
                        R.drawable.shadow_tower
                ));

                games.add(new Game(
                        "Shadow Tower: Abyss",
                        R.drawable.shadow_tower_abyss
                ));

                games.add(new Game(
                        "Echo Night",
                        R.drawable.echo_night
                ));

                games.add(new Game(
                        "Echo Night: Beyond",
                        R.drawable.echo_night_beyond
                ));

                games.add(new Game(
                        "Kuon",
                        R.drawable.kuon
                ));

                games.add(new Game(
                        "Lost Kingdoms",
                        R.drawable.lost_kingdoms
                ));

                break;

            default:
                title.setText("Коллекция");
                description.setText(
                        "Информация об этой коллекции пока недоступна."
                );

                Toast.makeText(
                        this,
                        "Коллекция не найдена",
                        Toast.LENGTH_SHORT
                ).show();

                break;
        }

        GameAdapter adapter = new GameAdapter(
                games,
                R.layout.item_collection_game
        );

        recyclerView.setAdapter(adapter);

        // Нижняя навигация
        FrameLayout navHome = findViewById(R.id.navHome);
        FrameLayout navSearch = findViewById(R.id.navSearch);
        FrameLayout navNews = findViewById(R.id.navNews);
        FrameLayout navGuides = findViewById(R.id.navGuides);

        navHome.setOnClickListener(v ->
                startActivity(
                        new Intent(this, HomeActivity.class)
                )
        );

        navSearch.setOnClickListener(v ->
                startActivity(
                        new Intent(this, SearchActivity.class)
                )
        );

        navNews.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Раздел новостей добавим позже",
                        Toast.LENGTH_SHORT
                ).show()
        );

        navGuides.setOnClickListener(v ->
                startActivity(
                        new Intent(this, GuidesActivity.class)
                )
        );
    }
}
