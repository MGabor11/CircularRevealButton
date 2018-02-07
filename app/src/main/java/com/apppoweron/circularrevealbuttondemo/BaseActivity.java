package com.apppoweron.circularrevealbuttondemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements FragmentCommunicator {

    private static final int DEFAULT_CONTAINER_ID = -1;

    private BackPressListener mBackPressListener;

    private void addFragment(Fragment fragment) throws NoContainerException {
        if (!hasContainerId()) {
            throw new NoContainerException();
        }
        getSupportFragmentManager().beginTransaction().add(getContainerId(), fragment, fragment.getClass().getSimpleName() + "_TAG").commit();
    }

    private void repleaceFragment(Fragment fragment, boolean needToBackStack) throws NoContainerException {
        if (!hasContainerId()) {
            throw new NoContainerException();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(getContainerId(), fragment, fragment.getClass().getSimpleName() + "_TAG");
        if (needToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
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
                repleaceFragment(fragment, needToBackStack);
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
        if(replaceIt){
            loadFragment(fragment, FragmentLoadType.REPLACE,needToBackStack);
        }else{
            loadFragment(fragment, FragmentLoadType.ADD,needToBackStack);
        }

    }


}
