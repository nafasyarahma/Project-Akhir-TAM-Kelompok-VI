package com.example.financialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    public static ProfileFragment Instance;

    private Button btnEmail;
    private Button btnFacebook;
    private Button btnTwitter;

    private EditText editName;
    private EditText editEmail;
    private EditText editFacebook;
    private EditText editTwitter;

    private View.OnClickListener btnEmailListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OpenBrowser(editEmail.getText().toString());
        }
    };

    private View.OnClickListener btnFacebookListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OpenBrowser(editFacebook.getText().toString());
        }
    };

    private View.OnClickListener btnTwitterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OpenBrowser(editTwitter.getText().toString());
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnEmail = (Button) getView().findViewById(R.id.profile_btn_email);
        btnFacebook = (Button) getView().findViewById(R.id.profile_btn_facebook);
        btnTwitter = (Button) getView().findViewById(R.id.profile_btn_twitter);
        editName = (EditText) getView().findViewById(R.id.profile_edit_name);
        editEmail = (EditText) getView().findViewById(R.id.profile_edit_email);
        editFacebook = (EditText) getView().findViewById(R.id.profile_edit_facebook);
        editTwitter = (EditText) getView().findViewById(R.id.profile_edit_twitter);

        LoadData();

        btnEmail.setOnClickListener(btnEmailListener);
        btnFacebook.setOnClickListener(btnFacebookListener);
        btnTwitter.setOnClickListener(btnTwitterListener);

        Instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void OpenBrowser(String _url){
        if(_url.isEmpty())
            return;

        Uri uri = Uri.parse(_url);

        if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
            uri = Uri.parse("http://" + _url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }

    public void SaveData(){
        ArrayList<String> listItem = new ArrayList<>();
        Gson gson = new Gson();
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("Profile Data", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String facebook = editFacebook.getText().toString();
        String twitter = editTwitter.getText().toString();

        name = name.isEmpty() ? null : name;
        email = email.isEmpty() ? null : email;
        facebook = facebook.isEmpty() ? null : facebook;
        twitter = twitter.isEmpty() ? null : twitter;

        listItem.add(name);
        listItem.add(email);
        listItem.add(facebook);
        listItem.add(twitter);

        String json = gson.toJson(listItem);
        editor.putString("Saved Profile Data", json);
        editor.apply();
    }

    private void LoadData(){
        ArrayList<String> listItem;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile Data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Saved Profile Data", null);
        listItem = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());

        if(listItem != null) {
            editName.setText(listItem.get(0));
            editEmail.setText(listItem.get(1));
            editFacebook.setText(listItem.get(2));
            editTwitter.setText(listItem.get(3));
        }
    }
}
