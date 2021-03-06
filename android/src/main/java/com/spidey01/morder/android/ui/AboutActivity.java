/*
 * Copyright 2014 Terry Mathew Poulin <BigBoss1964@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spidey01.morder.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.spidey01.morder.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.spidey01.morder.android.BuildConfig;


public class AboutActivity extends Activity {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_about);

        WebView view = (WebView)findViewById(R.id.about_webview);
        if (view == null) {
            Log.e(TAG, "view is null!");
            return;
        }

        //view.loadData(getLicense(), "text/html", null);
        view.loadUrl(getLicense());
    }


    static final String VERSION_INFO_HERE = "VERSION_INFO_HERE";
    static final String BUILD_INFO_HERE = "BUILD_INFO_HERE";
    private String getLicense() {

        try {
            InputStream stream =  getAssets().open("license.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF8"));
            String line;
            StringBuilder buffer = new StringBuilder();
            /*
             * Makes sure that the character set will be loaded correctly.
             */
            buffer.append("data:text/html;charset=UTF8;,");
            while ((line = reader.readLine()) != null) {
                if (line.equals(VERSION_INFO_HERE)) {
                    getVersion(buffer);
                } else if (line.equals(BUILD_INFO_HERE)) {
                    getBuildInfo(buffer);
                } else {
                    buffer.append(line);
                }
            }
            reader.close();
            return buffer.toString();
        } catch (IOException ex) {
            Log.e(TAG, "getLicense() failed", ex);
            return ex.toString();
        }
    }


    private void getVersion(StringBuilder s) {
        StringBuilder buffer = new StringBuilder();
        s.append("<p>Git Version: ");
        try {
            BufferedReader version = new BufferedReader(new InputStreamReader(getAssets().open("version")));
            s.append(version.readLine());
            version.close();
        } catch (IOException ex) {
            s.append(ex.toString());
        }
        s.append("</p>");

        s.append("<p>Git branch: ");
        try {
            BufferedReader branch = new BufferedReader(new InputStreamReader(getAssets().open("branch")));
            s.append(branch.readLine());
            branch.close();
        } catch (IOException ex) {
            s.append(ex.toString());
        }
        s.append("</p>");
    }


    private void getBuildInfo(StringBuilder s) {
        s.append("<p>")
         .append("Build Config: ")
         .append(BuildConfig.DEBUG ? "DEBUG" : "RELEASE")
         .append("</p>");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
            // return true;
        // }

        return super.onOptionsItemSelected(item);
    }
}
