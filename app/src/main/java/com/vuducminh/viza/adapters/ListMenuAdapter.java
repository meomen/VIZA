package com.vuducminh.viza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vuducminh.viza.R;
import com.vuducminh.viza.models.MenuObject;

import java.util.ArrayList;

/**
 * Created by Linh Lee on 4/6/2017.
 */
public class ListMenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MenuObject> listMenu;

    public ListMenuAdapter(Context context, ArrayList<MenuObject> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
    }

    @Override
    public int getCount() {
        return listMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return listMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_menu, parent, false);
        }

        ImageView menuIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        TextView menuName = (TextView) convertView.findViewById(R.id.menu_name);

        menuIcon.setImageResource(listMenu.get(position).getImgRes());
        menuName.setText(listMenu.get(position).getName());

        return convertView;
    }
}
