package com.cwenham.rssprocessing;

/**
 * Created by Theon on 2018-02-09.
 */

public class RssFeedModel {

    public String title;
    public String link;
    public String pubDate;
    public String description;

    public RssFeedModel(String title, String link, String pubDate, String description) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
    }
}
