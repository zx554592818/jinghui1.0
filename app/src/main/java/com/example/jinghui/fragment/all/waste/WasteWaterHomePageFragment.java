package com.example.jinghui.fragment.all.waste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinghui.R;

/**
 * Created by zhaoxin on 2017-05-19.
 */

public class WasteWaterHomePageFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_extra,null,false);
        return view;
    }
}
