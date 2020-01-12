package com.codevsolution.base.media.cameraview.video.encoding;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.codevsolution.base.media.cameraview.internal.utils.Pool;

/**
 * A simple {@link Pool(int, Factory)} implementation for output buffers.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class OutputBufferPool extends Pool<OutputBuffer> {

    OutputBufferPool(final int trackIndex) {
        super(Integer.MAX_VALUE, new Factory<OutputBuffer>() {
            @Override
            public OutputBuffer create() {
                OutputBuffer buffer = new OutputBuffer();
                buffer.trackIndex = trackIndex;
                buffer.info = new MediaCodec.BufferInfo();
                return buffer;
            }
        });
    }
}
