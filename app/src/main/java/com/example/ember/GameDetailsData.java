
package com.example.ember;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsData {

    public String title;
    public String genre;
    public String developer;
    public String releaseDate;
    public String platforms;
    public String description;
    public String storeUrl;

    public boolean hasPcVersion;

    public String minimumRequirements;
    public String recommendedRequirements;

    public List<Integer> screenshots = new ArrayList<>();

    public GameDetailsData(
            String title,
            String genre,
            String developer,
            String releaseDate,
            String platforms,
            String description,
            String storeUrl
    ) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.platforms = platforms;
        this.description = description;
        this.storeUrl = storeUrl;
    }

    public void setPcRequirements(
            String minimum,
            String recommended
    ) {
        hasPcVersion = true;
        minimumRequirements = minimum;
        recommendedRequirements = recommended;
    }

    public void addScreenshot(int imageResource) {
        if (imageResource != 0) {
            screenshots.add(imageResource);
        }
    }
}
