package com.fernandospr.twittersearch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class TweetStatuses {
    private final List<Status> statuses;

    public TweetStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public static class Status {
        @SerializedName("created_at")
        private final Date createdAt;

        private final String text;

        private final User user;

        private static class User {
            private final String name;
            @SerializedName("screen_name")
            private final String screenName;

            private User(String name, String screenName) {
                this.name = name;
                this.screenName = screenName;
            }

            public String getName() {
                return name;
            }

            public String getScreenName() {
                return screenName;
            }
        }

        private Status(Date createdAt, String text, User user) {
            this.createdAt = createdAt;
            this.text = text;
            this.user = user;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getText() {
            return text;
        }

        public String getUserName() {
            return user.getName();
        }

        public String getUserScreenName() {
            return user.getScreenName();
        }
    }

    public List<Status> getStatuses() {
        return statuses;
    }
}

