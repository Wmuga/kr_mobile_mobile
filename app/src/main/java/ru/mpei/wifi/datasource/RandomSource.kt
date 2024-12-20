package ru.mpei.wifi.datasource

import ru.mpei.wifi.models.Pass
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask

// RandomSource - источник даных, заполныющий случайными значениями
class RandomSource :Datasource{
    private var _callback: ((Pass) -> Unit)? = null
    private var counter: Int = 0
    private val timer: Timer = Timer()

    init {
        timer.schedule(object: TimerTask() {
            override fun run(){
                var date = LocalDateTime.now()
                if (counter % 2 == 0){
                    date = date.minusDays(1)
                }
                counter++
                _callback?.invoke(Pass(java.util.UUID.randomUUID().toString(),date))
            }
        },0,5000)
    }

    override fun setCallback(callback: (Pass) -> Unit) {
        _callback = callback

        callback(Pass("amogus",LocalDateTime.now()))
    }

    override fun stop() {
        timer.cancel()
    }
}