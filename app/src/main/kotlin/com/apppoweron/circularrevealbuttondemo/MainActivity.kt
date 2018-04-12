package com.apppoweron.circularrevealbuttondemo

import android.os.Bundle
import android.support.v7.widget.Toolbar

class MainActivity : BaseActivity() {

    override val containerId: Int
        get() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        try {
            loadDefaultFragment()
        } catch (e: NoContainerException) {
            e.printStackTrace()
        }
    }

    @Throws(NoContainerException::class)
    private fun loadDefaultFragment() {
        loadFragment(MainFragment.newInstance(), BaseActivity.FragmentLoadType.ADD)
    }
}
