package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Frag_1 extends Fragment {

    public static Frag_1 newInstance() {

        Bundle args = new Bundle();
        Frag_1 frag_1 = new Frag_1();
        frag_1.setArguments(args);
        return frag_1;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_1, container, false);

        return view;
    }
}
