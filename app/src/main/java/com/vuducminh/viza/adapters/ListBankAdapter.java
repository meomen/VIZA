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

import com.squareup.picasso.Picasso;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.RemoveAccent;

import java.util.ArrayList;



public class ListBankAdapter extends RecyclerView.Adapter<ListBankAdapter.RecyclerViewHolder> implements Filterable {
    private Context context;
    private ArrayList<CardObject> listBankOriginal;
    private ArrayList<CardObject> listBankDisplayed;

    private PositionClickListener listener;
    private int selectedPos = 0;

    public ListBankAdapter(Context context, ArrayList<CardObject> listBank, PositionClickListener listener) {
        this.context = context;
        this.listBankOriginal = listBank;
        this.listBankDisplayed = listBank;
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
        Picasso.get().load(Constant.IMAGE_CARD_URL + listBankDisplayed.get(position).getImage()).into(holder.imgBank);
        holder.textName.setText(listBankDisplayed.get(position).getBankName());

        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return listBankDisplayed.size();
    }

    public CardObject getItem(int position) {
        return listBankDisplayed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface PositionClickListener {
        void itemClicked(int position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgBank;
        public TextView textName;
        public TextView textInfo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imgBank = (ImageView) itemView.findViewById(R.id.img_bank);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textInfo = (TextView) itemView.findViewById(R.id.text_info);

            textInfo.setVisibility(View.GONE);

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
                ArrayList<CardObject> filteredArrList = new ArrayList<>();

                if (constraint == null) {
                    filteredArrList.addAll(listBankOriginal);
                } else {
                    constraint = RemoveAccent.removeAccent(constraint.toString().toLowerCase());
                    for (int i = 0; i < listBankOriginal.size(); i++) {
                        String name = listBankOriginal.get(i).getBankName();
                        name = RemoveAccent.removeAccent(name.toString().toLowerCase());
                        if (name.indexOf(constraint.toString()) > -1) {
                            filteredArrList.add(listBankOriginal.get(i));
                        }
                    }
                }

                results.values = filteredArrList;
                results.count = filteredArrList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listBankDisplayed = (ArrayList<CardObject>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
