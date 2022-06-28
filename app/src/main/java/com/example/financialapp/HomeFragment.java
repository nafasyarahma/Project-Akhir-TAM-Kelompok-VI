package com.example.financialapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private Button btnGetStarted;

    private View.OnClickListener btnGetStartedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = new WalletFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnGetStarted = (Button) getView().findViewById(R.id.home_btn_getstarted);

        btnGetStarted.setOnClickListener(btnGetStartedListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
