package com.gumibom.travelmaker.ui.login.forgetIdPw

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gumibom.travelmaker.databinding.FragmentLoginFindPwBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPwFragment : Fragment() {

    private var _binding : FragmentLoginFindPwBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginFindPwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkId()
        rePasswordCheck() // 비밀번호 다시 입력하기 check

        // TODO 비밀번호 재입력으로 error가 null일 시 비밀번호 재설정 버튼 활성화 -> 서버에 바뀐 비밀번호 넘겨주기

    }

    private fun checkId() {
        binding.btnLoginFindPwId.setOnClickListener {
            // TODO 서버 통신으로 해당 아이디가 존재하는지 확인하기
            // 존재할 시 비밀번호를 재설정할 수 있게 됨.
        }
    }

    // 비밀번호와 비밀번호 다시 입력이 다르면 error
    private fun rePasswordCheck() {
        binding.tieLoginFindPwRePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            // 닉네임이 비어있으면 error 표시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val password = binding.tieLoginFindPwPassword.text.toString()
                val rePassword = binding.tieLoginFindPwRePassword.text.toString()

                if (rePassword != password) {
                    binding.tilLoginFindPwRePassword.error = "비밀번호가 같지 않습니다."
                } else {
                    binding.tilLoginFindPwRePassword.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}