package com.ostopd.transparenttoolbar.activities;


import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.ostopd.transparenttoolbar.R;
import com.ostopd.transparenttoolbar.fragments.BooksFragment;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Debug.startMethodTracing("app");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("");

        BooksFragment booksFragment = (BooksFragment) getFragmentManager().findFragmentById(R.id.activity_main_last_books_fragment);
        booksFragment.setToolbar(toolbar);

        setSupportActionBar(toolbar);

        Debug.stopMethodTracing();
    }
}
