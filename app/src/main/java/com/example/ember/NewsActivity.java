package com.example.ember;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {

    private static final String[] RSS_URLS = {
            "https://shazoo.ru/feed/rss",
            "https://rss.stopgame.ru/rss_news.xml",
            "https://rss.stopgame.ru/rss_all.xml",
            "https://rss.stopgame.ru/rss_frontpage.xml",
            "https://rss.stopgame.ru/articles.xml",
            "https://www.igromania.ru/rss/news-game.rss",
            "https://dtf.ru/rss/all",
            "https://www.goha.ru/rss/news",
            "https://www.goha.ru/rss/articles"
    };

    private static final String PREFS_NAME = "ember_news_cache";
    private static final String CACHE_KEY = "saved_news";

    private final int backgroundColor = 0xFFF4DFCA;
    private final int cardColor = 0xFFFFF5E9;
    private final int textColor = 0xFF211812;
    private final int secondaryColor = 0xFF806B59;
    private final int accentColor = 0xFFB84F13;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private LinearLayout newsContainer;
    private ProgressBar progressBar;
    private TextView statusText;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createInterface();
        loadNews();
    }

    private void createInterface() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(backgroundColor);
        root.setPadding(dp(20), dp(20), dp(20), dp(12));

        TextView title = new TextView(this);
        title.setText("Новости");
        title.setTextSize(30);
        title.setTextColor(textColor);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Последние новости FromSoftware");
        subtitle.setTextSize(14);
        subtitle.setTextColor(secondaryColor);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subtitleParams.topMargin = dp(5);
        subtitleParams.bottomMargin = dp(12);
        root.addView(subtitle, subtitleParams);

        TextView refreshButton = new TextView(this);
        refreshButton.setText("↻  Обновить новости");
        refreshButton.setTextSize(15);
        refreshButton.setTextColor(accentColor);
        refreshButton.setTypeface(null, Typeface.BOLD);
        refreshButton.setPadding(0, dp(8), 0, dp(12));
        refreshButton.setClickable(true);
        refreshButton.setOnClickListener(v -> loadNews());
        root.addView(refreshButton);

        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(dp(32), dp(32));
        progressParams.gravity = Gravity.CENTER_HORIZONTAL;
        progressParams.bottomMargin = dp(12);
        root.addView(progressBar, progressParams);

        statusText = new TextView(this);
        statusText.setTextColor(secondaryColor);
        statusText.setTextSize(14);
        statusText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams statusParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statusParams.bottomMargin = dp(12);
        root.addView(statusText, statusParams);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        newsContainer = new LinearLayout(this);
        newsContainer.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(newsContainer);
        root.addView(scrollView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        // Нижняя панель навигации — как на остальных экранах.
        root.addView(createBottomNavigation());
        setContentView(root);
    }

    private LinearLayout createBottomNavigation() {
        LinearLayout navigation = new LinearLayout(this);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        navigation.setGravity(Gravity.CENTER);
        navigation.setBackgroundColor(0xFFE9CDB2);

        LinearLayout.LayoutParams navigationParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(64));
        navigationParams.topMargin = dp(8);
        navigation.setLayoutParams(navigationParams);

        addNavigationItem(navigation, R.drawable.home, "Главная", false, () -> {
            Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        addNavigationItem(navigation, R.drawable.search, "Поиск", false, () -> {
            startActivity(new Intent(NewsActivity.this, SearchActivity.class));
        });

        addNavigationItem(navigation, R.drawable.news, "Новости", true, () -> {
            // Уже на странице новостей.
        });

        addNavigationItem(navigation, R.drawable.guide, "Гайды", false, () -> {
            startActivity(new Intent(NewsActivity.this, GuidesActivity.class));
        });

        return navigation;
    }

    private void addNavigationItem(LinearLayout navigation, int iconRes,
                                   String description, boolean selected, Runnable action) {
        FrameLayout item = new FrameLayout(this);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        item.setLayoutParams(itemParams);
        item.setClickable(true);
        item.setFocusable(true);
        item.setForeground(getDrawable(android.R.drawable.list_selector_background));

        if (selected) {
            item.setBackgroundColor(0xFFF5B77C);
        }

        ImageView icon = new ImageView(this);
        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(
                dp(28), dp(28), Gravity.CENTER);
        icon.setLayoutParams(iconParams);
        icon.setImageResource(iconRes);
        icon.setContentDescription(description);
        item.addView(icon);

        item.setOnClickListener(v -> action.run());
        navigation.addView(item);
    }

    private void loadNews() {
        if (isLoading) return;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        statusText.setText("Загружаем новости...");
        newsContainer.removeAllViews();

        executor.execute(() -> {
            List<NewsItem> news = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (String feedUrl : RSS_URLS) {
                try {
                    List<NewsItem> feedNews = fetchFeed(feedUrl);
                    for (NewsItem item : feedNews) {
                        if (!containsLink(news, item.link)) news.add(item);
                    }
                } catch (Exception e) {
                    errors.add(feedUrl + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
                    Log.e("EMBER_NEWS", "Ошибка RSS: " + feedUrl, e);
                }
            }

            sortNewsByDate(news);
            if (news.size() > 50) news = new ArrayList<>(news.subList(0, 50));
            final List<NewsItem> finalNews = news;
            final List<String> finalErrors = errors;
            if (!finalNews.isEmpty()) saveNewsToCache(finalNews);

            runOnUiThread(() -> {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                if (!finalNews.isEmpty()) {
                    statusText.setText("Найдено новостей: " + finalNews.size());
                    newsContainer.removeAllViews();
                    for (NewsItem item : finalNews) addNewsCard(item);
                } else {
                    String message = finalErrors.isEmpty()
                            ? "Источники доступны, но подходящие публикации не найдены."
                            : "Не удалось загрузить новости. Проверь интернет-соединение.";
                    showCachedNews(message);
                }
            });
        });
    }

    private List<NewsItem> fetchFeed(String feedUrl) throws Exception {
        List<NewsItem> result = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(feedUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) EMBER/1.0");
            connection.setRequestProperty("Accept", "application/rss+xml, application/atom+xml, application/xml, text/xml, */*");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) throw new Exception("HTTP " + responseCode);

            try (InputStream input = connection.getInputStream()) {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(input, null);

                NewsItemBuilder builder = null;
                String activeField = null;
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tag = normalizedTag(parser.getName());
                        if ("item".equals(tag) || "entry".equals(tag)) {
                            builder = new NewsItemBuilder();
                            activeField = null;
                        } else if (builder != null) {
                            if ("title".equals(tag)) activeField = "title";
                            else if ("description".equals(tag) || "summary".equals(tag)
                                    || "content".equals(tag) || "encoded".equals(tag)) activeField = "description";
                            else if ("link".equals(tag)) {
                                String href = parser.getAttributeValue(null, "href");
                                if (href != null && !href.trim().isEmpty()) builder.link = href.trim();
                                activeField = "link";
                            } else if ("pubdate".equals(tag) || "published".equals(tag)
                                    || "updated".equals(tag) || "date".equals(tag)) activeField = "date";
                            else activeField = null;
                        }
                    } else if ((eventType == XmlPullParser.TEXT || eventType == XmlPullParser.CDSECT)
                            && builder != null && activeField != null) {
                        String value = parser.getText();
                        if (value != null) {
                            switch (activeField) {
                                case "title": builder.title.append(value); break;
                                case "description": builder.description.append(value); break;
                                case "link": if (builder.link.isEmpty()) builder.link = value.trim(); break;
                                case "date": builder.pubDate.append(value); break;
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        String tag = normalizedTag(parser.getName());
                        if (builder != null && ("item".equals(tag) || "entry".equals(tag))) {
                            NewsItem item = new NewsItem(
                                    cleanText(builder.title.toString()),
                                    cleanText(builder.description.toString()),
                                    cleanUrl(builder.link),
                                    builder.pubDate.toString().trim(),
                                    feedUrl);
                            if (!item.title.isEmpty() && !item.link.isEmpty()
                                    && isFromSoftwareNews(item) && !containsLink(result, item.link)) {
                                result.add(item);
                            }
                            builder = null;
                            activeField = null;
                            if (result.size() >= 50) break;
                        } else if (builder != null && activeField != null
                                && ("title".equals(tag) || "description".equals(tag)
                                || "summary".equals(tag) || "content".equals(tag)
                                || "encoded".equals(tag) || "link".equals(tag)
                                || "pubdate".equals(tag) || "published".equals(tag)
                                || "updated".equals(tag) || "date".equals(tag))) {
                            activeField = null;
                        }
                    }
                    eventType = parser.next();
                }
            }
        } finally {
            if (connection != null) connection.disconnect();
        }
        return result;
    }

    private String normalizedTag(String tag) {
        if (tag == null) return "";
        int colon = tag.indexOf(':');
        if (colon >= 0) tag = tag.substring(colon + 1);
        return tag.toLowerCase(Locale.ROOT);
    }

    private boolean isFromSoftwareNews(NewsItem item) {
        String text = (item.title + " " + item.description).toLowerCase(Locale.ROOT);
        String[] keywords = {
                "fromsoftware", "from software", "from soft", "фромсофт", "фром софт",
                "elden ring", "элден ринг", "nightreign", "night reign", "ночной рейн",
                "dark souls", "дарк соулс", "demon's souls", "demons souls", "demon’s souls",
                "demon souls", "демонс соулс", "bloodborne", "бладборн", "бладборн",
                "секиро", "sekiro", "armored core", "armoured core", "арморед кор",
                "the duskbloods", "duskbloods", "даскбладс", "дaскбладс",
                "elden ring: shadow of the erdtree", "shadow of the erdtree",
                "тень древa", "тень древа", "shadow of the erdtree", "fromsoftware's"
        };
        for (String keyword : keywords) if (text.contains(keyword)) return true;
        return false;
    }

    private boolean containsLink(List<NewsItem> news, String link) {
        String normalized = normalizeLink(link);
        for (NewsItem item : news) {
            if (normalizeLink(item.link).equalsIgnoreCase(normalized)) return true;
        }
        return false;
    }

    private String normalizeLink(String link) {
        if (link == null) return "";
        String value = link.trim();
        while (value.endsWith("/")) value = value.substring(0, value.length() - 1);
        return value;
    }

    private String cleanUrl(String url) {
        if (url == null) return "";
        String result = url.trim();
        if (result.startsWith("//")) result = "https:" + result;
        return result;
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.replaceAll("(?i)<br\\s*/?>", " ")
                .replaceAll("<[^>]*>", " ")
                .replace("&amp;", "&").replace("&quot;", "\"")
                .replace("&#39;", "'").replace("&#039;", "'")
                .replace("&lt;", "<").replace("&gt;", ">")
                .replace("&nbsp;", " ").replaceAll("\\s+", " ").trim();
    }

    private void saveNewsToCache(List<NewsItem> news) {
        try {
            JSONArray array = new JSONArray();
            for (NewsItem item : news) {
                JSONObject object = new JSONObject();
                object.put("title", item.title);
                object.put("description", item.description);
                object.put("link", item.link);
                object.put("pubDate", item.pubDate);
                object.put("source", item.source);
                array.put(object);
            }
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putString(CACHE_KEY, array.toString()).apply();
        } catch (Exception e) {
            Log.e("EMBER_NEWS", "Не удалось сохранить кэш", e);
        }
    }

    private List<NewsItem> loadNewsFromCache() {
        List<NewsItem> news = new ArrayList<>();
        try {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String saved = preferences.getString(CACHE_KEY, null);
            if (saved == null) return news;
            JSONArray array = new JSONArray(saved);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                NewsItem item = new NewsItem(object.optString("title", ""),
                        object.optString("description", ""), object.optString("link", ""),
                        object.optString("pubDate", ""), object.optString("source", ""));
                if (!item.title.isEmpty() && !item.link.isEmpty() && !containsLink(news, item.link)) news.add(item);
            }
        } catch (Exception e) {
            Log.e("EMBER_NEWS", "Не удалось прочитать кэш", e);
        }
        return news;
    }

    private long parseNewsDate(String value) {
        if (value == null || value.trim().isEmpty()) return 0L;
        String[] formats = {
                "EEE, dd MMM yyyy HH:mm:ss Z", "EEE, dd MMM yyyy HH:mm Z",
                "dd MMM yyyy HH:mm:ss Z", "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ssXXX", "yyyy-MM-dd"
        };
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                sdf.setLenient(true);
                Date date = sdf.parse(value.trim());
                if (date != null) return date.getTime();
            } catch (ParseException ignored) { }
        }
        return 0L;
    }

    private void sortNewsByDate(List<NewsItem> news) {
        Collections.sort(news, (first, second) -> Long.compare(
                parseNewsDate(second.pubDate), parseNewsDate(first.pubDate)));
    }

    private void showCachedNews(String message) {
        List<NewsItem> cached = loadNewsFromCache();
        newsContainer.removeAllViews();
        if (cached.isEmpty()) {
            statusText.setText(message + "\nПопробуй обновить позже.");
            return;
        }
        sortNewsByDate(cached);
        statusText.setText(message + "\nПоказаны сохранённые новости: " + cached.size());
        for (NewsItem item : cached) addNewsCard(item);
    }

    private String getSourceName(String feedUrl) {
        if (feedUrl.contains("shazoo.ru")) return "SHAZOO";
        if (feedUrl.contains("stopgame.ru")) return "STOPGAME";
        if (feedUrl.contains("igromania.ru")) return "ИГРОМАНИЯ";
        if (feedUrl.contains("dtf.ru")) return "DTF";
        if (feedUrl.contains("goha.ru")) return "GOHA";
        return "НОВОСТИ";
    }

    private void addNewsCard(NewsItem item) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        GradientDrawable background = new GradientDrawable();
        background.setColor(cardColor);
        background.setCornerRadius(dp(18));
        card.setBackground(background);
        card.setClickable(true);
        card.setFocusable(true);

        TextView category = new TextView(this);
        category.setText("FROMSOFTWARE" + (item.source.isEmpty() ? "" : "  •  " + getSourceName(item.source)));
        category.setTextColor(accentColor);
        category.setTextSize(11);
        category.setTypeface(null, Typeface.BOLD);
        card.addView(category);

        TextView title = new TextView(this);
        title.setText(item.title);
        title.setTextColor(textColor);
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = dp(8);
        card.addView(title, titleParams);

        if (!item.description.isEmpty()) {
            TextView description = new TextView(this);
            description.setText(item.description);
            description.setTextColor(secondaryColor);
            description.setTextSize(14);
            description.setMaxLines(3);
            description.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            descriptionParams.topMargin = dp(8);
            card.addView(description, descriptionParams);
        }

        TextView date = new TextView(this);
        date.setText(item.pubDate.isEmpty() ? "Дата не указана" : item.pubDate);
        date.setTextColor(secondaryColor);
        date.setTextSize(12);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.topMargin = dp(12);
        card.addView(date, dateParams);

        card.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
            } catch (Exception e) {
                Toast.makeText(NewsActivity.this, "Не удалось открыть статью", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = dp(12);
        newsContainer.addView(card, cardParams);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private static class NewsItemBuilder {
        final StringBuilder title = new StringBuilder();
        final StringBuilder description = new StringBuilder();
        final StringBuilder pubDate = new StringBuilder();
        String link = "";
    }

    private static class NewsItem {
        final String title;
        final String description;
        final String link;
        final String pubDate;
        final String source;

        NewsItem(String title, String description, String link, String pubDate, String source) {
            this.title = title == null ? "" : title;
            this.description = description == null ? "" : description;
            this.link = link == null ? "" : link;
            this.pubDate = pubDate == null ? "" : pubDate;
            this.source = source == null ? "" : source;
        }
    }
}
