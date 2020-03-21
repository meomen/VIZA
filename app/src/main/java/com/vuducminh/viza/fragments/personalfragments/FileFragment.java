package com.vuducminh.viza.fragments.personalfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vuducminh.viza.R;
import com.vuducminh.viza.fragments.BaseFragment;
import com.vuducminh.viza.utils.Constant;
/**
 * Created by Linh Lee on 4/9/2017.
 */
public class FileFragment extends BaseFragment {
    private BroadcastReceiver changeReceiver;

    public static FileFragment newInstance() {

        Bundle args = new Bundle();

        FileFragment fragment = new FileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_file;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        changeFragment(FileDisplayFragment.newInstance(), R.id.main_content);

        changeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int command = extras.getInt("command");
                    if (command == 0) {
                        changeFragment(FileUpdateFragment.newInstance(), R.id.main_content);
                    } else {
                        //changeFragment(FileDisplayFragment.newInstance(), R.id.main_content);
                        getChildFragmentManager().popBackStack();
                    }
                }
            }
        };
        getActivity().registerReceiver(changeReceiver, new IntentFilter(Constant.CHANGE_FILE_FRAGMENT));
    }

    protected  void changeFragment(Fragment fragment, int layoutId){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.enter_anim_ltr, R.anim.exit_anim_ltr, R.anim.enter_anim_rtl, R.anim.exit_anim_rtl);
        ft.replace(layoutId, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (changeReceiver != null) {
            getActivity().unregisterReceiver(changeReceiver);
        }
    }
}
