package com.gumibom.travelmaker.ui.signup.genderbirth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentSignupGenderBirthdayBinding
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

private const val TAG = "SignupGenderBirthdayFra_싸피"
@AndroidEntryPoint
class SignupGenderBirthdayFragment : Fragment(){
    private lateinit var activity: SignupActivity;
    private val signupViewModel: SignupViewModel by activityViewModels()
    private var _binding:FragmentSignupGenderBirthdayBinding? = null
    private val binding get() = _binding!!
    private var isNextPage = false
    private var isGenderSelected = false

    private var gender = ""
    private var birth = ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SignupActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: 1")
        _binding = FragmentSignupGenderBirthdayBinding.inflate(inflater,container,false)
        Log.d(TAG, "onCreateView: ${_binding.toString()}")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backAndNextNaviBtn()
        // A. 성별 버튼의 한쪽 선택하여 클릭시, 선택된 쪽의 색이 좀더 진해짐.
        setGenderBtnColorToggle()
        // B. 스피너로 생년월일의 연/월/일 선택 <- default 값, 시작~끝 범위 정해줘야 함
        // C. A,B 모두 제대로 선택시 <- 다음 버튼이 진해지고, isNextPage = true 됨
        setDatepicker()
        // D. 종료
    }
    private fun backAndNextNaviBtn() {
        val btnSignupPrevious = binding.tvSignupGenderbirthPrevious
        val btnSignupNext = binding.tvSignupGenderbirthNext

        btnSignupPrevious.setOnClickListener{
            activity.navigateToPreviousFragment()
        }
        btnSignupNext.setOnClickListener {
            Log.d(TAG, "selectGender: ${signupViewModel.selectGender}")
            Log.d(TAG, "selectBirthDate: ${signupViewModel.selectBirthDate}")

            if (isGenderSelected) {
                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*
    setGenderBtnColorToggle(){}
    manBtn 누르면 manBtn이 진해지고, womanBtn 누르면 wonmanBtn이 진해짐.
    */
    private fun setGenderBtnColorToggle() {
        val manBtn = binding.btnSignupMan
        val womanBtn = binding.btnSignupWoman

        // 만약 manBtn이 클릭된 경우
        manBtn.setOnClickListener {
            manBtn.setBackgroundResource(R.drawable.btn_gender_selected)
            womanBtn.setBackgroundResource(R.drawable.btn_gender_selection)
            isGenderSelected = true
            signupViewModel.selectGender = "MALE"
            setNextToggle()
        }
        // 만약 womanBtn이 클릭된 경우
        womanBtn.setOnClickListener {
            womanBtn.setBackgroundResource(R.drawable.btn_gender_selected)
            manBtn.setBackgroundResource(R.drawable.btn_gender_selection)
            isGenderSelected = true
            signupViewModel.selectGender = "FEMALE"
            setNextToggle()
        }
    }
    /*
    setDatepicker()
    */
    private fun setDatepicker() {
        val datepicker = binding.dpSignupBirthSpinner
        val maxCalendar = Calendar.getInstance()
        val minCalendar = Calendar.getInstance()

        datepicker.init(1994, 10, 10, DatePicker.OnDateChangedListener {
                    view, year, monthOfYear, dayOfMonth ->
                Log.d(TAG, "선택된 날짜: $year-${monthOfYear + 1}-$dayOfMonth")
            signupViewModel.selectBirthDate = datepicker.year.toString()

        })
        maxCalendar.set(2008, Calendar.JANUARY, 1)
        minCalendar.set(1924, Calendar.JANUARY, 1)
        datepicker.maxDate = maxCalendar.timeInMillis
        datepicker.minDate = minCalendar.timeInMillis
    }
    private fun setNextToggle() {
        isNextPage = isGenderSelected
    }
    /*
    onDestroyView()
    */
    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 파괴시 메모리 누수를 방지하기 위해 _binding = null
        _binding= null
    }
}