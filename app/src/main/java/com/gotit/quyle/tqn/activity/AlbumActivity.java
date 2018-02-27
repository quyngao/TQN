package com.gotit.quyle.tqn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.BuildConfig;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.adapter.PhotoAdapter;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.listener.RowClickListener;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;
import com.gotit.quyle.tqn.model.event.PhotosEvent;
import com.gotit.quyle.tqn.service.FlickrClient;
import com.gotit.quyle.tqn.utils.ScreenStateManager;
import com.gotit.quyle.tqn.utils.Utils;
import com.gotit.quyle.tqn.view.GridSpacingItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by QUYLE on 1/19/18.
 */

public class AlbumActivity extends AbstractBaseActivity implements RowClickListener<PhotoModel>,
        SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_PHOTOSET = "EXTRA_TITLE";


    private int page = 1;
    private boolean isLoading;
    private FlickrClient flickrClient;
    private PhotoAdapter adapter;
    private ScreenStateManager screenStateManager;


    @BindView(R.id.drawer)
    protected DrawerLayout drawer;
    @BindView(R.id.navigation)
    protected NavigationView navigation;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.swipe)
    protected SwipeRefreshLayout swipe;
    @BindView(R.id.linear)
    protected LinearLayout linear;
    @BindView(R.id.recycler)
    protected RecyclerView recycler;
    private PhotoSetModel model;

    public static Intent createIntent(Context context, PhotoSetModel photoSetModel) {
        return new Intent(context, AlbumActivity.class)
                .putExtra(EXTRA_PHOTOSET, photoSetModel);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model = (PhotoSetModel) getIntent().getSerializableExtra(this.EXTRA_PHOTOSET);
        getSupportActionBar().setTitle(model.title._content);
        navigation.setNavigationItemSelectedListener(this);

        swipe.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        swipe.setOnRefreshListener(this);

        adapter = new PhotoAdapter();
        adapter.setRowClickListener(this);
        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayoutManager);
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(this, 2), true));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
//        setScrollListener();
        recycler.setNestedScrollingEnabled(false);
        swipe.setRefreshing(false);
        screenStateManager = new ScreenStateManager(linear);
        sendRequest();

        navigation.getMenu().findItem(R.id.mnVersion)
                .setTitle(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");


    }

    private void setScrollListener() {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= 45) {
//                    sendRequest();
                }
            }
        });
    }

    private void sendRequest() {
        long id = model.id;

        if (Utils.isConnected()) {
            flickrClient = TQNApplication.getRestClient();
            flickrClient.getImageFromPhotosetAsync(id);
            if (isScreenEmpty()) screenStateManager.showLoading();
            else
                adapter.addAll(null); // add null , so the adapter will check view_type and show progress bar at bottom
        } else {
            List<PhotoModel> localPhotos = TQNApplication.getPrefManager().getPhotos(id);
            if (localPhotos != null) {
                onMessageEvent(new PhotosEvent(localPhotos, null));
            } else {
                swipe.setRefreshing(false);
                if (isScreenEmpty()) screenStateManager.showConnectionError();
                else showConnectionError();

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotosEvent event) {
        Log.d("quydz", "ok");

        isLoading = false;

        // fired by pull to refresh
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
            adapter.clear();
        }

        if (isScreenEmpty()) {
            if (event.exception != null) {
                screenStateManager.showError(R.string.errorMessage);
            } else if (Utils.isNullOrEmpty(event.item)) {
                screenStateManager.showEmpty(R.string.emptyText);
            } else {
                screenStateManager.hideAll();
                adapter.addAll(event.item);
            }
        } else {
            adapter.remove(adapter.getItemCount() - 1); //remove progress item
            if (event.exception != null) {
                showSnack(R.string.errorMessage);
            } else if (Utils.isNullOrEmpty(event.item)) {
                showSnack(R.string.emptyText);
            } else {
                adapter.addAll(event.item);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) drawer.openDrawer(GravityCompat.START);

        Glide.with(this)
                .load(model.getImageUrl())
                .into((ImageView) drawer.findViewById(R.id.navigation_bg));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.END);
        if (item.getItemId() == R.id.mnFeedback) {
//            startActivity(AppUtil.createMailIntent(AppUtil.FEEDBACK_MAIL, getString(R.string.feedbackSubject)));
        } else if (item.getItemId() == R.id.mnAbout) {
            String title = getString(R.string.about);
            String message = getString(R.string.aboutText);
            showInfoDialog(title, message);
        }
        return false;
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_album;
    }

    @Override
    public void onRefresh() {
//        page = 1;
//        sendRequest();
    }

    @Override
    public void onRowClicked(int row, PhotoModel item) {
        startActivity(DetailActivity.createIntent(this, row, adapter.getAll()));
    }

    private boolean isScreenEmpty() {
        return adapter == null || adapter.getItemCount() == 0;
    }


}
