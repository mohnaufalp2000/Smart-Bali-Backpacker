package com.smart.smartbalibackpaker.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.smart.smartbalibackpaker.databinding.ActivityRegistFirstFormBinding
import java.text.SimpleDateFormat
import java.util.*

class RegistFirstFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistFirstFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistFirstFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistDatepicker.setOnClickListener {
            showDataRangePicker()
        }
    }


    private fun showDataRangePicker() {

        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            supportFragmentManager,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->

            val startDate = dateSelected.first
            val endDate = dateSelected.second

//            if (startDate != null && endDate != null) {
//                binding.etRegistDate1.editText?.doOnTextChanged { date,_,_,_ ->  it = startDate}
////                =
////                    "Reserved\nStartDate: ${convertLongToTime(startDate)}\n" +
////                            "EndDate: ${convertLongToTime(endDate)}"
//            }
        }

    }


    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault())
        return format.format(date)
    }
}