package com.jcmsalves.flickrapi.ui.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jcmsalves.flickrapi.R;
import com.jcmsalves.flickrapi.data.model.Photo;

import org.apache.commons.lang3.StringUtils;

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
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        final Photo photo = photos.get(position);

        Glide.with(context)
                .load(photo.getMedia().getPhotoUrl())
                .error(context.getDrawable(R.drawable.placeholder))
                .placeholder(context.getDrawable(R.drawable.placeholder))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.flipIv.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.photoIv);

        holder.photoTitleTv.setText(photo.getTitle());
        holder.photoAuthorTv.setText(photo.getAuthor());

        if (!StringUtils.isEmpty(photo.getTags())) {
            holder.photoTagsTv.setText(photo.getTags());
        } else {
            holder.photoTagsTv.setText(R.string.gallery_photo_no_tags);
        }

        holder.flipIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo.isFlipped()) {
                    photo.setFlipped(false);
                    holder.cardBackFl.setVisibility(View.GONE);
                } else {
                    photo.setFlipped(true);
                    holder.cardBackFl.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_iv)
        ImageView photoIv;
        @BindView(R.id.photo_title_tv)
        TextView photoTitleTv;
        @BindView(R.id.photo_author_tv)
        TextView photoAuthorTv;
        @BindView(R.id.photo_tags_tv)
        TextView photoTagsTv;
        @BindView(R.id.flip_iv)
        ImageView flipIv;
        @BindView(R.id.card_back_fl)
        FrameLayout cardBackFl;

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
