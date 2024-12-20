package ru.mpei.wifi.models

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mpei.wifi.datasource.Datasource
import ru.mpei.wifi.server.Server
import java.time.LocalDate
import java.time.LocalDateTime

// Информация о проходе от источника данных
data class Pass(val mac:String, val datatime: LocalDateTime)

// Информация о проходе после обработки сервером
data class Visit(val mac: String, val person:String, val passTime: LocalDateTime)

class KRModel : ViewModel() {
    private val _data = ArrayList<Visit>()
    private val _liveData = MutableLiveData<ArrayList<Visit>>()
    private val _last = MutableLiveData<Visit>()

    private var _loaded = false
    private var _callback = false

    private val addrs = mutableSetOf<String>()
    private var curDate = LocalDate.now()

    private var _ds = MutableLiveData<Datasource>()
    private var _serv = MutableLiveData<Server>()

    val last: LiveData<Visit> get() = _last
    val all: LiveData<ArrayList<Visit>> get() = _liveData
    val datasource: LiveData<Datasource> get() = _ds
    val server: LiveData<Server> get() = _serv

    fun today(): List<Visit> {
        return _data.filter{it.passTime.toLocalDate().equals(LocalDate.now())}
    }

    fun add(data:Visit){
        if(curDate != LocalDate.now()){
            addrs.clear()
        }

        if (!addrs.add(data.mac)){
            return
        }

        _data.add(data)
        _liveData.postValue(_data)
        _last.postValue(data)
    }

    fun loaded():Boolean{
        return _loaded
    }

    fun setLoaded(){
        _loaded = true
    }

    fun hasCallback():Boolean{
        return _callback
    }

    fun setHasCallback(){
        _callback = true
    }

    fun hasDatasource():Boolean{
        return _ds.value != null
    }

    fun hasServer():Boolean{
        return _serv.value != null
    }

    fun setDatasource(d:Datasource){
        _ds.value?.stop()
        _callback = false
        _ds.postValue(d)
    }

    fun setServer(s:Server){
        _loaded = false
        _serv.postValue(s)
    }
}