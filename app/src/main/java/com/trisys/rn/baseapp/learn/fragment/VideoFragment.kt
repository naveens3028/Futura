package com.trisys.rn.baseapp.learn.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trisys.rn.baseapp.R
import com.vpnews24.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val path = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.son
        andExoPlayerView.setSource(Uri.parse(path).toString())

    }

}