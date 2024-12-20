package ru.mpei.wifi.datasource

import ru.mpei.wifi.models.Pass

// Интерфейс над источниками данных, поставляющих данные через callbackи
interface Datasource {
   fun setCallback(callback:(Pass)->Unit)
   fun stop()
}