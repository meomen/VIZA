package com.vuducminh.viza.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Created by Linh Lee on 4/9/2017.
 */
public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutResource();

    protected abstract void initVariables(Bundle savedInstanceState, View rootView);

    protected abstract void initData(Bundle savedInstanceState);

    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResource(), null, false);
        initVariables(savedInstanceState, rootView);
        initData(savedInstanceState);
        return rootView;
    }

    /**
     * Start Activity with Bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Start Activity without Bundle
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }
}
