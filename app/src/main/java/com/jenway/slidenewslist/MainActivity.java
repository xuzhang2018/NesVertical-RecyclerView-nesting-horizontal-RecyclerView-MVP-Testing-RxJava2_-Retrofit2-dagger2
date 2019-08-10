package com.jenway.slidenewslist;

import android.os.Bundle;

import com.jenway.slidenewslist.base.BaseActivity;
import com.jenway.slidenewslist.base.BaseFragment;
import com.jenway.slidenewslist.nestedNews.NestedNewsListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return NestedNewsListFragment.newInstance("", "");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
