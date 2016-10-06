package com.fernandospr.twittersearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fernandospr.twittersearch.repository.TwitterRepository;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public TwitterRepository getRepository() {
        return ((TwitterSearchApp)getApplication()).getRepository();
    }
}
