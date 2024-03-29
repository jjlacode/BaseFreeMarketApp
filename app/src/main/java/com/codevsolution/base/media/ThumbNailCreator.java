package com.codevsolution.base.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public class ThumbNailCreator extends Thread {

    private int mWidth;
    private int mHeight;
    private SoftReference<Bitmap> mThumb;
    private static HashMap<String, Bitmap> mCacheMap = null;
    private ArrayList<String> mFiles;
    private String mDir;
    private Handler mHandler;
    private boolean mStop = false;

    public ThumbNailCreator(int width, int height) {
        mHeight = height;
        mWidth = width;

        if (mCacheMap == null)
            mCacheMap = new HashMap<String, Bitmap>();
    }

    public Bitmap isBitmapCached(String name) {
        return mCacheMap.get(name);
    }

    public void setCancelThumbnails(boolean stop) {
        mStop = stop;
    }

    public void createNewThumbnail(ArrayList<String> files, String dir, Handler handler) {
        this.mFiles = files;
        this.mDir = dir;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        int len = mFiles.size();

        for (int i = 0; i < len; i++) {
            if (mStop) {
                mStop = false;
                mFiles = null;
                return;
            }
            final File file = new File(mDir + "/" + mFiles.get(i));

            if (isImageFile(file.getName())) {
                long len_kb = file.length() / 1024;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = mWidth;
                options.outHeight = mHeight;

                if (len_kb > 1000 && len_kb < 5000) {
                    options.inSampleSize = 32;
                    options.inPurgeable = true;
                    mThumb = new SoftReference<Bitmap>(BitmapFactory.decodeFile(file.getPath(), options));

                } else if (len_kb >= 5000) {
                    options.inSampleSize = 32;
                    options.inPurgeable = true;
                    mThumb = new SoftReference<Bitmap>(BitmapFactory.decodeFile(file.getPath(), options));

                } else if (len_kb <= 1000) {
                    options.inPurgeable = true;
                    mThumb = new SoftReference<Bitmap>(Bitmap.createScaledBitmap(
                            BitmapFactory.decodeFile(file.getPath()),
                            mWidth,
                            mHeight,
                            false));
                }

                mCacheMap.put(file.getPath(), mThumb.get());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = mThumb.get();
                        msg.sendToTarget();
                    }
                });
            }
        }
    }

    private boolean isImageFile(String file) {
        String ext = file.substring(file.lastIndexOf(".") + 1);

        return ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") ||
                ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif") ||
                ext.equalsIgnoreCase("tiff") || ext.equalsIgnoreCase("tif");
    }
}
