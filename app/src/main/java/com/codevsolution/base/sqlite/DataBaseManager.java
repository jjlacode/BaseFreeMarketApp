package com.codevsolution.base.sqlite;

import com.codevsolution.base.R;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.file.FileUtils;
import com.codevsolution.base.file.filemanager.FileManagerMain;

import java.io.File;

public class DataBaseManager extends FileManagerMain {

    @Override
    protected void setPath() {

        String idUser = AndroidUtil.getSharePreference(contexto, USERID, USERIDCODE, NULL);
        String outFileName = "/" + AppActivity.getAppContext().getString(R.string.app_name) + idUser + "/backupDB";

        File extFile = FileUtils.crearDirectorioPublico(outFileName, FileUtils.DOWNLOADS);

        path = extFile.getAbsolutePath() + "/";
    }

}
