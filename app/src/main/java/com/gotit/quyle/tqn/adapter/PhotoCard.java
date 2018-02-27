package com.gotit.quyle.tqn.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.model.Photo;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

/**
 * Created by QUYLE on 12/29/17.
 */

@Layout(R.layout.photo_card_view)
public class PhotoCard {
    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Photo mPhoto;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public PhotoCard(Context context, Photo profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mPhoto = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved() {
        Glide.with(mContext).load(mPhoto.getThumbnail()).into(profileImageView);
        nameAgeTxt.setText(mPhoto.getName());
        locationNameTxt.setText("15/12/2017");
    }

    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }
}
