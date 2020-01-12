/*
    Open Manager, an open source file manager for the Android system
    Copyright (C) 2009, 2010, 2011  Joe Berria <nexesdevelopment@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.codevsolution.base.file.filemanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codevsolution.base.R;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.style.Estilos;

import java.io.File;
import java.util.Date;

public class DirectoryInfo extends FragmentBase {
    private static final int KB = 1024;
    private static final int MG = KB * KB;
    private static final int GB = MG * KB;
    private String mPathName;
    private TextView mNameLabel, mPathLabel, mDirLabel,
            mFileLabel, mTimeLabel, mTotalLabel;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        mNameLabel = view.findViewById(Estilos.getIdResource(contexto, "name_label"));
        mPathLabel = view.findViewById(Estilos.getIdResource(contexto, "path_label"));
        mDirLabel = view.findViewById(Estilos.getIdResource(contexto, "dirs_label"));
        mFileLabel = view.findViewById(Estilos.getIdResource(contexto, "files_label"));
        mTimeLabel = view.findViewById(Estilos.getIdResource(contexto, "time_stamp"));
        mTotalLabel = view.findViewById(Estilos.getIdResource(contexto, "total_size"));
        /* make zip button visible and setup onclick logic to have zip button*/

        Button zip = view.findViewById(Estilos.getIdResource(contexto, "zip_button"));
        zip.setVisibility(Button.GONE);


        Button back = view.findViewById(Estilos.getIdResource(contexto, "back_button"));
        back.setOnClickListener(new ButtonHandler());

    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        mPathName = bundle.getString(PATH, Environment.getExternalStorageDirectory().getPath());
        new BackgroundWork().execute(mPathName);
    }

    @Override
    protected void setLayout() {
        super.setLayout();

        layoutCuerpo = Estilos.getIdLayout(contexto, "info_layout");
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }


    private class BackgroundWork extends AsyncTask<String, Void, Long> {
        private ProgressDialog dialog;
        private String mDisplaySize;
        private int mFileCount = 0;
        private int mDirCount = 0;

        protected void onPreExecute() {
            dialog = ProgressDialog.show(contexto, "", "Calculating information...", true, true);
        }

        protected Long doInBackground(String... vals) {
            FileManager flmg = new FileManager();
            File dir = new File(vals[0]);
            long size = 0;
            int len = 0;

            File[] list = dir.listFiles();
            if (list != null)
                len = list.length;

            for (int i = 0; i < len; i++) {
                if (list[i].isFile())
                    mFileCount++;
                else if (list[i].isDirectory())
                    mDirCount++;
            }

            if (vals[0].equals("/")) {
                StatFs fss = new StatFs(Environment.getRootDirectory().getPath());
                size = fss.getAvailableBlocks() * (fss.getBlockSize() / KB);

                mDisplaySize = (size > GB) ?
                        String.format("%.2f Gb ", (double) size / MG) :
                        String.format("%.2f Mb ", (double) size / KB);

            } else if (vals[0].equals("/sdcard")) {
                StatFs fs = new StatFs(Environment.getExternalStorageDirectory()
                        .getPath());
                size = fs.getBlockCount() * (fs.getBlockSize() / KB);

                mDisplaySize = (size > GB) ?
                        String.format("%.2f Gb ", (double) size / GB) :
                        String.format("%.2f Gb ", (double) size / MG);

            } else {
                size = flmg.getDirSize(vals[0]);

                if (size > GB)
                    mDisplaySize = String.format("%.2f Gb ", (double) size / GB);
                else if (size < GB && size > MG)
                    mDisplaySize = String.format("%.2f Mb ", (double) size / MG);
                else if (size < MG && size > KB)
                    mDisplaySize = String.format("%.2f Kb ", (double) size / KB);
                else
                    mDisplaySize = String.format("%.2f bytes ", (double) size);
            }

            return size;
        }

        protected void onPostExecute(Long result) {
            File dir = new File(mPathName);

            mNameLabel.setText(dir.getName());
            mPathLabel.setText(dir.getAbsolutePath());
            mDirLabel.setText(mDirCount + " folders ");
            mFileLabel.setText(mFileCount + " files ");
            mTotalLabel.setText(mDisplaySize);
            mTimeLabel.setText(new Date(dir.lastModified()) + " ");

            dialog.cancel();
        }
    }

    private class ButtonHandler implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back_button) {

            }

        }
    }
}
