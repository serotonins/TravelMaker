package com.gumibom.travelmaker.ui.main.findmate.meeting_post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ActivityMeetingPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetingPostActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMeetingPostBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeetingPostBinding.inflate(layoutInflater).apply {
            navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_meeting_post)
                    as NavHostFragment).navController
        }
        setContentView(binding.root)

        finishActivity()
    }

    /**
     * 다음 버튼을 눌렀을 때 다음 화면으로 넘어가는 함수
     */
    fun navigateToNextFragment() {
        when(navController.currentDestination?.id){
            R.id.meetingPostDateFragment-> {
                    binding.viewMeetingPostIndicator1.setBackgroundResource(R.drawable.circle_indicator_inactive)
                    binding.viewMeetingPostIndicator2.setBackgroundResource(R.drawable.circle_indicator_active)

                    navController.navigate(R.id.action_meetingPostDateFragment_to_meetingPostPictureFragment)
                }
            R.id.meetingPostPictureFragment->{
                    navController.navigate(R.id.action_meetingPostPictureFragment_to_meetingPostCategoryFragment)

                    binding.viewMeetingPostIndicator2.setBackgroundResource(R.drawable.circle_indicator_inactive)
                    binding.viewMeetingPostIndicator3.setBackgroundResource(R.drawable.circle_indicator_active)
                }
            }
        }

    /**
     * 이전 버튼을 눌렀을 때 다음 화면으로 넘어가는 함수
     */
    fun navigateToPreviousFragment() {
        when(navController.currentDestination?.id) {
            R.id.meetingPostPictureFragment -> {
                binding.viewMeetingPostIndicator2.setBackgroundResource(R.drawable.circle_indicator_inactive)
                binding.viewMeetingPostIndicator1.setBackgroundResource(R.drawable.circle_indicator_active)
            }

            R.id.meetingPostCategoryFragment -> {
                binding.viewMeetingPostIndicator3.setBackgroundResource(R.drawable.circle_indicator_inactive)
                binding.viewMeetingPostIndicator2.setBackgroundResource(R.drawable.circle_indicator_active)
            }
        }
        navController.navigateUp()
    }


    /**
     *  x버튼 클릭 시 액티비티 종료
     */
    private fun finishActivity() {
        binding.ivMeetingPostExit.setOnClickListener {
            finish()
        }
    }
}