package com.codevsolution.base.media.cameraview;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codevsolution.base.R;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.media.cameraview.size.AspectRatio;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentPicturePreview extends FragmentBase {
    private String photoPath;
    private Uri photoUri;

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    private static PictureResult picture;

    public static void setPictureResult(@Nullable PictureResult pictureResult) {
        picture = pictureResult;
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        final PictureResult result = picture;
        if (result == null) {

        }

        final ImageView imageView = view.findViewById(R.id.image);
        final MessageView captureResolution = view.findViewById(R.id.nativeCaptureResolution);
        final MessageView captureLatency = view.findViewById(R.id.captureLatency);
        final MessageView exifRotation = view.findViewById(R.id.exifRotation);

        final long delay = bundle.getLong("delay", 0);
        AspectRatio ratio = AspectRatio.of(result.getSize());
        captureLatency.setTitleAndMessage("Approx. latency", delay + " milliseconds");
        captureResolution.setTitleAndMessage("Resolution", result.getSize() + " (" + ratio + ")");
        exifRotation.setTitleAndMessage("EXIF rotation", result.getRotation() + "");
        try {
            result.toBitmap(1000, 1000, new BitmapCallback() {
                @Override
                public void onBitmapReady(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (UnsupportedOperationException e) {
            imageView.setImageDrawable(new ColorDrawable(Color.GREEN));
            Toast.makeText(contexto, "Can't preview this format: " + picture.getFormat(),
                    Toast.LENGTH_LONG).show();
        }

        if (result.isSnapshot()) {
            // Log the real size for debugging reason.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length, options);
            if (result.getRotation() % 180 != 0) {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getHeight() + "x" + result.getSize().getWidth());
            } else {
                Log.e("PicturePreview", "The picture full size is " + result.getSize().getWidth() + "x" + result.getSize().getHeight());
            }
        }

        visible(frPie);
        btnback = view.findViewById(R.id.btn_back);
        btnsave = view.findViewById(R.id.btn_save);
        visible(btnback);
        visible(btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Imagen");
                    values.put(MediaStore.Images.Media.DESCRIPTION, System.currentTimeMillis());
                    photoUri = contexto.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    CameraUtils.writeToFile(picture.getData(), photoFile, new FileCallback() {
                        @Override
                        public void onFileReady(@Nullable File file) {
                            System.out.println("Imagen guardada = " + file.getPath());
                        }
                    });
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icFragmentos.pressBack(null);
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String extension;
        switch (picture.getFormat()) {
            case JPEG:
                extension = ".jpg";
                break;
            case DNG:
                extension = ".dng";
                break;
            default:
                throw new RuntimeException("Unknown format.");
        }
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                extension,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void setLayout() {
        super.setLayout();
        layoutCuerpo = R.layout.activity_picture_preview;
        layoutPie = R.layout.btn_sdb;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!activityBase.isChangingConfigurations()) {
            setPictureResult(null);
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
            String extension;
            switch (picture.getFormat()) {
                case JPEG:
                    extension = "jpg";
                    break;
                case DNG:
                    extension = "dng";
                    break;
                default:
                    throw new RuntimeException("Unknown format.");
            }
            File file = new File(contexto.getFilesDir(), "picture." + extension);
            CameraUtils.writeToFile(picture.getData(), file, new FileCallback() {
                @Override
                public void onFileReady(@Nullable File file) {
                    if (file != null) {
                        AppActivity.compartir(file.getAbsolutePath(), "image/*");
                    } else {
                        Toast.makeText(contexto,
                                "Error while writing file.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
