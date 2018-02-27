package com.gotit.quyle.tqn.fragment;

import android.app.WallpaperManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.adapter.PhotoCard;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.model.Photo;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;
import com.gotit.quyle.tqn.utils.Utils;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by QUYLE on 1/9/18.
 */

public class WallPaperFragment extends Fragment {
    TextView tvday, tvhour, tvminute, tvsecond;
    View viewSetWp;
    int day, minute, hour, second;
    int indexbg = 1;

    int dembg = 27;
    Timer timer;
    TimerTask timerTask;
    Random random;
    private Typeface font_number, font_text;
    Handler handler = new Handler();
    int[] listBg = {
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4,
            R.drawable.bg5,
            R.drawable.bg6,
            R.drawable.bg7,
            R.drawable.bg8,
            R.drawable.bg9,
            R.drawable.bg10,
            R.drawable.bg11,
            R.drawable.bg12,
            R.drawable.bg13,
            R.drawable.bg14,
            R.drawable.bg15
    };
    private SwipePlaceHolderView mSwipeView;
    private List<Photo> photoList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);


        tvday = (TextView) view.findViewById(R.id.day);
        tvhour = (TextView) view.findViewById(R.id.hour);
        tvminute = (TextView) view.findViewById(R.id.minute);
        tvsecond = (TextView) view.findViewById(R.id.second);
        mSwipeView = (SwipePlaceHolderView) view.findViewById(R.id.swipeView);

        setupSwipeView();


        font_number = Typeface.createFromAsset(getContext().getAssets(), "mylove2.ttf");
        font_text = Typeface.createFromAsset(getContext().getAssets(), "mylove.ttf");
        tvday.setTypeface(font_number);
        tvhour.setTypeface(font_text);
        tvsecond.setTypeface(font_text);
        tvminute.setTypeface(font_text);

        viewSetWp = view.findViewById(R.id.img_setwp);
        viewSetWp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper();
            }
        });

        random = new Random();

        return view;
    }

    private void setupSwipeView() {
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_msg_view));

        prepareAlbums();
        for (Photo album : photoList) {
            mSwipeView.addView(new PhotoCard(getContext(), album, mSwipeView));
        }
    }

    private void prepareAlbums() {
        photoList = new ArrayList<>();
        List<PhotoModel> localPhotos = TQNApplication.getPrefManager().getPhotos(72157689675511292L);
        for (int i = 0; i < localPhotos.size(); i++) {
            photoList.add(new Photo(localPhotos.get(i)));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        dembg = 0;
        updateFirstView();
        startTimer();
    }


    @Override
    public void onPause() {
        super.onPause();
        stoptimertask();
    }


    public void setWallpaper() {
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            WallpaperManager wm = WallpaperManager.getInstance(getContext());
            wm.setResource(listBg[indexbg]);
            Toast.makeText(getContext(),
                    "successful",
                    Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "error",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        second++;
                        dembg++;
                        updateUI();


                    }
                });
            }
        };
    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void updateFirstView() {
        long oneDay = 1000 * 60 * 60 * 24;
        long oneHour = 1000 * 60 * 60;
        long oneMinute = 1000 * 60;
        long oneSecond = 1000;

        Calendar myStartDateDay = Calendar.getInstance();
        myStartDateDay.set(2016, Calendar.DECEMBER, 14, 22, 15, 15);
        float time1 = Utils.getDateDiffString(myStartDateDay.getTime());
        float time2 = time1 / oneDay;
        day = (int) time2;

        float time3 = (time1 - day * oneDay) / oneHour;
        hour = (int) time3;

        float time4 = (time1 - day * oneDay - hour * oneHour) / oneMinute;
        minute = (int) time4;

        float time5 = (time1 - day * oneDay - hour * oneHour - minute * oneMinute) / oneSecond;
        second = (int) time5;

        updateUI();

    }

    public void updateUI() {
        tvday.setText(day + " days together");

        if (second == 60) {
            second = 0;
            minute++;
        }
        if (minute == 60) {
            minute = 0;
            hour++;

        }
        if (hour == 24) {
            hour = 0;
            day++;

        }
        if (dembg == 10) {
            viewSetWp.setVisibility(View.GONE);
        }
        if (dembg == 15) {
            mSwipeView.doSwipe(true);
            dembg = 0;
            viewSetWp.setVisibility(View.VISIBLE);
        }
        if (hour < 10) {
            tvhour.setText("0" + hour + ":");
        } else {
            tvhour.setText(hour + ":");
        }
        if (minute < 10) {
            tvminute.setText("0" + minute + ":");
        } else {
            tvminute.setText(minute + ":");
        }
        if (second < 10) {
            tvsecond.setText("0" + second + "");
        } else {
            tvsecond.setText(second + "");

        }
    }


}
