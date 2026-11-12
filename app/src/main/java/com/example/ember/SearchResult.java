package com.example.ember;

public class SearchResult {

    private final String title;
    private final String subtitle;
    private final String category;
    private final String keywords;
    private final int imageResource;
    private final String type;
    private final String targetId;

    public SearchResult(
            String title,
            String subtitle,
            String category,
            String keywords,
            int imageResource,
            String type,
            String targetId
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.keywords = keywords;
        this.imageResource = imageResource;
        this.type = type;
        this.targetId = targetId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getCategory() {
        return category;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getType() {
        return type;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getSearchText() {
        return (title + " " + subtitle + " " +
                category + " " + keywords).toLowerCase();
    }
}