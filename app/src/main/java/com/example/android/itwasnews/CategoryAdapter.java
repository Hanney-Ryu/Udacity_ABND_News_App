package com.example.android.itwasnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CategoryAdapter extends FragmentStatePagerAdapter {
    private static final String LOG_TAG = CategoryAdapter.class.getSimpleName();
    private static final String CATEGORY_GOOGLE = "Google";
    private static final String CATEGORY_APPLE = "Apple";
    private static final String CATEGORY_MICROSOFT = "Microsoft";
    private static final String CATEGORY_FACEBOOK = "Facebook";
    private static final String CATEGORY_ORACLE = "Oracle";
    static final int CATEGORY_GOOGLE_INDEX = 0;
    static final int CATEGORY_APPLE_INDEX = 1;
    static final int CATEGORY_MICROSOFT_INDEX = 2;
    static final int CATEGORY_FACEBOOK_INDEX = 3;
    static final int CATEGORY_ORACLE_INDEX = 4;
    static final String[] categories = {CATEGORY_GOOGLE, CATEGORY_APPLE, CATEGORY_MICROSOFT,
            CATEGORY_FACEBOOK, CATEGORY_ORACLE};

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return NewsFeedFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return categories.length;
    }

}
