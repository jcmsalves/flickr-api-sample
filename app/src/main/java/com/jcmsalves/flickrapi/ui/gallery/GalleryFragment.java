package com.jcmsalves.flickrapi.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jcmsalves.flickrapi.R;
import com.jcmsalves.flickrapi.data.backend.BackendServiceFactory;
import com.jcmsalves.flickrapi.data.model.Photo;
import com.jcmsalves.flickrapi.data.repositories.FeedRepository;
import com.jcmsalves.flickrapi.ui.photodetail.PhotoDetailActivity;
import com.jcmsalves.flickrapi.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class GalleryFragment extends Fragment implements GalleryMvpView {

    @BindView(R.id.gallery_rv)
    RecyclerView galleryRv;
    @BindView(R.id.gallery_srl)
    SwipeRefreshLayout gallerySrl;
    @BindView(R.id.empty_container)
    LinearLayout emptyContainer;

    private GalleryPresenter galleryPresenter;
    private FeedRepository feedRepository;
    private GalleryAdapter galleryAdapter;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    public GalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        feedRepository = new FeedRepository(BackendServiceFactory.makeBackendService());
        galleryPresenter = new GalleryPresenter(feedRepository);
        galleryPresenter.attachView(this);

        galleryAdapter = new GalleryAdapter(getActivity(), new GalleryAdapter.OnPhotoClickedListener() {
            @Override
            public void onPhotoClicked(Photo photo) {

                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                intent.putExtra(Constants.BUNDLE_PHOTO, photo);
                startActivity(intent);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setItemPrefetchEnabled(false);
        galleryRv.setLayoutManager(gridLayoutManager);
        galleryRv.setAdapter(galleryAdapter);

        galleryPresenter.getFeedData(null);

        gallerySrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                galleryPresenter.getFeedData(null);
            }
        });

        return view;
    }

    @Override
    public void loadFeed(ArrayList<Photo> photos) {
        emptyContainer.setVisibility(View.GONE);
        galleryAdapter.setData(photos);
    }

    @Override
    public void showProgress() {
        gallerySrl.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        gallerySrl.setRefreshing(false);
    }

    @Override
    public void showEmptyContainer() {
        emptyContainer.setVisibility(View.GONE);
    }

    @Override
    public void showErrorDialog(String errorMessage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.default_error))
                .setMessage(errorMessage)
                .setPositiveButton(getString(R.string.default_ok), null);

        alertDialog.show();
    }
}
