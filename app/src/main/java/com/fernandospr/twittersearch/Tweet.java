package com.fernandospr.twittersearch;

import com.fernandospr.twittersearch.network.models.TweetStatuses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {
    private final String username;
    private final String content;
    private final Date createdAt;

    public Tweet(String username, String content, Date createdAt) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static List<Tweet> buildTweets(TweetStatuses statuses) {
        List<Tweet> tweetList = new ArrayList<>();
        for (TweetStatuses.Status status : statuses.getStatuses()) {
            String content = status.getText();
            String username = status.getUserName();
            Date createdAt = status.getCreatedAt();
            tweetList.add(new Tweet(username, content, createdAt));
        }
        return tweetList;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
