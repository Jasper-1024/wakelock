package com.js.wakelock

import android.widget.EditText
import androidx.databinding.InverseMethod

object Converter {
    @InverseMethod("stringToCount")
    @JvmStatic
    fun countToString(value: Int): String {
        // Converts long to String.
        return value.toString()
    }

    @JvmStatic
    fun stringToCount(value: String): Int {
        return value.toInt()
    }
}