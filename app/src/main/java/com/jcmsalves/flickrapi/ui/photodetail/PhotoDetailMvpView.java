package com.jcmsalves.flickrapi.ui.photodetail;

/**
 * Created by joaoalves on 15/01/2017.
 */

public interface PhotoDetailMvpView {

    void loadContent();
    void hideBottomSheet();
    void savePhoto();
    void openPhotoWithBrowser();
}
