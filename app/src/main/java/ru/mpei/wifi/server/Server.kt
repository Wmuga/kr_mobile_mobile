package ru.mpei.wifi.server

import ru.mpei.wifi.models.Pass
import ru.mpei.wifi.models.Visit

// Интерфейс для взаимодействия с сервером
interface Server {
    // Отправить на сервер информацию о проходе
    fun setPass(data:Pass):Visit
    // Получить текущие проходы
    fun getPasses():List<Visit>
}