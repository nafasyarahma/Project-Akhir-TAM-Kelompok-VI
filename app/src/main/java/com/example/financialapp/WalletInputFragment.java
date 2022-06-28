package com.example.financialapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WalletInputFragment extends Fragment{
    private Spinner spinnerType;
    private Spinner spinnerCategory;

    private Button btnDate;
    private Button btnSave;

    private TextView textPrice;
    private TextView textDate;
    private TextView textDesc;

    private int typeId;
    private int categoryId;

    private DialogFragment dateDialogFragment;

    private AdapterView.OnItemSelectedListener spinnerTypeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            typeId = i;

            RefreshCategory();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener spinnerCategoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            categoryId = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private View.OnClickListener btnDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dateDialogFragment.show(getActivity().getSupportFragmentManager(), "date picker");
        }
    };

    private View.OnClickListener btnSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SaveData();

            Fragment fragment = new WalletFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    };

    private TextWatcher textPriceListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().isEmpty())
                btnSave.setEnabled(false);
            else
                btnSave.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateDialogFragment = new DatePickerFragment();

        spinnerType = (Spinner) getView().findViewById(R.id.input_spinner_type);
        spinnerCategory = (Spinner) getView().findViewById(R.id.input_spinner_category);
        btnDate = (Button) getView().findViewById(R.id.input_btn_date);
        btnSave = (Button) getView().findViewById(R.id.input_btn_save);
        textPrice = (TextView) getView().findViewById(R.id.input_text_price);
        textDate = (TextView) getView().findViewById(R.id.input_text_date);
        textDesc = (TextView) getView().findViewById(R.id.input_text_desc);

        spinnerType.setOnItemSelectedListener(spinnerTypeListener);
        spinnerCategory.setOnItemSelectedListener(spinnerCategoryListener);
        btnDate.setOnClickListener(btnDateListener);
        btnSave.setOnClickListener(btnSaveListener);
        textPrice.addTextChangedListener(textPriceListener);

        btnSave.setEnabled(false);

        textDate.setText(new SimpleDateFormat("MMMM d, yyyy").format(Calendar.getInstance().getTime()));
    }

    private void RefreshCategory(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), typeId == 0 ? R.array.Category_Pengeluaran : R.array.Category_Pemasukan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(spinnerCategoryListener);
    }

    private void SaveData(){
        Gson gson = new Gson();
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("Wallet Data", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<ItemWalletDetail> listItem;

        String json = sharedPreferences.getString("Saved Wallet Data", null);
        listItem = gson.fromJson(json, new TypeToken<ArrayList<ItemWalletDetail>>(){}.getType());

        if(listItem == null)
            listItem = new ArrayList<>();

        Date date = null;
        try {
            date = new SimpleDateFormat("MMMM d, yyyy").parse(textDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String desc = textDesc.getText().toString();
        int price = Integer.parseInt(textPrice.getText().toString());

        if(desc == "")
            desc = " ";

        listItem.add(new ItemWalletDetail(
                date,
                ItemWalletDetail.EnumType.valueOf(spinnerType.getSelectedItem().toString()),
                ItemWalletDetail.EnumCategory.valueOf(spinnerCategory.getSelectedItem().toString()),
                desc,
                price));

        json = gson.toJson(listItem);
        editor.putString("Saved Wallet Data", json);
        editor.apply();
    }
}
