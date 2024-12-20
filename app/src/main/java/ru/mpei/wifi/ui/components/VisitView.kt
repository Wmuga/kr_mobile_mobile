package ru.mpei.wifi.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import ru.mpei.wifi.R
import ru.mpei.wifi.databinding.ViewVisitBinding
import ru.mpei.wifi.models.Visit
import java.time.format.DateTimeFormatter

class VisitView(context:Context, attrs:AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context,attrs,defStyleAttr){
    private var textMac: TextView
    private var textPerson: TextView
    private var textDatetime: TextView
    private var binding: ViewVisitBinding

    init{
        val inflater = LayoutInflater.from(context)

        binding = ViewVisitBinding.inflate(inflater)
        textMac = binding.textMac
        textPerson = binding.textPerson
        textDatetime = binding.textDatetime

        addView(binding.root)
    }

    fun setData(data:Visit){
        textMac.text = context.getString(R.string.textMac, data.mac)
        textPerson.text = context.getString(R.string.textPerson, data.person)
        textDatetime.text =
            context.getString(
                R.string.textDatetime,
                data.passTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
    }
}