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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import com.spidey01.morder.android.R;


/**
 * Implementation of ActionBarDrawerToggle used in our navigation drawer.
 */
public class ActionBarDrawerToggle
    extends android.support.v4.app.ActionBarDrawerToggle
{
    private static final String TAG = "ActionBarDrawerToggle";

    private Activity mActivity;
    private DrawerLayout mDrawerLayout;
    private String mTitle;


    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout,
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );
        initialize(activity, drawerLayout);
    }


    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                 boolean animate, int drawerImageRes,
                                 int openDrawerContentDescRes,
                                 int closeDrawerContentDescRes)
    {
        super(activity, drawerLayout, animate, drawerImageRes,
              openDrawerContentDescRes, closeDrawerContentDescRes);
        initialize(activity, drawerLayout);
    }


    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                 int drawerImageRes, int openDrawerContentDescRes,
                                 int closeDrawerContentDescRes)
    {
        super(activity, drawerLayout, drawerImageRes,
              openDrawerContentDescRes, closeDrawerContentDescRes);
        initialize(activity, drawerLayout);
    }


    private final void initialize(Activity activity, DrawerLayout drawerLayout) {
        mActivity = activity;
        mTitle = mActivity.getTitle().toString();
        mDrawerLayout = drawerLayout;
    }


    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        Log.v(TAG, "onDrawerOpened()");

        // TODO: What do we want here?
        mActivity.getActionBar().setTitle(mTitle);
    }


    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        Log.v(TAG, "onDrawerClosed()");

        mActivity.getActionBar().setTitle(mTitle);
    }
}
