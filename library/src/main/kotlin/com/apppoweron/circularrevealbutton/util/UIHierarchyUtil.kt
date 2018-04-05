package com.apppoweron.circularrevealbutton.util

import android.util.Log
import android.view.View

object UIHierarchyUtil {

    private val TAG = "UIHierarchyUtil"

    fun getViewByIdInParents(searchedViewId: Int, startView: View): View? {
        var currentView: View? = startView
        while (currentView != null) {
            if (currentView.id == searchedViewId) {
                return currentView
            }

            try {
                currentView = currentView.parent as View
            } catch (ex: ClassCastException) {
                //Handling case of last View in hierarchy
                Log.e(TAG, "This was the last View in hierarchy.")
                return null
            }

        }
        return null
    }
}
