package com.gumibom.travelmaker.ui.signup.phone

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.gumibom.travelmaker.constant.NOT_ALLOW_SMS
import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.databinding.FragmentSignupPhoneBinding
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel

import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "SignupPhoneFragment_싸피"
@AndroidEntryPoint
class SignupPhoneFragment : Fragment() {

    private var _binding : FragmentSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel : SignupViewModel by activityViewModels()

    private lateinit var phoneEditText : TextInputEditText
    private lateinit var certificationEditText : TextInputEditText
    private lateinit var activity:SignupActivity
    private var isCertificationSuccess = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SignupActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneEditText = binding.tieSignupPhone
        certificationEditText = binding.tieSignupCertificationNumber

        setInit()
        getCertificationNumber()

        checkSecretNumber()
        phoneNumberCheck()
        backAndNextNaviBtn()

        observeLiveData()
        Log.d(TAG, "onViewCreated: ")
    }

    private fun setInit() {
        isCertificationSuccess = false
    }

    private fun observeLiveData() {
        signupViewModel.isSendPhoneSuccess.observe(viewLifecycleOwner) { event ->
            val isSuccess = event.getContentIfNotHandled()

            if (isSuccess != null && isSuccess) {
                Log.d(TAG, "observeLiveData: 여기로 오니? null")
                startTimer()
            } else if (isSuccess != null && !isSuccess) {
                Log.d(TAG, "observeLiveData: 여기로 오니?")
                Toast.makeText(requireContext(), "인증 번호 전송 실패", Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.isCertificationSuccess.observe(viewLifecycleOwner) { event ->
            val isSuccess = event.getContentIfNotHandled()

            if (isSuccess != null && isSuccess) {
                isCertificationSuccess = true
                endTimer()
                Toast.makeText(requireContext(), "문자 인증 성공", Toast.LENGTH_SHORT).show()
            } else if (isSuccess != null && !isSuccess) {
                Toast.makeText(requireContext(), "문자 인증 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
        인증 요청 버튼을 누르면 서버에 문자 인증을 요청하고
        3분 타이머를 돌리기
     */
    private fun getCertificationNumber() {
        binding.btnSignupCertificationPhone.setOnClickListener {
            val phoneNumber = binding.tieSignupPhone.text.toString()
            val phoneNumberError = binding.tilSignupPhone.error

            // 정상 휴대폰 번호면 서버 통신
            if (phoneNumberError == null && phoneNumber.isNotEmpty()) {
                val phoneNumberRequestDTO = PhoneNumberRequestDTO(phoneNumber, "")
                signupViewModel.sendPhoneNumber(phoneNumberRequestDTO)
            }

        }
    }

    private fun checkSecretNumber() {
        binding.btnSignupCertificationNumber.setOnClickListener {
            val phoneNumber = binding.tieSignupPhone.text.toString()
            val certificationNumber = binding.tieSignupCertificationNumber.text.toString()

            val phoneCertificationRequestDTO = PhoneCertificationRequestDTO(
                phoneNumber,
                certificationNumber
            )
            signupViewModel.isCertification(phoneCertificationRequestDTO)
        }
    }

    // 인증번호 요청 시 사용자에게 3분 타이머가 돌아감
    private fun startTimer() {
        // 인증 번호 입력 화면 보여주기
        binding.layoutPhoneCertification.visibility = View.VISIBLE

        val timerText = binding.tvSignupTimer
        signupViewModel.startTimer(timerText)
    }

    private fun endTimer() {
        signupViewModel.endTimer()
    }


    // 뒤로 가기 버튼 클릭 시 EditText의 focus를 없애는 콜백 함수
    val callback = object : OnBackPressedCallback(true ) {
        override fun handleOnBackPressed() {
            if (phoneEditText.isFocused) {
                clearFocusAndHideKeyboard(phoneEditText)
            } else if (certificationEditText.isFocused) {
                clearFocusAndHideKeyboard(certificationEditText)
            } else {
                isEnabled = false
                // TODO 네비게이션 뒤로가기로 변경 필요
                activity?.onBackPressed()
            }
        }
    }

    // 휴대폰 번호 입력 error 체크
    private fun phoneNumberCheck() {
        binding.tieSignupPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            // 비어있으면 error 표시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val phoneNumber = binding.tieSignupPhone.text.toString()

                if (phoneNumber.length != 11) {
                    binding.tilSignupPhone.error = "휴대폰 번호는 11자리여야 합니다."
                } else {
                    binding.tilSignupPhone.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }



    private fun clearFocusAndHideKeyboard(editText: EditText) {
        editText.clearFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
    private fun backAndNextNaviBtn(){
        binding.tvSignupPhonenumberPrevious.setOnClickListener {
            activity.navigateToPreviousFragment()
        }
        binding.tvSignupPhonenumberNext.setOnClickListener {
            if (isCertificationSuccess){
                signupViewModel.phoneNumber = binding.tieSignupPhone.text.toString()
                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), NOT_ALLOW_SMS, Toast.LENGTH_SHORT).show()
            }
        }
    }
}