package com.example.ember;

public class Game {

    private final String title;
    private final int imageResource;

    public Game(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }
}
