package com.codevsolution.base.media.cameraview.frame;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.codevsolution.base.media.cameraview.CameraLogger;
import com.codevsolution.base.media.cameraview.controls.Engine;
import com.codevsolution.base.media.cameraview.size.Size;

/**
 * A preview frame to be processed by {@link FrameProcessor}s.
 */
public class Frame {

    private final static String TAG = Frame.class.getSimpleName();
    private final static CameraLogger LOG = CameraLogger.create(TAG);

    private final FrameManager mManager;
    private final Class<?> mDataClass;

    private Object mData = null;
    private long mTime = -1;
    private long mLastTime = -1;
    private int mRotation = 0;
    private Size mSize = null;
    private int mFormat = -1;

    Frame(@NonNull FrameManager manager) {
        mManager = manager;
        mDataClass = manager.getFrameDataClass();
    }

    void setContent(@NonNull Object data, long time, int rotation, @NonNull Size size, int format) {
        this.mData = data;
        this.mTime = time;
        this.mLastTime = time;
        this.mRotation = rotation;
        this.mSize = size;
        this.mFormat = format;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasContent() {
        return mData != null;
    }

    private void ensureHasContent() {
        if (!hasContent()) {
            LOG.e("Frame is dead! time:", mTime, "lastTime:", mLastTime);
            throw new RuntimeException("You should not access a released frame. " +
                    "If this frame was passed to a FrameProcessor, you can only use its contents " +
                    "synchronously, for the duration of the process() method.");
        }
    }


    @Override
    public boolean equals(Object obj) {
        // We want a super fast implementation here, do not compare arrays.
        return obj instanceof Frame && ((Frame) obj).mTime == mTime;
    }

    /**
     * Clones the frame, returning a frozen content that will not be overwritten.
     * This can be kept or safely passed to other threads.
     * Using freeze without clearing with {@link #release()} can result in memory leaks.
     *
     * @return a frozen Frame
     */
    @SuppressLint("NewApi")
    @NonNull
    public Frame freeze() {
        ensureHasContent();
        Frame other = new Frame(mManager);
        //noinspection unchecked
        Object data = mManager.cloneFrameData(getData());
        other.setContent(data, mTime, mRotation, mSize, mFormat);
        return other;
    }

    /**
     * Disposes the contents of this frame. Can be useful for frozen frames
     * that are not useful anymore.
     */
    public void release() {
        if (!hasContent()) return;
        LOG.v("Frame with time", mTime, "is being released.");
        Object data = mData;
        mData = null;
        mRotation = 0;
        mTime = -1;
        mSize = null;
        mFormat = -1;
        // After the manager is notified, this frame instance can be taken by
        // someone else, possibly from another thread. So this should be the
        // last call in this method. If we null data after, we can have issues.
        //noinspection unchecked
        mManager.onFrameReleased(this, data);
    }

    /**
     * Returns the frame data.
     *
     * @return the frame data
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> T getData() {
        ensureHasContent();
        return (T) mData;
    }

    /**
     * Returns the class returned by {@link #getData()}.
     * This class depends on the engine that produced this frame.
     * - {@link Engine#CAMERA1} will produce byte[] arrays
     * - {@link Engine#CAMERA2} will produce {@link android.media.Image}s
     *
     * @return the data class
     */
    @NonNull
    public Class<?> getDataClass() {
        return mDataClass;
    }

    /**
     * Returns the milliseconds epoch for this frame,
     * in the {@link System#currentTimeMillis()} reference.
     *
     * @return time data
     */
    public long getTime() {
        ensureHasContent();
        return mTime;
    }

    /**
     * Returns the clock-wise rotation that should be applied on the data
     * array, such that the resulting frame matches what the user is seeing
     * on screen.
     *
     * @return clock-wise rotation
     */
    public int getRotation() {
        ensureHasContent();
        return mRotation;
    }

    /**
     * Returns the frame size.
     *
     * @return frame size
     */
    @NonNull
    public Size getSize() {
        ensureHasContent();
        return mSize;
    }

    /**
     * Returns the data format, in one of the
     * {@link android.graphics.ImageFormat} constants.
     * This will always be {@link android.graphics.ImageFormat#NV21} for now.
     *
     * @return the data format
     * @see android.graphics.ImageFormat
     */
    public int getFormat() {
        ensureHasContent();
        return mFormat;
    }
}
