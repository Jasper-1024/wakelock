package com.js.wakelock

import android.os.PowerManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

class DWakelock(val level: Int,val typeString: String) {
    var timeout = ObservableBoolean().apply { this.set(false) }
    var countflag = ObservableBoolean().apply { this.set(false) }
    var count = ObservableInt().apply { this.set(0) }
    var tag = ObservableField<String>().apply { this.set(typeString) }

    var wakeLock: PowerManager.WakeLock? = null
}