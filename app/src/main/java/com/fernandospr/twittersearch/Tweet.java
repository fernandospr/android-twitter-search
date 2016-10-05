package com.fernandospr.twittersearch;

public class Tweet {
    private final String username;
    private final String content;
    private final String createdAt;

    public Tweet(String username, String content, String createdAt) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
