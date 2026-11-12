
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private LinearLayout contentLayout;
    private LinearLayout resultsContainer;
    private RecyclerView resultsRecyclerView;
    private TextView emptyResults;

    private SearchResultAdapter adapter;

    private final List<SearchResult> allResults = new ArrayList<>();
    private final List<View> homeSections = new ArrayList<>();

    private boolean resultsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);

        contentLayout = (LinearLayout) searchInput.getParent();

        setupBottomNavigation();
        setupExistingButtons();
        prepareHomeSections();
        createResultsView();
        prepareSearchData();
        setupSearch();
    }

    // ==========================================
    // НИЖНЯЯ НАВИГАЦИЯ
    // ==========================================

    private void setupBottomNavigation() {

        findViewById(R.id.navHome).setOnClickListener(v ->
                navigateTo(HomeActivity.class)
        );

        findViewById(R.id.navSearch).setOnClickListener(v -> {
            searchInput.requestFocus();
        });

        findViewById(R.id.navNews).setOnClickListener(v ->
                navigateTo(NewsActivity.class)
        );

        findViewById(R.id.navGuides).setOnClickListener(v ->
                navigateTo(GuidesActivity.class)
        );
    }

    private void navigateTo(Class<?> destination) {

        Intent intent = new Intent(this, destination);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }

    // ==========================================
    // СУЩЕСТВУЮЩИЕ КНОПКИ СТРАНИЦЫ
    // ==========================================

    private void setupExistingButtons() {

        findViewById(R.id.allCollectionsButton).setOnClickListener(v ->
                startActivity(new Intent(this, CollectionsActivity.class))
        );

        findViewById(R.id.allGamesButton).setOnClickListener(v ->
                startActivity(new Intent(this, GamesActivity.class))
        );

        findViewById(R.id.allGuidesButton).setOnClickListener(v ->
                startActivity(new Intent(this, GuidesActivity.class))
        );

        findViewById(R.id.collectionSouls).setOnClickListener(v ->
                openCollection("souls")
        );

        findViewById(R.id.collectionWorlds).setOnClickListener(v ->
                openCollection("armored")
        );

        findViewById(R.id.collectionJapan).setOnClickListener(v ->
                openCollection("tenchu")
        );

        findViewById(R.id.guideBosses).setOnClickListener(v ->
                openGuide("elden")
        );

        findViewById(R.id.guideBeginners).setOnClickListener(v ->
                startActivity(new Intent(
                        this,
                        BloodborneWeaponsActivity.class
                ))
        );

        findViewById(R.id.gameEldenRing).setOnClickListener(v ->
                openGame("Elden Ring", R.drawable.elden_ring)
        );

        findViewById(R.id.gameBloodborne).setOnClickListener(v ->
                openGame("Bloodborne", R.drawable.bloodborne)
        );

        findViewById(R.id.gameDarkSouls).setOnClickListener(v ->
                openGame("Dark Souls 3", R.drawable.dark_souls3)
        );

        findViewById(R.id.gameSekiro).setOnClickListener(v ->
                openGame(
                        "Sekiro: Shadows Die Twice",
                        R.drawable.sekiro
                )
        );
    }

    private void openCollection(String collectionId) {

        Intent intent = new Intent(
                this,
                CollectionGamesActivity.class
        );

        intent.putExtra("collection", collectionId);
        startActivity(intent);
    }

    private void openGame(String title, int image) {

        Intent intent = new Intent(
                this,
                GameDetailsActivity.class
        );

        intent.putExtra(GameDetailsActivity.EXTRA_TITLE, title);
        intent.putExtra(GameDetailsActivity.EXTRA_IMAGE, image);

        startActivity(intent);
    }

    private void openGuide(String game) {

        Intent intent = new Intent(
                this,
                GuideDetailActivity.class
        );

        intent.putExtra(GuideDetailActivity.EXTRA_GAME, game);

        startActivity(intent);
    }

    // ==========================================
    // ПОДГОТОВКА ГЛАВНОЙ СТРАНИЦЫ ПОИСКА
    // ==========================================

    private void prepareHomeSections() {

        int searchIndex = contentLayout.indexOfChild(searchInput);

        for (int i = searchIndex + 1;
             i < contentLayout.getChildCount(); i++) {

            homeSections.add(contentLayout.getChildAt(i));
        }
    }

    // ==========================================
    // СОЗДАНИЕ СПИСКА РЕЗУЛЬТАТОВ
    // ==========================================

    private void createResultsView() {

        resultsContainer = new LinearLayout(this);
        resultsContainer.setOrientation(LinearLayout.VERTICAL);
        resultsContainer.setVisibility(View.GONE);

        TextView heading = new TextView(this);
        heading.setText("Результаты поиска");
        heading.setTextColor(0xFFB84F13);
        heading.setTextSize(16);
        heading.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout.LayoutParams headingParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        headingParams.topMargin = dp(20);
        headingParams.bottomMargin = dp(10);

        resultsContainer.addView(heading, headingParams);

        resultsRecyclerView = new RecyclerView(this);

        resultsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        resultsRecyclerView.setNestedScrollingEnabled(false);

        adapter = new SearchResultAdapter(this::openSearchResult);

        resultsRecyclerView.setAdapter(adapter);

        resultsContainer.addView(
                resultsRecyclerView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        emptyResults = new TextView(this);
        emptyResults.setText("Ничего не найдено");
        emptyResults.setTextColor(0xFF806F60);
        emptyResults.setTextSize(15);
        emptyResults.setGravity(android.view.Gravity.CENTER);
        emptyResults.setPadding(0, dp(30), 0, dp(30));
        emptyResults.setVisibility(View.GONE);

        resultsContainer.addView(emptyResults);

        int searchIndex = contentLayout.indexOfChild(searchInput);

        contentLayout.addView(
                resultsContainer,
                searchIndex + 1
        );
    }

    // ==========================================
    // БАЗА ДАННЫХ ПОИСКА
    // ==========================================

    private void prepareSearchData() {

        // ИГРЫ

        addGame("Armored Core", R.drawable.armored_core,
                "меха робот armored core мехи");

        addGame("Armored Core 2", R.drawable.armored_core2,
                "роботы меха");

        addGame("Armored Core 3", R.drawable.armored_core3,
                "роботы меха стальные титаны");

        addGame("Armored Core 4", R.drawable.armored_core_4,
                "роботы меха");

        addGame("Dark Souls", R.drawable.dark_souls,
                "соулс souls тёмные души");

        addGame("Dark Souls 2", R.drawable.dark_souls2,
                "соулс souls тёмные души");

        addGame("Dark Souls 3", R.drawable.dark_souls3,
                "соулс souls тёмные души");

        addGame("Bloodborne", R.drawable.bloodborne,
                "бладборн охотник готика");

        addGame("Elden Ring", R.drawable.elden_ring,
                "элден ринг междумирье погасший");

        addGame("Sekiro: Shadows Die Twice", R.drawable.sekiro,
                "секиро синоби ниндзя самурай");

        addGame("Echo Night", R.drawable.echo_night,
                "хоррор приключение");

        addGame("Echo Night: Beyond", R.drawable.echo_night_beyond,
                "хоррор космос");

        addGame("Elden Ring: Nightreign",
                R.drawable.elden_ring_nightreign,
                "элден ринг кооператив");

        addGame("Elden Ring: Shadow of the Erdtree",
                R.drawable.elden_ring_shadow_of_the_erdtree,
                "элден ринг дополнение dlc");

        addGame("King's Field", R.drawable.kings_field,
                "королевское поле");

        addGame("King's Field 2", R.drawable.kings_field_2,
                "королевское поле");

        addGame("King's Field 3", R.drawable.kings_field3,
                "королевское поле");

        addGame("King's Field 4", R.drawable.kings_field_4,
                "королевское поле");

        addGame("Kuon", R.drawable.kuon,
                "япония хоррор");

        addGame("Lost Kingdoms", R.drawable.lost_kingdoms,
                "потерянные королевства");

        addGame("Shadow Tower", R.drawable.shadow_tower,
                "башня");

        addGame("Shadow Tower: Abyss", R.drawable.shadow_tower_abyss,
                "башня");

        addGame("Tenchu: Fatal Shadows",
                R.drawable.tenchu_fatal_shadows,
                "тенчу ниндзя японские тени");

        addGame("Tenchu: Shadow Assassins",
                R.drawable.tenchu_shadow_assassins,
                "тенчу ниндзя");

        addGame("Tenchu: Time of the Assassins",
                R.drawable.tenchu_time_of_the_assassins,
                "тенчу ниндзя");

        addGame("Tenchu: Wrath of Heaven",
                R.drawable.tenchu_wrath_of_heaven,
                "тенчу ниндзя");

        addGame("The Duskbloods", R.drawable.the_duskbloods,
                "вампиры охота");

        // ГАЙДЫ

        addGuide(
                "Все боссы Междуземья",
                "Elden Ring · Справочник боссов",
                R.drawable.elden_ring,
                "elden",
                "боссы boss маргит годрик радан морготт"
        );

        addGuide(
                "Гайд по оружию",
                "Bloodborne · 25 видов оружия",
                R.drawable.bloodborne,
                "bloodborne",
                "бладборн оружие пистолет секира клинок охотник"
        );

        addGuide(
                "Советы для новичков",
                "Sekiro · Боевая система и парирование",
                R.drawable.sekiro,
                "sekiro",
                "секиро новичок парирование стойка синоби протез"
        );

        // КОЛЛЕКЦИИ

        addCollection(
                "Наследие Souls",
                "Коллекция игр серии Souls",
                R.drawable.elden_ring,
                "souls",
                "dark souls elden ring bloodborne демоны"
        );

        addCollection(
                "Стальные титаны",
                "Коллекция Armored Core",
                R.drawable.armored_core3,
                "armored",
                "роботы меха armored core"
        );

        addCollection(
                "Тени ниндзя",
                "Коллекция игр о ниндзя",
                R.drawable.tenchu_fatal_shadows,
                "tenchu",
                "япония ниндзя тенчу samurai"
        );
    }

    private void addGame(
            String title,
            int image,
            String keywords
    ) {

        allResults.add(new SearchResult(
                title,
                "Игра · Каталог игр",
                "ИГРА",
                keywords,
                image,
                "game",
                title
        ));
    }

    private void addGuide(
            String title,
            String subtitle,
            int image,
            String target,
            String keywords
    ) {

        allResults.add(new SearchResult(
                title,
                subtitle,
                "ГАЙД",
                keywords,
                image,
                "guide",
                target
        ));
    }

    private void addCollection(
            String title,
            String subtitle,
            int image,
            String target,
            String keywords
    ) {

        allResults.add(new SearchResult(
                title,
                subtitle,
                "КОЛЛЕКЦИЯ",
                keywords,
                image,
                "collection",
                target
        ));
    }

    // ==========================================
    // ПОИСК В РЕАЛЬНОМ ВРЕМЕНИ
    // ==========================================

    private void setupSearch() {

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {

                String query = s.toString().trim();

                if (query.isEmpty()) {
                    showHomeContent();
                } else {
                    showSearchResults(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showSearchResults(String query) {

        if (!resultsVisible) {

            for (View section : homeSections) {
                section.setVisibility(View.GONE);
            }

            resultsContainer.setVisibility(View.VISIBLE);
            resultsVisible = true;
        }

        String normalizedQuery = query.toLowerCase(Locale.ROOT);

        List<SearchResult> filtered = new ArrayList<>();

        for (SearchResult result : allResults) {

            if (result.getSearchText()
                    .toLowerCase(Locale.ROOT)
                    .contains(normalizedQuery)) {

                filtered.add(result);
            }
        }

        adapter.setResults(filtered);

        if (filtered.isEmpty()) {
            emptyResults.setVisibility(View.VISIBLE);
            resultsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyResults.setVisibility(View.GONE);
            resultsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showHomeContent() {

        resultsContainer.setVisibility(View.GONE);

        for (View section : homeSections) {
            section.setVisibility(View.VISIBLE);
        }

        resultsVisible = false;
    }

    // ==========================================
    // ОТКРЫТИЕ РЕЗУЛЬТАТА
    // ==========================================

    private void openSearchResult(SearchResult result) {

        String type = result.getType();
        String target = result.getTargetId();

        if ("game".equals(type)) {

            int image = result.getImageResource();

            openGame(target, image);

        } else if ("guide".equals(type)) {

            if ("bloodborne".equals(target)) {

                startActivity(new Intent(
                        this,
                        BloodborneWeaponsActivity.class
                ));

            } else {

                openGuide(target);
            }

        } else if ("collection".equals(type)) {

            openCollection(target);
        }
    }

    private int dp(int value) {
        return (int) (
                value * getResources().getDisplayMetrics().density
        );
    }
}
