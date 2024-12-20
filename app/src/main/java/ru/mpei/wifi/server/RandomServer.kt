package ru.mpei.wifi.server

import ru.mpei.wifi.models.Pass
import ru.mpei.wifi.models.Visit
import java.time.LocalDateTime

// Мок сервера для тестирования
class RandomServer: Server{

    private val names = arrayOf("Vitalia Zuleima","Arnfinnr Cam","John Abisai","Franciscus Christy","Jaakob Charlie","Mikael Lamprecht","Geert Lumír","Aþanagild Mehitabel","Arran Tanis","John Paul Meginhard","Naftali Elke","Omri Kyriaki","Neoptolemus Izolda","Vadimu Hajni","Henricus Shaw")

    override fun setPass(data: Pass): Visit {
        // Нет данные про того, кто этот человек - вернуть "случайную строку"
        return Visit(
            data.mac,
            names.random(),
            data.datatime,
        )
    }

    override fun getPasses(): List<Visit> {
        // Создать 6 случайных записей
        val count = 6
        return (1..count).map{Visit(
            java.util.UUID.randomUUID().toString(),
            names.random(),
            LocalDateTime.now(),
        )}
    }

}