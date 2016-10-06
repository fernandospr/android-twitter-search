package com.fernandospr.twittersearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.ViewHolder> {

    private List<Tweet> mTweetList;

    public TweetListAdapter() {
        this.mTweetList = new ArrayList<>();
    }

    public void update(List<Tweet> tweetList) {
        this.mTweetList = tweetList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetItemView = inflater.inflate(R.layout.tweet_item, parent, false);
        return new ViewHolder(tweetItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweetList.get(position);
        holder.content.setText(tweet.getContent());
        DateFormat format = SimpleDateFormat.getTimeInstance();
        holder.createdAt.setText(format.format(tweet.getCreatedAt()));
        holder.username.setText(tweet.getUsername());
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView username;
        final TextView createdAt;
        final TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.usernameTextView);
            createdAt = (TextView) itemView.findViewById(R.id.createdAtTextView);
            content = (TextView) itemView.findViewById(R.id.contentTextView);
        }
    }
}
