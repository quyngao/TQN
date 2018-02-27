package com.gotit.quyle.tqn.fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.activity.AlbumActivity;
import com.gotit.quyle.tqn.activity.MainActivity;
import com.gotit.quyle.tqn.adapter.AlbumsAdapter;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.listener.onPhotoSetClickListener;
import com.gotit.quyle.tqn.model.Photo;
import com.gotit.quyle.tqn.model.PhotoSetModel;
import com.gotit.quyle.tqn.model.event.PhotosetEvent;
import com.gotit.quyle.tqn.service.FlickrClient;
import com.gotit.quyle.tqn.utils.ScreenStateManager;
import com.gotit.quyle.tqn.utils.Utils;
import com.gotit.quyle.tqn.view.GridSpacingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by QUYLE on 1/9/18.
 */

public class AlbumsFragment extends Fragment {
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private AlbumsAdapter adapter;
    private List<PhotoSetModel> albumList;
    private ScreenStateManager screenStateManager;
    private FlickrClient flickrClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, view);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        initCollapsingToolbar(view);
        screenStateManager = new ScreenStateManager((ViewGroup) view.findViewById(R.id.main_content));

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), albumList);
        adapter.setmOnPhotoSetClickListener(new onPhotoSetClickListener() {
            @Override
            public void onPhotoSetClick(PhotoSetModel photoSetModel) {
                if (photoSetModel != null) {
                    Log.d("quydz", "" + photoSetModel.id);
                    if (photoSetModel.id != FlickrClient.ID_FAVORATE_PHOTOSET) {
                        startActivity(AlbumActivity.createIntent(getContext(), photoSetModel));
                    } else {
                        ((MainActivity) getActivity()).loadFragment(new FavouriteFragment());
                    }
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(view.getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getContext(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        try {
            Glide.with(this).load(R.drawable.bg7).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        fetchStoreItems();
        return view;
    }



    private void fetchStoreItems() {
        screenStateManager.showLoading();
        if (Utils.isConnected()) {
            flickrClient = TQNApplication.getRestClient();
            flickrClient.getPhotosetsAsync();
            if (isScreenEmpty()) screenStateManager.showLoading();
            else
                adapter.addAll(null); // add null , so the adapter will check view_type and show progress bar at bottom
        } else {
            List<PhotoSetModel> localPhotoSets = TQNApplication.getPrefManager().getPhotoset();
            if (localPhotoSets != null) {
                onPhotoSetEvent(new PhotosetEvent(localPhotoSets, null));
            } else {
                if (isScreenEmpty()) screenStateManager.showConnectionError();
                else ((MainActivity) getContext()).showConnectionError();
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoSetEvent(PhotosetEvent event) {
        Log.d("DEBUG", "event photoset: " + event);
        if (isScreenEmpty()) {
            if (event.exception != null) {
                screenStateManager.showError(R.string.errorMessage);
            } else if (Utils.isNullOrEmpty(event.item)) {
                screenStateManager.showEmpty(R.string.emptyText);
            } else {
                screenStateManager.hideAll();
                adapter.addAll(event.item);
            }
        }
    }

    private boolean isScreenEmpty() {
        return adapter == null || adapter.getItemCount() == 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    private void prepareAlbums() {
        adapter.notifyDataSetChanged();
    }


    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


}
