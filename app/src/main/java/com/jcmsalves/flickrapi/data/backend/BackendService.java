package com.jcmsalves.flickrapi.data.backend;

import com.jcmsalves.flickrapi.data.model.Feed;
import com.jcmsalves.flickrapi.data.model.Photo;

import java.util.ArrayList;

import retrofit2.http.GET;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by joaoalves on 14/01/2017.
 */

public interface BackendService {

    @GET("feeds/photos_public.gne")
    Observable<Feed> getPublicPictures(@Query("tags") String tags);

}
