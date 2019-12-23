package com.codevsolution.base.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.codevsolution.base.android.AppActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    private ArrayList readList(Context context, String path) {

        FileInputStream fis = null;
        ArrayList list = null;
        try {
            fis = context.openFileInput(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void writeList(Context context, String path, ArrayList lista) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object readObject(Context context, String path) {

        FileInputStream fis = null;
        Object object = null;
        try {
            fis = context.openFileInput(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static void writeObject(Context context, String path, Object object) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList loadListJson(Context ctx, String path) {
        try {

            FileInputStream fis = ctx.openFileInput(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList>() {
            }.getType();
            return gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public static void saveListJson(Context ctx, String path, ArrayList list) {
        try {
            FileOutputStream fos = ctx.openFileOutput(path, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(list, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }

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

    public static List<File> getSpecificTypeOfFile(Context context, String[] extension) {
        List<String> fileUrls = new ArrayList<>();

        Uri fileUri = MediaStore.Files.getContentUri("external");
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
        if (cursor == null)
            return null;
        if (cursor.moveToLast()) {
            do {
                String data = cursor.getString(0);
                Log.d("tag", data);
                fileUrls.add(data);

            } while (cursor.moveToPrevious());
        }
        cursor.close();
        List<File> rets = new ArrayList<>();
        for (int i = 0; i < fileUrls.size(); i++) {
            File file = new File(fileUrls.get(i));
            rets.add(file);
        }
        Log.d("ccccccc", "getSpecificTypeOfFile: " + rets.size());
        return rets;
    }

    public static Intent openFile(String filePath) {


        File file = new File(filePath);
        if (!file.exists()) return null;
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    public static List<File> searchFileInDir(File dir, String fileName) {
        if (dir == null || !dir.isDirectory()) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
//                (str.indexOf("ABC")!=-1
                if (file.getName().toUpperCase().contains(fileName.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(searchFileInDir(file, fileName));
                }
            }
        }
        return list;
    }

}
