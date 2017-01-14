package com.jcmsalves.flickrapi.base;

import com.jcmsalves.flickrapi.base.mvp.MvpPresenter;
import com.jcmsalves.flickrapi.base.mvp.MvpView;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> weakReference;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void attachView(V view) {
        weakReference = new WeakReference<V>(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        compositeSubscription.clear();
        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }
    }

    public V getView() {
        return weakReference == null ? null : weakReference.get();
    }

    public boolean isViewAttached() {
        return weakReference != null && weakReference.get() != null;
    }

    protected void addSubscription(Subscription subscription) {
        this.compositeSubscription.add(subscription);
    }
}
