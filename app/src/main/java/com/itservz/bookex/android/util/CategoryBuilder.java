package com.itservz.bookex.android.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itservz.bookex.android.BookListActivity;
import com.itservz.bookex.android.DrawerActivity;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.view.FlowLayout;

import java.util.Random;

/**
 * Created by Raju on 1/16/2017.
 */

public class CategoryBuilder {

    private final TypedArray colors;
    private Activity activity;
    private int strokeWidth;
    private Random random;
    public CategoryBuilder(Activity activity){
        this.activity = activity;
        colors = activity.getResources().obtainTypedArray(R.array.category_colors);
        strokeWidth = new ScreenSizeScaler(activity.getResources()).getdpAspixel(1);
        random =  new Random();
    }

    public void addCategories(FlowLayout flowLayout, final String text) {
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layout);
        ScreenSizeScaler screenSizeScaler = new ScreenSizeScaler(activity.getResources());
        int px = screenSizeScaler.getdpAspixel(8);
        linearLayout.setPadding(px, px, px, px);

        final TextView textView = new TextView(activity);
        textView.setLayoutParams(layout);
        GradientDrawable drawable = (GradientDrawable) activity.getResources().getDrawable(R.drawable.rounded_border);
        int color = colors.getColor(random.nextInt(colors.length()), activity.getResources().getColor(R.color.colorPrimaryDark));
        drawable.setStroke(strokeWidth, color);
        textView.setBackground(drawable);
        textView.setPadding(px, px, px, px);
        textView.setTextColor(color);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BookListActivity.class);
                intent.putExtra(BundleKeys.CATEGORY.name(), text);
                activity.startActivity(intent);
            }
        });

        linearLayout.addView(textView);
        flowLayout.addView(linearLayout);
    }

    public void expandOrCollapseCategories(final View v, String exp_or_colpse) {
        TranslateAnimation anim = null;
        if ("expand".equals(exp_or_colpse)) {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        } else {
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(v.getHeight() - 96f) );
            Animation.AnimationListener collapselistener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };
            anim.setAnimationListener(collapselistener);
        }
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    private int pickColor(String key) {
        // String.hashCode() is not supposed to change across java versions, so
        // this should guarantee the same key always maps to the same color
        final int color = Math.abs(key.hashCode()) % 8;
        try {
            return colors.getColor(color, Color.BLACK);
        } finally {
            colors.recycle();
        }
    }
}
