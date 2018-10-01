package com.apppoweron.circularrevealbuttondemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.apppoweron.circularrevealbutton.AnimatedLoadingButton
import com.apppoweron.circularrevealbutton.container.CircularRevealContainerNotFoundException
import com.apppoweron.circularrevealbuttondemo.common.BaseFragment
import com.apppoweron.circularrevealbuttondemo.common.NoContainerException
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val standard1 = view.findViewById<AnimatedLoadingButton>(R.id.main_standard_1_btn)
        val standard2 = view.findViewById<AnimatedLoadingButton>(R.id.main_standard_2_btn)
        val animated1 = view.findViewById<AnimatedLoadingButton>(R.id.main_animated_1_btn)
        val animated2 = view.findViewById<AnimatedLoadingButton>(R.id.main_animated_2_btn)


        /*Completable.complete()
                .delay(3,TimeUnit.SECONDS)
                .doOnComplete(() -> System.out.println("Time to complete " + (System.currentTimeMillis() - start)))
        .subscribe();*/
        Completable.complete()
                .doOnSubscribe{}
                .delay(3, TimeUnit.SECONDS)
                .doOnComplete {  }
                /*.doOnComplete(() -> run {})*/.subscribe()

        val hybridUsage = view.findViewById<AnimatedLoadingButton>(R.id.main_hybrid_btn)
        hybridUsage.setOnClickListener { view1 ->
            view.postDelayed({
                try {
                    val button = view1 as AnimatedLoadingButton
                    button.startProgressEndAnimation(R.id.main_circular_reveal_container) {
                        Toast.makeText(view.context, "onAnimEnd - " + button.text, Toast.LENGTH_LONG).show()

                        if (button.isCircularRevealEnabled) {
                            navigateToSecondPage()
                        } else {
                            button.isCircularRevealEnabled = true
                            button.setText(R.string.tap_to_start_circular_reveal_animation)
                        }
                    }
                } catch (e: CircularRevealContainerNotFoundException) {
                    e.printStackTrace()
                }
            }, 6000)
        }

        standard1.setOnClickListener(getOnClickListener(standard1))
        standard2.setOnClickListener(getOnClickListener(standard2))
        animated1.setOnClickListener(getOnClickListener(animated1))
        animated2.setOnClickListener(getOnClickListener(animated2))
    }

    private fun getOnClickListener(button: AnimatedLoadingButton): View.OnClickListener {
        return View.OnClickListener { view ->
            Toast.makeText(view.context, "onclick - " + button.text, Toast.LENGTH_LONG).show()
            view.postDelayed({
                try {
                    button.startProgressEndAnimation(R.id.main_circular_reveal_container) {
                        Toast.makeText(view.context, "onAnimEnd - " + button.text, Toast.LENGTH_LONG).show()

                        if (it != R.id.main_standard_1_btn && it != R.id.main_standard_2_btn) {
                            navigateToSecondPage()
                        }
                    }
                } catch (e: CircularRevealContainerNotFoundException) {
                    e.printStackTrace()
                }
            }, 6000)
        }
    }

    private fun navigateToSecondPage() {
        try {
            mFragmentCommunicator.onNewFragmentSelected(SecondPageFragment.newInstance(), true, true)
        } catch (e: NoContainerException) {
            e.printStackTrace()
        }
    }

}
