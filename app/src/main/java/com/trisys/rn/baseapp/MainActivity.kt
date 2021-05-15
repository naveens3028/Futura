package com.trisys.rn.baseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager2.widget.ViewPager2
import com.trisys.rn.baseapp.adapter.HomeTabViewAdapter
import com.trisys.rn.baseapp.helper.BottomNavigationBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {
    lateinit var homeTabViewAdapter : HomeTabViewAdapter
    lateinit var bottomNavigationBehavior: BottomNavigationBehavior
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()


        bottomNavigationBehavior = BottomNavigationBehavior()
        val layoutParams = navigationView.getLayoutParams() as CoordinatorLayout.LayoutParams
        layoutParams.behavior = bottomNavigationBehavior

        homeTabViewAdapter = HomeTabViewAdapter(this)
        viewPager.adapter = homeTabViewAdapter
        viewPager.offscreenPageLimit = 3


        val pageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    navigationView.setSelectedItemId(R.id.navigation_home);
                }else if(position == 1){
                    navigationView.setSelectedItemId(R.id.navigation_learn);
                }else if(position == 2){
                    navigationView.setSelectedItemId(R.id.navigation_live);
                }else if(position == 3){
                    navigationView.setSelectedItemId(R.id.navigation_test);
                }else if(position == 4){
                    navigationView.setSelectedItemId(R.id.navigation_doubts);
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
                appBarLayout.setExpanded(true)
                bottomNavigationBehavior.showBottomNavigationView(navigationView)
            }
        }
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }
}