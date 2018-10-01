package com.apppoweron.circularrevealbuttondemo.common


import android.support.v4.app.Fragment

interface FragmentCommunicator {
    @Throws(NoContainerException::class)
    fun onNewFragmentSelected(fragment: Fragment, replaceIt: Boolean, needToBackStack: Boolean)
}
