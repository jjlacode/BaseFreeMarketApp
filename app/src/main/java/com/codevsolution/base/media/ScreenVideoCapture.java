package com.codevsolution.base.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.controls.NestedScrollCoordinatorLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.Contador;
import com.codevsolution.freemarketsapp.R;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScreenVideoCapture extends Fragment {

    private ImageView btnRec;
    private ImageView btnPlay;
    private ImageView btnStop;
    private ImageView btnPause;
    private TextView estado;
    private ProgressBar bar;
    private boolean grabando;
    private boolean reproduciendo;
    private File archivo;
    private String path;
    private Context context;
    private View view;
    private Listeners listener;
    private boolean land;
    private AppCompatActivity activity;
    private boolean multiPanel;
    private boolean tablet;
    private int ancho;
    private int alto;
    private int leng;
    private Contador contador;
    private ViewGroupLayout vistaAudioVideoControl;
    private MediaProjectionManager mMediaProjectionManager;
    private static final int REQUEST_CODE_CAPTURE_PERM = 1234;
    private MediaProjection mMediaProjection;
    private static final String VIDEO_MIME_TYPE = "video/avc";
    private static final int VIDEO_WIDTH = 1280;
    private static final int VIDEO_HEIGHT = 720;
    // …
    private boolean mMuxerStarted = false;
    private Surface mInputSurface;
    private MediaMuxer mMuxer;
    private MediaCodec mVideoEncoder;
    private MediaCodec.BufferInfo mVideoBufferInfo;
    private int mTrackIndex = -1;


    public ScreenVideoCapture(AppCompatActivity activity, View view, Context context, ViewGroup viewGroup) {

        this.view = view;
        this.context = context;
        this.activity = activity;
        mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
        crearLayout(viewGroup);

    }

    public void init() {

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        land = context.getResources().getBoolean(R.bool.esLand) && multiPanel;
        tablet = context.getResources().getBoolean(R.bool.esTablet);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        if (!land) {
            btnPlay.setMinimumHeight((int) ((double) alto / 4));
            btnPlay.setMinimumWidth(ancho);
        } else {
            btnPlay.setMinimumWidth((int) ((double) ancho / 4));
            btnPlay.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnRec.setMinimumHeight((int) ((double) alto / 4));
            btnRec.setMinimumWidth(ancho);
        } else {
            btnRec.setMinimumWidth((int) ((double) ancho / 4));
            btnRec.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnStop.setMinimumHeight((int) ((double) alto / 4));
            btnStop.setMinimumWidth(ancho);
        } else {
            btnStop.setMinimumWidth((int) ((double) ancho / 4));
            btnStop.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnPause.setMinimumHeight((int) ((double) alto / 4));
            btnPause.setMinimumWidth(ancho);
        } else {
            btnPause.setMinimumWidth((int) ((double) ancho / 4));
            btnPause.setMinimumHeight((int) ((double) alto / 4));
        }

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grabar();
                btnStop.setVisibility(View.VISIBLE);
                btnRec.setVisibility(View.GONE);

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reproducir();
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detener();

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contador.pause();
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);


            }
        });


        if (path == null) {
            btnRec.setVisibility(View.VISIBLE);
        } else {
            btnPlay.setVisibility(View.VISIBLE);
        }
    }

    private void crearLayout(ViewGroup viewGroup) {

        viewGroup.removeAllViewsInLayout();
        NestedScrollCoordinatorLayout nested = new NestedScrollCoordinatorLayout(context);
        nested.setPassMode(NestedScrollCoordinatorLayout.PASS_MODE_PARENT_FIRST);
        viewGroup.addView(nested);
        ViewGroupLayout vistaForm = new ViewGroupLayout(context, nested);
        estado = vistaForm.addTextView("");

        vistaAudioVideoControl = new ViewGroupLayout(context, vistaForm.getViewGroup());
        vistaAudioVideoControl.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        btnRec = vistaAudioVideoControl.addImageView(R.drawable.ic_rec_indigo, 1);
        btnStop = vistaAudioVideoControl.addImageView(R.drawable.ic_stop_indigo, 1);
        btnPause = vistaAudioVideoControl.addImageView(R.drawable.ic_pausa_indigo, 1);
        btnPlay = vistaAudioVideoControl.addImageView(R.drawable.ic_play_indigo, 1);
        bar = (ProgressBar) vistaForm.addVista(new ProgressBar(context, null,
                Estilos.getIdStyle(context, "Widget.Holo.ProgressBar.Horizontal")));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        System.out.println("requestCode = " + requestCode);
        if (REQUEST_CODE_CAPTURE_PERM == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, intent);
                startRecording(); // defined below
            } else {
                // user did not grant permissions
            }
        }
    }

    private final Handler mDrainHandler = new Handler(Looper.getMainLooper());
    private Runnable mDrainEncoderRunnable = new Runnable() {
        @Override
        public void run() {
            drainEncoder();
        }
    };
// …

    private void startRecording() {
        DisplayManager dm = (DisplayManager) activity.getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        if (defaultDisplay == null) {
            throw new RuntimeException("No display found.");
        }
        prepareVideoEncoder();
        File pathAudio = new File(Environment.getExternalStorageDirectory().getPath());
        try {
            archivo = File.createTempFile("video", ".mp4", pathAudio);
            path = archivo.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }

        // Get the display size and density.
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int screenDensity = metrics.densityDpi;

        // Start the video input.
        mMediaProjection.createVirtualDisplay("Recording Display", screenWidth,
                screenHeight, screenDensity, 0 /* flags */, mInputSurface,
                null /* callbackDatos */, null /* handler */);

        // Start the encoders
        drainEncoder();

        grabando = true;
        estado.setText(context.getString(R.string.grabando));
        listener.onRec(path);
        btnRec.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        estado.setVisibility(View.VISIBLE);

    }

    private void prepareVideoEncoder() {
        mVideoBufferInfo = new MediaCodec.BufferInfo();
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
        int frameRate = 30; // 30 fps

        // Set some required properties. The media codec may fail if these aren't defined.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / frameRate);
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds between I-frames

        // Create a MediaCodec encoder and configure it. Get a Surface we can use for recording into.
        try {
            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mInputSurface = mVideoEncoder.createInputSurface();
            mVideoEncoder.start();
        } catch (IOException e) {
            e.printStackTrace();
            releaseEncoders();
        }
    }

    private boolean drainEncoder() {
        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
        while (true) {
            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, 0);

            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // nothing available yet
                break;
            } else if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                if (mTrackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                mTrackIndex = mMuxer.addTrack(mVideoEncoder.getOutputFormat());
                if (!mMuxerStarted && mTrackIndex >= 0) {
                    mMuxer.start();
                    mMuxerStarted = true;
                }
            } else if (bufferIndex < 0) {
                // not sure what's going on, ignore it
            } else {
                ByteBuffer encodedData = mVideoEncoder.getOutputBuffer(bufferIndex);
                if (encodedData == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + bufferIndex);
                }

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    mVideoBufferInfo.size = 0;
                }

                if (mVideoBufferInfo.size != 0) {
                    if (mMuxerStarted) {
                        encodedData.position(mVideoBufferInfo.offset);
                        encodedData.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
                        mMuxer.writeSampleData(mTrackIndex, encodedData, mVideoBufferInfo);
                    } else {
                        // muxer not started
                        System.out.println("Muxer not started");
                    }
                }

                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }

        mDrainHandler.postDelayed(mDrainEncoderRunnable, 10);
        return false;
    }

    private void releaseEncoders() {
        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
        if (mMuxer != null) {
            if (mMuxerStarted) {
                mMuxer.stop();
            }
            mMuxer.release();
            mMuxer = null;
            mMuxerStarted = false;
        }
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        mVideoBufferInfo = null;
        mDrainEncoderRunnable = null;
        mTrackIndex = -1;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setEstado(int rEstado) {
        estado = view.findViewById(rEstado);
    }

    public void grabar() {

        Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
        activity.startActivityForResult(permissionIntent, REQUEST_CODE_CAPTURE_PERM);

    }

    public void detener() {

        if (grabando) {
            grabando = false;
            releaseEncoders();
            listener.onEndRec();

        }

    }

    public void reproducir() {


    }

    public void setListener(Listeners listener) {
        this.listener = listener;
    }

    public interface Listeners {

        void onRec(String path);

        void onEndRec();

    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        // Determinar que siempre sera multipanel
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
    }

    private boolean checkCameraHardware(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}