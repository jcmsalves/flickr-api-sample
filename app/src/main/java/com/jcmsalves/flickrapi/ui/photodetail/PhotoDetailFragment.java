package com.jcmsalves.flickrapi.ui.photodetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jcmsalves.flickrapi.R;
import com.jcmsalves.flickrapi.data.model.Photo;
import com.jcmsalves.flickrapi.ui.SquareImageView;
import com.jcmsalves.flickrapi.utils.Constants;
import com.jcmsalves.flickrapi.utils.ImageSaver;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joaoalves on 15/01/2017.
 */

public class PhotoDetailFragment extends Fragment implements PhotoDetailMvpView {

    Photo photo;
    @BindView(R.id.photo_iv)
    SquareImageView photoIv;
    @BindView(R.id.photo_title_tv)
    TextView photoTitleTv;
    @BindView(R.id.photo_author_tv)
    TextView photoAuthorTv;
    @BindView(R.id.photo_tags_tv)
    TextView photoTagsTv;
    @BindView(R.id.save_photo_btn)
    Button savePhotoBtn;
    @BindView(R.id.open_browser_btn)
    Button openBrowserBtn;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheet;

    BottomSheetBehavior bottomSheetBehavior;

    public static PhotoDetailFragment newInstance(Photo photo) {

        PhotoDetailFragment photoDetailFragment = new PhotoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_PHOTO, photo);
        photoDetailFragment.setArguments(bundle);
        return photoDetailFragment;
    }

    public PhotoDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        ButterKnife.bind(this, view);

        photo = (Photo) getArguments().getSerializable(Constants.BUNDLE_PHOTO);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight((int) (48 * Resources.getSystem().getDisplayMetrics().density));

        savePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheet();
                savePhoto();
            }
        });

        openBrowserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheet();
                openPhotoWithBrowser();
            }
        });

        loadContent();

        return view;
    }



    @Override
    public void loadContent() {

        Glide.with(getActivity())
                .load(photo.getMedia().getPhotoUrl())
                .error(getActivity().getDrawable(R.drawable.placeholder))
                .placeholder(getActivity().getDrawable(R.drawable.placeholder))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        return false;
                    }
                })
                .into(photoIv);

        photoTitleTv.setText(photo.getTitle());
        photoAuthorTv.setText(photo.getAuthor());

        if (!StringUtils.isEmpty(photo.getTags())) {
            photoTagsTv.setText(photo.getTags());
        } else {
            photoTagsTv.setText(R.string.gallery_photo_no_tags);
        }

        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showBottomSheet();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);

    }

    @Override
    public void hideBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void savePhoto() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(getActivity(), permissions, Constants.PERMISSION_WRITE);
            return;
        }

        Glide.with(getActivity()).load(photo.getMedia().getPhotoUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap imageBitmap = resource;
                ImageSaver.saveImageToDevice(getActivity(), imageBitmap);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePhoto();
                }
                return;
            }
        }
    }

    @Override
    public void openPhotoWithBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(photo.getWebUrl()));
        startActivity(intent);
    }

}
