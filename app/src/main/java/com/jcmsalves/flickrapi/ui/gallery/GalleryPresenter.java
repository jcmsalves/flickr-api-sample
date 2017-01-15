package com.jcmsalves.flickrapi.ui.gallery;

import com.jcmsalves.flickrapi.base.MvpBasePresenter;
import com.jcmsalves.flickrapi.data.model.Feed;
import com.jcmsalves.flickrapi.data.model.Photo;
import com.jcmsalves.flickrapi.data.repositories.FeedRepository;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class GalleryPresenter extends MvpBasePresenter<GalleryMvpView> {

    private FeedRepository feedRepository;
    private final Scheduler mainScheduler, ioScheduler;

    public GalleryPresenter(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
        this.mainScheduler = AndroidSchedulers.mainThread();
        this.ioScheduler = Schedulers.io();
    }

    /**
     * Constructor used for testing
     * @param feedRepository
     * @param mainScheduler
     * @param ioScheduler
     */
    public GalleryPresenter(FeedRepository feedRepository, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.feedRepository = feedRepository;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    public void getFeedData(String tags) {

        if (!isViewAttached()) {
            return;
        }

        getView().showProgress();

        addSubscription(feedRepository.getPublicPictures(tags)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .map(new Func1<Feed, ArrayList<Photo>>() {
                    @Override
                    public ArrayList<Photo> call(Feed feed) {
                        return feed.getPhotos();
                    }
                })
                .subscribe(new Subscriber<ArrayList<Photo>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgress();

                        String errorMessage;
                        if (e instanceof HttpException) {
                            errorMessage = e.getMessage() + " Code: " + ((HttpException) e).code();
                        } else {
                            errorMessage = e.getMessage();
                        }

                        getView().showErrorDialog(errorMessage);
                    }

                    @Override
                    public void onNext(ArrayList<Photo> photos) {
                        if (photos.size() == 0) {
                            getView().showEmptyContainer();
                        } else {
                            getView().loadFeed(photos);
                        }
                    }
                })
        );
    }
}
