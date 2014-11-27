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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import com.spidey01.morder.android.R;


/**
 * Created by terry on 11/22/2014.
 */
public class MorderWebView
    extends WebView
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "MorderWebView";

    private String mHomePage;

    private MorderWebViewClient mWebViewClient = new MorderWebViewClient();


    public MorderWebView(Context context) {
        super(context);
        assertPreferences();
        setWebViewClient(mWebViewClient);
    }


    public MorderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        assertPreferences();
        setWebViewClient(mWebViewClient);
    }


    public MorderWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        assertPreferences();
        setWebViewClient(mWebViewClient);
    }


    public String getHomePage() {
        return mHomePage;
    }


    public void enableJavaScript() {
        Log.i(TAG, "enableJavaScript()");
        getSettings().setJavaScriptEnabled(true);
    }


    public void disableJavaScript() {
        Log.i(TAG, "disableJavaScript()");
        getSettings().setJavaScriptEnabled(false);
    }

    /*
     * This code needs major clean up.
     */

    private static final String PREF_NEW_TAB_PAGE = "pref_newTabPage_key";
    private static final String PREF_ENABLE_JAVASCRIPT = "pref_javascript_key";


    private final void assertPreferences() {
        String p;
        final String msg = TAG+": out of sync with resources.";
        final Resources res = getResources();

        p = res.getString(R.string.pref_newTabPage_key);
        assert PREF_NEW_TAB_PAGE.equals(p) : msg;

        p = res.getString(R.string.pref_javascript_key);
        assert PREF_ENABLE_JAVASCRIPT.equals(p) : msg;
    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged(): key=" + key);

        if (key.equals(PREF_NEW_TAB_PAGE)) {
            mHomePage = sharedPreferences.getString(key, getResources().getString(R.string.pref_newTabPage_default));
        }
        else if (key.equals(PREF_ENABLE_JAVASCRIPT)) {
            if (sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_javascript_default))) {
                enableJavaScript();
            } else {
                disableJavaScript();
            }
        }
    }


    public void setup(SharedPreferences prefs) {
        Log.d(TAG, "setup()");

        Resources res = getResources();

        final String defaultHomePage = res.getString(R.string.pref_newTabPage_key);
        mHomePage = prefs.getString(PREF_NEW_TAB_PAGE, defaultHomePage);
        Log.d(TAG, "default home page is: " + mHomePage);

        final boolean defaultJS = res.getBoolean(R.bool.pref_javascript_default);
        if (prefs.getBoolean(PREF_ENABLE_JAVASCRIPT, defaultJS)) {
            enableJavaScript();
        } else {
            disableJavaScript();
        }
    }


}