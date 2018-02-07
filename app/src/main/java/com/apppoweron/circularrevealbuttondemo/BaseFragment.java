package com.apppoweron.circularrevealbuttondemo;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected FragmentCommunicator mFragmentCommunicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mFragmentCommunicator = (FragmentCommunicator) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement all interface");
        }
    }

}
