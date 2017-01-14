package com.jcmsalves.flickrapi.ui.gallery;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jcmsalves.flickrapi.R;
import com.jcmsalves.flickrapi.data.model.Photo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private ArrayList<Photo> photos = new ArrayList<>();
    private Context context;
    private OnPhotoClickedListener onPhotoClickedListener;

    public GalleryAdapter(Context context, OnPhotoClickedListener onPhotoClickedListener) {
        this.context = context;
        this.onPhotoClickedListener = onPhotoClickedListener;
    }

    public void setData(ArrayList<Photo> photos) {
        this.photos.clear();
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        final Photo photo = photos.get(position);

        Glide.with(context)
                .load(photo.getMedia().getPhotoUrl())
                .error(context.getDrawable(R.drawable.placeholder))
                .placeholder(context.getDrawable(R.drawable.placeholder))
                .into(holder.photoIv);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_iv)
        ImageView photoIv;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhotoClickedListener.onPhotoClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface OnPhotoClickedListener {
        void onPhotoClicked(int position);
    }

}
