package ru.mpei.wifi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.mpei.wifi.R
import ru.mpei.wifi.databinding.FragmentOptionsBinding
import ru.mpei.wifi.datasource.RandomSource
import ru.mpei.wifi.datasource.WifiDatasource
import ru.mpei.wifi.models.Encoder
import ru.mpei.wifi.models.KRModel
import ru.mpei.wifi.server.ApiServer
import ru.mpei.wifi.server.RandomServer

class OptionsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding
    private val viewModel: KRModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBinding.inflate(layoutInflater)

        binding.btnServerRandom.setOnClickListener{viewModel.setServer(RandomServer())}
        binding.btnServerApi.setOnClickListener{
            viewModel.setServer(ApiServer(Encoder(getString(R.string.key))))
        }
        binding.btnSourceRandom.setOnClickListener{viewModel.setDatasource(RandomSource())}
        binding.btnSourceWifi.setOnClickListener{viewModel.setDatasource(WifiDatasource(requireContext()))}


        return binding.root
    }

}