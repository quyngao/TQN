package com.gotit.quyle.tqn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by gturedi on 7.02.2017.
 */
public abstract class AbstractBaseActivity
        extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    protected abstract int getLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setIcon(R.drawable.ic_info_outline_24dp)
                .setMessage(message)
                .setNegativeButton(R.string.close, null)
                .show();
    }

    protected void showSnack(@StringRes int msgId) {
        Utils.createSnackbar(this, msgId).show();
    }

    public void showConnectionError() {
//        AppUtil.createSnackbar(this, R.string.connectionErrorMessage)
//                .setAction(R.string.settings, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    }
//                }).show();
    }

}