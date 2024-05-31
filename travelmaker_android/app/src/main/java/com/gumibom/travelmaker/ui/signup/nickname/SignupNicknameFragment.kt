package com.gumibom.travelmaker.ui.signup.nickname

import android.content.Context
import android.os.Build
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
import androidx.fragment.app.viewModels
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentSignupNicknameBinding
import com.gumibom.travelmaker.model.GoogleUser
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "SignupNicknameFramgnet_싸피"
@AndroidEntryPoint
class SignupNicknameFragment : Fragment(){
    private lateinit var activity : SignupActivity
    private var isNextPage = false
    private var _binding :FragmentSignupNicknameBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel : SignupViewModel by activityViewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SignupActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val googleUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("googleUser", GoogleUser::class.java)
        } else {
            arguments?.getParcelable("googleUser")
        }
        signupViewModel.email = googleUser?.email
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupNicknameBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Z. 이전/다음 버튼 활성화
        setInit()
        backAndNextNaviBtn()
        checkDuplicateNickName()
        observeViewModel()
        signupNicknameCheck()
    }

    private fun setInit() {
        isNextPage = false
    }

    private fun observeViewModel(){
        signupViewModel.isDupNick.observe(viewLifecycleOwner){
            isNextPage = if (it.booleanValue) {
                Toast.makeText(requireContext(), "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show()
                false
            } else {
                Toast.makeText(requireContext(), "사용 가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
    private fun checkDuplicateNickName(){
        binding.btnSignupNickname.setOnClickListener {
            signupViewModel.checkDupNickName(binding.etSignupNickname.text.toString())
        }

        // A-1. nickname_et가 비어 있으면 EditText에 error 표시 하기
        // A-2. nickname_et의 내용으로 valid check 돌리기 -> 1. 성공 or 2. 실패

        // B. A에서 성공 하면, id_et 내용으로 dup check 돌리기(ViewModel,liveData, observe) -> 1. 성공 or 2. 실패

    }
    private fun backAndNextNaviBtn(){
        val btnSignupPrevious = binding.tvSignupNicknamePrevious
        val btnSignupNext = binding.tvSignupNicknameNext
        // 뒤로가기 버튼 기능은 늘 가능
        btnSignupPrevious.setOnClickListener {
            activity.navigateToPreviousFragment()
        // 앞으로가기 버튼 기능을 특정한 경우에만 가능
        }
        btnSignupNext.setOnClickListener {
            val nickname = binding.etSignupNickname.text.toString()
            if (isNextPage  && nickname.isNotEmpty()) {
                signupViewModel.nickname = nickname
                Log.d(TAG, "loginId: ${signupViewModel.loginId}")
                Log.d(TAG, "password: ${signupViewModel.password}")
                Log.d(TAG, "nickname: ${signupViewModel.nickname}")
                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), "입력을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    signupNicknameCheck()
    닉네임 유효성 검사와 중복검사를 위한... 부모 함수
    */
    private fun signupNicknameCheck(){
        binding.etSignupNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            // nm_et가 비어있으면 error 표시
            // nm_et가 유효성 검사를 통과 못하면 error 표시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val nicknameContent = binding.etSignupNickname.text.toString()
                if (nicknameContent.isEmpty()){
                    binding.tilSignupNickname.error = "닉네임을 입력 해주세요"
                } else {
                    binding.tilSignupNickname.error = null
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }


    // 마지막.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}