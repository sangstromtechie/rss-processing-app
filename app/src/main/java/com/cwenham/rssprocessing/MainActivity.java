package com.cwenham.rssprocessing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    private static final String TAG = "Christian";

    private SharedPreferences sharedPreferences;
    private LinearLayout linearLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titles;
    private ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity - onCreate");
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        ProcessInBackground task = new ProcessInBackground();
        task.execute();

        sharedPreferences = getSharedPreferences("general", 0);
        String checkIfSet = sharedPreferences.getString("Link", null);
        String ifSetTitle = sharedPreferences.getString("Title", null);

        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();


                Intent intent = new Intent(view.getContext(), RSSWebviewActivity.class);
                Uri uri = Uri.parse(links.get(position));
                String urlString = "";
                if(sharedPreferences.getInt("Feed", 0) == 1) {
                    urlString = uri.toString();
                } else if(sharedPreferences.getInt("Feed", 0) == 0) {
                    urlString = "http://www.zam.com" + uri.toString();
                }
                intent.putExtra("Link", urlString);
                intent.putExtra("Title", titles.get(position));
                startActivity(intent);

            }
        });

        if(checkIfSet != null) {
            Intent intent = new Intent(this, RSSWebviewActivity.class);
            intent.putExtra("Link", checkIfSet);
            intent.putExtra("Title", ifSetTitle);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity - onStart");

        try {
            sharedPreferences = getSharedPreferences("general", 0);
            linearLayout = findViewById(R.id.activity_main);

            String fontSizePref = sharedPreferences.getString("Size", "18");

            int themeID = R.style.FontSizeMedium;
            if(fontSizePref == "14") {
                themeID = R.style.FontSizeSmall;
            } else if (fontSizePref == "22") {
                themeID = R.style.FontSizeLarge;
            }

            int backgroundColor = sharedPreferences.getInt("Color", Color.WHITE);

            linearLayout.setBackgroundColor(backgroundColor);

            setTheme(themeID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity - onResume");

        ProcessInBackground task = new ProcessInBackground();
        task.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity - onDestroy");
        sharedPreferences = getSharedPreferences("general", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getString("Remember", "No") == "No") {
            editor.putString("Link", null);
            editor.putString("Title", null);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                ProcessInBackground task = new ProcessInBackground();
                task.execute();
                return true;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public InputStream getInputStream(URL url) {
        Log.d(TAG, "MainActivity - getInputStream");
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "MainActivity - onPreExecute");
            progressDialog.setMessage("Loading feeds...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {
            Log.d(TAG, "MainActivity - doInBackground");
            try {

                sharedPreferences = getSharedPreferences("general", 0);
                URL url = new URL(sharedPreferences.getString("URL","http://www.zam.com/feeds/rss/"));
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();

                titles.clear();
                links.clear();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            Log.d(TAG, "MainActivity - onPostExecute");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, titles);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
}
