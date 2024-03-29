package com.codevsolution.base.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.codevsolution.base.R;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.file.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.codevsolution.base.android.AppActivity.getAppContext;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;

public class SQLiteUtil {

    public static boolean exportDatabase(String databaseName, String backup) {

        String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

            if (sd.canWrite()) {
                System.out.println("nombre paquete" + AppActivity.getPackage());
                String currentDBPath = "//data//" + AppActivity.getPackage() + "//databases//" + idUser + "//" + databaseName + idUser + ".db ";
                String backupDBPath = timeStamp + "_backup_" + databaseName + ".db";
                if (backup != null) {
                    backupDBPath = backup;
                }
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                System.out.println("currentDB = " + currentDB.getAbsolutePath());

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    return true;
                } else {
                    System.out.println("no existe la BD a copiar");
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static void copiarBaseDatosAssets(String archivo, String prefijo) {

        String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERIDCODE, NULL);

        String ruta = AppActivity.getDirData() + AppActivity.getPackage() + "/databases/" + idUser + "/";
        File archivoDB = new File(ruta + prefijo + archivo);
        if (!archivoDB.exists()) {
            try {
                InputStream IS = getAppContext().getAssets().open(archivo);
                OutputStream OS = new FileOutputStream(archivoDB);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = IS.read(buffer)) > 0) {
                    OS.write(buffer, 0, length);
                }
                OS.flush();
                OS.close();
                IS.close();
            } catch (FileNotFoundException e) {
                Log.e("ERROR", "Archivo no encontrado, " + e.toString());
            } catch (IOException e) {
                Log.e("ERROR", "Error al copiar la Base de Datos, " + e.toString());
            }
        }
    }

    public static void copy(File source, File destination) throws IOException {

        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(destination).getChannel()) {
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean BD_backup(String basedatos, boolean instant) {

        String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERIDCODE, NULL);

        try {
            boolean ready = false;
            if (basedatos == null) {
                basedatos = AppActivity.getAppContext().getString(R.string.app_name);
            }
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
            final String inFileName = Environment.getDataDirectory().getPath() + "/data/" + AppActivity.getPackage() + "/databases/"
                    + basedatos + idUser + ".db";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = "/" + AppActivity.getAppContext().getString(R.string.app_name) + idUser + "/backupDB";

            File extFile = FileUtils.crearDirectorioPublico(outFileName, FileUtils.DOWNLOADS);

            System.out.println("extFile = " + extFile.getAbsolutePath());
            if (!extFile.exists()) {
                if (extFile.mkdir()) {
                    ready = true;
                } else {
                    System.out.println("No se ha podido crear el directorio externo");
                }
            } else {
                ready = true;
            }

            if (ready) {

                outFileName = extFile.getAbsolutePath();
                if (instant) {
                    outFileName += "/dbInstant" + idUser + ".db";
                } else {
                    outFileName += "/" + timeStamp + basedatos + idUser + ".db";
                }
                // Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No se ha podido realizar la copia de seguridad de la BD");
        return false;
    }

    public static boolean BD_backup(String basedatos, String idUser, boolean instant) {

        try {
            boolean ready = false;
            if (basedatos == null) {
                basedatos = AppActivity.getAppContext().getString(R.string.app_name);
            }
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
            final String inFileName = Environment.getDataDirectory().getPath() + "/data/" + AppActivity.getPackage() + "/databases/"
                    + basedatos + idUser + ".db";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = "/" + AppActivity.getAppContext().getString(R.string.app_name) + idUser + "/backupDB";

            File extFile = FileUtils.crearDirectorioPublico(outFileName, FileUtils.DOWNLOADS);

            System.out.println("extFile = " + extFile.getAbsolutePath());
            if (!extFile.exists()) {
                if (extFile.mkdir()) {
                    ready = true;
                } else {
                    System.out.println("No se ha podido crear el directorio externo");
                }
            } else {
                ready = true;
            }

            if (ready) {

                outFileName = extFile.getAbsolutePath();
                if (instant) {
                    outFileName += "/dbInstant" + idUser + ".db";
                } else {
                    outFileName += "/" + timeStamp + AppActivity.getAppContext().getString(R.string.app_name) + idUser + ".db";
                }
                // Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No se ha podido realizar la copia de seguridad de la BD");
        return false;
    }

    public static boolean restoreBD_backup(String basedatos, File fileToRestore) {

        String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);
        try {
            if (basedatos == null) {
                basedatos = AppActivity.getAppContext().getString(R.string.app_name);
            }
            FileInputStream fis = new FileInputStream(fileToRestore);

            String outFileName = Environment.getDataDirectory().getPath() + "/data/" + AppActivity.getPackage() + "/databases/"
                    + basedatos + idUser + ".db";
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No se ha podido restaurar la copia de seguridad de la BD");
        return false;
    }

    public static boolean restoreBD_backup(String basedatos, File fileToRestore, String idUser) {

        try {
            if (basedatos == null) {
                basedatos = AppActivity.getAppContext().getString(R.string.app_name);
            }
            FileInputStream fis = new FileInputStream(fileToRestore);

            String outFileName = Environment.getDataDirectory().getPath() + "/data/" + AppActivity.getPackage() + "/databases/"
                    + basedatos + idUser + ".db";
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No se ha podido restaurar la copia de seguridad de la BD");
        return false;
    }

    public static final String BASE_PATH = Environment.getExternalStoragePublicDirectory
            ("App_BackUp").getAbsolutePath();
    public static final String SEPARATOR = "/";
    private static boolean operationStatus = true;
    private static String dataDirectory = null;
    private static String appName = AppActivity.getAppContext().getString(R.string.app_name);

    public static boolean copyAppDataToLocal(String appName) {

        dataDirectory = getAppContext().getFilesDir() + "/data/" + AppActivity.getPackage();
        if (appName != null) {
            SQLiteUtil.appName = appName;
        }
        String TAG = "Developer_Option";
        try {
            if (dataDirectory != null) {
                copyAppData(new File(dataDirectory, "shared_prefs"), "shared_prefs");
                copyAppData(new File(dataDirectory, "files"), "files");
                copyAppData(new File(dataDirectory, "databases"), "databases");
            } else {
                Log.e(TAG, "!!!!!Unable to get data directory");
            }
        } catch (Exception ex) {
            Log.e(TAG, "!!!!@@@Exception Occurred while copying DATA--->" + ex.getMessage(), ex.fillInStackTrace());
            operationStatus = false;
        }

        return operationStatus;
    }

    private static void copyFileToStorage(String directoryName, String inFile, String fileName, boolean
            isDirectory, String subdirectoryName) {
        try {
            FileInputStream myInput = new FileInputStream(inFile);
            File out_dir;
            if (!isDirectory) {
                out_dir = new File(BASE_PATH + SEPARATOR + appName +
                        SEPARATOR + directoryName);
            } else {
                out_dir = new File(BASE_PATH + SEPARATOR + appName +
                        SEPARATOR + directoryName + SEPARATOR + subdirectoryName);
            }
            if (!out_dir.exists()) {
                operationStatus = out_dir.mkdirs();
            }
            String outFileName = out_dir + "/" + fileName;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer1 = new byte[1024];
            int length;
            while ((length = myInput.read(buffer1)) > 0) {
                myOutput.write(buffer1, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyAppData(File fileTypeToBeCopied, String outDirectoryName) {
        if (fileTypeToBeCopied.exists() && fileTypeToBeCopied.isDirectory()) {
            File[] files = fileTypeToBeCopied.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    copyFileToStorage(outDirectoryName, file.getAbsolutePath(), file.getName(), false, "");
                } else {
                    String folderName = file.getName();
                    File databaseDirsNew = new File(dataDirectory, outDirectoryName + "/" + folderName);
                    if (databaseDirsNew.exists() && databaseDirsNew.isDirectory()) {
                        File[] filesDB = databaseDirsNew.listFiles();
                        for (File file1 : filesDB) {
                            if (file1.isFile()) {
                                copyFileToStorage(outDirectoryName, file1.getAbsolutePath(), file1.getName(),
                                        true, folderName);
                            }
                        }
                    }
                }
            }
        }

    }

    public static boolean checkDataBase(String dataBasePath) {
        return new File(dataBasePath).exists();
    }

    public static boolean isTableExists(String nombreTabla, SQLiteDatabase db) {
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nombreTabla + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }

}
