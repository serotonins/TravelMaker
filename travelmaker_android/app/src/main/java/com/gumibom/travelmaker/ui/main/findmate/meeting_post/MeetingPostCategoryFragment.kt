package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.gumibom.travelmaker.constant.FAIL_CREATE_MEETING_POST
import com.gumibom.travelmaker.constant.NOT_ENOUGH_INPUT
import com.gumibom.travelmaker.constant.SUCCESS_CREATE_MEETING_POST
import com.gumibom.travelmaker.databinding.FragmentMeetingPostCategoryBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.util.ApplicationClass
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MeetingPostCategoryFrag_싸피"
@AndroidEntryPoint
class MeetingPostCategoryFragment : Fragment() {

    private var _binding: FragmentMeetingPostCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MeetingPostActivity

    private val meetingPostViewModel : MeetingPostViewModel by activityViewModels()

    private lateinit var spinnerMaxMember : Spinner
    private lateinit var spinnerMinTraveler : Spinner
    private lateinit var spinnerMinNative : Spinner


    private var maxMember = 0
    private var isNextPage = false
    private var initFlag = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MeetingPostActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetingPostCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerMaxMember = binding.spinnerMaxMember
        spinnerMinTraveler = binding.spinnerMinTraveler
        spinnerMinNative = binding.spinnerMinNative

        setSpinner()
        selectCategory()
        createMeeting()
        observeFinish()
    }

    /**
     * 모임 생성에 성공하면 페이지를 종료한다.
     */
    private fun observeFinish() {
        meetingPostViewModel.isSuccess.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), SUCCESS_CREATE_MEETING_POST, Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)

            } else {
                Toast.makeText(requireContext(), FAIL_CREATE_MEETING_POST, Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }

    /**
     * 스피너를 설정하는 함수
     */
    private fun setSpinner() {
        val items = arrayOf("선택", "1", "2", "3", "4", "5", "6")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinnerMaxMember.adapter = adapter
        spinnerMinTraveler.adapter = adapter
        spinnerMinNative.adapter = adapter

        spinnerMaxMember.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 기본 형태는 toString()으로 치환되므로 toString() -> toInt() 작업 필요
                val value = parent.getItemAtPosition(position).toString()

                if (!isInitSpinner(value)) {
                    meetingPostViewModel.maxMember = value.toInt()
                    maxMember = value.toInt()
                    initFlag = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 항목도 선택되지 않았을 때 실행할 코드
            }
        }

        spinnerMinTraveler.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 기본 형태는 toString()으로 치환되므로 toString() -> toInt() 작업 필요
                val value = parent.getItemAtPosition(position).toString()

                if (maxMember == 0 && initFlag) {
                    Toast.makeText(requireContext(), "모집 인원을 정해주세요.", Toast.LENGTH_SHORT).show()
                    spinnerMinTraveler.setSelection(0)
                }

                if (!isInitSpinner(value) && value.toInt() < maxMember) {
                    meetingPostViewModel.minTraveler = value.toInt()
                    isNextPage = true
                } else if (!isInitSpinner(value) && value.toInt() > maxMember) {
                    isNextPage = false
                    Toast.makeText(requireContext(), "모임 최대 인원을 넘을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    // 여기에서 스피너의 선택을 첫 번째 아이템으로 설정
                    spinnerMinTraveler.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 항목도 선택되지 않았을 때 실행할 코드
            }
        }

        spinnerMinNative.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 기본 형태는 toString()으로 치환되므로 toString() -> toInt() 작업 필요
                val value = parent.getItemAtPosition(position).toString()

                if (maxMember == 0 && initFlag) {
                    Toast.makeText(requireContext(), "모집 인원을 정해주세요.", Toast.LENGTH_SHORT).show()
                    spinnerMinNative.setSelection(0)
                }

                if (!isInitSpinner(value) && value.toInt() < maxMember) {
                    meetingPostViewModel.minNative = value.toInt()
                    isNextPage = true
                } else if (!isInitSpinner(value) && value.toInt() > maxMember){
                    isNextPage = false
                    Toast.makeText(requireContext(), "모임 최대 인원을 넘을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    // 여기에서 스피너의 선택을 첫 번째 아이템으로 설정
                    spinnerMinNative.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 항목도 선택되지 않았을 때 실행할 코드
            }
        }

    }

    private fun isInitSpinner(value : String) : Boolean {
        if (value == "선택") {
            return true
        }
        return false
    }

    /**
     * 카테고리를 설정하는 함수
     */
    private fun selectCategory() {

        val chipMap = mapOf<String, String>("맛집" to "taste", "힐링" to "healing", "문화" to "culture", "활동" to "active",
                                            "사진" to "picture", "자연" to "nature", "쇼핑" to "shopping", "휴식" to "rest")

        val chipGroup1 = binding.chipGroup1
        val chipGroup2 = binding.chipGroup2

        for (index in 0 until chipGroup1.childCount) {
            val chip = chipGroup1.getChildAt(index) as? Chip
            chip?.setOnClickListener {
                val chipText = chipMap.getValue(chip.text.toString())
                // Chip 클릭 시 실행할 코드
                if (chip.isChecked) {
                    meetingPostViewModel.categoryList.add(chipText)
                    Log.d(TAG, "selectCategory: ${meetingPostViewModel.categoryList}")
                } else {
                    meetingPostViewModel.categoryList.remove(chipText)
                }
            }
        }

        for (index in 0 until chipGroup2.childCount) {
            val chip = chipGroup2.getChildAt(index) as? Chip
            chip?.setOnClickListener {
                val chipText = chipMap.getValue(chip.text.toString())
                // Chip 클릭 시 실행할 코드
                if (chip.isChecked) {
                    meetingPostViewModel.categoryList.add(chipText)
                } else {
                    meetingPostViewModel.categoryList.remove(chipText)
                }
            }
        }
    }

    private fun createMeeting() {
        binding.btnMeetingPostCreate.setOnClickListener {
            if (
                meetingPostViewModel.categoryList.isNotEmpty() &&
                meetingPostViewModel.maxMember != 0 &&
                meetingPostViewModel.minNative != 0 &&
                meetingPostViewModel.minTraveler != 0 &&
                isNextPage ) {
                // 모두 입력되었으면 서버에 통신
                meetingPostViewModel.username = ApplicationClass.sharedPreferencesUtil.getLoginId()

                meetingPostViewModel.createMeeting()

            } else {
                Toast.makeText(requireContext(), NOT_ENOUGH_INPUT, Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}