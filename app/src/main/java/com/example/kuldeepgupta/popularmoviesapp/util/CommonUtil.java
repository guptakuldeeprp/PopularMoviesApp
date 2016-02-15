package com.example.kuldeepgupta.popularmoviesapp.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.example.kuldeepgupta.popularmoviesapp.R;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kuldeep.gupta on 15-01-2016.
 */
public class CommonUtil {
    private static final String TAG = CommonUtil.class.getName();
    private static final String TRUE = "true";
    private static final CommonUtil INSTANCE = new CommonUtil();
    private static final String APP_BASE_FLDR = "PopularMoviesApp";
    private Context context;
    private File baseDir;

    private CommonUtil() {
    }

    public static CommonUtil get() {
        if (INSTANCE.context == null)
            throw new IllegalStateException("Context is not initialized!");
        return INSTANCE;
    }

    public static CommonUtil get(Context context) {
        if (context != null)
            synchronized (INSTANCE) {
                INSTANCE.context = context;
            }
        return INSTANCE;
    }

    public boolean isLargeDevice() {
        boolean isLargeDev = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        if (isLargeDev)
            return true;

        return context.getString(R.string.large_dev).equals(TRUE); // checks for landscape mode as well
    }

    public String saveImg(String uri, String imgName) {
        File saveImg = getFileObj(imgName);
        Log.w(TAG,"File loc to save img " + imgName +" is " + saveImg.getAbsolutePath());
        Picasso.with(context).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(getTarget(saveImg));
        return saveImg.getAbsolutePath();

    }

    public File getFileObj(String fileName) {
        if(baseDir ==  null) {
            if(isSDCardPresent()) {
                baseDir = new File(context.getExternalFilesDir(null)+File.separator + APP_BASE_FLDR);

            } else {
                baseDir = new File(context.getFilesDir()+File.separator + APP_BASE_FLDR);
            }
            if(!baseDir.exists()) {
                if(baseDir.mkdirs())
                    Log.i(TAG, "Base dir created:: " + baseDir.getAbsolutePath());
                else
                    Log.e(TAG, "Base dir could not be created");
            } else {
                Log.i(TAG,"Base dir already exists..");
            }
        }

        return new File(baseDir,fileName);

    }

    public static boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private Target getTarget(final File file) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                //Thread _t = new Thread(new Runnable() {
                //    @Override
                //    public void run() {
                        FileOutputStream fos = null;
                        try {
                            Log.i(TAG, "Attempting to create a new file " + file.getAbsolutePath());
                            file.createNewFile();
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                            Log.i(TAG, "Image written successfully");

                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        } finally {
                            if(fos != null)
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    Log.e(TAG,e.getMessage(),e);
                                }
                        }
                 //   }
                //});
                //_t.start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

}
