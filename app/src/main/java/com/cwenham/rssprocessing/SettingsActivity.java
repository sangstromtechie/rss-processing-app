package com.cwenham.rssprocessing;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "Christian";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SettingsActivity - onCreate");
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("general", 0);

        Spinner feedSpinner = findViewById(R.id.feed);
        Spinner fontSpinner = findViewById(R.id.font);
        Spinner colorSpinner = findViewById(R.id.fontColor);
        Spinner rememberSpinner = findViewById(R.id.style);

        ArrayAdapter<CharSequence> feedAdapter = ArrayAdapter.createFromResource(this,
                R.array.rrs_feed, android.R.layout.simple_spinner_item);
        feedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedSpinner.setAdapter(feedAdapter);
        feedSpinner.setSelection(sharedPreferences.getInt("Feed", 0));
        feedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position) {
                    case 0:
                        editor.putString("URL", "http://www.zam.com/feeds/rss/");
                        editor.putInt("Feed", 0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putString("URL", "http://www.wowhead.com/news&rss");
                        editor.putInt("Feed", 1);
                        editor.apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this,
                R.array.font_size, android.R.layout.simple_spinner_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);
        fontSpinner.setSelection(sharedPreferences.getInt("SizeSelect", 1));
        fontSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position) {
                    case 0:
                        editor.putString("Size", "14");
                        editor.putInt("SizeSelect", 0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putString("Size", "18");
                        editor.putInt("SizeSelect", 1);
                        editor.apply();
                        break;
                    case 2:
                        editor.putString("Size", "22");
                        editor.putInt("SizeSelect", 2);
                        editor.apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.back_color, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setSelection(sharedPreferences.getInt("ColorSelect", 0));
        colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position) {
                    case 0:
                        editor.putInt("Color", Color.WHITE);
                        editor.putInt("ColorSelect", 0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putInt("Color", Color.CYAN);
                        editor.putInt("ColorSelect", 1);
                        editor.apply();
                        break;
                    case 2:
                        editor.putInt("Color", Color.RED);
                        editor.putInt("ColorSelect", 2);
                        editor.apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> rememberAdapter = ArrayAdapter.createFromResource(this,
                R.array.remember, android.R.layout.simple_spinner_item);
        rememberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rememberSpinner.setAdapter(rememberAdapter);
        rememberSpinner.setSelection(sharedPreferences.getInt("RememberSelect", 0));
        rememberSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position) {
                    case 0:
                        editor.putString("Remember", "No");
                        editor.putInt("RememberSelect", 0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putString("Remember", "Yes");
                        editor.putInt("RememberSelect", 1);
                        editor.apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
