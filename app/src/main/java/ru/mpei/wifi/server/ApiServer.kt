package ru.mpei.wifi.server

import ru.mpei.wifi.models.Encoder
import ru.mpei.wifi.models.Pass
import ru.mpei.wifi.models.Visit
import java.io.Serializable
import java.time.LocalDateTime
import kotlinx.serialization.json.Json


class ApiServer(enc: Encoder) : Server{
    @kotlinx.serialization.Serializable
    private data class Person(val mac:String, val name:String, val position:String, val pass_time:String) : Serializable
    @kotlinx.serialization.Serializable
    private data class AddResponse(val data:Person, val count:Int, val status:Int, val message:String) :Serializable
    @kotlinx.serialization.Serializable
    private data class ListResponse(val data:Array<Person>, val count:Int, val status:Int, val message:String) :Serializable

    private val encoder = enc

    private val baseURL = "http://127.0.0.1"
    private val urlAdd = "$baseURL/api/add"
    private val urlList = "$baseURL/api/list/all"

    override fun setPass(data: Pass): Visit {
        val req = mapOf("mac" to data.mac, "token" to encoder.getToken(mapOf("mac" to data.mac)))

        val resp = khttp.post(urlAdd,json=req)
        val obj = Json.decodeFromString<AddResponse>(resp.text)

        // Нет данные про того, кто этот человек - вернуть "случайную строку"
        return Visit(
            obj.data.mac,
            obj.data.name,
            data.datatime,
        )
    }

    override fun getPasses(): List<Visit> {
        val req = mapOf("offset" to 0, "token" to encoder.getToken(mapOf("offset" to 0)))

        val resp = khttp.post(urlList,json=req)
        val obj = Json.decodeFromString<ListResponse>(resp.text)

        return obj.data.map{
            Visit(
                it.mac,
                it.name,
                LocalDateTime.now(),
            )
        }
    }
}