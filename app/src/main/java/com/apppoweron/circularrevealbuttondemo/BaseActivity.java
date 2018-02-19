package com.apppoweron.circularrevealbuttondemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity implements FragmentCommunicator {

    private static final int DEFAULT_CONTAINER_ID = -1;

    private BackPressListener mBackPressListener;
    private FragmentTransaction mDaleayedTransaction;
    private boolean mIsSavedInstanceStateCalled;

    private void addFragment(Fragment fragment) throws NoContainerException {
        if (!hasContainerId()) {
            throw new NoContainerException();
        }
        getSupportFragmentManager().beginTransaction().add(getContainerId(), fragment, fragment.getClass().getSimpleName() + "_TAG").commit();
    }

    private void replaceFragment(Fragment fragment, boolean needToBackStack) throws NoContainerException {
        if (!hasContainerId()) {
            throw new NoContainerException();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(getContainerId(), fragment, fragment.getClass().getSimpleName() + "_TAG");
        if (needToBackStack) {
            transaction.addToBackStack(null);
        }
        if (mIsSavedInstanceStateCalled) {
            mDaleayedTransaction = transaction;
            return;
        }
        transaction.commit();

        if (needToBackStack) {
            switchBackButton(true);
        }
    }

    public void loadFragment(Fragment fragment, FragmentLoadType loadType) throws NoContainerException {
        loadFragment(fragment, loadType, false);
    }


    public void loadFragment(Fragment fragment, FragmentLoadType loadType, boolean needToBackStack) throws NoContainerException {
        switch (loadType) {
            case ADD:
                addFragment(fragment);
                break;
            case REPLACE:
                replaceFragment(fragment, needToBackStack);
                break;
        }
    }

    private boolean hasContainerId() {
        return getContainerId() != DEFAULT_CONTAINER_ID;
    }

    protected int getContainerId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            switchBackButton(false);
        }
        if (mBackPressListener != null) {
            mBackPressListener.onBackPressed();
        } else {
            callSuperBackPressed();
        }
    }

    public void setBackPressListener(BackPressListener listener) {
        mBackPressListener = listener;
    }

    public void callSuperBackPressed() {
        super.onBackPressed();
    }

    interface BackPressListener {
        void onBackPressed();
    }

    public enum FragmentLoadType {
        ADD, REPLACE
    }


    /**
     * Remove items from backstack
     */
    protected void clearBackStack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onNewFragmentSelected(Fragment fragment, boolean replaceIt, boolean needToBackStack) throws NoContainerException {
        if (replaceIt) {
            loadFragment(fragment, FragmentLoadType.REPLACE, needToBackStack);
        } else {
            loadFragment(fragment, FragmentLoadType.ADD, needToBackStack);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mIsSavedInstanceStateCalled = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsSavedInstanceStateCalled = false;
        if (mDaleayedTransaction != null) {
            mDaleayedTransaction.commit();
            mDaleayedTransaction = null;
        }
    }

    /**
     * Show backbutton on toolbar
     *
     * @param turnOn show it or not
     */
    private void switchBackButton(boolean turnOn) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(turnOn);
            getSupportActionBar().setHomeButtonEnabled(turnOn);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
