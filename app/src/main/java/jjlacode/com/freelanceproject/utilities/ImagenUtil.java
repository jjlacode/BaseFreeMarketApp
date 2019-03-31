package jjlacode.com.freelanceproject.utilities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
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
import android.support.v4.content.CursorLoader;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ImagenUtil {


    private Context context;

    private String photoPath;

    private Uri photoUri;

    private ClipData clipData;
    private Uri uri;

    public ImagenUtil(Context context){

        this.context = context;
    }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
        }

        public Intent takePhotoIntent() throws IOException {
                Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (in.resolveActivity(context.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = createImageFile();

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    }
                }
            return in;
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            photoPath = image.getAbsolutePath();
            return image;
        }

        public void addToGallery() {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(photoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
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

        public String getPath() {

            String path;
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11) {
                path = getRealPathFromURI_BelowAPI11(context, photoUri);
            }

            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19){
                path = getRealPathFromURI_API11to18(context, photoUri);
            }
            // SDK > 19 (Android 4.4)
            else
                path = getRealPathFromURI_API19(context, photoUri);

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
            this.height = width;
            this.width = height;
            return instance;
        }


        public Bitmap getBitmap() throws FileNotFoundException {

            File file = new File(filePath);
            if(!file.exists()){
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
}
