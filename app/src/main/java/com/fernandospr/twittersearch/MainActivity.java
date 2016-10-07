package com.fernandospr.twittersearch;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.fernandospr.twittersearch.repository.TwitterRepository;

public class MainActivity extends AppCompatActivity
        implements SearchFragment.SearchFragmentListener,
        SearchView.OnQueryTextListener {

    private SearchFragment mFragment;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.searchFragment);
    }

    @Override
    public TwitterRepository getRepository() {
        return ((TwitterSearchApp) getApplication()).getRepository();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mFragment.isVisible()) {
            mFragment.onQueryTextSubmit(query);
            mSearchView.clearFocus();
            return true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
