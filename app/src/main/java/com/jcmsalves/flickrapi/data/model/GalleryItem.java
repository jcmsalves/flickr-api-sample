package com.jcmsalves.flickrapi.data.model;

import java.io.Serializable;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class GalleryItem implements Serializable {

    private boolean isFlipped = false;

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}
