package com.jcmsalves.flickrapi.ui.gallery;

import com.jcmsalves.flickrapi.base.mvp.MvpView;
import com.jcmsalves.flickrapi.data.model.Photo;

import java.util.ArrayList;

/**
 * Created by joaoalves on 14/01/2017.
 */

public interface GalleryMvpView extends MvpView {

    void loadFeed(ArrayList<Photo> photos);
    void showProgress();
    void hideProgress();
    void showEmptyContainer();
    void showErrorDialog(String errorMessage);

}
