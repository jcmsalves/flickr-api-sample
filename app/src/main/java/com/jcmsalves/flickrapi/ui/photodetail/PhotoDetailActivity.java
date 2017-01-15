package com.jcmsalves.flickrapi.ui.photodetail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.jcmsalves.flickrapi.R;
import com.jcmsalves.flickrapi.data.model.Photo;
import com.jcmsalves.flickrapi.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joaoalves on 15/01/2017.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;

    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.BUNDLE_PHOTO)) {
            photo = (Photo) getIntent().getExtras().getSerializable(Constants.BUNDLE_PHOTO);
        } else {
            onBackPressed();
        }

        toolbar.setTitle(photo.getTitle());
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        PhotoDetailFragment photoDetailFragment = PhotoDetailFragment.newInstance(photo);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, photoDetailFragment, PhotoDetailFragment.class.getSimpleName());
        transaction.commit();

    }
}
