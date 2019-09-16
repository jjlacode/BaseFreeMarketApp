package com.codevsolution.base.file;

import android.content.Context;
import android.os.Environment;

import com.codevsolution.base.android.AppActivity;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

    public final static String PUBLICEXT = "externo_publico";
    public final static String PRIVATEEXT = "externo_privado";
    public final static String INTERN = "interno";
    public final static String DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
    public final static String PICTURES = Environment.DIRECTORY_PICTURES;
    public final static String DOCUMENTS = Environment.DIRECTORY_DOCUMENTS;

    public static File crearDirectorioPublico(String nombreDirectorio, String carpetaRaiz) {

        File directorio = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            //Crear directorio público en la carpeta Pictures.
            directorio = new File(Environment.getExternalStoragePublicDirectory(carpetaRaiz), nombreDirectorio);
            //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
            if (!directorio.exists()) {
                if (!directorio.mkdirs())
                    System.out.println("Error: No se creo el directorio público externo");
            }
        }
        return directorio;
    }

    public static File crearDirectorioPrivado(Context context, String nombreDirectorio, String carpetaRaiz) {

        File directorio = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            //Crear directorio privado en la carpeta Pictures.
            directorio = new File(
                    context.getExternalFilesDir(carpetaRaiz),
                    nombreDirectorio);
            //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
            if (!directorio.exists()) {
                if (!directorio.mkdirs())
                    System.out.println("Error: No se creo el directorio privado externo");
            }
        }
        return directorio;
    }

    public static File crearCarpetaAlmInterno(String nombreDirectorio) {
        //Crear directorio en la memoria interna.
        File directorio = new File(AppActivity.getAppContext().getFilesDir(), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.exists()) {

            if (!directorio.mkdirs()) {
                System.out.println("Error: No se creo el directorio privado Interno");
            }
        }
        return directorio;
    }

    public static ArrayList<String> getListFiles(String directorio, String carpetaRaiz, String storage) {

        ArrayList<String> item = new ArrayList<String>();
        File f = null;
        File[] files = null;

        if (storage.equals(PUBLICEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(Environment.getExternalStoragePublicDirectory(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(PRIVATEEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(AppActivity.getAppContext().getExternalFilesDir(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(INTERN)) {
            f = new File(AppActivity.getAppContext().getFilesDir() + "/" + directorio + "/");
            files = f.listFiles();
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (!file.isDirectory())

                item.add(file.getName());

        }

        return item;
    }

    public static ArrayList<String> getListDirectory(String directorio, String carpetaRaiz, String storage) {

        ArrayList<String> item = new ArrayList<String>();
        File f = null;
        File[] files = null;

        if (storage.equals(PUBLICEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(Environment.getExternalStoragePublicDirectory(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(PRIVATEEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(AppActivity.getAppContext().getExternalFilesDir(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(INTERN)) {
            f = new File(AppActivity.getAppContext().getFilesDir() + "/" + directorio + "/");
            files = f.listFiles();
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory())

                item.add(file.getName() + "/");

        }

        return item;
    }

    public static ArrayList<String> getListDirectoryFiles(String directorio, String carpetaRaiz, String storage) {

        ArrayList<String> item = new ArrayList<String>();
        File f = null;
        File[] files = null;

        if (storage.equals(PUBLICEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(Environment.getExternalStoragePublicDirectory(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(PRIVATEEXT) && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            f = new File(AppActivity.getAppContext().getExternalFilesDir(carpetaRaiz) + "/" + directorio + "/");
            files = f.listFiles();
        } else if (storage.equals(INTERN)) {
            f = new File(AppActivity.getAppContext().getFilesDir() + "/" + directorio + "/");
            files = f.listFiles();
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory())

                item.add(file.getName() + "/");

            else

                item.add(file.getName());
        }

        return item;
    }
}
