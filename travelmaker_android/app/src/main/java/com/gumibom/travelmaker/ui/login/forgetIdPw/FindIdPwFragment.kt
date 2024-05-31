package com.gumibom.travelmaker.ui.login.forgetIdPw

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gumibom.travelmaker.databinding.FragmentLoginFindIdPwBinding
import com.gumibom.travelmaker.databinding.FragmentSignupProfileBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FindIdPwFragment_싸피"

@AndroidEntryPoint
class FindIdPwFragment : Fragment() {

    private var _binding : FragmentLoginFindIdPwBinding? = null
    private val binding get() = _binding!!
    private lateinit var idOrPassword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idOrPassword = arguments?.getString("idOrPassword") ?: ""
        Log.d(TAG, "onCreate: $idOrPassword")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginFindIdPwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    // ViewPager2를 설정하는 함수
    private fun setViewPager() {
        val adapter = FindIdPwViewPagerAdapter(this)
        val viewPager = binding.viewpagerLoginFindId
        val tabLayout = binding.tabLoginFindId

        viewPager.adapter = adapter
        setTabLayout(viewPager, tabLayout)



    }

    // 탭 레이아웃을 설정하는 함수
    private fun setTabLayout(viewPager : ViewPager2, tabLayout : TabLayout) {
        val tabLayoutTextArray = arrayOf("아이디 찾기","비밀번호 재생성")

        //TablayoutMediator로 탭 레이아웃과 뷰페이저를 연결 한다. 이때 탭 아이템도 같이 생성된다.
        TabLayoutMediator(tabLayout, viewPager){tab,position->
            tab.text = tabLayoutTextArray[position]
        }.attach()

        // 아이디 찾기를 눌렀을 때 바로 아이디 찾기로 가고
        // 비밀번호 재성성을 눌렀을 때 바로 비밀번호 재성성으로 간다.
        viewPager.post {
            viewPager.currentItem = idOrPassword.toIntOrNull() ?: 0
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}