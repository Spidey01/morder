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

package com.spidey01.morder.android.browser;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.spidey01.morder.android.DrawerItemClickListener;
import com.spidey01.morder.android.R;


public class BrowserActivity
    extends Activity
{
    private static final String TAG = "BrowserActivity";

    private DrawerItemClickListener mDrawerListener = new DrawerItemClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.browser_activity);

        enableDrawer();

        MorderWebView view = (MorderWebView) findViewById(R.id.webview);
        view.setWebViewClient(new MorderWebViewClient());
        if (view != null) {
            final String home = getResources().getString(R.string.pref_newTabPage_key);
            final String def = getResources().getString(R.string.pref_newTabPage_default);
            /* Huh? We get "pref_userAgent_title" instead of the text boxes stuff. */
            Log.d(TAG, "saved home page: " + home);
            Log.d(TAG, "default home page: " + def);
            view.loadUrl(PreferenceManager.getDefaultSharedPreferences(this).getString(home, def));
        }
    }


    public void enableDrawer() {
        String[] activities = getResources().getStringArray(R.array.drawer_array);
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView view = (ListView)findViewById(R.id.drawer_view);
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);

        view.setAdapter(adapter);
        view.setOnItemClickListener(mDrawerListener);
    }
}


