package com.codevsolution.base.media.cameraview.video.encoding;

import com.codevsolution.base.media.cameraview.internal.utils.Pool;

import java.nio.ByteBuffer;

/**
 * A simple {@link Pool(int, Factory)} implementation for byte buffers.
 */
class ByteBufferPool extends Pool<ByteBuffer> {

    ByteBufferPool(final int bufferSize, int maxPoolSize) {
        super(maxPoolSize, new Factory<ByteBuffer>() {
            @Override
            public ByteBuffer create() {
                return ByteBuffer.allocateDirect(bufferSize);
            }
        });
    }
}
