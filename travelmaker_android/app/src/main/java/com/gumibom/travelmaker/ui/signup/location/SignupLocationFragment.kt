package com.gumibom.travelmaker.ui.signup.location

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.DISPLAY
import com.gumibom.travelmaker.constant.ENGLISH
import com.gumibom.travelmaker.constant.ENGLISH_PATTERN
import com.gumibom.travelmaker.constant.GOOGLE_API_KEY
import com.gumibom.travelmaker.constant.KAKAO_API_KEY
import com.gumibom.travelmaker.constant.KOREAN
import com.gumibom.travelmaker.constant.KOREAN_PATTERN
import com.gumibom.travelmaker.constant.NAVER_ID_KEY
import com.gumibom.travelmaker.constant.NAVER_SECRET_KEY
import com.gumibom.travelmaker.constant.NO_SEARCH_LOCATION
import com.gumibom.travelmaker.constant.WRONG_INPUT
import com.gumibom.travelmaker.databinding.FragmentSignupLocationBinding
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "SignupLocationFragment_싸피"
@AndroidEntryPoint
class SignupLocationFragment : Fragment() {
    private var _binding : FragmentSignupLocationBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel : SignupViewModel by activityViewModels()
    private lateinit var adapter : SignupLocationAdapter
    private lateinit var activity : SignupActivity
    private var isNextPage = false // 다음 페이지로 넘어갈지 말지 결정하는 변수
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupLocationBinding.inflate(inflater, container, false)

        binding.etSignupLocation.requestFocus()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SignupActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInit()
        setAdapter()
        searchLocation()
        openNextPage()
        backAndNextNaviBtn()
    }

    private fun setInit() {
        signupViewModel.selectTown = ""
        signupViewModel.selectNation = ""

        signupViewModel.setAddress("")
        isNextPage = false
    }

    private fun backAndNextNaviBtn(){
        binding.tvSignupLocationPrevious.setOnClickListener {
            activity.navigateToPreviousFragment()
        }
        binding.tvSignupLocationNext.setOnClickListener {
            if (signupViewModel.selectTown.isNotEmpty() && signupViewModel.selectNation.isNotEmpty()) {

                Log.d(TAG, "loginId: ${signupViewModel.loginId}")
                Log.d(TAG, "password: ${signupViewModel.password}")
                Log.d(TAG, "nickname: ${signupViewModel.nickname}")
                Log.d(TAG, "selectTown: ${signupViewModel.selectTown}")
                Log.d(TAG, "selectNation: ${signupViewModel.selectNation}")

                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), "동네를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // 리싸이클러뷰 세팅
    private fun setAdapter() {
        adapter = SignupLocationAdapter(requireContext(), signupViewModel)

        // 네이버 장소가 갱신된 경우
        signupViewModel.kakaoAddressList.observe(viewLifecycleOwner) { addressList ->
            Log.d(TAG, "setAdapter: $addressList")

            if (addressList.isNotEmpty()) {
                adapter.submitList(addressList.toMutableList())
            } else {
                adapter.submitList(addressList.toMutableList())
                Toast.makeText(requireContext(), NO_SEARCH_LOCATION, Toast.LENGTH_SHORT).show()
            }
        }

        // 구글 장소가 갱신된 경우
        signupViewModel.googleAddressList.observe(viewLifecycleOwner) { addressList ->
            Log.d(TAG, "googleAddress: $addressList")

            if (addressList.isNotEmpty()) {
                adapter.submitList(addressList.toMutableList())
            } else {
                adapter.submitList(addressList.toMutableList())
                Toast.makeText(requireContext(), NO_SEARCH_LOCATION, Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvLocationList.adapter = adapter
    }
    // EditText에 위치 검색
    private fun searchLocation() {
        // 반짝이는 애니메이션 정의
        val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        val editText = binding.etSignupLocation

        binding.ivSignupLocationSearch.setOnClickListener {
            val location = binding.etSignupLocation.text.toString()

            val language = selectKoreanEnglish(location)
            Log.d(TAG, "searchLocation: $location")
            // 한국어면 네이버 api, 영어면 구글 api 호출
            when (language) {
                KOREAN -> {
                    signupViewModel.getKakaoLocation(KAKAO_API_KEY, location)
                }
                ENGLISH -> {
                    signupViewModel.getGoogleLocation(location, GOOGLE_API_KEY)
                }
                else -> {
                    // 둘다 아니면 토스트 메시지 띄움
                    Toast.makeText(requireContext(), language, Toast.LENGTH_SHORT).show()
                }
            }

            // 애니메이션 적용
            binding.ivSignupLocationSearch.startAnimation(animation)
            // 키보드를 숨기는 함수
            hideKeyboard()
        }

        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                keyEvent?.action == KeyEvent.ACTION_DOWN &&
                keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                val location = binding.etSignupLocation.text.toString()

                val language = selectKoreanEnglish(location)
                Log.d(TAG, "searchLocation: $location")
                // 한국어면 네이버 api, 영어면 구글 api 호출
                when (language) {
                    KOREAN -> {
                        signupViewModel.getKakaoLocation(KAKAO_API_KEY, location)
                    }
                    ENGLISH -> {
                        signupViewModel.getGoogleLocation(location, GOOGLE_API_KEY)
                    }
                    else -> {
                        // 둘다 아니면 토스트 메시지 띄움
                        Toast.makeText(requireContext(), language, Toast.LENGTH_SHORT).show()
                    }
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    // 다음 Text가 활성화 되는 함수


    // 다음 버튼이 활성화 되어있을 시 페이지 전환하는 함수
    private fun openNextPage() {
        if (isNextPage) {
        }
    }

    // 키보드 숨김 함수
    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.etSignupLocation.clearFocus() // editText의 Focus를 잃게 한다.
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    // 한국어인지 영어인지 구분하는 함수 (정규식 이용)
    private fun selectKoreanEnglish(location : String) : String {
        val koreanRegex = Regex(KOREAN_PATTERN)
        val englishRegex = Regex(ENGLISH_PATTERN)

        // 입력한 장소가 한국어 + 숫자면
        return if (koreanRegex.matches(location)) {
            KOREAN
        }
        // 영어 + 숫자면
        else if (englishRegex.matches(location)) {
            ENGLISH
        }
        // 둘다 아니면
        else {
            WRONG_INPUT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}