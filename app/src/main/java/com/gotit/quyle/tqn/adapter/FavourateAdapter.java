package com.gotit.quyle.tqn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.listener.onPhotoSetClickListener;
import com.gotit.quyle.tqn.model.ImageSize;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoModel;
import com.gotit.quyle.tqn.model.PhotoSetModel;

import java.util.List;

/**
 * Created by QUYLE on 1/22/18.
 */

public class FavourateAdapter extends RecyclerView.Adapter<FavourateAdapter.MyViewHolder> {

    private Context mContext;
    private List<PhotoModel> favourates;

    public FavourateAdapter(Context mContext, List<PhotoModel> favourates) {
        this.mContext = mContext;
        this.favourates = favourates;
    }

    @Override
    public FavourateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_favourite_photo, parent, false);

        return new FavourateAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(FavourateAdapter.MyViewHolder holder, int position) {
        PhotoModel photo = favourates.get(position);
        holder.render(photo);
    }

    @Override
    public int getItemCount() {
        return favourates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }

        public void render(PhotoModel photo) {
            this.name.setText("" + photo.id);
            this.price.setText("TQN");

            Glide.with(mContext)
                    .load(photo.getImageUrl(ImageSize.MEDIUM))
                    .into(this.thumbnail);
        }

    }

    public void addAll(List<PhotoModel> newItems) {
        if (newItems == null) {
            favourates.add(null);
            notifyItemInserted(getItemCount() - 1);
        } else {
            favourates.addAll(newItems);
            notifyDataSetChanged();
        }
    }
}
