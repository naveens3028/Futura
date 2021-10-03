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
        var batchId = ""
        if (myList?.get(position)?.additionalCourseId.isNullOrEmpty()) {
            myList?.get(position)?.let { it1 ->
                it1.courseId?.let { it2 ->
                    it1.id?.let { it3 ->
                        batchId = it2
                    }
                }
            }
        } else {
            myList?.get(position)?.let { it1 ->
                it1.additionalCourseId?.let { it2 ->
                    it1.id?.let { it3 ->
                        batchId = it2
                    }
                }
            }
        }

        return FragmentLearnCommon.newInstance(batchId,"")
    }

}