package com.apppoweron.circularrevealbutton.util

import com.apppoweron.circularrevealbutton.BuildConfig
import com.apppoweron.circularrevealbutton.Config

object DebugUtil {

    inline fun debug(code: () -> Unit) {
        if (BuildConfig.BUILD_TYPE == "debug" && Config.isDebugMessagesEnabled){
            code()
        }
    }

}
