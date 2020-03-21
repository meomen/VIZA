package com.vuducminh.viza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.vuducminh.viza.R;
import com.vuducminh.viza.models.PaySaveObject;
import com.vuducminh.viza.utils.RemoveAccent;

import java.util.ArrayList;


public class ListPaySaveAdapter extends RecyclerView.Adapter<ListPaySaveAdapter.RecyclerViewHolder> implements Filterable {
    private Context context;
    private ArrayList<PaySaveObject> listPaySaveOriginal;
    private ArrayList<PaySaveObject> listPaySaveDisplayed;

    private PositionClickListener listener;
    private int selectedPos = 0;

    public ListPaySaveAdapter(Context context, ArrayList<PaySaveObject> listPaySave, PositionClickListener listener) {
        this.context = context;
        this.listPaySaveOriginal = listPaySave;
        this.listPaySaveDisplayed = listPaySave;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_pay_save, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textName.setText(listPaySaveDisplayed.get(position).getFullname());
        holder.textInfo.setText(listPaySaveDisplayed.get(position).getCardnumber());

        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return listPaySaveDisplayed.size();
    }

    public PaySaveObject getItem(int position) {
        return listPaySaveDisplayed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface PositionClickListener {
        void itemClicked(int position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textName;
        public TextView textInfo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textInfo = (TextView) itemView.findViewById(R.id.text_info);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.itemClicked(getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<PaySaveObject> filteredArrList = new ArrayList<>();

                if (constraint == null) {
                    filteredArrList.addAll(listPaySaveOriginal);
                } else {
                    constraint = RemoveAccent.removeAccent(constraint.toString().toLowerCase());
                    for (int i = 0; i < listPaySaveOriginal.size(); i++) {
                        String name = listPaySaveOriginal.get(i).getCardnumber();
                        name = RemoveAccent.removeAccent(name.toString().toLowerCase());
                        if (name.indexOf(constraint.toString()) > -1) {
                            filteredArrList.add(listPaySaveOriginal.get(i));
                        }
                    }
                }

                results.values = filteredArrList;
                results.count = filteredArrList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listPaySaveDisplayed = (ArrayList<PaySaveObject>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
