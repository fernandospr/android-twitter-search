package com.fernandospr.twittersearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fernandospr.twittersearch.repository.RepositoryCallback;
import com.fernandospr.twittersearch.repository.TwitterRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends Fragment {

    private final static long TIMER_DELAY = 1000;

    private EditText mSearchEditText;
    private RecyclerView mTweetsRecyclerView;
    private TweetListAdapter mAdapter;
    private SearchFragmentListener mListener;
    private Timer mTimer;

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

        mSearchEditText = (EditText) view.findViewById(R.id.searchEditText);
        setupSearchView();
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
        cancelScheduledSearch();
        mListener = null;
    }

    private void setupTweetListView() {
        mAdapter = new TweetListAdapter();
        mTweetsRecyclerView.setAdapter(mAdapter);
        mTweetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupSearchView() {
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // no-op
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                cancelScheduledSearch();
                if (TextUtils.isEmpty(query)) {
                    showHelp();
                } else {
                    scheduleSearch(editable.toString());
                }
            }
        });
    }

    private void cancelScheduledSearch() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void scheduleSearch(final String query) {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getTweetList(query);
            }
        }, TIMER_DELAY);
    }

    private void getTweetList(String query) {
        if (mListener != null) {
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

    private void showError(String message) {
        // TODO: Hide loading, show error
    }

    private void showHelp() {
        // TODO: show help
    }

    private void showTweetList(List<Tweet> tweetList) {
        mAdapter.update(tweetList);
        // TODO: Hide loading, show recyclerview
    }

    public interface SearchFragmentListener {
        TwitterRepository getRepository();
    }
}
