package ru.mpei.wifi.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.mpei.wifi.databinding.ActivityMainBinding
import ru.mpei.wifi.datasource.Datasource
import ru.mpei.wifi.datasource.RandomSource
import ru.mpei.wifi.datasource.WifiDatasource
import ru.mpei.wifi.models.Pass
import ru.mpei.wifi.models.KRModel
import ru.mpei.wifi.server.RandomServer
import ru.mpei.wifi.server.Server
import ru.mpei.wifi.ui.fragments.OptionsFragment
import ru.mpei.wifi.ui.fragments.VisitedFragment


/*
 TODO:
 * Options menu
 * Datasource and Server to options menu
 * Horizontal View
*/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: KRModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.server.observe(this){
            Log.d("observer","server")
            if (!viewModel.loaded()){
                Thread {
                    it.getPasses().forEach { viewModel.add(it) }
                }.start()
                viewModel.setLoaded()
            }
        }

        viewModel.datasource.observe(this){ it ->
            Log.d("observer","datasource")
            if (!viewModel.hasCallback()){
                it.setCallback { newPass(it) }
            }
            viewModel.setHasCallback()
        }

        binding.btnAll.setOnClickListener{setVisits(false)}
        binding.btnToday.setOnClickListener{setVisits(true)}
        binding.btnOptions.setOnClickListener{setOptions()}

        if (!viewModel.hasServer()){
            viewModel.setServer(RandomServer())
        }

        if (!viewModel.hasDatasource()){
            viewModel.setDatasource(RandomSource())
        }

        setVisits(false)
    }

    private fun newPass(pass:Pass){
        try{
            val visit = viewModel.server.value?.setPass(pass)
            if (visit != null){
                viewModel.add(visit)
            }
        }catch (e: Exception){
            Log.e("newpass",e.toString())
        }
    }

    private fun setVisits(today:Boolean){
        val bundle = Bundle()
        bundle.putBoolean(VisitedFragment.TODAY_KEY,today)
        val visitFragment = VisitedFragment()
        visitFragment.arguments = bundle

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(binding.fragmentMain.id,visitFragment)
        trans.commit()
    }

    private fun setOptions(){
        val options = OptionsFragment()
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(binding.fragmentMain.id,options)
        trans.commit()
    }
}