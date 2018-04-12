package com.apppoweron.circularrevealbuttondemo

import android.content.Context
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    protected lateinit var mFragmentCommunicator: FragmentCommunicator

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mFragmentCommunicator = context as FragmentCommunicator

        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement all interface")
        }

    }

}
