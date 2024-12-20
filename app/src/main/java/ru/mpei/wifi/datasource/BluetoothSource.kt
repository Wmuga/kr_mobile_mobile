package ru.mpei.wifi.datasource

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import ru.mpei.wifi.models.Pass
import java.time.LocalDate
import java.time.LocalDateTime

class BluetoothSource(private val ctx:Context): Datasource {
    private lateinit var callback: (Pass) -> Unit
    private val addrs = mutableSetOf<String>()
    private var curDate = LocalDate.now()

    override fun setCallback(callback: (Pass) -> Unit) {
        this.callback = callback
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        ctx.registerReceiver(receiver, filter)
    }

    override fun stop() {
        ctx.unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                     // MAC address
                    Log.d("bluetooth","$device.address : $device.name")
                    callback(Pass(device.address, LocalDateTime.now()))
                }
            }
        }
    }

    private fun tryAdd(addr:String){
        if (curDate != LocalDate.now()){
            addrs.clear()
        }

        if (addr == ""){
            return
        }



    }
}