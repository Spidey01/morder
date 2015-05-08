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

import com.spidey01.morder.android.browser.MorderWebView;

import  android.view.KeyEvent.Callback;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

/**
 * Created by terry on 5/8/15.
 */
public class ShortcutManager
    implements KeyEvent.Callback
{
    private MorderWebView mWebView;

    public ShortcutManager(MorderWebView view) {
        mWebView = view;
    }


    /**
     * Call this when a key was pressed down and needs to be handled by the ShortcutManager.
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

        return false;
    }


    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return false;
    }


    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return false;
    }


    @Override
    public boolean onKeyMultiple(int i, int i1, KeyEvent keyEvent) {
        return false;
    }

}
