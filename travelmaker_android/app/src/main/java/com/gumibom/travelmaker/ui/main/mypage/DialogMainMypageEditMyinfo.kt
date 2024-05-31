package com.gumibom.travelmaker.ui.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gumibom.travelmaker.databinding.DialogMainMypageEditMyinfoBinding
import com.gumibom.travelmaker.databinding.FragmentPamphletBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DialogMainMypageEditMyinfo:Fragment() {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private var _binding:DialogMainMypageEditMyinfoBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMainMypageEditMyinfoBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1. 회원탈퇴
        deleteAccount()
        // 2. 생년월일
        editBirthday()
        // 3. 카테고리
        editCategory()
        // 4. 돌아가기
        backToMypage()
    }
    /*
    회원탈퇴
    */
    private fun deleteAccount(){
        // 회원탈퇴 하시겠습니까?
        // 회원탈퇴 버튼 클릭
        // 회원탈퇴 완료
    }

    /*
    생년월일
     */
    private fun editBirthday(){
        val datepicker = binding.dpMyinfoBirthday
        val maxCalendar = Calendar.getInstance()
        val minCalendar = Calendar.getInstance()

        datepicker.init(1994, 10, 10, DatePicker.OnDateChangedListener {
                view, year, monthOfYear, dayOfMonth ->
            Log.d(com.gumibom.travelmaker.ui.signup.idpw.TAG, "선택된 날짜: $year-${monthOfYear + 1}-$dayOfMonth")
            myPageViewModel.selectBirthDate = datepicker.year.toString()
        })
        maxCalendar.set(2008, Calendar.JANUARY, 1)
        minCalendar.set(1924, Calendar.JANUARY, 1)
        datepicker.maxDate = maxCalendar.timeInMillis
        datepicker.minDate = minCalendar.timeInMillis
    }

    /*
    카테고리
     */
    private fun editCategory(){
        //
    }

    /*
    돌아가기
     */
    private fun backToMypage(){
        //
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}