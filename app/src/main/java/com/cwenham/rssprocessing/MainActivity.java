package com.cwenham.rssprocessing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    private static final String TAG = "Christian";

    private SharedPreferences sharedPreferences;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titles;
    private ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        ProcessInBackground task = new ProcessInBackground();
        task.execute();

        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();

                Uri uri = Uri.parse(links.get(position));
                String urlString = uri.toString();
                Intent intent = new Intent(view.getContext(), RSSWebviewActivity.class);
                intent.putExtra("Link", "http://www.zam.com" + urlString);
                startActivity(intent);

            }
        });
    }

    public InputStream getInputStream(URL url) {
        Log.d(TAG, "getInputStream");
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
            Log.d(TAG, "onPreExecute");
            progressDialog.setMessage("Loading feeds...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {
            Log.d(TAG, "doInBackground");
            try {
                URL url = new URL("http://www.zam.com/feeds/rss/");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();

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
            Log.d(TAG, "onPostExecute");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, titles);
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
}
