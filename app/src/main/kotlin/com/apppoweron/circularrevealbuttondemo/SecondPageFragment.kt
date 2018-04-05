package com.apppoweron.circularrevealbuttondemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SecondPageFragment : BaseFragment() {

    companion object {
        fun newInstance(): SecondPageFragment {
            return SecondPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second_page, container, false)
    }



}
