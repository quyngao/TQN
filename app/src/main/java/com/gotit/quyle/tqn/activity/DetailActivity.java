package com.gotit.quyle.tqn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.adapter.DetailPagerAdapter;
import com.gotit.quyle.tqn.app.TQNApplication;
import com.gotit.quyle.tqn.model.ImageSize;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.event.AddPhotoEvent;
import com.gotit.quyle.tqn.model.event.ClickEvent;
import com.gotit.quyle.tqn.model.event.DetailEvent;
import com.gotit.quyle.tqn.model.event.PhotosEvent;
import com.gotit.quyle.tqn.service.FlickrClient;
import com.gotit.quyle.tqn.utils.ParallaxPageTransformer;
import com.gotit.quyle.tqn.utils.ScreenStateManager;
import com.gotit.quyle.tqn.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by QUYLE on 1/20/18.
 */

public class DetailActivity extends AbstractBaseActivity {

    private static final String EXTRA_INDEX = "EXTRA_INDEX";
    private static final String EXTRA_ITEMS = "EXTRA_ITEMS";
    private static final String ERROR_LITERAL = "-";

    private List<PhotoModel> items;
    private FlickrClient flickrClient;
    private DetailEvent detailEvent;

    @BindView(R.id.pager)
    protected ViewPager pager;
    @BindView(R.id.tvOwner)
    protected TextView tvOwner;
    @BindView(R.id.tvTitle)
    protected TextView tvTitle;
    @BindView(R.id.tvDate)
    protected TextView tvDate;
    @BindView(R.id.tvViewCount)
    protected TextView tvViewCount;
    @BindView(R.id.lnrFooter)
    protected View lnrFooter;
    @BindView(R.id.ivClose)
    protected View ivClose;
    @BindView(R.id.ivInfo)
    protected View ivInfo;
    @BindView(R.id.ivShare)
    protected View ivShare;

    private ScreenStateManager screenStateManager;


    public static Intent createIntent(Context context, int index, List<PhotoModel> items) {
        return new Intent(context, DetailActivity.class)
                .putExtra(EXTRA_INDEX, index)
                .putExtra(EXTRA_ITEMS, (Serializable) items);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = getIntent().getIntExtra(EXTRA_INDEX, -1);
        flickrClient = TQNApplication.getRestClient();
        items = (List<PhotoModel>) getIntent().getSerializableExtra(EXTRA_ITEMS);

        screenStateManager = new ScreenStateManager((ViewGroup) findViewById(R.id.detail_content));
        screenStateManager.hideAll();

        if (index == -1) {
            finish();
        } else if (!Utils.isConnected()) {
            showConnectionError();
        } else {
            pager.setPageTransformer(false, new ParallaxPageTransformer(R.id.image));
            pager.setAdapter(new DetailPagerAdapter(getSupportFragmentManager(), items));
            pager.setCurrentItem(index);
            onPageSelected(index);
        }

        Utils.setVectorBg(ivClose, R.drawable.ic_close_24dp, android.R.color.white, R.color.gray2);
        Utils.setVectorBg(ivInfo, R.drawable.ic_info_outline_24dp, android.R.color.white, R.color.gray2);
        Utils.setVectorBg(ivShare, R.drawable.ic_share_24dp, android.R.color.white, R.color.gray2);
    }


    @OnClick(R.id.ivClose)
    public void onCloseClick(View v) {
        finish();
    }

    @OnClick(R.id.ivInfo)
    public void onInfoClick(View v) {
//        addPhoto(detailEvent.item.id);
    }

    public void addPhoto(long idPhoto) {

//        if (Utils.isConnected()) {
//            flickrClient = TQNApplication.getRestClient();
//            flickrClient.addPhotoToPhotosetAsync(idPhoto);
//            screenStateManager.showLoading();
//        } else {
//            // todo add local image for favourate album
////            List<PhotoModel> localPhotos = TQNApplication.getPrefManager().getPhotos(id);
////            if (localPhotos != null) {
////                onMessageEvent(new PhotosEvent(localPhotos, null));
////            } else {
////                showConnectionError();
////
////            }
//        }

    }

    @OnClick(R.id.ivShare)
    public void onShareClick(View v) {
        if (detailEvent == null || detailEvent.item == null) return;
        String subject = detailEvent.item.title._content;
        String text = items.get(pager.getCurrentItem()).getImageUrl(ImageSize.LARGE);
        startActivity(Utils.createShareIntent(subject, text));
    }


    @OnPageChange(R.id.pager)
    protected void onPageSelected(int position) {
        //showLoadingDialog();
        tvOwner.setText(R.string.loading);
        tvTitle.setText(R.string.loading);
        tvDate.setText(R.string.loading);
        tvViewCount.setText(R.string.loading);
        flickrClient.getDetailAsync(items.get(position).id);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceEvent(DetailEvent event) {
        //dismissLoadingDialog();
        detailEvent = event;
        if (event.exception == null) {
            tvOwner.setText(event.item.owner.toString());
            tvTitle.setText(event.item.title.toString());
            tvDate.setText(event.item.getFormattedDate());
            tvViewCount.setText(getResources().getQuantityString(R.plurals.views, event.item.views, event.item.views));
        } else {
            tvOwner.setText(ERROR_LITERAL);
            tvTitle.setText(ERROR_LITERAL);
            tvDate.setText(ERROR_LITERAL);
            tvViewCount.setText(ERROR_LITERAL);
            showSnack(R.string.errorMessage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPhotoEvent(AddPhotoEvent event) {
        if (event.item == getString(R.string.ok)) {
            screenStateManager.hideAll();
        } else {
            showSnack(R.string.errorMessage);
        }
    }


    // fired by child fragment
    // child's photoView absorbs touch event so parent's touch events not fired
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickEvent(ClickEvent event) {
        int value = lnrFooter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        lnrFooter.setVisibility(value);
        ivClose.setVisibility(value);
        tvOwner.setVisibility(value);
    }

}
