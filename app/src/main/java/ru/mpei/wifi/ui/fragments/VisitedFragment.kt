package ru.mpei.wifi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import ru.mpei.wifi.databinding.FragmentVisitedBinding
import ru.mpei.wifi.models.KRModel
import ru.mpei.wifi.models.Visit
import ru.mpei.wifi.ui.components.VisitView


class VisitedFragment : Fragment() {
    companion object{
        const val TODAY_KEY = "TODAY"
    }

    private lateinit var binding: FragmentVisitedBinding
    private lateinit var passes: LinearLayout
    private val viewModel: KRModel by activityViewModels()
    private var isToday: Boolean = false
    private var length: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Сделать привязки
        binding = FragmentVisitedBinding.inflate(inflater, container,false)
        passes = binding.linearPasses

        // Выбрать, какие данные отображать
        isToday = arguments?.getBoolean(TODAY_KEY) ?: false

        viewModel.last.observe(viewLifecycleOwner) {
            // выбор данных для проверки
            val data:List<Visit>
            if (isToday){
                data = viewModel.today()
            }else{
                data = viewModel.all.value ?: ArrayList()
            }

            // Если количество уменьшилось - полное обновление данных
            if (data.size != length+1){
                setValue(data)
                return@observe
            }
            // Просто увеличилось - добавить новый
            length++
            addValue(viewModel.last.value!!)
        }

        return binding.root
    }

    private fun setValue(data:List<Visit>){
        length = data.size
        passes.removeAllViews()
        data.forEach{addValue(it)}
    }

    private fun addValue(data:Visit){
        val newChild = VisitView(requireContext())
        newChild.setData(data)
        newChild.setPadding(0,10,0,0)
        passes.addView(newChild,0)
    }
}