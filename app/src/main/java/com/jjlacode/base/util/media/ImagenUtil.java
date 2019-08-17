package com.jjlacode.base.util.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.freelanceproject.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImagenUtil {

    static Context context = AppActivity.getAppContext();

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
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho, alto)
                .error(R.drawable.alert_box_r);

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
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho, alto)
                .circleCrop()
                .error(R.drawable.alert_box_r);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }


    public static void setImageFireStore(String rutafoto, final ImageView imagen) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        spaceRef.getStream().addOnCompleteListener(new OnCompleteListener<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<StreamDownloadTask.TaskSnapshot> task) {

                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                        .error(R.drawable.alert_box_r);
                Glide.with(context)
                        .load(spaceRef)
                        .apply(options)
                        .into(imagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUri(int recurso, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUri(int recurso, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUri(String uri, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho, alto)
                .error(R.drawable.alert_box_r);

        Glide.with(context).load(uri).thumbnail(0.25f).apply(options).into(imagen);
    }

    public static void setImageUri(int recurso, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho, alto)
                .error(R.drawable.alert_box_r);

        Glide.with(context).load(recurso).thumbnail(0.25f).apply(options).into(imagen);
    }

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen) {

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

    public static void setImageFireStoreCircle(String rutafoto, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(rutafoto);
        System.out.println("spaceRef = " + spaceRef);
        Glide.with(context).load(spaceRef).apply(options).into(imagen);

    }

    public static void setImageWebCircle(String url, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageWebCircle(String url, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(url).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(String uri, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(uri).apply(options).into(imagen);
    }

    public static void setImageUriCircle(int recurso, ImageView imagen, float multiplicador) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .sizeMultiplier(multiplicador)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }

    public static void setImageUriCircle(int recurso, ImageView imagen, int ancho, int alto) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .override(ancho, alto)
                .circleCrop()
                .error(R.drawable.ic_add_a_photo_black_24dp);

        Glide.with(context).load(recurso).apply(options).into(imagen);
    }
}
