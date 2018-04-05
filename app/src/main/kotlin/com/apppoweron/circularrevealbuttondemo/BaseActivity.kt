package com.apppoweron.circularrevealbuttondemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseActivity : AppCompatActivity(), FragmentCommunicator {

    companion object {
        private const val DEFAULT_CONTAINER_ID = -1
    }

    private var mBackPressListener: BackPressListener? = null
    private var mDelayedTransaction: FragmentTransaction? = null
    private var mIsSavedInstanceStateCalled: Boolean = false

    protected open val containerId: Int
        get() = 0

    @Throws(NoContainerException::class)
    private fun addFragment(fragment: Fragment) {
        if (!hasContainerId()) {
            throw NoContainerException()
        }
        supportFragmentManager.beginTransaction().add(containerId, fragment, fragment.javaClass.getSimpleName() + "_TAG").commit()
    }

    @Throws(NoContainerException::class)
    private fun replaceFragment(fragment: Fragment, needToBackStack: Boolean) {
        if (!hasContainerId()) {
            throw NoContainerException()
        }
        val transaction = supportFragmentManager.beginTransaction().replace(containerId, fragment, fragment.javaClass.getSimpleName() + "_TAG")
        if (needToBackStack) {
            transaction.addToBackStack(null)
        }
        if (mIsSavedInstanceStateCalled) {
            mDelayedTransaction = transaction
            return
        }
        transaction.commit()

        if (needToBackStack) {
            switchBackButton(true)
        }
    }


    @Throws(NoContainerException::class)
    @JvmOverloads
    fun loadFragment(fragment: Fragment, loadType: FragmentLoadType, needToBackStack: Boolean = false) {
        when (loadType) {
            BaseActivity.FragmentLoadType.ADD -> addFragment(fragment)
            BaseActivity.FragmentLoadType.REPLACE -> replaceFragment(fragment, needToBackStack)
        }
    }

    private fun hasContainerId(): Boolean {
        return containerId != DEFAULT_CONTAINER_ID
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            switchBackButton(false)
        }
        if (mBackPressListener != null) {
            mBackPressListener!!.onBackPressed()
        } else {
            callSuperBackPressed()
        }
    }

    internal fun setBackPressListener(listener: BackPressListener) {
        mBackPressListener = listener
    }

    private fun callSuperBackPressed() {
        super.onBackPressed()
    }

    internal interface BackPressListener {
        fun onBackPressed()
    }

    enum class FragmentLoadType {
        ADD, REPLACE
    }


    /**
     * Remove items from backstack
     */
    protected fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    @Throws(NoContainerException::class)
    override fun onNewFragmentSelected(fragment: Fragment, replaceIt: Boolean, needToBackStack: Boolean) {
        if (replaceIt) {
            loadFragment(fragment, FragmentLoadType.REPLACE, needToBackStack)
        } else {
            loadFragment(fragment, FragmentLoadType.ADD, needToBackStack)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        mIsSavedInstanceStateCalled = true
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mIsSavedInstanceStateCalled = false
        if (mDelayedTransaction != null) {
            mDelayedTransaction!!.commit()
            mDelayedTransaction = null
        }
    }

    /**
     * Show backbutton on toolbar
     *
     * @param turnOn show it or not
     */
    private fun switchBackButton(turnOn: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(turnOn)
            supportActionBar!!.setHomeButtonEnabled(turnOn)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }



}
