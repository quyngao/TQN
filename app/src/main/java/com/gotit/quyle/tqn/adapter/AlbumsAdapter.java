package com.gotit.quyle.tqn.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gotit.quyle.tqn.R;
import com.gotit.quyle.tqn.listener.onPhotoSetClickListener;
import com.gotit.quyle.tqn.model.Photo;
import com.gotit.quyle.tqn.model.PhotoSetModel;

import java.util.List;

/**
 * Created by QUYLE on 1/8/18.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private Context mContext;
    private List<PhotoSetModel> albumList;
    private onPhotoSetClickListener mOnPhotoSetClickListener;


    public AlbumsAdapter(Context mContext, List<PhotoSetModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public void setmOnPhotoSetClickListener(onPhotoSetClickListener mOnPhotoSetClickListener) {
        this.mOnPhotoSetClickListener = mOnPhotoSetClickListener;
    }

    @Override
    public AlbumsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumsAdapter.MyViewHolder holder, int position) {
        PhotoSetModel album = albumList.get(position);
        holder.render(album);
        holder.setOnPhotoSetClickListener(this.mOnPhotoSetClickListener);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public onPhotoSetClickListener onPhotoSetClickListener;
        public PhotoSetModel mPhotoSetModel;

        public void setOnPhotoSetClickListener(onPhotoSetClickListener onPhotoSetClickListener) {
            this.onPhotoSetClickListener = onPhotoSetClickListener;
        }

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoSetClickListener != null && mPhotoSetModel != null) {
                        onPhotoSetClickListener.onPhotoSetClick(mPhotoSetModel);
                    }
                }
            });
        }

        public void render(PhotoSetModel mPhotoSetModel) {
            this.mPhotoSetModel = mPhotoSetModel;
            this.title.setText(mPhotoSetModel.title._content);
            this.count.setText(mPhotoSetModel.description._content);

            Glide.with(mContext)
                    .load(mPhotoSetModel.getImageUrl())
                    .into(this.thumbnail);
        }
    }

    public void addAll(List<PhotoSetModel> newItems) {
        if (newItems == null) {
            albumList.add(null);
            notifyItemInserted(getItemCount() - 1);
        } else {
            albumList.addAll(newItems);
            notifyDataSetChanged();
        }
    }
}
