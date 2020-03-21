package com.vuducminh.viza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.utils.Constant;

import java.util.ArrayList;



public class ListCardVerticalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CardObject> listCard;

    public ListCardVerticalAdapter(Context context, ArrayList<CardObject> listCard) {
        this.context = context;
        this.listCard = listCard;
    }

    @Override
    public int getCount() {
        return listCard.size();
    }

    @Override
    public Object getItem(int position) {
        return listCard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_card_vertical, parent, false);
        }

        ImageView cardImg = (ImageView) convertView.findViewById(R.id.card_img);
        TextView cardName = (TextView) convertView.findViewById(R.id.card_name);

        Picasso.get().load(Constant.IMAGE_CARD_URL + listCard.get(position).getImage()).into(cardImg);
        cardName.setText(listCard.get(position).getBankName());

        return convertView;
    }
}
