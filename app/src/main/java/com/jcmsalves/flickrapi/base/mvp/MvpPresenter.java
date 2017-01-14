package com.jcmsalves.flickrapi.base.mvp;

/**
 * Created by joaoalves on 14/01/2017.
 */
public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);
    void detachView(boolean retainInstance);
}
