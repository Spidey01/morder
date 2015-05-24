/*
 * Copyright 2015 Terry Mathew Poulin <BigBoss1964@gmail.com>
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

package com.spidey01.morder.android.shortcuts;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.view.KeyEvent;


/**
 * Helper class for keyboard shortcuts.
 *
 * FIXME: back button broken because not hanelded in activity anymore.
 * FIXME: shortcuts for back/forward no work.
 * FIXME: Should use same IDs as ActionBar buttons.
 */
public class ShortcutManager
{
    private static final String TAG = "ShortcutManager";

    public enum Shortcuts {
        NOT_A_SHORTCUT,

        GO_BACKWARDS_IN_HISTORY,
        GO_FORWARDS_IN_HISTORY,

        INCREASE_ZOOM,
        DECREASE_ZOOM,
        DEFAULT_ZOOM,
    }


    public ShortcutManager() { }



    public Shortcuts isShortcut(KeyEvent event) {
        Log.v(TAG, "isShortcut()");

        // Note: Android calls backspace DEL and delete FORWARD_DEL.

        int keyCode = event.getKeyCode();
        Log.v(TAG, event.toString());

        /*
         * Go backwards in history.
         */
        if (keyCode == KeyEvent.KEYCODE_BACK
        /* XXX We need to figure out how to go oh, you're typing. Don't do that.
            || keyCode == KeyEvent.KEYCODE_DEL
        */
            || (event.isAltPressed() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT))
        {
            return Shortcuts.GO_BACKWARDS_IN_HISTORY;
        }

        /*
         * Go forwards in history.
         */
        /* XXX ditto.
        */
        if ((event.isAltPressed() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            || (event.isShiftPressed() && keyCode == KeyEvent.KEYCODE_DEL))
        {
            return Shortcuts.GO_FORWARDS_IN_HISTORY;
        }

        /*
         * Increase zoom level.
         */
        if (event.isCtrlPressed() && keyCode == KeyEvent.KEYCODE_PLUS)
        {
            return Shortcuts.INCREASE_ZOOM;
        }

        /*
         * Decrease zoom level.
         */
        if (event.isCtrlPressed() && keyCode == KeyEvent.KEYCODE_MINUS)
        {
            return Shortcuts.DECREASE_ZOOM;
        }

        /*
         * Default zoom level.
         */
        if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_NUMPAD_0)
        {
            return Shortcuts.DEFAULT_ZOOM;
        }

    /*
    Things that should just work whenever suitable features/ui is added:

        Zoom Controls:
            Control+0 => default zoom level.
            Control++ => increase zoom level.
            Control+mouse wheel up => increase zoom level.
            Control+- => decrease zoom level.
            Control+mouse wheel down => decrease zoom level.
        Misc shortcuts:
            Alt+Home => Goto home page.
            F11 => toggle full screen.
            Control+R => reload page.
            F5 => reload page.
            Control+Shift+R => reload page (bypass cache).
            Control+F5 => reload page (bypass cache).
     */

        return Shortcuts.NOT_A_SHORTCUT;
    }


    public void doShortcut(WebView webView, Shortcuts shortcut) {
        Log.v(TAG, "doShortcut()");

        Log.v(TAG, "shortcut => " + shortcut.name());
        switch (shortcut) {
            case GO_BACKWARDS_IN_HISTORY:
                if (webView.canGoBack()) {
                    Log.d(TAG, "Go back");
                    webView.goBack();
                }
                break;
            case GO_FORWARDS_IN_HISTORY:
                if (webView.canGoForward()) {
                    Log.d(TAG, "Go forward");
                    webView.goForward();
                }
                break;
            case INCREASE_ZOOM:
                Log.d(TAG, "Increase zoom level");
                webView.zoomIn();
                break;
            case DECREASE_ZOOM:
                Log.d(TAG, "Decrease zoom level");
                webView.zoomOut();
                break;
            case DEFAULT_ZOOM:
                Log.d(TAG, "Default zoom level");

                // if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                Log.w(TAG, "WebView#zoomBy requires API level 21 :'(");
                //     mWebView.zoomBy(0f);
                // } else
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    Log.i(TAG, "Using Pre Lollipop method of setting default zoom.");
                    do {
                        Log.v(TAG, "Calling WebView#zoomOut().");
                    } while(webView.zoomOut());
                }
                break;
            case NOT_A_SHORTCUT:
            default:
                Log.w(TAG, "doShortcut() => NOT A SHORTCUT!");
                break;
        }

        /*
        Things that should just work whenever suitable features/ui is added:

            Zoom Controls:
                Control+0 => default zoom level.
                Control++ => increase zoom level.
                Control+mouse wheel up => increase zoom level.
                Control+- => decrease zoom level.
                Control+mouse wheel down => decrease zoom level.
            Misc shortcuts:
                Alt+Home => Goto home page.
                F11 => toggle full screen.
                Control+R => reload page.
                F5 => reload page.
                Control+Shift+R => reload page (bypass cache).
                Control+F5 => reload page (bypass cache).
         */
    }


}

