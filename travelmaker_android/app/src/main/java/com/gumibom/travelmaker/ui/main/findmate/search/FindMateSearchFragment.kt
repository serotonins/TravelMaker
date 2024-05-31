package com.gumibom.travelmaker.ui.main.findmate.search

import android.app.Dialog
import android.content.Context
import android.location.Address
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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.ENGLISH
import com.gumibom.travelmaker.constant.ENGLISH_PATTERN
import com.gumibom.travelmaker.constant.GOOGLE_API_KEY
import com.gumibom.travelmaker.constant.KAKAO_API_KEY
import com.gumibom.travelmaker.constant.KOREAN
import com.gumibom.travelmaker.constant.KOREAN_PATTERN
import com.gumibom.travelmaker.constant.NO_SEARCH_LOCATION
import com.gumibom.travelmaker.constant.WRONG_INPUT
import com.gumibom.travelmaker.databinding.DialogMainFindMateSearchBinding
import com.gumibom.travelmaker.ui.common.CommonViewModel
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.ui.main.findmate.FindMateActivity
import com.gumibom.travelmaker.ui.main.findmate.meeting_post.MeetingPostViewModel
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.location.SignupLocationAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FindMateSearchFragment_싸피"
@AndroidEntryPoint
class FindMateSearchFragment(private val commonViewModel: CommonViewModel) : DialogFragment() {

    private var _binding: DialogMainFindMateSearchBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel : MainViewModel by activityViewModels()
    private val meetingPostViewModel : MeetingPostViewModel by activityViewModels()
    private lateinit var adapter : SignupLocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMainFindMateSearchBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그를 화면에 꽉 차게 표시하기 위해 설정
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 다이얼로그의 테마를 설정
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle("Location Search")
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_click_event_backgound)

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchPlaces()
        setAdapter()

        adapter.setOnItemClickListener { address ->
            Log.d(TAG, "onViewCreated: $address")
            if (commonViewModel is MainViewModel) {
                mainViewModel.userSelectAddress(address)
                dismiss()
            } else if (commonViewModel is MeetingPostViewModel) {
                meetingPostViewModel.meetingSelectAddress(address)
                dismiss()
            }

        }
    }

    private fun setAdapter() {
        adapter = SignupLocationAdapter(requireContext(), commonViewModel)

        // 네이버 장소가 갱신된 경우
        mainViewModel.kakaoAddressList.observe(viewLifecycleOwner) { addressList ->
            if (addressList.isNotEmpty()) {
                adapter.submitList(addressList.toMutableList())
            } else {
                adapter.submitList(addressList.toMutableList())
                Toast.makeText(requireContext(), NO_SEARCH_LOCATION, Toast.LENGTH_SHORT).show()
            }
        }

        // 구글 장소가 갱신된 경우
        mainViewModel.googleAddressList.observe(viewLifecycleOwner) { addressList ->
            if (addressList.isNotEmpty()) {
                adapter.submitList(addressList.toMutableList())
            } else {
                adapter.submitList(addressList.toMutableList())
                Toast.makeText(requireContext(), NO_SEARCH_LOCATION, Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvFindMateSearch.adapter = adapter
    }



    /**
     *  장소 검색
     */
    private fun searchPlaces() {
        // 반짝이는 애니메이션 정의
        val animation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        val editText = binding.etFindMateSearch

        binding.ivFindMateLocationSearch.setOnClickListener {
            val location = binding.etFindMateSearch.text.toString()

            val language = selectKoreanEnglish(location)

            // 한국어면 네이버 api, 영어면 구글 api 호출
            when (language) {
                KOREAN -> {
                    mainViewModel.getKakaoLatLng(location)
                }

                ENGLISH -> {
                    mainViewModel.getGoogleLatLng(location)
                }

                else -> {
                    // 둘다 아니면 토스트 메시지 띄움
                    Toast.makeText(requireContext(), language, Toast.LENGTH_SHORT).show()
                }
            }

            // 애니메이션 적용
            binding.ivFindMateLocationSearch.startAnimation(animation)
            // 키보드를 숨기는 함수
            hideKeyboard()
        }

        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                keyEvent?.action == KeyEvent.ACTION_DOWN &&
                keyEvent.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val location = binding.etFindMateSearch.text.toString()

                val language = selectKoreanEnglish(location)
                Log.d(TAG, "searchLocation: $location")
                // 한국어면 네이버 api, 영어면 구글 api 호출
                when (language) {
                    KOREAN -> {
                        mainViewModel.getKakaoLatLng(location)
                    }

                    ENGLISH -> {
                        mainViewModel.getGoogleLatLng(location)
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

    // 키보드 숨김 함수
    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.etFindMateSearch.clearFocus() // editText의 Focus를 잃게 한다.
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}