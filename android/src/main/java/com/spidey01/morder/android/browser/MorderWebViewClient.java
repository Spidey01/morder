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

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/** Custom WebViewClient to load content in our own web view.
 *
 * Created by terry on 11/22/2014.
 */
public class MorderWebViewClient
    extends WebViewClient
{
    private static final String TAG = "MorderWebViewClient";

    private MorderWebObserver mObserver;


    public void setObserver(MorderWebObserver observer) {
        mObserver = observer;
    }


    public MorderWebObserver getObserver() {
        return mObserver;
    }


    /**
     * Give the host application a chance to take over the control when a new
     * url is about to be loaded in the current WebView. If WebViewClient is not
     * provided, by default WebView will ask Activity Manager to choose the
     * proper handler for the url. If WebViewClient is provided, return true
     * means the host application handles the url, while return false means the
     * current WebView handles the url.
     * This method is not called for requests using the POST "method".
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //return super.shouldOverrideUrlLoading(view, url);
        return false;
    }


    /** Notify the host application that a page has started loading.
     *
     *  This method is called once for each main frame load so a page with
     * iframes or framesets will call onPageStarted one time for the main
     * frame. This also means that onPageStarted will not be called when the
     * contents of an embedded frame changes, i.e. clicking a link whose target
     * is an iframe.
     *
     * @param view The WebView that is initiating the callback.
     * @param url The url to be loaded.
     * @param favicon The favicon for this page if it already exists in the database.
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.v(TAG, "onPageStarted(): url="+url);
        mObserver.onPageStarted((MorderWebView)view, url, favicon);
    }


    /** Notify the host application that a page has finished loading.
     *
     * This method is called only for main frame. When onPageFinished() is
     * called, the rendering picture may not be updated yet. To get the
     * notification for the new Picture, use onNewPicture(WebView, Picture).
     *
     * @param view The WebView that is initiating the callback.
     * @param url The url of the page.
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.v(TAG, "onPageFinished(): url="+url);
        mObserver.onPageFinished((MorderWebView)view, url);
    }


}
