package com.example.jinghui.ui;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by zhaoxin on 2017-05-15.
 */

public class FragmentManagerMyself {
    private static FragmentManagerMyself fragmentManagerMyself;
    private List<Fragment> fragments;

    public void set(List<Fragment> fragments) {

        this.fragments = fragments;
    }

    public List<Fragment> get() {
        return fragments;
    }

    public FragmentManagerMyself() {

    }

    public static FragmentManagerMyself getInstance() {
        if (fragmentManagerMyself == null) {
            fragmentManagerMyself = new FragmentManagerMyself();
        }
        return fragmentManagerMyself;
    }
}
