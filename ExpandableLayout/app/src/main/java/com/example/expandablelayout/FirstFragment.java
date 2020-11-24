package com.example.expandablelayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

public class FirstFragment extends Fragment implements View.OnClickListener {

    private ExpandableLayout expandableLayout0;
    private TextView textView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableLayout0 = view.findViewById(R.id.expandable_layout_0);
        textView = view.findViewById(R.id.expand_button_0);
        textView.setOnClickListener(this);

        expandableLayout0.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.e("ExpandableLayout0", "State: " + state);
                if (state >= 0) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_keyboard_arrow_up_black_24dp),
                            null);
                }if (state < 3){
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_keyboard_arrow_down_black_24dp),
                            null);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.expand_button_0) {
            expandableLayout0.toggle();
        }
    }
}
