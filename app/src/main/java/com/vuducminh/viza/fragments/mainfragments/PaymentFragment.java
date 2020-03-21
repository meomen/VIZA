package com.vuducminh.viza.fragments.mainfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuducminh.viza.MyApplication;
import com.vuducminh.viza.R;
import com.vuducminh.viza.activities.BuyGameCardActivity;
import com.vuducminh.viza.activities.BuyPhoneCardActivity;
import com.vuducminh.viza.activities.DepositActivity;
import com.vuducminh.viza.activities.DepositCardActivity;
import com.vuducminh.viza.activities.PhoneRechargeActivity;
import com.vuducminh.viza.activities.TraGopActivity;
import com.vuducminh.viza.activities.WithdrawActivity;
import com.vuducminh.viza.adapters.ListFunctionAdapter;
import com.vuducminh.viza.fragments.BaseFragment;
import com.vuducminh.viza.models.MenuObject;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.GridSpacingItemDecoration;

import java.util.ArrayList;

/**
 * Created by Linh Lee on 4/9/2017.
 */
public class PaymentFragment extends BaseFragment {
    private RecyclerView listPaymentView;
    private ListFunctionAdapter adapter;
    private ArrayList<MenuObject> listPayment;
    private SharedPreferences sharedPreferences;

    public static PaymentFragment newInstance() {

        Bundle args = new Bundle();

        PaymentFragment fragment = new PaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_payment;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        sharedPreferences = MyApplication.getSharedPreferences();

        listPaymentView = (RecyclerView) rootView.findViewById(R.id.list_payment);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listPayment = new ArrayList<>();
        listPayment.add(new MenuObject(R.drawable.ic_transfer_money_state, getActivity().getResources().getString(R.string.chuyen_tien)));
        listPayment.add(new MenuObject(R.drawable.ic_nap_phone_card_state, getActivity().getResources().getString(R.string.nap_dien_thoai)));
        listPayment.add(new MenuObject(R.drawable.ic_mua_phone_card_state, getActivity().getResources().getString(R.string.mua_the_dien_thoai)));
        listPayment.add(new MenuObject(R.drawable.ic_mua_the_game_state, getActivity().getResources().getString(R.string.mua_the_game)));
        listPayment.add(new MenuObject(R.drawable.ic_doi_the_cao_state, getActivity().getResources().getString(R.string.doi_the_cao)));
        listPayment.add(new MenuObject(R.drawable.ic_deposit_state, getActivity().getResources().getString(R.string.nap_tien)));
        listPayment.add(new MenuObject(R.drawable.ic_withdraw_state, getActivity().getResources().getString(R.string.rut_tien)));
        listPayment.add(new MenuObject(R.drawable.ic_diem_thanh_toan_state, getActivity().getResources().getString(R.string.thanh_toan_hoa_don)));


        adapter = new ListFunctionAdapter(getActivity(), listPayment, R.layout.item_function_payment, new ListFunctionAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(Constant.GOTO_TRANSFER);
                        getActivity().sendBroadcast(i);
                        //Chuyen tien
                        break;
                    case 1:
                        startActivity(PhoneRechargeActivity.class);
                        //Nap tien dt
                        break;
                    case 2:
                        startActivity(BuyPhoneCardActivity.class);
                        //Mua the dt
                        break;
                    case 3:
                        startActivity(BuyGameCardActivity.class);
                        //Mua the game
                        break;
                    case 4:
                        startActivity(DepositCardActivity.class);
                        //Doi the cao thanh tien mat
                        break;
                    case 5:
                        startActivity(DepositActivity.class);
                        //Nap tien
                        break;
                    case 6:
                        startActivity(WithdrawActivity.class);
                        //Rut tien
                        break;
                    case 7:
                        startActivity(TraGopActivity.class);
                        //Diem thanh toan tra gop
                        break;
                }
            }
        });
        adapter.setHasStableIds(true);

        listPaymentView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        listPaymentView.addItemDecoration(new GridSpacingItemDecoration(3, Constant.convertDpIntoPixels(40, getActivity()), false));
        listPaymentView.setAdapter(adapter);
    }
}
