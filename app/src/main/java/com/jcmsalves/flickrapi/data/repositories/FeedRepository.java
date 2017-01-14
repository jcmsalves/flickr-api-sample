package com.jcmsalves.flickrapi.data.repositories;

import com.jcmsalves.flickrapi.data.backend.BackendService;
import com.jcmsalves.flickrapi.data.model.Photo;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class FeedRepository {

    private BackendService backendService;

    public FeedRepository(BackendService backendService) {
        this.backendService = backendService;
    }

    public Observable<ArrayList<Photo>> getPublicPictures(final String tags) {
        return Observable.defer(new Func0<Observable<ArrayList<Photo>>>() {
            @Override
            public Observable<ArrayList<Photo>> call() {
                return backendService.getPublicPictures(tags);
            }
        });
    }
}
