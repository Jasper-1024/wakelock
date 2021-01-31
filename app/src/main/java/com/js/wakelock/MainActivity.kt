package com.js.wakelock

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import androidx.databinding.DataBindingUtil
import com.js.wakelock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val dWakelock = DWakelock(PowerManager.PARTIAL_WAKE_LOCK, "PARTIAL_WAKE_LOCK")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.dWakelock = dWakelock
        binding.activity = this
    }

    fun acquire(dWakelock: DWakelock) {

        val powerManger = getSystemService(Context.POWER_SERVICE) as PowerManager

        // 获取 wakelock
        val wakelock = dWakelock.wakeLock ?: powerManger
            .run { newWakeLock(dWakelock.level/*type*/, dWakelock.tag.get()/*tag*/) }
            .apply {
                //计数锁?
                this.setReferenceCounted(dWakelock.countflag.get())
                dWakelock.count.set(0)
            }

        when (dWakelock.timeout.get()) {
            //计时锁
            true -> wakelock.acquire(1 * 60 * 1000L /*1 minutes*/)
            //非计时锁
            false -> wakelock.apply {
                this.acquire()
                dWakelock.count.let {//cont
                    if (dWakelock.countflag.get()) it.set(it.get() + 1)
                    else it.set(0)
                }
            }
        }

        dWakelock.wakeLock = wakelock

        LogUtil.d("test", "cont: ${dWakelock.count.get()}")
    }

    fun release(dWakelock: DWakelock) {
        dWakelock.wakeLock?.let {
            it.release()
            dWakelock.count.let { cont ->
                if (dWakelock.countflag.get())
                    cont.set(if (cont.get() >= 1) cont.get() - 1 else 0)
                else cont.set(0)
            }
        }
        if (!(dWakelock.countflag.get() && dWakelock.count.get() > 0)) dWakelock.wakeLock = null
        LogUtil.d("test", "cont: ${dWakelock.count.get()}")
    }
}