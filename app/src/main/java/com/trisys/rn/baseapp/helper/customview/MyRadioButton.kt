package com.trisys.rn.baseapp.helper.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences

class MyRadioButton : AppCompatRadioButton {
    internal var myPreferences: MyPreferences

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        myPreferences = MyPreferences(context)
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        myPreferences = MyPreferences(context)
        init()
    }

    constructor(context: Context) : super(context) {
        myPreferences = MyPreferences(context)
        init()
    }

    private fun init() {
        if (!isInEditMode) {

            val fontType = myPreferences.getInt(Define.CURRENT_FONT,1)

            if (fontType == 0) {

                //set default font
            } else if (fontType == 1) {
                val tf = Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
                typeface = tf
            }
        }
    }

}