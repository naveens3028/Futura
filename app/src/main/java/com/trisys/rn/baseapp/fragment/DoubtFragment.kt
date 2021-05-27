package com.trisys.rn.baseapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.doubt.AskDoubtActivity
import kotlinx.android.synthetic.main.fragment_doubt.*

class DoubtFragment : Fragment() {

    private lateinit var fragment: Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doubt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment = ClarifiedFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.doubtFrameLayout, fragment)
        fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction?.commit()

        doubtTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> fragment = ClarifiedFragment()
                    1 -> fragment = ClarifiedFragment()
                }
                val fm: FragmentManager? = activity?.supportFragmentManager
                val ft = fm?.beginTransaction()
                ft?.replace(R.id.doubtFrameLayout, fragment)
                ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft?.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        askDoubt.setOnClickListener {
            val intent = Intent(requireContext(), AskDoubtActivity::class.java)
            startActivity(intent)
        }
    }
}