package com.example.financialapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WalletFragment extends Fragment {
    private ArrayList<ItemWalletDetail> listItem;

    private Button btnWalletInput;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private  TextView textGraphDate;
    private TextView textGraphBalance;

    private ItemWalletRecyclerAdapter itemWalletAdapter;

    private View.OnClickListener btnWalletInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = new WalletInputFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadData();
        //DebugWalletItem();

        btnWalletInput = (Button) getView().findViewById(R.id.btn_wallet_input);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        textGraphDate = (TextView) getView().findViewById(R.id.text_graph_date);
        textGraphBalance = (TextView) getView().findViewById(R.id.text_graph_balance_value);

        itemWalletAdapter = new ItemWalletRecyclerAdapter(listItem);
        itemWalletAdapter.RefreshWallet();
        SetWalletAdapter();

        textGraphDate.setText(new SimpleDateFormat("MMMM").format(new Date()));

        SetBalance();
        SetProgressBar();

        btnWalletInput.setOnClickListener(btnWalletInputListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    private void SetWalletAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemWalletAdapter);
    }

    private void SetBalance(){
        int balance = itemWalletAdapter.GetBalance();
        String value = "";

        if(balance < 0)
            value += "- ";

        value += "Rp" + Math.abs(balance);

        textGraphBalance.setText(value);
    }

    private void SetProgressBar(){
        final Handler handler = new Handler();
        progressBar.setMax(itemWalletAdapter.GetIncome());

        handler.postDelayed(new Runnable() {
            int i = 0;
            int max = itemWalletAdapter.GetExpense();
            int increment = itemWalletAdapter.GetIncome() / 50;

            @Override
            public void run() {
                if(max < 0)
                    max = 100;

                if(i <= max){
                    progressBar.setProgress(i);
                    i += increment;

                    handler.postDelayed(this, 50);
                }else {
                    handler.removeCallbacks(this);
                }
            }
        }, 50);
    }

    private void LoadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Wallet Data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Saved Wallet Data", null);
        listItem = gson.fromJson(json, new TypeToken<ArrayList<ItemWalletDetail>>(){}.getType());

        if(listItem == null)
            listItem = new ArrayList<>();
    }
}
