package com.gumibom.travelmaker.ui.signup.idpw

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentSignupIdPwBinding
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "SignupIdPwFramgnet_싸피"
@AndroidEntryPoint
class SignupIdPwFragment : Fragment() {
    private lateinit var activity : SignupActivity
    private var _binding :FragmentSignupIdPwBinding? = null
    private val binding get() = _binding!!
    private var isNextPage = false // 다음 페이지로 넘어갈지 결정하는 함수
    private val signupViewModel : SignupViewModel by activityViewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SignupActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupIdPwBinding.inflate(inflater,container,false)
        return binding.root
    }

    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInit()
        // Z. 이전/다음 버튼 활성화
        backAndNextNaviBtn()
        // Y. liveData가 지켜보다가 검사 돌리기
        observeData()
        // A-1. id_et가 비어 있으면 EditText에 error 표시 하기
        // A-2. id_et의 내용으로 valid check 돌리기 -> 1. 성공 or 2. 실패
        signupIdCheck()
        // B. A에서 성공 하면, id_et 내용으로 dup check 돌리기(ViewModel,liveData, observe) -> 1. 성공 or 2. 실패
        // D-1. pw_et가 비어 있으면 EditText에 error 표시 하기
        // D-2. pw_et 내용으로 valid check 돌리기 -> 1. 성공 or 2. 실패
        signupPwCheck()
        isDupId()
    }

    private fun setInit() {
        isNextPage = false
    }

    private fun backAndNextNaviBtn() {
        binding.tvSignupIdpwNext.setOnClickListener {
            val loginId = binding.etSignupId.text.toString()
            val password = binding.etSignupPw.text.toString()

            if (isNextPage && loginId.isNotEmpty() && password.isNotEmpty()) {
                signupViewModel.loginId = loginId
                signupViewModel.password = password
                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), "입력을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeData() {
        signupViewModel.isDuplicatedId.observe(viewLifecycleOwner) { it ->
            if (it.booleanValue) {
                Toast.makeText(requireContext(), "중복된 아이디 입니다.", Toast.LENGTH_LONG).show()
            } else {
                isNextPage = true
                Toast.makeText(requireContext(), "가능한 아이디 입니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /*
    signupIdCheck(){}
    아이디의 유효성 검사와 중복 검사를 위한 ...부모 함수

    id_et 가 비어 있으면 EditText에 error 표시 하기
    id_et 내용으로 valid check 돌리기
    둘다 성공하면 tilSignupId.error = null 됨
     */
    private fun signupIdCheck(){
        binding.etSignupId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            // id_et가 비어있으면 error 표시
            // id_et가 유효성 검사를 통과 못하면 error 표시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val idContent = binding.etSignupId.text.toString()
                if (idContent.isEmpty()){
                    binding.tilSignupId.error = "ID를 입력 해주세요"
                    isNextPage = false
                } else {
                    isNextPage = true
                    binding.tilSignupId.error = null
                }
                if (!isValidateId(idContent)) {
                    binding.tilSignupId.error = "영문, 숫자 포함 6자리 이상"
                    isNextPage = false
                } else {
                    isNextPage = true
                    binding.tilSignupId.error = null
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
    /*
    isValidateId(id: String): Boolean{}
    아이디의 유효성 검사를 위한... 자식 함수
    */
    private fun isValidateId(id: String): Boolean {
        if (id.length < 6 || !id.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+\$"))) {
            return false // 유효성 검사 실패
        }
        return true // 유효성 검사 통과
    }
    /*
    isDupId(){}
    아이디의 중복 검사를 위한... 자식 함수
    1. 중복되지 않은 아이디 입니다. => Toast
    2. 중복된 아이디 입니다. => Toast
    */
    private fun isDupId(){
        binding.btnSignupId.setOnClickListener{
            val idContents = binding.etSignupId.text.toString()
            Log.d(TAG, "isDupId: ${idContents}")
            signupViewModel.checkId(idContents)
        }
        // 1. 중복검사 버튼을 누를때마다 뷰모델에 liveData를 만들고
        // 2. liveData에 옵저버를 달고
        // 3. 값이 바뀔때마다
        // 4. onChanged 메소드가 호출되는 것을 확인한다
    }
    /*
    signupPwCheck(){}
    비밀번호의 유효성 검사를 위한... 부모 함수
    1. PW를 입력 해주세요 => Toast
    2. 유효하지 않은 PW 입니다. => Toast
    3. 입력되었고 유효함 => error = null
    */
    private fun signupPwCheck(){
        binding.etSignupPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            // id_et가 비어있으면 error 표시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val pwContent = binding.etSignupPw.text.toString()
                if (pwContent.isEmpty()){
                    binding.tilSignupPw.error = "PW를 입력 해주세요"
                    isNextPage = false
                } else {
                    isNextPage = true
                    binding.tilSignupPw.error = null
                }
                if (!isValidatePw(pwContent)) {
                    binding.tilSignupPw.error = "영문, 숫자 포함 8자리 이상"
                    isNextPage = false
                } else {
                    isNextPage = true
                    binding.tilSignupPw.error = null
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    /*
    isValidatePw(pw: String): Boolean {}
    비밀번호의 유효성 검사를 위한... 자식 함수
    boolean 값만 내뱉음
    */
    private fun isValidatePw(pw: String): Boolean {
        // 비밀번호 길이 및 문자 조건 검사
        if (pw.length < 8 || !pw.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+\$"))) {
            return false // 유효성 검사 실패
        }
        return true // 유효성 검사 통과
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}