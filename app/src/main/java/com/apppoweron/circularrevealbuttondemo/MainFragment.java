package com.apppoweron.circularrevealbuttondemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apppoweron.circularrevealbutton.AnimatedLoadingButton;
import com.apppoweron.circularrevealbutton.container.CircularRevealContainerNotFoundException;

public class MainFragment extends BaseFragment {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnimatedLoadingButton standard1 = view.findViewById(R.id.main_standard_1_btn);
        AnimatedLoadingButton standard2 = view.findViewById(R.id.main_standard_2_btn);
        AnimatedLoadingButton animated1 = view.findViewById(R.id.main_animated_1_btn);
        AnimatedLoadingButton animated2 = view.findViewById(R.id.main_animated_2_btn);

        standard1.setOnClickListener(getOnClickListener(standard1));
        standard2.setOnClickListener(getOnClickListener(standard2));
        animated1.setOnClickListener(getOnClickListener(animated1));
        animated2.setOnClickListener(getOnClickListener(animated2));
    }

    private View.OnClickListener getOnClickListener(AnimatedLoadingButton button) {
        return view -> {
            Toast.makeText(view.getContext(), "onclick - " + button.getText(), Toast.LENGTH_LONG).show();
            view.postDelayed(() -> {

                try {
                    button.startProgressEndAnimation(R.id.main_circular_reveal_container, viewId -> {
                        Toast.makeText(view.getContext(), "onAnimEnd - " + button.getText(), Toast.LENGTH_LONG).show();

                        if (button.getId() != R.id.main_standard_1_btn && button.getId() != R.id.main_standard_2_btn) {
                            try {
                                mFragmentCommunicator.onNewFragmentSelected(SecondPageFragment.newInstance(), true, true);
                            } catch (NoContainerException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (CircularRevealContainerNotFoundException e) {
                    e.printStackTrace();
                }

            }, 6000);
        };
    }
}
