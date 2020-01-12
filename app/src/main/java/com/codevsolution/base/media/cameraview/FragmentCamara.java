package com.codevsolution.base.media.cameraview;

import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codevsolution.base.R;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.media.cameraview.controls.Mode;
import com.codevsolution.base.media.cameraview.controls.Preview;
import com.codevsolution.base.media.cameraview.filter.Filters;
import com.codevsolution.base.media.cameraview.frame.Frame;
import com.codevsolution.base.media.cameraview.frame.FrameProcessor;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.codevsolution.base.media.cameraview.FragmentPicturePreview.setPictureResult;
import static com.codevsolution.base.media.cameraview.FragmentVideoPreview.setVideoResult;

public class FragmentCamara extends FragmentBase implements View.OnClickListener, OptionView.Callback {
    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    private final static CameraLogger LOG = CameraLogger.create("DemoApp");
    private final static boolean USE_FRAME_PROCESSOR = true;
    private final static boolean DECODE_BITMAP = true;

    private CameraView camera;
    private ViewGroup controlPanel;
    private long mCaptureTime;

    private int mCurrentFilter = 0;
    private final Filters[] mAllFilters = Filters.values();

    @Override
    protected void setLayout() {
        super.setLayout();
        layoutCuerpo = R.layout.activity_camera;
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);

        camera = view.findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.addCameraListener(new Listener());

        if (USE_FRAME_PROCESSOR) {
            camera.addFrameProcessor(new FrameProcessor() {
                private long lastTime = System.currentTimeMillis();

                @Override
                public void process(@NonNull Frame frame) {
                    long newTime = frame.getTime();
                    long delay = newTime - lastTime;
                    lastTime = newTime;
                    LOG.e("Frame delayMillis:", delay, "FPS:", 1000 / delay);
                    if (DECODE_BITMAP) {
                        if (frame.getFormat() == ImageFormat.NV21
                                && frame.getDataClass() == byte[].class) {
                            byte[] data = frame.getData();
                            YuvImage yuvImage = new YuvImage(data,
                                    frame.getFormat(),
                                    frame.getSize().getWidth(),
                                    frame.getSize().getHeight(),
                                    null);
                            ByteArrayOutputStream jpegStream = new ByteArrayOutputStream();
                            yuvImage.compressToJpeg(new Rect(0, 0,
                                    frame.getSize().getWidth(),
                                    frame.getSize().getHeight()), 100, jpegStream);
                            byte[] jpegByteArray = jpegStream.toByteArray();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(jpegByteArray,
                                    0, jpegByteArray.length);
                            //noinspection ResultOfMethodCallIgnored
                            bitmap.toString();
                        }
                    }
                }
            });
        }

        view.findViewById(R.id.edit).setOnClickListener(this);
        view.findViewById(R.id.capturePicture).setOnClickListener(this);
        view.findViewById(R.id.capturePictureSnapshot).setOnClickListener(this);
        view.findViewById(R.id.captureVideo).setOnClickListener(this);
        view.findViewById(R.id.captureVideoSnapshot).setOnClickListener(this);
        view.findViewById(R.id.toggleCamera).setOnClickListener(this);
        view.findViewById(R.id.changeFilter).setOnClickListener(this);

        controlPanel = view.findViewById(R.id.controls);
        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        final View watermark = view.findViewById(R.id.watermark);

        List<Option<?>> options = Arrays.asList(
                // Layout
                new Option.Width(), new Option.Height(),
                // Engine and preview
                new Option.Mode(), new Option.Engine(), new Option.Preview(),
                // Some controls
                new Option.Flash(), new Option.WhiteBalance(), new Option.Hdr(),
                new Option.PictureMetering(), new Option.PictureSnapshotMetering(),
                new Option.PictureFormat(),
                // Video recording
                new Option.PreviewFrameRate(), new Option.VideoCodec(), new Option.Audio(),
                // Gestures
                new Option.Pinch(), new Option.HorizontalScroll(), new Option.VerticalScroll(),
                new Option.Tap(), new Option.LongTap(),
                // Watermarks
                new Option.OverlayInPreview(watermark),
                new Option.OverlayInPictureSnapshot(watermark),
                new Option.OverlayInVideoSnapshot(watermark),
                // Frame Processing
                new Option.FrameProcessingFormat(),
                // Other
                new Option.Grid(), new Option.GridColor(), new Option.UseDeviceOrientation()
        );
        List<Boolean> dividers = Arrays.asList(
                // Layout
                false, true,
                // Engine and preview
                false, false, true,
                // Some controls
                false, false, false, false, false, true,
                // Video recording
                false, false, true,
                // Gestures
                false, false, false, false, true,
                // Watermarks
                false, false, true,
                // Frame Processing
                true,
                // Other
                false, false, true
        );
        for (int i = 0; i < options.size(); i++) {
            OptionView viewOpt = new OptionView(contexto);
            //noinspection unchecked
            viewOpt.setOption(options.get(i), this);
            viewOpt.setHasDivider(dividers.get(i));
            group.addView(viewOpt,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
                b.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // Animate the watermark just to show we record the animation in video snapshots
        ValueAnimator animator = ValueAnimator.ofFloat(1F, 0.8F);
        animator.setDuration(300);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                watermark.setScaleX(scale);
                watermark.setScaleY(scale);
                watermark.setRotation(watermark.getRotation() + 2);
            }
        });
        animator.start();

        activityBase.setCallbackBackPress(new MainActivityBase.CallbackBackPress() {
            @Override
            public boolean onPressBack() {

                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
                if (b.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    b.setState(BottomSheetBehavior.STATE_HIDDEN);
                    return false;
                }
                return true;
            }
        });
    }

    protected void message(@NonNull String content, boolean important) {
        if (important) {
            LOG.w(content);
            Toast.makeText(contexto, content, Toast.LENGTH_LONG).show();
        } else {
            LOG.i(content);
            Toast.makeText(contexto, content, Toast.LENGTH_SHORT).show();
        }
    }

    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {
            ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                OptionView view = (OptionView) group.getChildAt(i);
                view.onCameraOpened(camera, options);
            }
        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);
            if (camera.isTakingVideo()) {
                message("Captured while taking video. Size=" + result.getSize(), false);
                return;
            }

            // This can happen if picture was taken with a gesture.
            long callbackTime = System.currentTimeMillis();
            if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - mCaptureTime);
            setPictureResult(result);

            bundle.putLong("delay", callbackTime - mCaptureTime);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentPicturePreview());
            mCaptureTime = 0;
            LOG.w("onPictureTaken called! Launched activity.");
        }

        @Override
        public void onVideoTaken(@NonNull VideoResult result) {
            super.onVideoTaken(result);
            LOG.w("onVideoTaken called! Launching activity.");
            setVideoResult(result);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentVideoPreview());
            LOG.w("onVideoTaken called! Launched activity.");
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();
            LOG.w("onVideoRecordingStart!");
        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();
            message("Video taken. Processing...", false);
            LOG.w("onVideoRecordingEnd!");
        }

        @Override
        public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
            message("Exposure correction:" + newValue, false);
        }

        @Override
        public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
            message("Zoom:" + newValue, false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                edit();
                break;
            case R.id.capturePicture:
                capturePicture();
                break;
            case R.id.capturePictureSnapshot:
                capturePictureSnapshot();
                break;
            case R.id.captureVideo:
                captureVideo();
                break;
            case R.id.captureVideoSnapshot:
                captureVideoSnapshot();
                break;
            case R.id.toggleCamera:
                toggleCamera();
                break;
            case R.id.changeFilter:
                changeCurrentFilter();
                break;
        }
    }

    private void edit() {
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void capturePicture() {
        if (camera.getMode() == Mode.VIDEO) {
            message("Can't take HQ pictures while in VIDEO mode.", false);
            return;
        }
        if (camera.isTakingPicture()) return;
        mCaptureTime = System.currentTimeMillis();
        message("Capturing picture...", false);
        camera.takePicture();
    }

    private void capturePictureSnapshot() {
        if (camera.isTakingPicture()) return;
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Picture snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }
        mCaptureTime = System.currentTimeMillis();
        message("Capturing picture snapshot...", false);
        camera.takePictureSnapshot();
    }

    private void captureVideo() {
        if (camera.getMode() == Mode.PICTURE) {
            message("Can't record HQ videos while in PICTURE mode.", false);
            return;
        }
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        message("Recording for 5 seconds...", true);
        camera.takeVideo(new File(contexto.getFilesDir(), "video.mp4"), 5000);
    }

    private void captureVideo(String path) {
        if (camera.getMode() == Mode.PICTURE) {
            message("Can't record HQ videos while in PICTURE mode.", false);
            return;
        }
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        message("Recording...", true);
        camera.takeVideo(new File(path));
    }

    private void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            message("Already taking video.", false);
            return;
        }
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }
        message("Recording snapshot for 5 seconds...", true);
        camera.takeVideoSnapshot(new File(contexto.getFilesDir(), "video.mp4"), 5000);
    }

    private void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                message("Switched to back camera!", false);
                break;

            case FRONT:
                message("Switched to front camera!", false);
                break;
        }
    }

    private void changeCurrentFilter() {
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Filters are supported only when preview is Preview.GL_SURFACE.", true);
            return;
        }
        if (mCurrentFilter < mAllFilters.length - 1) {
            mCurrentFilter++;
        } else {
            mCurrentFilter = 0;
        }
        Filters filter = mAllFilters[mCurrentFilter];
        message(filter.toString(), false);

        // Normal behavior:
        camera.setFilter(filter.newInstance());

        // To test MultiFilter:
        // DuotoneFilter duotone = new DuotoneFilter();
        // duotone.setFirstColor(Color.RED);
        // duotone.setSecondColor(Color.GREEN);
        // camera.setFilter(new MultiFilter(duotone, filter.newInstance()));
    }

    @Override
    public <T> boolean onValueChanged(@NonNull Option<T> option, @NonNull T value, @NonNull String name) {
        if ((option instanceof Option.Width || option instanceof Option.Height)) {
            Preview preview = camera.getPreview();
            boolean wrapContent = (Integer) value == ViewGroup.LayoutParams.WRAP_CONTENT;
            if (preview == Preview.SURFACE && !wrapContent) {
                message("The SurfaceView preview does not support width or height changes. " +
                        "The view will act as WRAP_CONTENT by default.", true);
                return false;
            }
        }
        option.set(camera, value);
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_HIDDEN);
        message("Changed " + option.getName() + " to " + name, false);
        return true;
    }

    //region Permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }
}
