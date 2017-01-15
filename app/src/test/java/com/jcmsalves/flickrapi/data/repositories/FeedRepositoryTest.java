package com.jcmsalves.flickrapi.data.repositories;

import com.jcmsalves.flickrapi.data.backend.BackendService;
import com.jcmsalves.flickrapi.data.model.Feed;
import com.jcmsalves.flickrapi.data.model.Photo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by joaoalves on 15/01/2017.
 */
public class FeedRepositoryTest {

    private static final String TEST_POTHO_WEB_URL = "http://dummy_url";

    @Mock
    BackendService backendService;

    FeedRepository feedRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        feedRepository = new FeedRepository(backendService);
    }

    @Test
    public void getPublicPictures_success() throws Exception {

        when(backendService.getPublicPictures(null)).thenReturn(Observable.just(getTestFeed()));

        TestSubscriber<Feed> subscriber = new TestSubscriber<>();
        feedRepository.getPublicPictures(null).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<Feed> onNextEvents = subscriber.getOnNextEvents();

        Feed feed = onNextEvents.get(0);
        Photo photo = feed.getPhotos().get(0);
        assertTrue(photo.getWebUrl().equals(TEST_POTHO_WEB_URL));
    }

    @Test
    public void getPublicPictures_noConnection() throws Exception {

        when(backendService.getPublicPictures(null)).thenReturn(Observable.<Feed>error(new IOException("connection error")));

        TestSubscriber<Feed> subscriber = new TestSubscriber<>();
        feedRepository.getPublicPictures(null).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(IOException.class);
    }

    @Test
    public void getPublicPictures_httpError() throws Exception {

        HttpException httpException = new HttpException(
                Response.error(499, ResponseBody.create(MediaType.parse("application/json"), "Dummy HTTP Error")));

        when(backendService.getPublicPictures(null)).thenReturn(Observable.<Feed>error(httpException));

        TestSubscriber<Feed> subscriber = new TestSubscriber<>();
        feedRepository.getPublicPictures(null).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);

    }

    private Feed getTestFeed() {
        Feed feed = new Feed();
        ArrayList<Photo> photos = new ArrayList<>();
        Photo photo = new Photo();
        photo.setWebUrl(TEST_POTHO_WEB_URL);
        photos.add(photo);
        feed.setPhotos(photos);

        return feed;
    }

}