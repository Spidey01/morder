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
import com.spidey01.morder.android.settings.SettingsActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;



/**
 * Created by terry on 11/22/2014.
 */
public class DrawerItemClickListener
    implements ListView.OnItemClickListener
{
    private static final String TAG = "DrawerItemClickListener";

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "You clicked position " + position + " with id " + id);
        Context context = parent.getContext();

        final String item = ((TextView)view).getText().toString();
        Log.d(TAG, "onItemClick(): " + item);

        Intent intent = null;
        switch(item) {
            case "Settings":
                intent = new Intent(context, SettingsActivity.class);
                break;
            case "Quit":
                ((Activity)context).finish();
                return;
            default:
                Log.e(TAG, "onItemClick(): don't know " + item);
                return;
        }

        context.startActivity(intent);
    }
}
