
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GamesActivity extends AppCompatActivity {

    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);


        FrameLayout navHome = findViewById(R.id.navHome);
        FrameLayout navSearch = findViewById(R.id.navSearch);
        FrameLayout navNews = findViewById(R.id.navNews);
        FrameLayout navGuides = findViewById(R.id.navGuides);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(GamesActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(GamesActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        navNews.setOnClickListener(v ->
                Toast.makeText(this,
                        "Раздел новостей добавим следующим этапом",
                        Toast.LENGTH_SHORT).show());

        navGuides.setOnClickListener(v -> {
            Intent intent = new Intent(
                    GamesActivity.this,
                    GuidesActivity.class
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


        RecyclerView recyclerView =
                findViewById(R.id.gamesRecyclerView);

        EditText searchInput =
                findViewById(R.id.gameSearchInput);

        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 3));

        List<Game> games = new ArrayList<>();

        games.add(new Game("Armored Core", R.drawable.armored_core));
        games.add(new Game("Armored Core 2", R.drawable.armored_core2));
        games.add(new Game("Armored Core 3", R.drawable.armored_core3));
        games.add(new Game("Armored Core 4", R.drawable.armored_core_4));

        games.add(new Game("Dark Souls", R.drawable.dark_souls));
        games.add(new Game("Dark Souls 2", R.drawable.dark_souls2));


        games.add(new Game("Dark Souls 3", R.drawable.dark_souls3));

        games.add(new Game("Bloodborne", R.drawable.bloodborne));

        games.add(new Game("Elden Ring", R.drawable.elden_ring));

        games.add(new Game("Sekiro: Shadows Die Twice", R.drawable.sekiro));


        games.add(new Game("Echo Night", R.drawable.echo_night));
        games.add(new Game("Echo Night: Beyond", R.drawable.echo_night_beyond));

        games.add(new Game("Elden Ring: Nightreign",
                R.drawable.elden_ring_nightreign));
        games.add(new Game("Elden Ring: Shadow of the Erdtree",
                R.drawable.elden_ring_shadow_of_the_erdtree));

        games.add(new Game("King's Field", R.drawable.kings_field));
        games.add(new Game("King's Field 2", R.drawable.kings_field_2));
        games.add(new Game("King's Field 3", R.drawable.kings_field3));
        games.add(new Game("King's Field 4", R.drawable.kings_field_4));

        games.add(new Game("Kuon", R.drawable.kuon));
        games.add(new Game("Lost Kingdoms", R.drawable.lost_kingdoms));

        games.add(new Game("Shadow Tower", R.drawable.shadow_tower));
        games.add(new Game("Shadow Tower: Abyss",
                R.drawable.shadow_tower_abyss));

        games.add(new Game("Tenchu: Fatal Shadows",
                R.drawable.tenchu_fatal_shadows));
        games.add(new Game("Tenchu: Shadow Assassins",
                R.drawable.tenchu_shadow_assassins));
        games.add(new Game("Tenchu: Time of the Assassins",
                R.drawable.tenchu_time_of_the_assassins));
        games.add(new Game("Tenchu: Wrath of Heaven",
                R.drawable.tenchu_wrath_of_heaven));

        games.add(new Game("The Duskbloods", R.drawable.the_duskbloods));

        adapter = new GameAdapter(games);
        recyclerView.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
