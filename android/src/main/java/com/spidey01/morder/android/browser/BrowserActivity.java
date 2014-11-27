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
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.spidey01.morder.android.ui.DrawerItemClickListener;
import com.spidey01.morder.android.R;
import com.spidey01.morder.android.ui.ActionBarDrawerToggle;


public class BrowserActivity
    extends Activity
{
    private static final String TAG = "BrowserActivity";

    private DrawerItemClickListener mDrawerListener = new DrawerItemClickListener();

    private MorderWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_browser);

        enableDrawer();

        mWebView = (MorderWebView)findViewById(R.id.webview);
        mWebView.setup(PreferenceManager.getDefaultSharedPreferences(this));
        mWebView.loadUrl(mWebView.getHomePage());
    }


    private ActionBarDrawerToggle mDrawerToggle;
    public void enableDrawer() {
        Log.d(TAG, "enableDrawer()");

        String[] activities = getResources().getStringArray(R.array.drawer_array);
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView view = (ListView)findViewById(R.id.drawer_view);
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);

        view.setAdapter(adapter);
        view.setOnItemClickListener(mDrawerListener);

        assert mDrawerToggle == null;
        mDrawerToggle = new ActionBarDrawerToggle(this, layout);
        layout.setDrawerListener(mDrawerToggle);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mWebView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(mWebView);
    }


    /**
     * Called when a key was pressed down and not handled by any of the views
     * inside of the activity. So, for example, key presses while the cursor
     * is inside a TextView will not trigger the event (unless it is a navigation
     * to another object) because TextView handles its own key presses.
     * <p/>
     * <p>If the focused view didn't want this event, this method is called.
     * <p/>
     * <p>The default implementation takes care of {@link android.view.KeyEvent#KEYCODE_BACK}
     * by calling {@link #onBackPressed()}, though the behavior varies based
     * on the application compatibility mode: for
     * {@link android.os.Build.VERSION_CODES#ECLAIR} or later applications,
     * it will set up the dispatch to call {@link #onKeyUp} where the action
     * will be performed; for earlier applications, it will perform the
     * action immediately in on-down, as those versions of the platform
     * behaved.
     * <p/>
     * <p>Other additional default key handling may be performed
     * if configured with {@link #setDefaultKeyMode}.
     *
     * @param keyCode code for key.
     * @param event event for key.
     * @return Return <code>true</code> to prevent this event from being propagated
     * further, or <code>false</code> to indicate that you have not handled
     * this event and it should continue to be propagated.
     * @see #onKeyUp
     * @see android.view.KeyEvent
     */
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        // Note: Android calls backspace DEL and delete FORWARD_DEL.
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DEL)
            && mWebView.canGoBack())
        {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


}

