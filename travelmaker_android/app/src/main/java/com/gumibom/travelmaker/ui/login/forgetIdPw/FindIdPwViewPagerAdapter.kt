package com.gumibom.travelmaker.ui.login.forgetIdPw

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FindIdPwViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> FindIdFragment()
            1 -> FindPwFragment()
            else -> FindIdFragment()
        }
    }
}