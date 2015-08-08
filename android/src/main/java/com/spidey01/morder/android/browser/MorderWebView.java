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

    /** Value used to indicate that page loads should never timeout. */
    public static final int PAGE_TIMEOUT_NEVER = -1;

    /** Default UA of the systems WebView. */
    private static String SYSTEM_WEBVIEW_UA;

    private int mPageTimeout;

    private String mHomePage;

    private MorderWebViewClient mWebViewClient = new MorderWebViewClient();

    private MorderWebObserver mObserver;


    public MorderWebView(Context context) {
        super(context);
        assertPreferences();
        setWebViewClient(mWebViewClient);
        initStaticFields();
    }


    public MorderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        assertPreferences();
        setWebViewClient(mWebViewClient);
        initStaticFields();
    }


    public MorderWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        assertPreferences();
        setWebViewClient(mWebViewClient);
        initStaticFields();
    }


    private final void initStaticFields() {
        if (SYSTEM_WEBVIEW_UA == null) {
            SYSTEM_WEBVIEW_UA = getSettings().getUserAgentString();
        }
    }


    public void setObserver(MorderWebObserver observer) {
        mObserver = observer;
        mWebViewClient.setObserver(mObserver);
    }


    public MorderWebObserver getObserver() {
        return mObserver;
    }


    public int getPageTimeout() {
        return mPageTimeout;
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
    private static final String PREF_USER_AGENT_MODE = "pref_userAgentMode_key";
    private static final String PREF_PAGE_TIMEOUT = "pref_pageTimeout_key";
    private static final String PREF_SHOW_ZOOM_CONTROLS = "pref_showZoomControls_key";
    private static final String PREF_SEARCH = "pref_search_key";


    private final void assertPreferences() {
        String p;
        final String msg = TAG+": out of sync with resources.";
        final Resources res = getResources();

        p = res.getString(R.string.pref_newTabPage_key);
        assert PREF_NEW_TAB_PAGE.equals(p) : msg;

        p = res.getString(R.string.pref_javascript_key);
        assert PREF_ENABLE_JAVASCRIPT.equals(p) : msg;

        p = res.getString(R.string.pref_pageTimeout_key);
        assert PREF_PAGE_TIMEOUT.equals(p) : msg;

        p = res.getString(R.string.pref_showZoomControls_key);
        assert PREF_SHOW_ZOOM_CONTROLS.equals(p) : msg;
    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged(): key=" + key);

        switch (key) {
            case PREF_NEW_TAB_PAGE:
                mHomePage = sharedPreferences.getString(key, getResources().getString(R.string.pref_newTabPage_default));
                break;
            case PREF_ENABLE_JAVASCRIPT: {
                if (sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_javascript_default))) {
                    enableJavaScript();
                } else {
                    disableJavaScript();
                }
                break;
            }
            case PREF_USER_AGENT_MODE: {
                Log.d(TAG, "Do we want dynamic UA updating?");
                getSettings().setUserAgentString(parseUserAgentMode(
                        sharedPreferences.getString(PREF_USER_AGENT_MODE,
                                getResources().getString(R.string.pref_userAgentMode_default)
                        )
                ));
                break;
            }
            case PREF_PAGE_TIMEOUT: {
                try {
                    mPageTimeout = parsePageTimeout(
                            sharedPreferences.getString(
                                    key,
                                    getResources().getString(R.string.pref_pageTimeout_default)
                            )
                    );
                } catch (NumberFormatException ex) {
                    Log.e(TAG, "onSharedPreferenceChanged(): Couldn't update mPageTimeout:", ex);
                }
                break;
            }
            case PREF_SHOW_ZOOM_CONTROLS: {
                Log.d(TAG, "Zoom controls toggled.");
                getSettings().setSupportZoom(true);
                getSettings().setDisplayZoomControls(
                        sharedPreferences.getBoolean(
                                key,
                                getResources().getBoolean(R.bool.pref_showZoomControls_default)
                        )
                );
                // Enables pinch to zoom.
                // And the zoom buttons if the above is true.
                getSettings().setBuiltInZoomControls(true);
                break;
            }
            case PREF_SEARCH: {
                Log.d(TAG, "Search setting updated");
                Log.d(TAG, "New search template is" + sharedPreferences.getString(PREF_SEARCH, ""));
            }
        }
    }


    public void setup(SharedPreferences prefs) {
        Log.d(TAG, "setup()");
        // Do we need to unregister this view later?
        prefs.registerOnSharedPreferenceChangeListener(this);


        onSharedPreferenceChanged(prefs, PREF_NEW_TAB_PAGE );
        Log.d(TAG, "default home page is: " + mHomePage);

        onSharedPreferenceChanged(prefs, PREF_USER_AGENT_MODE);
        Log.i(TAG, "setup(): user agent => " + getSettings().getUserAgentString());

        onSharedPreferenceChanged(prefs, PREF_ENABLE_JAVASCRIPT);

        onSharedPreferenceChanged(prefs, PREF_PAGE_TIMEOUT);
        Log.d(TAG, "setup(): pageTimeout=" + mPageTimeout);

        onSharedPreferenceChanged(prefs, PREF_SHOW_ZOOM_CONTROLS);
    }


    private int parsePageTimeout(String preference_value)
        throws NumberFormatException
    {
        if (preference_value.equals("Never")) {
            return PAGE_TIMEOUT_NEVER;
        } else {
            return Integer.valueOf(preference_value.substring(0, preference_value.indexOf(" ")));
        }
    }


    private String parseUserAgentMode(String mode) {
        assert SYSTEM_WEBVIEW_UA != null;

        final String morderElement = " Morder/0";
        switch (mode) {
            case "Smart":
                /*
                 * WebView already has the desired behaviour of stuff in "Mobile"
                 * on phone and leave it be otherwise. So just update your UA.
                 */
                return SYSTEM_WEBVIEW_UA + morderElement;
            case "Phone":
                if (SYSTEM_WEBVIEW_UA.matches("Mobile")) {
                    return SYSTEM_WEBVIEW_UA + morderElement;
                } else {
                    /* Technically we should find "Chrome/ver Safari/ver"
                     * and insert Mobile in between.
                     *
                     * This should be sufficent though.
                     */
                    return SYSTEM_WEBVIEW_UA + " Mobile" + morderElement;
                }
            case "Tablet":
                return SYSTEM_WEBVIEW_UA.replace(" Mobile ", " ") + morderElement;
            case "Desktop":
                StringBuilder buffer = new StringBuilder(SYSTEM_WEBVIEW_UA.length());
                boolean inParen = false;
                for (String word : SYSTEM_WEBVIEW_UA.split(" ")) {
                    if (word.startsWith("(Linux;")) {
                        inParen = true;
                        buffer.append("(X11; Linux x86_64) ");
                    } else if (inParen) {
                        if (word.contains(")")) {
                            inParen = false;
                        }
                    } else {
                        buffer.append(word);
                        buffer.append(" ");
                    }
                }
                return buffer.toString().replace(" Mobile ", " ").trim()+ morderElement;
            default:
                Log.e(TAG, "setup(): Unknown user agent mode " + mode);
                throw new IllegalArgumentException("Unknown user agent mode " + mode);
        }
    }


}
