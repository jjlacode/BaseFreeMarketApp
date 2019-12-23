package com.codevsolution.base.sqlite;

import android.widget.Toast;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.file.FileUtils;
import com.codevsolution.base.file.filemanager.FileManagerMain;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.freemarketsapp.R;

import java.io.File;

public class DataBaseBackup extends FileManagerMain {

    @Override
    protected void setPath() {

        String idUser = AndroidUtil.getSharePreference(contexto, USERID, USERIDCODE, NULL);
        String outFileName = "/" + AppActivity.getAppContext().getString(R.string.app_name) + idUser + "/backupDB";

        File extFile = FileUtils.crearDirectorioPublico(outFileName, FileUtils.DOWNLOADS);

        path = extFile.getAbsolutePath() + "/";
    }

    @Override
    protected boolean setCondition(String item, String itemExt, File file, boolean multiSelect) {
        return itemExt.equals(".db");
    }

    @Override
    protected void setOnClick(String item, String itemExt, File file, boolean multiSelect) {
        super.setOnClick(item, itemExt, file, multiSelect);

        dialogoRestore(file);

    }

    protected void dialogoRestore(File file) {

        String titulo = "Restore DB";
        String mensaje = "Se creara una copia de la base de datos actual por si desea revertir esta accion ";
        new Dialogos.DialogoTexto(titulo, mensaje, contexto, new Dialogos.DialogoTexto.OnClick() {
            @Override
            public void onConfirm() {

                if (SQLiteUtil.BD_backup(null, false)) {
                    if (SQLiteUtil.restoreBD_backup(null, file)) {
                        Toast.makeText(contexto, "operacion ejecutada", Toast.LENGTH_SHORT).show();
                        selector();
                    }
                }
            }

            @Override
            public void onCancel() {

                Toast.makeText(contexto, "operacion cancelada", Toast.LENGTH_SHORT).show();
            }

        }).show(getFragmentManager(), "dialogo restDB");
    }
}
