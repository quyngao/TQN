package com.gotit.quyle.tqn.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.fragment.AlbumsFragment;
import com.gotit.quyle.tqn.fragment.FavouriteFragment;
import com.gotit.quyle.tqn.fragment.ProfileFragment;
import com.gotit.quyle.tqn.fragment.NotificationFragment;
import com.gotit.quyle.tqn.fragment.WallPaperFragment;
import com.gotit.quyle.tqn.model.event.PhotosetEvent;
import com.gotit.quyle.tqn.view.BottomNavigationBehavior;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by QUYLE on 1/9/18.
 */

public class MainActivity extends AbstractBaseActivity {

    @BindView(R.id.navigation)
    protected BottomNavigationView navigation;


    @Override
    protected int getLayout() {
        return R.layout.activity_bottom_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        loadFragment(new FavouriteFragment());

    }

    public void loadFragment(Fragment storeFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, storeFragment);
        transaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_favourite:
                    fragment = new FavouriteFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_setting:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_album:
                    fragment = new AlbumsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_home:
                    fragment = new WallPaperFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notification:
                    fragment = new NotificationFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoSetEvent(PhotosetEvent event) {

    }

}
