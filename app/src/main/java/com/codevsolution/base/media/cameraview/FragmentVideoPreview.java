package com.codevsolution.base.media.cameraview;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codevsolution.base.R;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.media.cameraview.size.AspectRatio;

public class FragmentVideoPreview extends FragmentBase {
    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    private VideoView videoView;

    private static VideoResult videoResult;

    public static void setVideoResult(@Nullable VideoResult result) {
        videoResult = result;
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        final VideoResult result = videoResult;
        if (result == null) {

        }

        videoView = view.findViewById(R.id.video);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });
        final MessageView actualResolution = view.findViewById(R.id.actualResolution);
        final MessageView isSnapshot = view.findViewById(R.id.isSnapshot);
        final MessageView rotation = view.findViewById(R.id.rotation);
        final MessageView audio = view.findViewById(R.id.audio);
        final MessageView audioBitRate = view.findViewById(R.id.audioBitRate);
        final MessageView videoCodec = view.findViewById(R.id.videoCodec);
        final MessageView videoBitRate = view.findViewById(R.id.videoBitRate);
        final MessageView videoFrameRate = view.findViewById(R.id.videoFrameRate);

        AspectRatio ratio = AspectRatio.of(result.getSize());
        actualResolution.setTitleAndMessage("Size", result.getSize() + " (" + ratio + ")");
        isSnapshot.setTitleAndMessage("Snapshot", result.isSnapshot() + "");
        rotation.setTitleAndMessage("Rotation", result.getRotation() + "");
        audio.setTitleAndMessage("Audio", result.getAudio().name());
        audioBitRate.setTitleAndMessage("Audio bit rate", result.getAudioBitRate() + " bits per sec.");
        videoCodec.setTitleAndMessage("VideoCodec", result.getVideoCodec().name());
        videoBitRate.setTitleAndMessage("Video bit rate", result.getVideoBitRate() + " bits per sec.");
        videoFrameRate.setTitleAndMessage("Video frame rate", result.getVideoFrameRate() + " fps");
        MediaController controller = new MediaController(contexto);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.fromFile(result.getFile()));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();
                float viewWidth = videoView.getWidth();
                lp.height = (int) (viewWidth * (videoHeight / videoWidth));
                videoView.setLayoutParams(lp);
                playVideo();

                if (result.isSnapshot()) {
                    // Log the real size for debugging reason.
                    Log.e("VideoPreview", "The video full size is " + videoWidth + "x" + videoHeight);
                }
            }
        });

    }

    @Override
    protected void setLayout() {
        super.setLayout();
        layoutCuerpo = R.layout.activity_video_preview;
    }

    protected void playVideo() {
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!activityBase.isChangingConfigurations()) {
            setVideoResult(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Toast.makeText(contexto, "Sharing...", Toast.LENGTH_SHORT).show();
            AppActivity.compartir(videoResult.getFile().getAbsolutePath(), "video/*");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
