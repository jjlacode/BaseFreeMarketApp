package com.codevsolution.base.media.cameraview.picture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codevsolution.base.media.cameraview.CameraLogger;
import com.codevsolution.base.media.cameraview.PictureResult;

/**
 * Helps with logging.
 */
public abstract class FullPictureRecorder extends PictureRecorder {
    private static final String TAG = FullPictureRecorder.class.getSimpleName();
    protected static final CameraLogger LOG = CameraLogger.create(TAG);

    public FullPictureRecorder(@NonNull PictureResult.Stub stub,
                               @Nullable PictureResultListener listener) {
        super(stub, listener);
    }
}
