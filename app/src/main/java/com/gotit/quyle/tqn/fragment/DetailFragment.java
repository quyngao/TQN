package com.gotit.quyle.tqn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.model.ImageSize;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.event.ClickEvent;
import com.gotit.quyle.tqn.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gturedi on 8.02.2017.
 */
public class DetailFragment
        extends AbstractBaseFragment {

    private static final String EXTRA_ITEM = "EXTRA_ITEM";

    @BindView(R.id.image)
    protected PhotoView image;

    public static Fragment newInstance(PhotoModel item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEM, item);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PhotoModel item = (PhotoModel) getArguments().getSerializable(EXTRA_ITEM);
        Utils.bindImage(item.getImageUrl(ImageSize.LARGE), image, false);
        image.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                EventBus.getDefault().post(new ClickEvent());
            }
        });
    }

}