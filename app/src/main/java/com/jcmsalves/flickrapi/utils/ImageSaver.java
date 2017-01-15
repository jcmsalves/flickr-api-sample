package com.jcmsalves.flickrapi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by joaoalves on 15/01/2017.
 */

public class ImageSaver {

    private static final String TAG = ImageSaver.class.getSimpleName();

    public static void saveImageToDevice(Context context, Bitmap imageBitmap) {

        String filename = "flickr_sample" + System.currentTimeMillis() + ".png";

        File file = new File(getStorageDirectory() + File.separator + filename);
        FileOutputStream fileoutputstream;

        try {

            file.createNewFile();
            fileoutputstream = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileoutputstream);
            fileoutputstream.flush();
            fileoutputstream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to save picture in external storage. Error: " + e.getMessage());
        }

        Log.d(TAG, "Picture saved to device successfully");

    }

    private static File getStorageDirectory() {

        if (!isExternalStorageWritable()) {
            return null;
        }

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "flickr-sample");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(TAG, "Didn't create directory to save picture");
            }
        }

        return dir;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
