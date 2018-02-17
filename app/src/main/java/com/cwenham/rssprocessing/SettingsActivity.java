package com.cwenham.rssprocessing;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "Christian";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editor = getSharedPreferences("general", 0).edit();

        Spinner feedSpinner = findViewById(R.id.feed);
        Spinner fontSpinner = findViewById(R.id.font);

        ArrayAdapter<CharSequence> feedAdapter = ArrayAdapter.createFromResource(this,
                R.array.rrs_feed, android.R.layout.simple_spinner_item);
        feedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedSpinner.setAdapter(feedAdapter);

        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this,
                R.array.font_size, android.R.layout.simple_spinner_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);

    }

}
