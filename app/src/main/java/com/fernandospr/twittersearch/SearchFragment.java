package com.fernandospr.twittersearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fernandospr.twittersearch.repository.RepositoryCallback;
import com.fernandospr.twittersearch.repository.TwitterRepository;

import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView mTweetsRecyclerView;
    private TweetListAdapter mAdapter;
    private SearchFragmentListener mListener;
    private ProgressBar mProgressBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentListener) {
            mListener = (SearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTweetsRecyclerView = (RecyclerView) view.findViewById(R.id.tweetsRecyclerView);
        setupTweetListView();

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        hideLoading();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // TODO: Restore tweets
        } else {
            showHelp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Save tweets
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener.getRepository().stop();
        }
        mListener = null;
    }

    private void setupTweetListView() {
        mAdapter = new TweetListAdapter(new TweetDateFormatter());
        mTweetsRecyclerView.setAdapter(mAdapter);
        mTweetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showError(String message) {
        showLoading(false);
        // TODO: Show error
    }

    private void showHelp() {
        showLoading(false);
        showRecyclerView(false);
        // TODO: Show help
    }

    private void showTweetList(List<Tweet> tweetList) {
        mAdapter.update(tweetList);
        showLoading(false);
        showRecyclerView(true);
        // TODO: Check if empty
    }

    public void onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            showHelp();
        } else {
            getTweetList(query);
        }
    }

    private void getTweetList(String query) {
        if (mListener != null) {
            showLoading(true);
            showRecyclerView(false);
            mListener.getRepository().getTweetList(query, new RepositoryCallback<List<Tweet>>() {
                @Override
                public void onSuccess(List<Tweet> tweetList) {
                    showTweetList(tweetList);
                }

                @Override
                public void onFailure(Throwable error) {
                    showError(error.getMessage());
                }
            });
        }
    }

    private void showLoading(boolean visible) {
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showRecyclerView(boolean visible) {
        if (visible) {
            mTweetsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mTweetsRecyclerView.setVisibility(View.GONE);
        }
    }

    public interface SearchFragmentListener {
        TwitterRepository getRepository();
    }
}
