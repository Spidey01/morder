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
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.*;
import com.spidey01.morder.android.BuildConfig;
import com.spidey01.morder.android.ui.DrawerItemClickListener;
import com.spidey01.morder.android.R;
import com.spidey01.morder.android.ui.ActionBarDrawerToggle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class BrowserActivity
    extends Activity
    implements MorderWebObserver
{
    private static final String TAG = "BrowserActivity";

    private DrawerItemClickListener mDrawerListener = new DrawerItemClickListener();

    private MorderWebView mWebView;

    private Menu mMenu;

    private ShareActionProvider mShareActionProvider;

    /** SearchView used when the show search UI preference is set. */
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(): " + savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_browser);

        enableDrawer();

        mWebView = (MorderWebView)findViewById(R.id.webview);
        mWebView.setup(PreferenceManager.getDefaultSharedPreferences(this));
        mWebView.setObserver(this);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            Log.d(TAG, "No intent: load home page");
            mWebView.loadUrl(mWebView.getHomePage());
        } else {
            Log.d(TAG, "we got an intent url to load!");
            handleIntent(intent);
        }

    }


    private ActionBarDrawerToggle mDrawerToggle;
    public void enableDrawer() {
        Log.d(TAG, "enableDrawer()");

        String[] activities = getResources().getStringArray(R.array.drawer_array);
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView view = (ListView)findViewById(R.id.drawer_view);
        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);

        view.setAdapter(adapter);
        view.setOnItemClickListener(mDrawerListener);

        assert mDrawerToggle == null;
        mDrawerToggle = new ActionBarDrawerToggle(this, layout);
        layout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
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

        /*
         * Handle landscape / portrait orientation change
         */
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "onConfigurationChanged(): Configuration.ORIENTATION_LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(TAG, "onConfigurationChanged(): Configuration.ORIENTATION_PORTRAIT");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch(item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                break;
            case R.id.action_home:
                mWebView.loadUrl(mWebView.getHomePage());
                break;
            case R.id.action_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                break;
            case R.id.action_forward:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                break;
            /*
            default:
                Log.e(TAG, "onOptionsItemSelected(): unknown item:" + item.toString());
                break;
            */
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * Helper to make the SearchView call our handleSearch() method.
     */
    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.d(TAG, "mSearchListener.onQueryTextSubmit(): s=" + s);
            BrowserActivity.this.handleSearch(s);
            return false;
        }


        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };


    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_browser_activity_actions, menu);
        mMenu = menu;

        menu.findItem(R.id.action_back).setEnabled(mWebView.canGoBack());
        menu.findItem(R.id.action_forward).setEnabled(mWebView.canGoForward());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean searchShowUi = prefs.getBoolean(
                getResources().getString(R.string.pref_searchShowUi_key),
                getResources().getBoolean(R.bool.pref_showZoomControls_default)
        );

        if (searchShowUi) {
            SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
            //mSearchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
            mSearchView = new SearchView(getActionBar().getThemedContext());
            menu.findItem(R.id.action_search).setActionView(mSearchView);
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconifiedByDefault(true);
            mSearchView.setOnQueryTextListener(mSearchListener);
        }

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)shareItem.getActionProvider();

        return super.onCreateOptionsMenu(menu);
    }


    private final Handler mUiHandler = new Handler();
    private Runnable mPageLoadStopper = new Runnable() {
        @Override public void run() {
            Log.v(TAG, "mPageLoadStopper.run(): force stopping page load");
            mWebView.stopLoading();
        }
    };

    @Override
    public void onPageStarted(MorderWebView view, String url, Bitmap favicon) {
        Log.v(TAG, "onPageStarted()");

        int pageTimeout = mWebView.getPageTimeout();
        if (pageTimeout != MorderWebView.PAGE_TIMEOUT_NEVER) {
            Log.v(TAG, "Setting page timeout.");
            mUiHandler.postDelayed(mPageLoadStopper, pageTimeout /* seconds */ * 1000);
        }

        if (mMenu != null) {
            mMenu.findItem(R.id.action_back).setEnabled(mWebView.canGoBack());
            Log.d(TAG, "action_back: "+ mMenu.findItem(R.id.action_back).isEnabled());
        }

        if (mShareActionProvider != null) {
            Log.v(TAG, "Updating Intent for mShareActionProvider.");
            /*
             * We don't really want to do all this every page start.
             * We should find a way to do it dynamically via callback or a custom impl of ShareActionProvider that has a reference to mWebView.
             */
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.putExtra(Intent.EXTRA_SUBJECT, view.getTitle());
            //intent.putExtra(???, favicon);
            mShareActionProvider.setShareIntent(intent);
        }
    }


    @Override
    public void onPageFinished(MorderWebView view, String url) {
        Log.v(TAG, "onPageFinished()");
        if (mMenu != null) {
            mMenu.findItem(R.id.action_forward).setEnabled(mWebView.canGoForward());
            Log.d(TAG, "action_forward: "+ mMenu.findItem(R.id.action_forward).isEnabled());
        }


        if (mWebView.getPageTimeout() != MorderWebView.PAGE_TIMEOUT_NEVER) {
            Log.v(TAG, "Clearing page timeout.");
            mUiHandler.removeCallbacks(mPageLoadStopper);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent()");
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent()");

        // We don't have tabs yet so just push it into the history and load the new page.
        Uri uri = intent.getData();
        Log.d(TAG, "handleIntent(): intent.getData(): " + uri);
        if  (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearch(intent);
        }
        else if (uri != null) {
            mWebView.loadUrl(intent.getData().toString());
        }

    }


    private void handleSearch() {
        handleSearch("");
    }


    private void handleSearch(Intent intent) {
        handleSearch(intent.getStringExtra(SearchManager.QUERY));
    }


    private void handleSearch(String query) {
        Log.e(TAG, "handleSearch(): query="+query);

        // Make sure he search UI closes after running the search.
        if (mSearchView != null) {
            // calling setIconified(true) doesn't close the SearchView but this does.
            mMenu.findItem(R.id.action_search).collapseActionView();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String mode = prefs.getString(getResources().getString(R.string.pref_searchMode_key), "");
        Log.d(TAG, "handleSearch(): mode is " + mode);

        if (mode.equals(getResources().getString(R.string.pref_searchMode_mode_goToSite))) {
            String template = prefs.getString(
                    getResources().getString(R.string.pref_search_website_key),
                    getResources().getString(R.string.pref_search_website_default));
            try {
                // TODO: handle empty query.
                mWebView.loadUrl(template.replace("%s", URLEncoder.encode(query, "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                Log.e(TAG, "handleSearch(): couldn't URL encode the query.", ex);
            }
        }
        else if (mode.equals(getResources().getString(R.string.pref_searchMode_mode_goToActivity))) {
            // This will launch the systems web search
            Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
            if (!query.isEmpty()) {
                i.putExtra(SearchManager.QUERY, query);
            }
            startActivity(i);
        }
        else {
            Log.e(TAG, "handlsSearch(): unknown search mode");
            Toast.makeText(this, "Mörder: search preferences may be corrupt.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    @Override
    public void onTrimMemory(int level) {

        /*
         * We've gone into the LRU list.
         * Clean up resources that can efficiently and quickly be re-built on user return.
         */
        if (level >= TRIM_MEMORY_BACKGROUND) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_BACKGROUND)");
        }

        /*
         * We're nearing the end of the LRU list.
         * If more memory isn't found soon, we are dead.
         */
        if (level >= TRIM_MEMORY_COMPLETE) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_COMPLETE)");
            // Same level as onLowMemory().
            // I assume we can just do nothing and let the OS
            // call our onLowMemory().
        }

        /*
         * We're around the middle of the LRU list.
         * Freeing up memory will help the world.
         */
        if (level >= TRIM_MEMORY_MODERATE) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_MODERATE)");
        }

        /*
         * We're critical on memory.
         * Please for the love of users, free some memory!
         */
        if (level >= TRIM_MEMORY_RUNNING_CRITICAL) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_CRITICAL)");
        }

        /*
         * We're getting low on memory.
         * Be kind, be polite, trim some fat.
         */
        if (level >= TRIM_MEMORY_RUNNING_LOW) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_LOW)");
        }

        /*
         * We're moderate on memory.
         * Be kind, be polite, trim some fat.
         */
        if (level >= TRIM_MEMORY_RUNNING_MODERATE) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_MODERATE)");
        }

        /*
         * We're no longer showing UI.
         * Clean up UI stuff.
         */
        if (level >= TRIM_MEMORY_UI_HIDDEN) {
            Log.w(TAG, "onTrimMemory(TRIM_MEMORY_UI_HIDDEN)");
        }

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "Mörder onTrimMemory(): " + level, Toast.LENGTH_LONG).show();
        }

        super.onTrimMemory(level);
    }
}

// TODO: handle the searchShowUi setting being changed while the activity is alive.
