package com.apppoweron.circularrevealbuttondemo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            loadDefaultFragment();
        } catch (NoContainerException e) {
            e.printStackTrace();
        }
    }

    protected void loadDefaultFragment() throws NoContainerException {
        loadFragment(MainFragment.newInstance(), BaseActivity.FragmentLoadType.ADD);
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }
}
