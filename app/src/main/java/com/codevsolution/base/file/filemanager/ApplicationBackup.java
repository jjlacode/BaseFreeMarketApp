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
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.FragmentRVnoSQL;
import com.codevsolution.base.style.Estilos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to display an activity to the user so they can
 * view the third party applications on their phone and have a button
 * that gives them the ability to backup said applications to the SDCard
 * <br>
 * <p>
 * The location that the backup will be placed is at
 * <br>/sdcard/open manager/AppBackup/
 * <br>
 * note: that /sdcard/open manager/ should already exists. This is check at start
 * up from the SettingsManager class.
 *
 * @author Joe Berria <nexesdevelopment@gmail.com>
 */


public class ApplicationBackup extends FragmentRVnoSQL implements OnClickListener {
    private static final String BACKUP_LOC = Environment.getExternalStorageDirectory().getPath() + "/open manager/AppBackup/";
    private static final int SET_PROGRESS = 0x00;
    private static final int FINISH_PROGRESS = 0x01;
    private static final int FLAG_UPDATED_SYS_APP = 0x80;

    private TextView mAppLabel;
    private PackageManager mPackMag;
    private ProgressDialog mDialog;
    ViewHolderRv viewHolderRv;

    /*
     * Our handler object that will update the GUI from
     * our background thread.
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SET_PROGRESS:
                    mDialog.setMessage((String) msg.obj);
                    break;
                case FINISH_PROGRESS:
                    mDialog.cancel();
                    Toast.makeText(contexto,
                            "Applications have been backed up",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        mAppLabel = new TextView(contexto);
        frCabecera.addView(mAppLabel);
        cabecera = true;

        btnsave.setOnClickListener(this::onClick);
        mPackMag = getActivity().getPackageManager();

        layoutItem = Estilos.getIdLayout(contexto, "tablerow");

        viewHolderRv = new ViewHolderRv(view);
    }

    @Override
    protected void setLista() {
        super.setLista();

        lista = new ArrayList<ApplicationInfo>();
        get_downloaded_apps();
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }


    @Override
    public void onClick(View view) {
        mDialog = ProgressDialog.show(contexto,
                "Backing up applications",
                "", true, false);

        Thread all = new Thread(new BackgroundWork(lista));
        all.start();
    }

    private void get_downloaded_apps() {
        List<ApplicationInfo> all_apps = mPackMag.getInstalledApplications(
                PackageManager.GET_UNINSTALLED_PACKAGES);

        for (ApplicationInfo appInfo : all_apps) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    (appInfo.flags & FLAG_UPDATED_SYS_APP) == 0 &&
                    appInfo.flags != 0)

                lista.add(appInfo);
        }

        mAppLabel.setText("You have " + lista.size() + " downloaded apps");
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRv(view);
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList<?> lista, String[] campos) {
        return new AdaptadorFiltro(contexto, layoutItem, lista);
    }

    @Override
    public void setOnClickRV(Object object) {

    }


    /*
     * This private inner class will perform the backup of applications
     * on a background thread, while updating the user via a message being
     * sent to our handler object.
     */
    private class BackgroundWork implements Runnable {
        private static final int BUFFER = 256;

        private ArrayList<ApplicationInfo> mDataSource;
        private File mDir = new File(BACKUP_LOC);
        private byte[] mData;

        public BackgroundWork(ArrayList<ApplicationInfo> data) {
            mDataSource = data;
            mData = new byte[BUFFER];

            /*create dir if needed*/
            File d = new File("/sdcard/open manager/");
            if (!d.exists()) {
                d.mkdir();

                //then create this directory
                mDir.mkdir();

            } else {
                if (!mDir.exists())
                    mDir.mkdir();
            }
        }

        public void run() {
            BufferedInputStream mBuffIn;
            BufferedOutputStream mBuffOut;
            Message msg;
            int len = mDataSource.size();
            int read = 0;

            for (int i = 0; i < len; i++) {
                ApplicationInfo info = mDataSource.get(i);
                String source_dir = info.sourceDir;
                String out_file = source_dir.substring(source_dir.lastIndexOf("/") + 1);

                try {
                    mBuffIn = new BufferedInputStream(new FileInputStream(source_dir));
                    mBuffOut = new BufferedOutputStream(new FileOutputStream(BACKUP_LOC + out_file));

                    while ((read = mBuffIn.read(mData, 0, BUFFER)) != -1)
                        mBuffOut.write(mData, 0, read);

                    mBuffOut.flush();
                    mBuffIn.close();
                    mBuffOut.close();

                    msg = new Message();
                    msg.what = SET_PROGRESS;
                    msg.obj = i + " out of " + len + " apps backed up";
                    mHandler.sendMessage(msg);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mHandler.sendEmptyMessage(FINISH_PROGRESS);
        }
    }

    private class AdaptadorFiltro extends ListaAdaptadorFiltro {

        public AdaptadorFiltro(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);
        }

        @Override
        public void onEntrada(Object entrada, View view) {

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();

            System.out.println("constraint = " + constraint.toString());

            for (Object entrada : entradas) {

            }

            return suggestion;
        }
    }

    public class ViewHolderRv extends BaseViewHolder implements TipoViewHolder {

        TextView top_view, bottom_view;
        ImageView icon, check_mark;

        public ViewHolderRv(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            top_view = itemView.findViewById(Estilos.getIdResource(contexto, "top_view"));
            bottom_view = itemView.findViewById(Estilos.getIdResource(contexto, "bottom_view"));
            check_mark = itemView.findViewById(Estilos.getIdResource(contexto, "multiselect_icon"));
            icon = itemView.findViewById(Estilos.getIdResource(contexto, "row_image"));
            icon.setMaxHeight(40);

            ApplicationInfo info = (ApplicationInfo) lista.get(position);
            top_view.setText(info.processName);
            bottom_view.setText(info.packageName);

            //this should not throw the exception
            try {
                icon.setImageDrawable(mPackMag.getApplicationIcon(info.packageName));
            } catch (NameNotFoundException e) {
                icon.setImageResource(Estilos.getIdDrawable(contexto, "appicon"));
            }


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRv(view);
        }
    }
}
