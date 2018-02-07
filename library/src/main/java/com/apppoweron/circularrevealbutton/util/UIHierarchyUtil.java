package com.apppoweron.circularrevealbutton.util;

import android.util.Log;
import android.view.View;

public class UIHierarchyUtil {

    private static final String TAG = "UIHierarchyUtil";

    public static View getViewByIdInParents(int searchedViewId, View startView) {
        View currentView = startView;
        while (currentView != null) {
            if (currentView.getId() == searchedViewId) {
                return currentView;
            }

            try {
                currentView = (View) currentView.getParent();
            } catch (ClassCastException ex) {
                //Handling case of last View in hierarchy
                Log.e(TAG, "This was the last View in hierarchy.");
                return null;
            }
        }
        return null;
    }
}
