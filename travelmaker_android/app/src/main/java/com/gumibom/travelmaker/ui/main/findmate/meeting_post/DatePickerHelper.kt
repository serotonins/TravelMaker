package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.gumibom.travelmaker.constant.NO_DATE
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val TAG = "DatePickerHelper_싸피"
class DatePickerHelper(private val activity : FragmentActivity) {
    /**
     * DatePicker 생성 함수
     */
    fun pickDate(listener: (String) -> Unit) {
        val builder = MaterialDatePicker.Builder.datePicker()

        val today = Calendar.getInstance()
        builder.setSelection(today.timeInMillis)

        val datePicker = builder.build()


        datePicker.show(activity.supportFragmentManager, datePicker.toString())



        datePicker.addOnPositiveButtonClickListener { selectedDate ->

            // 선택된 날짜를 Calendar 객체로 변환
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate

            /*
                현재 날짜보다 과거를 선택하면 선택을 할 수 없다.
             */
            if (!selectedCalendar.after(today) || selectedCalendar.equals(today)) {
                Toast.makeText(activity, NO_DATE, Toast.LENGTH_SHORT).show()
                listener("")
            } else {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val dateString = dateFormatter.format(Date(selectedDate))

                listener(dateString)
            }



        }
    }
}