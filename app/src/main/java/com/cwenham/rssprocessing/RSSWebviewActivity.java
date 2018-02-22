package com.cwenham.rssprocessing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RSSWebviewActivity extends AppCompatActivity {

    private static final String TAG = "Christian";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsswebview);
        Log.d(TAG, "RSSWebViewActivity - onCreate");

        sharedPreferences = getSharedPreferences("general", 0);
        editor = sharedPreferences.edit();

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("Link");
        String title = extras.getString("Title");
        setTitle(title);
        editor.putString("Link", url);
        editor.putString("Title", title);
        editor.apply();
        WebView myWebView = findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "RSSWebViewActivity - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RSSWebViewActivity - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "RSSWebViewActivity - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "RSSWebViewActivity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RSSWebViewActivity - onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(sharedPreferences.getString("Remember", "No") != "Yes") {
            editor.putString("Link", null);
            editor.putString("Title", null);
            editor.apply();
        }
    }

}
