package com.gotit.quyle.tqn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.activity.MainActivity;
import com.gotit.quyle.tqn.adapter.FavourateAdapter;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;
import com.gotit.quyle.tqn.model.event.PhotosEvent;
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

public class FavouriteFragment extends Fragment {
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private FavourateAdapter mAdapter;
    private List<PhotoModel> favourites;

    private ScreenStateManager screenStateManager;
    private FlickrClient flickrClient;


    public FavouriteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        screenStateManager = new ScreenStateManager((ViewGroup) view.findViewById(R.id.main_content));

        favourites = new ArrayList<>();
        mAdapter = new FavourateAdapter(getContext(), favourites);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchStoreItems();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void fetchStoreItems() {
        screenStateManager.showLoading();
        favourites = TQNApplication.getPrefManager().getPhotos(FlickrClient.ID_FAVORATE_PHOTOSET);
        if (!Utils.isNullOrEmpty(favourites)) {
            screenStateManager.hideAll();
            mAdapter.addAll(favourites);
            mAdapter.notifyDataSetChanged();
        } else {
            if (Utils.isConnected()) {
                flickrClient = TQNApplication.getRestClient();
                flickrClient.getImageFromPhotosetAsync(FlickrClient.ID_FAVORATE_PHOTOSET);
                screenStateManager.showLoading();
            } else {
                screenStateManager.showConnectionError();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhotosEvent event) {
        Log.d("quydz", "ok");
        if (event.exception != null) {
            screenStateManager.showError(R.string.errorMessage);
        } else if (Utils.isNullOrEmpty(event.item)) {
            screenStateManager.showEmpty(R.string.emptyText);
        } else {
            screenStateManager.hideAll();
            mAdapter.addAll(event.item);
            mAdapter.notifyDataSetChanged();

        }

    }
}
