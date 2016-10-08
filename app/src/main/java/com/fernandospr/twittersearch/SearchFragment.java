package com.fernandospr.twittersearch;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TWEET_LIST_KEY = "TWEET_LIST_KEY";
    private static final String QUERY_KEY = "QUERY_KEY";
    private RecyclerView mTweetsRecyclerView;
    private TweetListAdapter mAdapter;
    private SearchFragmentListener mListener;
    private ProgressBar mProgressBar;
    private List<Tweet> mTweetList;
    private View mEmptyView;
    private View mErrorView;
    private View mHelpView;
    private String mQuery;

    public SearchFragment() {
        // Required empty public constructor
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

        mEmptyView = view.findViewById(R.id.emptyView);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.requestSearchViewFocus();
                }
            }
        });

        mErrorView = view.findViewById(R.id.errorView);
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onQueryTextSubmit(mQuery);
            }
        });

        mHelpView = view.findViewById(R.id.helpView);
        mHelpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.requestSearchViewFocus();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString(QUERY_KEY);
            mTweetList = savedInstanceState.getParcelableArrayList(TWEET_LIST_KEY);
            if (mTweetList != null) {
                showTweetList(mTweetList);
                return;
            }
        }

        showHelp();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTweetList != null) {
            outState.putParcelableArrayList(TWEET_LIST_KEY, new ArrayList<Parcelable>(mTweetList));
        }
        outState.putString(QUERY_KEY, mQuery);
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
        mAdapter = new TweetListAdapter(getContext(), new TweetDateFormatter());
        mTweetsRecyclerView.setAdapter(mAdapter);
        mTweetsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            showHelp();
        } else {
            getTweetList(query);
        }
    }

    private void getTweetList(String query) {
        mQuery = query;
        if (mListener != null) {
            showLoading();
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

    private void showLoading() {
        showLoadingView(true);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(false);
        showHelpView(false);
    }

    private void showError(String message) {
        mTweetList = null;

        showLoadingView(false);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(true);
        showHelpView(false);
    }

    private void showHelp() {
        showLoadingView(false);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(false);
        showHelpView(true);
    }

    private void showTweetList(List<Tweet> tweetList) {
        mTweetList = tweetList;
        mAdapter.update(tweetList);
        boolean empty = tweetList == null || tweetList.size() == 0;

        showLoadingView(false);
        showEmptyView(empty);
        showRecyclerView(!empty);
        showErrorView(false);
        showHelpView(false);

        mTweetsRecyclerView.scrollToPosition(0);
    }

    private void showLoadingView(boolean visible) {
        showView(mProgressBar, visible);
    }

    private void showEmptyView(boolean visible) {
        showView(mEmptyView, visible);
    }

    private void showRecyclerView(boolean visible) {
        showView(mTweetsRecyclerView, visible);
    }

    private void showErrorView(boolean visible) {
        showView(mErrorView, visible);
    }

    private void showHelpView(boolean visible) {
        showView(mHelpView, visible);
    }

    private void showView(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public interface SearchFragmentListener {
        TwitterRepository getRepository();

        void requestSearchViewFocus();
    }
}
