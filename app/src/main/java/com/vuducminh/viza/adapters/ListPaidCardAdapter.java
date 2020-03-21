package com.vuducminh.viza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vuducminh.viza.R;
import com.vuducminh.viza.models.TransactionObject;

import java.util.ArrayList;



public class ListPaidCardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TransactionObject> listTrans;

    public ListPaidCardAdapter(Context context, ArrayList<TransactionObject> listTrans) {
        this.context = context;
        this.listTrans = listTrans;
    }

    @Override
    public int getCount() {
        return listTrans.size();
    }

    @Override
    public Object getItem(int position) {
        return listTrans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_paid_card, parent, false);
        }

        WebView webView = (WebView) convertView.findViewById(R.id.web_view);
        TextView textTime = (TextView) convertView.findViewById(R.id.text_time);

        webView.loadDataWithBaseURL(null, listTrans.get(position).getContent(), "text/html", "utf-8", null);
        textTime.setText(listTrans.get(position).getTransactionDate());

        return convertView;
    }
}
