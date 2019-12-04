package com.codevsolution.base.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_RUTAFOTO;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PATH;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PERSISTENCIA;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.TSIMG;

public class ImagenUtil {

    static Context context = AppActivity.getAppContext();
    static int logo = R.drawable.logo;
    static int error = R.drawable.logo;


    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmap(ImageView imagen) {

        return ((BitmapDrawable) imagen.getDrawable()).getBitmap();
    }

    public static Bitmap getScaledBitmap(ImageView imagen, double factorAncho, double factorAlto, boolean ampliar) {

        Bitmap image = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        if (ampliar) {

            return Bitmap.createScaledBitmap(image, Math.round((int) ((double) image.getWidth() * factorAncho)),
                    Math.round((int) ((double) image.getHeight() * factorAlto)), false);

        }
        return Bitmap.createScaledBitmap(image, Math.round((int) ((double) image.getWidth() / factorAncho)),
                Math.round((int) ((double) image.getHeight() / factorAlto)), false);

    }

    public static Bitmap getScaledBitmap(ImageView imagen, int nuevoAncho, int nuevoAlto) {

        Bitmap image = ((BitmapDrawable) imagen.getDrawable()).getBitmap();

        return Bitmap.createScaledBitmap(image, nuevoAncho,
                nuevoAlto, false);

    }

    public static Bitmap getScaledBitmap(ImageView imagen, double factor, boolean ampliar) {

        Bitmap image = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        if (ampliar) {

            return Bitmap.createScaledBitmap(image, Math.round((int) ((double) image.getWidth() * factor)),
                    Math.round((int) ((double) image.getHeight() * factor)), false);

        }
        return Bitmap.createScaledBitmap(image, Math.round((int) ((double) image.getWidth() / factor)),
                Math.round((int) ((double) image.getHeight() / factor)), false);

    }


    public static ByteArrayInputStream bitmapToInputStream(ImageView imagen, double factorAncho, double factorAlto) {

        Bitmap imageScaled = getScaledBitmap(imagen, factorAncho, factorAlto, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageScaled.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        return new ByteArrayInputStream(bitmapdata);

    }

    public static ByteArrayInputStream bitmapToInputStream(ImageView imagen, int nuevoAncho, int nuevoAlto) {

        Bitmap imageScaled = getScaledBitmap(imagen, nuevoAncho, nuevoAlto);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageScaled.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        return new ByteArrayInputStream(bitmapdata);

    }

    public static ByteArrayInputStream bitmapToInputStream(ImageView imagen, double factorReduccion) {

        Bitmap imageScaled = getScaledBitmap(imagen, factorReduccion, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageScaled.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        return new ByteArrayInputStream(bitmapdata);

    }

    public static void setImageFireStore(String rutafoto, ImageView imagen, int drawable) {

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

    public static void setImageFireStore(String rutafoto, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
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

    public static void setImageFireStore(String rutafoto, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .override(ancho, alto)
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);

    }

    public static void setImageFireStore(String rutafoto, ImageView imagen, int ancho, int alto, boolean cache) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(cache)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .override(ancho, alto)
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);

    }


    public static void setImageWeb(String url, ImageView imagen, int drawable) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageWeb(String url, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImage(String uri, ImageView imagen) {

        Glide.with(context).load(uri).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, int drawable) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }


    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, int drawable) {

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

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
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

    public static void setImageWebCircle(String url, ImageView imagen, int drawable) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageWebCircle(String url, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen, int drawable) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen, int drawable, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen, int drawable, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .placeholder(drawable)
                .override(ancho, alto)
                .circleCrop()
                .error(drawable);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .error(error)
                .override(ancho, alto)
                .circleCrop();

        Glide.with(context).load(uri).apply(options).into(imagen);
    }


    public static void setImageFireStore(String rutafoto, final ImageView imagen) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error)
                .placeholder(logo);
        Glide.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);
    }

    public static void setImageFireStore(String rutafoto, final ImageView imagen, boolean cache) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(cache)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error)
                .placeholder(logo);
        Glide.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);
    }

    public static void setImageFireStore(String rutafoto, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .sizeMultiplier(multiplicador);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        GlideApp.with(context)
                .load(spaceRef)
                .apply(options)
                .into(imagen);

    }

    public static void setImageWeb(String url, ImageView imagen) {

        Glide.with(context).load(url).into(imagen);
    }

    public static void setImageWeb(String url, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .sizeMultiplier(multiplicador);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .error(error);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .error(error);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUri(int recurso, ImageView imagen, float multiplicador) {

        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(logo)
                    .sizeMultiplier(multiplicador)
                    .error(error);

            Glide.with(context).load(recurso).apply(options).into(imagen);
        } catch (Exception e) {
            imagen.setImageResource(recurso);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    public static void setImageUri(Context context, int recurso, ImageView imagen, float multiplicador) {

        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(logo)
                    .sizeMultiplier(multiplicador)
                    .error(error);

            Glide.with(context).load(recurso).apply(options).into(imagen);
        } catch (Exception e) {
            imagen.setImageResource(recurso);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    public static void setImageUri(int recurso, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .error(error);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(logo)
                .error(error)
                .override(ancho, alto);

        Glide.with(context).load(uri).thumbnail(0.25f).apply(options).into(imagen);
    }

    public static void setImageUri(int recurso, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(logo)
                .error(error)
                .override(ancho, alto);

        Glide.with(context).load(recurso).thumbnail(0.25f).apply(options).into(imagen);
    }

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .circleCrop()
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, boolean cache) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(cache)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .circleCrop()
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, float multiplicador, boolean cache) {

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(cache)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(error);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public static void setImageWebCircle(String url, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .circleCrop()
                .error(error);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageWebCircle(String url, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(error);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .circleCrop()
                .error(error);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageDrawableCircle(Drawable bitmap, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .circleCrop()
                .error(error);

        Glide.with(context).load(bitmap).apply(options).into(imagen);
    }
    public static void setImageUriCircle(String uri, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(error);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(int recurso, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .circleCrop()
                .error(error);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUriCircle(int recurso, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(error);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUriCircle(int recurso, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .placeholder(logo)
                .override(ancho, alto)
                .circleCrop()
                .error(error);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void deleteImagefirestore(final String path) {

        Thread tn = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(path);
                storageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("imagen firestore borrada");
                        } else {
                            System.out.println("borrar imagen firestore.getException() = " + task.getException());
                        }
                    }
                });
            }
        });
        tn.start();

    }

    public static void copyImageFirestore(final String pathsource, final String pathTarget) {

        Thread tn = new Thread(new Runnable() {
            @Override
            public void run() {
                final FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRefOrigen = storage.getReference().child(pathsource);
                File foto = null;
                try {
                    foto = createImageFile();
                    final File finalFoto = foto;
                    storageRefOrigen.getFile(foto).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Cargada imagen origen");
                                StorageReference storageRefTarget = storage.getReference().child(pathTarget);
                                System.out.println("finalFoto = " + finalFoto.getAbsolutePath());
                                storageRefTarget.putFile(Uri.fromFile(finalFoto)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            System.out.println("Copiada imagen firestore");
                                            if (finalFoto.delete()) {
                                                System.out.println("foto tmp borrada");
                                            } else {
                                                System.out.println("Error al borrar foto tmp");
                                            }
                                        } else {
                                            System.out.println("Copiar imagen firestore.getException() = " + task.getException());
                                        }
                                    }
                                });


                            } else {
                                System.out.println("Copiar imagen firestore.getException() = " + task.getException());
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        tn.start();


    }

    public static void copyImageFirestoreToCrud(final String pathsource, final ModeloSQL modeloSQL, final String sufijoCampo) {

        Thread tn = new Thread(new Runnable() {
            @Override
            public void run() {
                final FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRefOrigen = storage.getReference().child(pathsource);
                File foto = null;
                try {
                    foto = createImageFile();
                    final File finalFoto = foto;
                    storageRefOrigen.getFile(foto).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Cargada imagen origen");

                                CRUDutil.actualizarCampo(modeloSQL, CAMPO_RUTAFOTO + sufijoCampo, finalFoto.getAbsolutePath());
                                System.out.println("Copiada imagen firestore");

                            } else {
                                System.out.println("Copiar imagen firestore.getException() = " + task.getException());
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        tn.start();


    }

    public static void guardarImageFirestore(final String pathFirestore, final ViewImagenLayout imagen, String path) {

        if (path != null && !path.equals(NULL) && !path.isEmpty()) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference().child(pathFirestore);

            UploadTask uploadTask = storageRef.putStream(imagen.getInputStreamAuto(path));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(context, "Imagen subida OK", Toast.LENGTH_SHORT).show();
                    AndroidUtil.setSharePreference(context, PERSISTENCIA, PATH, NULL);
                    AndroidUtil.setSharePreference(context, PERSISTENCIA, TSIMG, TimeDateUtil.ahora());
                    if (!pathFirestore.equals("") && !pathFirestore.equals(NULL)) {

                        imagen.setImageFirestore(pathFirestore, true);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(context, "Fallo al subir imagen", Toast.LENGTH_SHORT).show();
                    AndroidUtil.setSharePreference(context, PERSISTENCIA, PATH, NULL);

                }
            });
        }
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
