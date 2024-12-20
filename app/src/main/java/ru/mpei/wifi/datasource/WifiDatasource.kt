package ru.mpei.wifi.datasource

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import ru.mpei.wifi.models.Pass
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask


class WifiDatasource(private val ctx: Context) : Datasource {
    private lateinit var callback: (Pass) -> Unit

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timer: Timer
    private lateinit var thr:Thread

    override fun setCallback(callback: (Pass) -> Unit) {
        this.callback = callback
        thr = Thread{
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    task()
                }
            },0,5*60*1000)
        }
        thr.start()
    }

    override fun stop() {
        thr.interrupt()
        thr.join()
    }

    private fun task(){
        val wifiManager = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }
        val subnet = ipAddress and 0xFFFFFF00.toInt() // Get the subnet mask

        try {
            (1..254).mapNotNull { i ->
                val ipByteArray = BigInteger.valueOf((subnet or i).toLong()).toByteArray()
                Log.d("ip","$ipByteArray")
                val host = InetAddress.getByAddress(ipByteArray)
                if (host.isReachable(100)) {
                    callback(Pass(host.hostAddress?:"", LocalDateTime.now()))
                }
            }
        } catch (e: Exception){
            print(ctx,e.toString())
            e.printStackTrace()
        }

    }

    private fun print(ctx:Context,str:String){
        handler.post { Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show() }
    }


}