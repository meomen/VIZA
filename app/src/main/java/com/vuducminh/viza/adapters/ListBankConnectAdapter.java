package com.vuducminh.viza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.CardObject;
import com.vuducminh.viza.utils.Constant;
import com.vuducminh.viza.utils.GlideApp;

import java.util.ArrayList;

public class ListBankConnectAdapter extends RecyclerView.Adapter<ListBankConnectAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<CardObject> listCard;

    private PositionClickListener listener;
    private int selectedPos = 0;


    public ListBankConnectAdapter(Context context, ArrayList<CardObject> listCard, PositionClickListener listener) {
        this.context = context;
        this.listCard = listCard;
        this.listener = listener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_card, parent, false);
        return new ListBankConnectAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Picasso.get().load(listCard.get(position).getImage()).into(holder.cardImg);
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference(listCard.get(position).getImage());
//        GlideApp.with(context)
//                .load(storageReference)
//                .into(holder.cardImg);

        holder.textChietKhau.setText("");

        holder.itemView.setSelected(selectedPos == position);

        if (selectedPos == position) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listCard.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface PositionClickListener {
        void itemClicked(int position);
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cardImg;
        public TextView textChietKhau;
        public View border;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cardImg = (ImageView) itemView.findViewById(R.id.card_img);
            textChietKhau = (TextView) itemView.findViewById(R.id.text_chiet_khau);
            border = itemView.findViewById(R.id.border);

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
}
