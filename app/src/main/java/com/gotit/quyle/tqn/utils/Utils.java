package com.gotit.quyle.tqn.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.app.TQNApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by QUYLE on 1/9/18.
 */

public class Utils {

    private static final String MIME_TYPE_SHARE = "text/plain";


    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) TQNApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivitymanager.getActiveNetworkInfo() != null
                && connectivitymanager.getActiveNetworkInfo().isAvailable()
                && connectivitymanager.getActiveNetworkInfo().isConnected();
    }

    public static void bindImage(String url, ImageView target, boolean centerCrop) {
        Drawable drawable = ContextCompat.getDrawable(target.getContext(), R.drawable.ic_image_24dp);
        DrawableRequestBuilder<String> builder = Glide.with(TQNApplication.getContext())
                .load(url)
                .error(R.drawable.ic_broken_image_24dp)
                .placeholder(drawable)
                .crossFade();
        if (centerCrop) builder.centerCrop();
        builder.into(target);
    }

    public static void setVectorBg(View target, @DrawableRes int drRes, @ColorRes int normalRes,
                                   @ColorRes int pressedRes) {
        int[][] states = new int[][]{
                new int[]{
                        android.R.attr.state_pressed},
                new int[]{

                }
        };
        int[] colors = new int[]{
                ContextCompat.getColor(target.getContext(), pressedRes),
                ContextCompat.getColor(target.getContext(), normalRes)
        };
        ColorStateList cl = new ColorStateList(states, colors);

        // if you pass application as context is throw exception: Resources$NotFoundException:
        // File res/drawable/ic_close_24dp.xml from drawable resource ID #0x7f02005c
        Drawable drawable = ContextCompat.getDrawable(target.getContext(), drRes);
        Drawable wrapped = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrapped, cl);
        target.setBackground(wrapped);
    }

    public static Intent createShareIntent(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType(MIME_TYPE_SHARE)
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(intent, TQNApplication.getContext().getString(R.string.chooserTitle));
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static Snackbar createSnackbar(Activity activity, @StringRes int resId) {
        View root = activity.findViewById(android.R.id.content);
        return Snackbar.make(root, resId, Snackbar.LENGTH_LONG);
    }

    public static int getNumberDay() {
        long oneDay = 1000 * 60 * 60 * 24;
        long oneHour = 1000 * 60 * 60;
        long oneMinute = 1000 * 60;
        long oneSecond = 1000;

        Calendar myStartDateDay = Calendar.getInstance();
        myStartDateDay.set(2016, Calendar.DECEMBER, 14, 22, 15, 15);
        float time1 = getDateDiffString(myStartDateDay.getTime());
        float time2 = time1 / oneDay;
        return ((int) time2);
    }

    public static float getDateDiffString(Date dateOne) {
        long timeOne = dateOne.getTime();
        long timeTwo = Calendar.getInstance().getTime().getTime();

        long delta = (timeTwo - timeOne);
        return delta;
    }

    public static String getListQuite(Context context) {
        String[] quotes = context.getResources().getStringArray(R.array.quote);
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        return quotes[index];

    }
}
