package com.example.ember;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ember_news_cache";
    private static final String CACHE_KEY = "saved_news";
    private static final int NEWS_LIMIT = 1;

    private final int cardColor = 0xFFFFF5E9;
    private final int textColor = 0xFF211812;
    private final int secondaryColor = 0xFF806B59;
    private final int accentColor = 0xFFB84F13;

    private LinearLayout homeNewsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Переходы к страницам игр
        findViewById(R.id.gameEldenRingHome).setOnClickListener(v ->
                openGame("Elden Ring", R.drawable.elden_ring));

        findViewById(R.id.gameBloodborneHome).setOnClickListener(v ->
                openGame("Bloodborne", R.drawable.bloodborne));

        findViewById(R.id.gameSekiroHome).setOnClickListener(v ->
                openGame("Sekiro: Shadows Die Twice", R.drawable.sekiro));

// Игра дня
        findViewById(R.id.gameOfDayCard).setOnClickListener(v ->
                openGame("Elden Ring", R.drawable.elden_ring));

        FrameLayout navGuides = findViewById(R.id.navGuides);
        homeNewsContainer = findViewById(R.id.homeNewsContainer);

        // Переход в профиль пользователя
        findViewById(R.id.profileButton).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.navNews).setOnClickListener(v -> openNews());
        findViewById(R.id.allNewsButton).setOnClickListener(v -> openNews());

        findViewById(R.id.navSearch).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        });

        navGuides.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GuidesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        showLatestNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем блок, если пользователь вернулся с экрана новостей.
        if (homeNewsContainer != null) showLatestNews();
    }

    private void openNews() {
        startActivity(new Intent(HomeActivity.this, NewsActivity.class));
    }

    private void showLatestNews() {
        homeNewsContainer.removeAllViews();
        List<HomeNewsItem> news = loadCachedNews();

        if (news.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Новости появятся после первой загрузки. Нажми «Все →», чтобы открыть ленту.");
            empty.setTextColor(secondaryColor);
            empty.setTextSize(14);
            empty.setPadding(dp(4), dp(8), dp(4), dp(12));
            homeNewsContainer.addView(empty);
            return;
        }

        int count = Math.min(NEWS_LIMIT, news.size());
        for (int i = 0; i < count; i++) addHomeNewsCard(news.get(i), i);
    }

    private void openGame(String title, int imageResource) {
        Intent intent = new Intent(
                HomeActivity.this,
                GameDetailsActivity.class
        );

        intent.putExtra(GameDetailsActivity.EXTRA_TITLE, title);
        intent.putExtra(GameDetailsActivity.EXTRA_IMAGE, imageResource);

        startActivity(intent);
    }

    private List<HomeNewsItem> loadCachedNews() {
        List<HomeNewsItem> news = new ArrayList<>();
        try {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String saved = preferences.getString(CACHE_KEY, null);
            if (saved == null) return news;

            JSONArray array = new JSONArray(saved);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String title = object.optString("title", "").trim();
                String description = object.optString("description", "").trim();
                String link = object.optString("link", "").trim();
                String date = object.optString("pubDate", "").trim();
                String source = object.optString("source", "").trim();

                if (!title.isEmpty() && !link.isEmpty()) {
                    news.add(new HomeNewsItem(title, description, link, date, source));
                }
                if (news.size() >= NEWS_LIMIT) break;
            }
        } catch (Exception e) {
            android.util.Log.e("EMBER_HOME_NEWS", "Не удалось прочитать кэш новостей", e);
        }
        return news;
    }

    private void addHomeNewsCard(HomeNewsItem item, int index) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);
        card.setPadding(dp(12), dp(12), dp(12), dp(12));

        GradientDrawable background = new GradientDrawable();
        background.setColor(cardColor);
        background.setCornerRadius(dp(14));
        card.setBackground(background);
        card.setClickable(true);
        card.setFocusable(true);

        TextView icon = new TextView(this);
        icon.setLayoutParams(new LinearLayout.LayoutParams(dp(42), dp(42)));
        icon.setGravity(android.view.Gravity.CENTER);
        GradientDrawable iconBackground = new GradientDrawable();
        iconBackground.setColor(0xFFFBE1C9);
        iconBackground.setCornerRadius(dp(10));
        icon.setBackground(iconBackground);
        icon.setText("⚔");
        icon.setTextSize(22);
        icon.setTextColor(accentColor);
        card.addView(icon);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        contentParams.leftMargin = dp(12);
        contentParams.rightMargin = dp(8);
        card.addView(content, contentParams);

        TextView category = new TextView(this);
        category.setText(sourceName(item.source));
        category.setTextColor(accentColor);
        category.setTextSize(11);
        category.setTypeface(null, Typeface.BOLD);
        content.addView(category);

        TextView title = new TextView(this);
        title.setText(item.title);
        title.setTextColor(textColor);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        title.setMaxLines(2);
        title.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = dp(3);
        content.addView(title, titleParams);

        if (!item.date.isEmpty()) {
            TextView date = new TextView(this);
            date.setText(item.date);
            date.setTextColor(secondaryColor);
            date.setTextSize(11);
            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dateParams.topMargin = dp(5);
            content.addView(date, dateParams);
        }

        TextView arrow = new TextView(this);
        arrow.setText("›");
        arrow.setTextSize(26);
        arrow.setTextColor(0xFF9B8775);
        card.addView(arrow);

        card.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, "Не удалось открыть статью", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = dp(index == NEWS_LIMIT - 1 ? 0 : 8);
        homeNewsContainer.addView(card, cardParams);
    }

    private String sourceName(String source) {
        if (source.contains("shazoo.ru")) return "SHAZOO";
        if (source.contains("stopgame.ru")) return "STOPGAME";
        if (source.contains("igromania.ru")) return "ИГРОМАНИЯ";
        if (source.contains("dtf.ru")) return "DTF";
        if (source.contains("goha.ru")) return "GOHA";
        return "FROMSOFTWARE";
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class HomeNewsItem {
        final String title;
        final String description;
        final String link;
        final String date;
        final String source;

        HomeNewsItem(String title, String description, String link, String date, String source) {
            this.title = title;
            this.description = description;
            this.link = link;
            this.date = date;
            this.source = source;
        }
    }
}
