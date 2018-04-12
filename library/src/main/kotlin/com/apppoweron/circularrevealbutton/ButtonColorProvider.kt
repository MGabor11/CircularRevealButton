package com.apppoweron.circularrevealbutton

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.widget.Button

/**
 * Collection of button regarded color methods
 */
internal class ButtonColorProvider(private val button: Button) {

    private var originalTextColorHex: String? = null

    private var originalColorId: Int? = null

    private//Cutting off the first two characters, because it is ARGB
    val buttonTextColorHex: String
        get() {
            if (TextUtils.isEmpty(originalTextColorHex)) {
                originalTextColorHex = Integer.toHexString(buttonTextColor).substring(2)
            }
            return originalTextColorHex.toString()
        }

    val buttonTextColor: Int
        get() = button.textColors.defaultColor

    val buttonOriginalColorId: Int?
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        get() {
            if (originalColorId == null) {
                if (button.background is ColorDrawable) {
                    val buttonColor = button.background as ColorDrawable
                    originalColorId = buttonColor.color
                } else if (button.background is RippleDrawable) {
                    val buttonColor = button.background as RippleDrawable
                    val state = buttonColor.constantState
                    try {
                        val colorField = state!!.javaClass.getDeclaredField("mColor")
                        colorField.isAccessible = true
                        val colorStateList = colorField.get(state) as ColorStateList
                        originalColorId = colorStateList.defaultColor
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }

                }
            }
            return originalColorId
        }

    fun translateAnimatedValueIntoFadingColor(valueAnimator: ValueAnimator): Int {
        return Color.parseColor(getFirstAlphaTag(valueAnimator.animatedValue as Int) + buttonTextColorHex)
    }

    private fun getFirstAlphaTag(value: Int): String {
        var result = Integer.toHexString(value)
        if (result.length < 2) {
            result = "0$result"
        }
        return "#$result"
    }

}
