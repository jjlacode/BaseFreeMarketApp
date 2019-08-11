package com.jjlacode.freelanceproject.util.media;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjlacode.freelanceproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediaUtil {


    private Context context;

    private String photoPath;

    private Uri photoUri;

    private ClipData clipData;
    private Uri uri;

    private int ancho = 128;

    private int alto = 128;
    private Uri videoUri;
    private Uri audioUri;
    private String videoPath;
    private String audioPath;

    public MediaUtil(Context context){

        this.context = context;
    }

    public void setDims(int alto, int ancho, String uri, ImageView imagen){

        this.alto = alto;
        this.ancho = ancho;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho,alto)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public Uri getVideoUri(){
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public Uri getAudioUri(){
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public Intent takePhotoIntent() throws IOException {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activityBase to handle the intent
        if (in.resolveActivity(context.getPackageManager()) != null) {

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
                photoUri = context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }

        }
        return in;
    }

    public Intent recordVideoIntent() throws IOException {
        Intent in = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activityBase to handle the intent
        if (in.resolveActivity(context.getPackageManager()) != null) {

            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {


                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.TITLE, "Video");
                values.put(MediaStore.Video.Media.DESCRIPTION, System.currentTimeMillis());
                videoUri = context.getContentResolver().insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                in.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            }

        }
        return in;
    }

    public Intent recordAudioIntent() throws IOException {
        Intent in = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        // Ensure that there's a camera activityBase to handle the intent
        if (in.resolveActivity(context.getPackageManager()) != null) {

            File audioFile = null;
            try {
                audioFile = createAudioFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (audioFile != null) {


                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.TITLE, "Audio_" + System.currentTimeMillis());
                audioUri = context.getContentResolver().insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
                in.putExtra(MediaStore.EXTRA_OUTPUT, audioUri);
            }

        }
        return in;
    }

    public Uri getCanonicalizeUri(Uri uri){

        return context.getContentResolver().canonicalize(uri);
    }

    public Uri getPhotoUri(){

        return photoUri;
    }
    public Bitmap getBitmap(){

        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void setImageFireStore(String rutafoto, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .error(drawable);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);

    }

    public void setImageWeb(String url, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public void setImageUri(String uri, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }


    public void setImageFireStoreCircle(String rutafoto, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .circleCrop()
                .error(drawable);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .into(imagen);

    }

    public void setImageWebCircle(String url, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public void setImageUriCircle(String uri, ImageView imagen, int drawable){

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }
    public void setImageFireStore(String rutafoto, ImageView imagen){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .into(imagen);

    }

    public void setImageWeb(String url, ImageView imagen){

        Glide.with(context).load(url).into(imagen);
    }

    public void setImageUri(String uri, ImageView imagen){

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }


    public void setImageFireStoreCircle(String rutafoto, ImageView imagen){

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public void setImageWebCircle(String url, ImageView imagen){

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public void setImageUriCircle(String uri, ImageView imagen){

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }



    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VID_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        videoPath = video.getAbsolutePath();
        return video;
    }

    private File createAudioFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "AUD_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File audio = File.createTempFile(
                audioFileName,  /* prefix */
                ".mp3",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        audioPath = audio.getAbsolutePath();
        return audio;
    }

    public File getAudioFile(String path){

        File audio = new File(path);
        audioPath = audio.getAbsolutePath();
        return  audio;
    }

    public void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void addVideoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(videoUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void addAudioToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(audioUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public Intent openGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, getChooserTitle());
    }

    public String getChooserTitle(){
        return "Select Pictures";
    }

    public String getPath(Uri uri) {

        String path;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            path = getRealPathFromURI_BelowAPI11(context, uri);
        }

        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19){
            path = getRealPathFromURI_API11to18(context, uri);
        }
        // SDK > 19 (Android 4.4)
        else
            path = getRealPathFromURI_API19(context, uri);

        return path;
    }


    public void setPhotoData(ClipData clipData, Uri uri) {
        this.clipData = clipData;
        this.uri = uri;
    }


    public Intent openMultiPhotoGalleryIntent() throws IllegalAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            return Intent.createChooser(intent, getChooserTitle());
        }
        else{
            throw new IllegalAccessException("This feature of multiple images selection is only available for KITKAT (API 19) or above.");
        }
    }

    public List<String> getPhotoPathList() throws IllegalAccessException {
        List<String> pathList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(clipData != null){
                for(int i=0; i<clipData.getItemCount(); i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri u = item.getUri();
                    String path = getRealPathFromURI_API19(context, u);
                    pathList.add(path);
                }
            }
            else{
                String path = getRealPathFromURI_API19(context, uri);
                pathList.add(path);
            }
        }
        else{
            throw new IllegalAccessException("This feature of multiple images selection is only available for KITKAT (API 19) or above.");
        }

        return pathList;
    }


    public String encode(Bitmap bitmap){
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int quality = 100; //100: compress nothing
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);

        if(bitmap != null && !bitmap.isRecycled()){//important! prevent out of memory
            bitmap.recycle();
            bitmap = null;
        }

        byte [] ba = bao.toByteArray();
        String encodedImage = Base64.encodeToString(ba, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap decode(String encodedImage){
        final String pureBase64Encoded = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.URL_SAFE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }


    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(Context context, Uri photoUri){
        String path = "";
        if (DocumentsContract.isDocumentUri(context, photoUri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(photoUri)) {
                final String docId = DocumentsContract.getDocumentId(photoUri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(photoUri)) {

                final String id = DocumentsContract.getDocumentId(photoUri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                path = getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(photoUri)) {
                final String docId = DocumentsContract.getDocumentId(photoUri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(photoUri.getScheme())) {
            path = getDataColumn(context, photoUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(photoUri.getScheme())) {
            path = photoUri.getPath();
        }
        return path;
    }


    @SuppressLint("NewApi")
    public String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    public String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String path = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static class ImageLoader {

        private String filePath;

        private static ImageLoader instance;

        private int width = 128, height = 128; //default

        protected ImageLoader(){
        }


        public static ImageLoader init(){
            if(instance == null){
                synchronized (ImageLoader.class) {
                    if(instance == null){
                        instance = new ImageLoader();
                    }
                }
            }
            return instance;
        }

        public ImageLoader from(String filePath) {
            this.filePath = filePath;
            return instance;
        }

        public ImageLoader requestSize(int width, int height) {
            this.height = height;
            this.width = width;
            return instance;
        }


        public Bitmap getBitmap() throws FileNotFoundException {

            if (filePath!=null) {
                File file = new File(filePath);
                if (!file.exists()) {
                    throw new FileNotFoundException();
                }

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(filePath, options);

                options.inSampleSize = calculateInSampleSize(options, width, height);

                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                return bitmap;
            }
            return null;
        }

        public Drawable getImageDrawable() throws FileNotFoundException{
            File file = new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException();
            }
            Drawable drawable = Drawable.createFromPath(filePath);
            return drawable;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

    public class PhotoLoader extends ImageLoader {
    }

    public static class MyVideoView extends VideoView {

        private int mVideoWidth;
        private int mVideoHeight;

        public MyVideoView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public MyVideoView(Context context) {
            super(context);
        }

        public void setVideoSize(int width, int height) {
            mVideoWidth = width;
            mVideoHeight = height;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Log.i("@@@", "onMeasure");
            int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
            int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (mVideoWidth * height > width * mVideoHeight) {
                    // Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height < width * mVideoHeight) {
                    // Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else {
                    // Log.i("@@@", "aspect ratio is correct: " +
                    // width+"/"+height+"="+
                    // mVideoWidth+"/"+mVideoHeight);
                }
            }
            // Log.i("@@@", "setting sizeLista: " + width + 'x' + height);
            setMeasuredDimension(width, height);
        }
    }

    /*
    public void mostrarDialogoOpcionesImagen() {

        final CharSequence[] opciones = {"Imagen del proyecto relacionado","Hacer foto desde cámara",
                "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MediaUtil = new MediaUtil(getContext());

                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        startActivityForResult(MediaUtil.takePhotoIntent(), COD_FOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MediaUtil.addPhotoToGallery();

                } else if (opciones[which].equals("Elegir de la galería")) {

                    startActivityForResult(MediaUtil.openGalleryIntent(), COD_SELECCIONA);

                }else if (opciones[which].equals("Imagen del proyecto relacionado")) {

                    if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null) {
                        path = proyecto.getString(PROYECTO_RUTAFOTO);
                        imagen.setImageURI(Uri.parse(path));
                    }

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String photoPath;

        switch (requestCode) {

            case COD_SELECCIONA:
                MediaUtil.setPhotoUri(data.getData());
                photoPath = MediaUtil.getPath();
                try {
                    Bitmap bitmap = MediaUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap);
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                photoPath = MediaUtil.getPhotoPath();
                try {
                    Bitmap bitmap = MediaUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap); //imageView is your ImageView
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


     */

    public static void eliminarArchivosPorExtension(String path, final String extension){

        File[] archivos = new File(path).listFiles(new FileFilter() {

            public boolean accept(File archivo) {

                if (archivo.isFile())

                return archivo.getName().endsWith('.' + extension);

                return false;

            }

        });

        for (File archivo : archivos) {

            boolean res = archivo.delete();

        }

    }


}
