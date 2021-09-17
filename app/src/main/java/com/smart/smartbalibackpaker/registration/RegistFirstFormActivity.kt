package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.dashboard.DashboardFragment
import com.smart.smartbalibackpaker.databinding.ActivityRegistFirstFormBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RegistFirstFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistFirstFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistFirstFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistDatepicker.setOnClickListener {
            showDataRangePicker()
        }

        val items = listOf("Pelabuhan Gilimanuk", "Pelabuhan Benoa", "Bandara Internasional Ngurah Rai")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.listArrivalPlace as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.apply {
            btnNextRegister.setOnClickListener {
                when {
                    etRegistFillDate1.text.toString().trim().isEmpty() -> {
                        etRegistFillDate1.error = "Tanggal kedatangan harus diisi"
                        etRegistFillDate2.error = "Tanggal kepulangan harus diisi"
                    }
                    edtRegistName.text.toString().trim().isEmpty() -> {
                        edtRegistName.error = "Nama backpacker harus diisi"
                    }
                    edtRegistPhone.text.toString().trim().isEmpty() -> {
                        edtRegistPhone.error = "No. Telp backpacker harus diisi"
                    }
                    listArrivalPlace.text.toString().trim().isEmpty() -> {
                        listArrivalPlace.error = "Tempat kedatangan harus dipilih"
                    } else -> startActivity(Intent(this@RegistFirstFormActivity, DashboardFragment::class.java))
                }
            }
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

            val vacationDuration = TimeUnit.DAYS.convert(endDate-startDate, TimeUnit.MILLISECONDS)

            if (startDate != null && endDate != null) {
                binding.etRegistFillDate1.setText("${convertLongToTime(startDate)}")
                binding.etRegistFillDate2.setText("${convertLongToTime(endDate)}")
                binding.btnRegistDatepicker.text = "$vacationDuration Day(s)"
            }


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