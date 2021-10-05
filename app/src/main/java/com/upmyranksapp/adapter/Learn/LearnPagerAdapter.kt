package com.upmyranksapp.adapter.Learn

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.upmyranksapp.fragment.FragmentLearnCommon
import com.upmyranksapp.model.onBoarding.batchItem

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class LearnPagerAdapter(
    private val context: Context,
    fm: FragmentActivity,
    val myList: ArrayList<batchItem>?
) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return myList?.size!!
    }

    override fun createFragment(position: Int): Fragment {
        return if (!myList?.get(position)?.additionalCourseId.isNullOrEmpty()){
            FragmentLearnCommon.newInstance(myList?.get(position)?.additionalCourseId.toString(),myList?.get(position)?.id.toString())
        }else{
            FragmentLearnCommon.newInstance(myList?.get(position)?.courseId.toString(),myList?.get(position)?.id.toString())
        }
    }

}