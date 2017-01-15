package com.jcmsalves.flickrapi.ui.gallery;

import com.jcmsalves.flickrapi.data.model.Feed;
import com.jcmsalves.flickrapi.data.model.Photo;
import com.jcmsalves.flickrapi.data.repositories.FeedRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by joaoalves on 15/01/2017.
 */
public class GalleryPresenterTest {

    @Mock
    FeedRepository feedRepository;

    @Mock
    GalleryMvpView galleryMvpView;

    @Mock
    Photo photo;

    Feed feed;

    ArrayList<Photo> photos = new ArrayList<>();

    GalleryPresenter galleryPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        galleryPresenter = new GalleryPresenter(feedRepository, Schedulers.immediate(), Schedulers.immediate());
        galleryPresenter.attachView(galleryMvpView);
        photos.add(photo);
        feed = new Feed();
        feed.setPhotos(photos);
    }

    @Test
    public void getFeedData_success() throws Exception {

        when(feedRepository.getPublicPictures(null)).thenReturn(Observable.just(feed));

        galleryPresenter.getFeedData(null);

        verify(galleryMvpView).showProgress();
        verify(galleryMvpView).hideProgress();
        verify(galleryMvpView).loadFeed(feed.getPhotos());

    }

    @Test
    public void getFeedData_success_noResults() throws Exception {

        when(feedRepository.getPublicPictures(null)).thenReturn(Observable.just(new Feed()));

        galleryPresenter.getFeedData(null);

        verify(galleryMvpView).showProgress();
        verify(galleryMvpView).hideProgress();
        verify(galleryMvpView).showEmptyContainer();
    }

    @Test
    public void getFeedData_noConnection() throws Exception {

        String errorMessage = "Check your connection";

        when(feedRepository.getPublicPictures(null)).thenReturn(Observable.<Feed>error(new IOException(errorMessage)));

        galleryPresenter.getFeedData(null);

        verify(galleryMvpView).showProgress();
        verify(galleryMvpView).hideProgress();
        verify(galleryMvpView).showErrorDialog(anyString());
    }

    @Test
    public void getFeedData_httpError() throws Exception {

        String errorMessage = "Some http error";

        HttpException httpException = new HttpException(Response.error(450, ResponseBody.create(MediaType.parse("application/json"), errorMessage)));

        when(feedRepository.getPublicPictures(null)).thenReturn(Observable.<Feed>error(httpException));

        galleryPresenter.getFeedData(null);

        verify(galleryMvpView).showProgress();
        verify(galleryMvpView).hideProgress();
        verify(galleryMvpView).showErrorDialog(anyString());
    }

}