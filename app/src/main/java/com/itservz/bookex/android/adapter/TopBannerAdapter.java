package com.itservz.bookex.android.adapter;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.backend.FirebaseStorageService;

/**
 * Created by Raju on 12/7/2016.
 */


public class TopBannerAdapter extends PagerAdapter {
    private final Resources resources;
    private final int[] layouts;
    private LayoutInflater layoutInflater;


    public TopBannerAdapter(LayoutInflater layoutInflater, Resources resources) {
        this.layoutInflater = layoutInflater;
        this.resources = resources;
        layouts = new int[]{
                R.layout.top_slide,
                R.layout.top_slide,
                R.layout.top_slide};
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = layoutInflater.inflate(R.layout.top_slide, container, false);
        final ImageView iv = (ImageView) view.findViewById(R.id.topSlide);
        if (position == 0) {
            view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.bg_screen1, null));
            FirebaseStorageService.getInstance().getImage("discount.png").addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        } else if (position == 1) {
            view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.bg_screen2, null));
            FirebaseStorageService.getInstance().getImage("food.png").addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        } else if (position == 2) {
            view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.bg_screen3, null));
            FirebaseStorageService.getInstance().getImage("movie.png").addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

