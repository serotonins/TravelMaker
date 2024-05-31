package com.gumibom.travelmaker.ui.login.forgetIdPw

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ClickEventDialogBinding
import com.gumibom.travelmaker.databinding.FragmentLoginFindIdBinding
import com.gumibom.travelmaker.ui.dialog.ClickEventDialog
import com.gumibom.travelmaker.ui.login.LoginActivity
import com.gumibom.travelmaker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindIdFragment : Fragment() {

    private var _binding : FragmentLoginFindIdBinding? = null
    private val binding get() = _binding!!

    private val findIdPwViewModel : FindIdPwViewModel by viewModels()
    private lateinit var activity: LoginActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginFindIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkMyId()
        observeUserId()
    }

    private fun observeUserId() {
        findIdPwViewModel.userId.observe(viewLifecycleOwner) { event ->
            val message = event.getContentIfNotHandled() ?: ""

            if (message.isNotEmpty()) {
                showUserId(message)
            } else {
                Toast.makeText(requireContext(), "전화번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 아이디 확인 하기 눌렀을 때 아이디 찾기
     */
    private fun checkMyId() {
        binding.btnLoginFindIdId.setOnClickListener {
            val phoneNumber = binding.tieLoginFindIdPhone.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                findIdPwViewModel.findId(phoneNumber)
            }
        }
    }

    private fun showUserId(message : String) {
        val alertDialog = ClickEventDialog(requireContext())

        alertDialog.setTitle("요청하신 ID에요")
        alertDialog.setContent(message)

        alertDialog.setPositiveBtnTitle("확인")
        alertDialog.setNegativeBtnTitle("취소")

        alertDialog.setPositiveButtonListener {
            // 사진 + 텍스트 화면으로 이동
            activity.backToLoginFragment()
        }

        alertDialog.clickDialogShow()
    }
}