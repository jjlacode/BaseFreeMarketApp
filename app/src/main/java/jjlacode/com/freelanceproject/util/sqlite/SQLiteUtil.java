package jjlacode.com.freelanceproject.util.sqlite;

import android.os.Environment;
import android.util.Log;

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

import jjlacode.com.freelanceproject.util.android.AppActivity;

import static jjlacode.com.freelanceproject.util.android.AppActivity.getAppContext;

public class SQLiteUtil {

    public static void exportDatabase(String databaseName, String backup) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + AppActivity.getPackage() + "//databases//" + databaseName + ".db ";
                String backupDBPath = timeStamp + "backup" + databaseName + ".db";
                if (backup != null) {
                    backupDBPath = backup;
                }
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public static void copiarBaseDatosAssets(String archivo, String prefijo) {
        String ruta = "/data/data/" + AppActivity.getPackage() + "/databases/";
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

}
