package com.gotit.quyle.tqn.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActivity;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.service.FlickrClient;
import com.gotit.quyle.tqn.utils.ScreenStateManager;
import com.gotit.quyle.tqn.utils.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by QUYLE on 1/17/18.
 */

public class SplashActivity extends OAuthLoginActivity<FlickrClient> {

    private ScreenStateManager screenStateManager;
    int STORAGE_PERMISSION_CODE = 100;
    int todDay;
    String quote;
    TextView mDayTextView;
    TextView mQuoteTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDayTextView = (TextView) findViewById(R.id.day);
        mQuoteTextView = (TextView) findViewById(R.id.quote);

        screenStateManager = new ScreenStateManager((ViewGroup) findViewById(R.id.main_content));
        screenStateManager.showLoading();
        getContentText();


    }

    public void getContentText() {
        todDay = TQNApplication.getPrefManager().getToDay();
        int day = Utils.getNumberDay();
        if (todDay == day) {
            quote = TQNApplication.getPrefManager().getQuoteToDay();
        } else if (todDay < day) {
            todDay = day;
            quote = Utils.getListQuite(this);
            TQNApplication.getPrefManager().setToDay(day);
            TQNApplication.getPrefManager().setQuote(quote);
        }
        mDayTextView.setText("#TQN day " + day);
        mQuoteTextView.setText(quote);
    }

    public void nextScreen() {
        screenStateManager.hideAll();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReadStorageAllowed()) {
            if (Utils.isConnected()) {
//                TQNApplication.getPrefManager().clearPreferences(); TODO
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing())
                            getClient().connect();
                    }
                }, 3000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            List<PhotoModel> favourites = TQNApplication.getPrefManager().getPhotos(FlickrClient.ID_FAVORATE_PHOTOSET);
                            if (Utils.isNullOrEmpty(favourites)) {
                                screenStateManager.showConnectionError();
                            } else {
                                nextScreen();
                            }


                        }
                    }
                }, 3000);
            }
        } else {
            requestStoragePermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                nextScreen();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SET_WALLPAPER)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SET_WALLPAPER}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onLoginSuccess() {
        nextScreen();
    }

    @Override
    public void onLoginFailure(Exception e) {

    }
}
