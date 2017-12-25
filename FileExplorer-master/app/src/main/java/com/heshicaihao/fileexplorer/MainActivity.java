/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.heshicaihao.fileexplorer;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ActionMode;

import com.heshicaihao.fileexplorer.common.SharedData;
import com.heshicaihao.fileexplorer.fragment.FileCategoryFragment;
import com.heshicaihao.fileexplorer.fragment.FileViewFragment;
import com.heshicaihao.fileexplorer.fragment.ServerControlFragment;
import com.heshicaihao.fileexplorer.utils.LogUtils;
import com.heshicaihao.fileexplorer.utils.PermissionUtils;
import com.heshicaihao.fileexplorer.utils.Util;

import java.util.ArrayList;

/**
 * @author heshicaihao
 */
public class MainActivity extends Activity {
    private static final String INSTANCESTATE_TAB = "tab";
    private static final int DEFAULT_OFFSCREEN_PAGES = 2;
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    ActionMode mActionMode;
    private PermissionUtils mPermissionUtils;//权限控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate前");
        setContentView(R.layout.activity_mian);
        mPermissionUtils = new PermissionUtils(MainActivity.this);//权限控制
        SharedData.setParam(MainActivity.this, "isPerm", false);//初始化权限是否全部授权判断
        //判断使用APP的android版本是否是android6.0如果是android6.0则需要进行权限判断后再检查更新，是android6.0以下的版本则直接检查更新
        int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel < 23) {
        } else {
            mPermissionUtils.insertDummyContactWrapper(true);
        }
        initView();
        LogUtils.d("onCreate后");
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.tab_category),
                FileCategoryFragment.class, null);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.tab_sd),
                FileViewFragment.class, null);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.tab_remote),
                ServerControlFragment.class, null);
        bar.setSelectedNavigationItem(PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(INSTANCESTATE_TAB, Util.CATEGORY_TAB_INDEX));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause前");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt(INSTANCESTATE_TAB, getActionBar().getSelectedNavigationIndex());
        editor.commit();
        LogUtils.d("onPause后");
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        //权限适配监听
        mPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.d("onRequestPermissionsResult");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d("onRestart前");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt(INSTANCESTATE_TAB, getActionBar().getSelectedNavigationIndex());
        if ((Boolean) SharedData.getParam(MainActivity.this, "isPerm", false)) {//在权限回调后检查
            mPermissionUtils.insertDummyContactWrapper(true);
        }
        LogUtils.d("onRestart后");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (getActionBar().getSelectedNavigationIndex() == Util.CATEGORY_TAB_INDEX) {
            FileCategoryFragment categoryFragement = (FileCategoryFragment) mTabsAdapter.getItem(Util.CATEGORY_TAB_INDEX);
            if (categoryFragement.isHomePage()) {
                reInstantiateCategoryTab();
            } else {
                categoryFragement.setConfigurationChanged(true);
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    public void reInstantiateCategoryTab() {
        mTabsAdapter.destroyItem(mViewPager, Util.CATEGORY_TAB_INDEX,
                mTabsAdapter.getItem(Util.CATEGORY_TAB_INDEX));
        mTabsAdapter.instantiateItem(mViewPager, Util.CATEGORY_TAB_INDEX);
    }

    @Override
    public void onBackPressed() {
        IBackPressedListener backPressedListener = (IBackPressedListener) mTabsAdapter
                .getItem(mViewPager.getCurrentItem());
        if (!backPressedListener.onBack()) {
            super.onBackPressed();
        }
    }

    public interface IBackPressedListener {
        /**
         * 处理back事件。
         *
         * @return True: 表示已经处理; False: 没有处理，让基类处理。
         */
        boolean onBack();
    }

    public void setActionMode(ActionMode actionMode) {
        mActionMode = actionMode;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public Fragment getFragment(int tabIndex) {
        return mTabsAdapter.getItem(tabIndex);
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(Activity activity, ViewPager pager) {
            super(activity.getFragmentManager());
            mContext = activity;
            mActionBar = activity.getActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            if (info.fragment == null) {
                info.fragment = Fragment.instantiate(mContext, info.clss.getName(), info.args);
            }
            return info.fragment;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i = 0; i < mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
            if (!tab.getText().equals(mContext.getString(R.string.tab_sd))) {
                ActionMode actionMode = ((MainActivity) mContext).getActionMode();
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }
}
