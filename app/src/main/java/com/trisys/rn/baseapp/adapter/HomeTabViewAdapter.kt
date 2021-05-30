package com.trisys.rn.baseapp.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trisys.rn.baseapp.fragment.*


/**
 * Created by Prabhu2757 on 19-06-2016.
 */
class HomeTabViewAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return HomeFragment.newInstance("","")
        } else if (position == 1) {
            return LearnFragment.newInstance("", "")
        } else if (position == 2) {
            return LiveFragment.newInstance("", "")
        } else if (position == 3) {
            return TestFragment.newInstance("", "")
        } else {
            return DoubtFragment.newInstance("", "")
        }
    }
}
