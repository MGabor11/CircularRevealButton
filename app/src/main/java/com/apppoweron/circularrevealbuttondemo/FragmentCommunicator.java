package com.apppoweron.circularrevealbuttondemo;


import android.support.v4.app.Fragment;

public interface FragmentCommunicator {
    void onNewFragmentSelected(Fragment fragment, boolean replaceIt, boolean needToBackStack) throws NoContainerException;
}
