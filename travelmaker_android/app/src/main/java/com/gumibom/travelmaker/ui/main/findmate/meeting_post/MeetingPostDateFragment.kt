package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.AFTER_DATE
import com.gumibom.travelmaker.constant.BEFORE_DATE
import com.gumibom.travelmaker.constant.NOT_ENOUGH_INPUT
import com.gumibom.travelmaker.constant.WRONG_DATE
import com.gumibom.travelmaker.databinding.FragmentMeetingPostDateBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.findmate.search.FindMateSearchFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "MeetingPostDateFragment_싸피"

@AndroidEntryPoint
class MeetingPostDateFragment : Fragment() {

    private var _binding: FragmentMeetingPostDateBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MeetingPostActivity
    
    private lateinit var datePickerHelper: DatePickerHelper
    private lateinit var timePickerHelper: TimePickerHelper
    private lateinit var findMateSearchFragment : FindMateSearchFragment

    private val meetingPostViewModel : MeetingPostViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MeetingPostActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetingPostDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePickerHelper = DatePickerHelper(requireActivity())
        timePickerHelper = TimePickerHelper(requireActivity())
        findMateSearchFragment = FindMateSearchFragment(meetingPostViewModel)

        moveNextFragment()

        showDatePicker()
        showTimePicker()
        selectMeetingLocation()
        observeLiveData()

        // TODO 데이터 viewModel에 담기
    }

    private fun observeLiveData() {
        meetingPostViewModel.selectMeetingAddress.observe(viewLifecycleOwner) { address ->
            binding.tvMeetingPostAddress.visibility = View.VISIBLE
            binding.tvMeetingPostAddress.text = address.title
        }
    }
    /**
     * 다음 버튼 클릭 시 다음 화면으로 넘어감
     */
    private fun moveNextFragment() {
        binding.btnMeetingPostNext.setOnClickListener {
            val startDate = binding.tvMeetingStartDate.text.toString()
            val endDate = binding.tvMeetingEndDate.text.toString()
            val deadLineDate = binding.tvMeetingDeadlineDate.text.toString()
            val deadLineTime = binding.tvMeetingDeadlineClock.text.toString()
            val meetingPlace = binding.tvMeetingPostPlace.text.toString()

            if (startDate.isNotEmpty() && endDate.isNotEmpty() && deadLineDate.isNotEmpty() && deadLineTime.isNotEmpty() && !meetingPlace.isNullOrEmpty()) {
                meetingPostViewModel.startDate = startDate
                meetingPostViewModel.endDate = endDate
                meetingPostViewModel.deadlineDate = deadLineDate

                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), NOT_ENOUGH_INPUT, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 달력 아이콘 클릭 시 datePicker가 나오는 함수
     */
    private fun showDatePicker() {
        binding.tvMeetingStartDate.setOnClickListener {
            datePickerHelper.pickDate { dateString ->
                // 빈 String이 아니면 text 추가
                if (dateString.isNotEmpty()){
                    binding.tvMeetingStartDate.text = dateString
                }
            }
        }

        binding.tvMeetingEndDate.setOnClickListener {
            datePickerHelper.pickDate { dateString ->
                // 빈 String이 아니면 text 추가
                if (dateString.isNotEmpty()){
                    binding.tvMeetingEndDate.text = dateString
                    
                    val isCompareDate = compareDate(dateString, binding.tvMeetingStartDate.text.toString()) ?: false
                    
                    if (!isCompareDate) {
                        binding.tvMeetingEndDate.text = dateString
                    } else {
                        Toast.makeText(requireContext(), BEFORE_DATE, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvMeetingDeadlineDate.setOnClickListener {
            datePickerHelper.pickDate { dateString ->
                // 빈 String이 아니면 text 추가
                if (dateString.isNotEmpty()){
                    val isCompareDate = compareDate(dateString, binding.tvMeetingStartDate.text.toString()) ?: false

                    if (isCompareDate) {
                        binding.tvMeetingDeadlineDate.text = dateString
                    } else {
                        Toast.makeText(requireContext(), AFTER_DATE, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    /**
     * 시계 아이콘 클릭 시
     */
    @SuppressLint("SetTextI18n")
    private fun showTimePicker() {
        binding.tvMeetingDeadlineClock.setOnClickListener {
            timePickerHelper.pickTime{ hour, minute ->
                // 12보다 크면 오후
                if (hour >= 12) {
                    binding.tvMeetingDeadlineClock.text = "오후 ${hour - 12}시 ${minute}분"
                } else {
                    binding.tvMeetingDeadlineClock.text = "오전 ${hour}시 ${minute}분"
                }
                meetingPostViewModel.deadlineTime = "${hour}:$minute"
            }
        }
    }

    private fun selectMeetingLocation() {
        binding.ivMeetingPostSearch.setOnClickListener {
            findMateSearchFragment.show(childFragmentManager, "tag")
        }
    }

    /**
     * 모임 시작 날짜와 모집 마감 날짜를 비교하는 함수
     */
    private fun compareDate(dateString1 : String, dateString2 : String): Boolean? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        try {
            val date1 = format.parse(dateString1)
            val date2 = format.parse(dateString2)

            // 모집 마감날짜가 모임 시작날짜 보다 과거면
            return date1 != null && date2 != null && date1 < date2
        } catch (e : Exception) {
            Toast.makeText(requireContext(), WRONG_DATE, Toast.LENGTH_SHORT).show()
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}