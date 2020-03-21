package com.vuducminh.viza.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.vuducminh.viza.R;
import com.vuducminh.viza.models.BannerObject;
import com.vuducminh.viza.utils.Constant;

import java.util.ArrayList;


public class HomePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<BannerObject> listImg;
    private LayoutInflater inflater;

    public HomePagerAdapter(Context context, ArrayList<BannerObject> listImg) {
        this.context = context;
        this.listImg = listImg;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.item_home_pager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);
        Picasso.get().load(Constant.IMAGE_BANNER_URL + listImg.get(position).getFilename()).into(imageView);
        //imageView.setImageResource(listImg.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return listImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
