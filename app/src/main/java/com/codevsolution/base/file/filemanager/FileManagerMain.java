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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.FragmentRVnoSQL;
import com.codevsolution.base.style.Estilos;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;

/**
 * This is the main activity. The activity that is presented to the user
 * as the application launches. This class is, and expected not to be, instantiated.
 * <br>
 * <p>
 * This class handles creating the buttons and
 * text views. This class relies on the class EventHandler to handle all button
 * press logic and to control the data displayed on its ListView. This class
 * also relies on the FileManager class to handle all file operations such as
 * copy/paste zip/unzip etc. However most interaction with the FileManager class
 * is done via the EventHandler class. Also the SettingsMangager class to load
 * and save user settings.
 * <br>
 * <p>
 * The design objective with this class is to control only the look of the
 * GUI (option menu, context menu, ListView, buttons and so on) and rely on other
 * supporting classes to do the heavy lifting.
 *
 * @author Joe Berria
 */
public class FileManagerMain extends FragmentRVnoSQL {

    private static final String PREFS_NAME = "ManagerPrefsFile";    //user preference file name
    private static final String PREFS_HIDDEN = "hidden";
    private static final String PREFS_COLOR = "color";
    private static final String PREFS_THUMBNAIL = "thumbnail";
    private static final String PREFS_SORT = "sort";
    private static final String PREFS_STORAGE = "sdcard space";

    private static final int MENU_MKDIR = 0x00;            //option menu id
    private static final int MENU_SETTING = 0x01;            //option menu id
    private static final int MENU_SEARCH = 0x02;            //option menu id
    private static final int MENU_SPACE = 0x03;            //option menu id
    private static final int MENU_QUIT = 0x04;            //option menu id
    private static final int SEARCH_B = 0x09;

    private static final int D_MENU_DELETE = 0x05;            //context menu id
    private static final int D_MENU_RENAME = 0x06;            //context menu id
    private static final int D_MENU_COPY = 0x07;            //context menu id
    private static final int D_MENU_PASTE = 0x08;            //context menu id
    private static final int D_MENU_ZIP = 0x0e;            //context menu id
    private static final int D_MENU_UNZIP = 0x0f;            //context menu id
    private static final int D_MENU_MOVE = 0x30;            //context menu id
    private static final int F_MENU_MOVE = 0x20;            //context menu id
    private static final int F_MENU_DELETE = 0x0a;            //context menu id
    private static final int F_MENU_RENAME = 0x0b;            //context menu id
    private static final int F_MENU_ATTACH = 0x0c;            //context menu id
    private static final int F_MENU_COPY = 0x0d;            //context menu id
    private static final int SETTING_REQ = 0x10;            //request code for intent

    protected FileManager mFileMag;
    protected EventHandler mHandler;

    protected SharedPreferences mSettings;
    private boolean mHoldingFile = false;
    private boolean mHoldingZip = false;
    private boolean mUseBackKey = true;
    private String mCopiedTarget;
    private String mZippedTarget;
    private String mSelectedListItem;                //item from context menu
    private TextView mPathLabel, mDetailLabel, mStorageLabel;
    private EventHandler.ViewHolderRV viewHolderRV;
    protected String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPath();
    }

    @Override
    protected void setRecuperarPersistencia(SharedPreferences persistencia) {
        super.setRecuperarPersistencia(persistencia);

        path = persistencia.getString("location", Environment.
                getExternalStorageDirectory().getPath());
        //mHandler = new EventHandler(contexto, mFileMag,parent, location);
        //mPathLabel.setText("path: "+location);
        init();

    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        init();

    }

    private void init() {

        mSettings = AndroidUtil.openSharePreference(contexto, PREFS_NAME);
        boolean hide = mSettings.getBoolean(PREFS_HIDDEN, false);
        boolean thumb = mSettings.getBoolean(PREFS_THUMBNAIL, true);
        int space = mSettings.getInt(PREFS_STORAGE, View.VISIBLE);
        int color = mSettings.getInt(PREFS_COLOR, -1);
        int sort = mSettings.getInt(PREFS_SORT, 3);

        mFileMag = new FileManager(path);
        mFileMag.setShowHiddenFiles(hide);
        mFileMag.setSortType(sort);

        mHandler = new EventHandler(contexto, mFileMag, parent, path);

        mHandler.setTextColor(color);
        mHandler.setShowThumbnails(thumb);


        /*sets the ListAdapter for our ListActivity and
         *gives our EventHandler class the same adapter
         */
        viewHolderRV = mHandler.getViewHolderRV(view);
        mHandler.setListAdapter(adaptadorRV);

        /* register context menu for our list view */
        registerForContextMenu(viewHolderRV.itemView);

        mStorageLabel = view.findViewById(Estilos.getIdResource(contexto, "storage_label"));
        mDetailLabel = view.findViewById(Estilos.getIdResource(contexto, "detail_label"));
        mPathLabel = view.findViewById(Estilos.getIdResource(contexto, "path_label"));
        //visible(frdetalle);


        updateStorageLabel();
        mStorageLabel.setVisibility(space);

        mHandler.setUpdateLabels(mPathLabel, mDetailLabel);

        /* setup buttons */
        int[] img_button_id = {Estilos.getIdResource(contexto, "help_button"),
                Estilos.getIdResource(contexto, "home_button"),
                Estilos.getIdResource(contexto, "back_button"),
                Estilos.getIdResource(contexto, "info_button"),
                Estilos.getIdResource(contexto, "manage_button"),
                Estilos.getIdResource(contexto, "multiselect_button")};

        int[] button_id = {Estilos.getIdResource(contexto, "hidden_copy"),
                Estilos.getIdResource(contexto, "hidden_attach"),
                Estilos.getIdResource(contexto, "hidden_delete"),
                Estilos.getIdResource(contexto, "hidden_move")};

        ImageButton[] bimg = new ImageButton[img_button_id.length];
        Button[] bt = new Button[button_id.length];

        for (int i = 0; i < img_button_id.length; i++) {
            bimg[i] = view.findViewById(img_button_id[i]);
            bimg[i].setOnClickListener(mHandler);

            if (i < 4) {
                bt[i] = view.findViewById(button_id[i]);
                bt[i].setOnClickListener(mHandler);
            }
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(pressKeyListener);
        swipeOn = false;
        refreshOn = false;
        gone(lybusca);
        gone(btnback);
    }

    @Override
    protected void setLayout() {
        super.setLayout();

        layoutCabecera = Estilos.getIdLayout(contexto, "file_manager_main");
        layoutListaPie = Estilos.getIdLayout(contexto, "file_manager_main_pie");
        layoutItem = Estilos.getIdLayout(contexto, "tablerow");

    }

    protected void setPath() {
        path = Environment.
                getExternalStorageDirectory().getPath();
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return viewHolderRV;
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList<?> lista, String[] campos) {
        return null;
    }

    @Override
    public void setOnClickRV(Object object) {

    }

    @Override
    protected void setLista() {
        super.setLista();

        lista = mHandler.mDataSource;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setPersistencia(SharedPreferences.Editor editor) {
        super.setPersistencia(editor);

        editor.putString("location", mFileMag.getCurrentDir());
    }


    @Override
    protected void setAcciones() {
        super.setAcciones();

        mHandler.setListAdapter(adaptadorRV);
    }

    private void updateStorageLabel() {
        long total, aval;
        int kb = 1024;

        StatFs fs = new StatFs(Environment.
                getExternalStorageDirectory().getPath());

        total = fs.getBlockCount() * (fs.getBlockSize() / kb);
        aval = fs.getAvailableBlocks() * (fs.getBlockSize() / kb);

        mStorageLabel.setText(String.format("sdcard: Total %.2f GB " +
                        "\t\tAvailable %.2f GB",
                (double) total / (kb * kb), (double) aval / (kb * kb)));
    }

    protected boolean setCondition(String item, String itemExt, File file, boolean multiSelect) {
        return false;
    }

    protected void setOnClick(String item, String itemExt, File file, boolean multiSelect) {

    }

    /**
     * To add more functionality and let the user interact with more
     * file types, this is the function to add the ability.
     * <p>
     * (note): this method can be done more efficiently
     */


    public void setOnClickRV(Object object, int position) {
        final String item = mHandler.getData(position);
        boolean multiSelect = mHandler.isMultiSelected();
        File file = new File(mFileMag.getCurrentDir() + "/" + item);
        String item_ext = null;

        try {
            item_ext = item.substring(item.lastIndexOf("."));

        } catch (IndexOutOfBoundsException e) {
            item_ext = "";
        }
        System.out.println("Current dir = " + mFileMag.getCurrentDir() + "/" + item);

        /*
         * If the user has multi-select on, we just need to record the file
         * not make an intent for it.
         */
        if (multiSelect) {
            viewHolderRV.addMultiPosition(position, file.getPath());

        } else {

            if (setCondition(item, item_ext, file, multiSelect)) {
                setOnClick(item, item_ext, file, multiSelect);
            } else if (file.isDirectory()) {
                if (file.canRead()) {
                    mHandler.stopThumbnailThread();
                    mHandler.updateDirectory(mFileMag.getNextDir(item, false));
                    mPathLabel.setText(mFileMag.getCurrentDir());


                    /*set back button switch to true
                     * (this will be better implemented later)
                     */
                    if (!mUseBackKey)
                        mUseBackKey = true;

                } else {
                    Toast.makeText(contexto, "Can't read folder due to permissions",
                            Toast.LENGTH_SHORT).show();
                }
            }

            /*music file selected--add more audio formats*/
            else if (item_ext.equalsIgnoreCase(".mp3") ||
                    item_ext.equalsIgnoreCase(".m4a") ||
                    item_ext.equalsIgnoreCase(".ogg") ||
                    item_ext.equalsIgnoreCase(".wav")) {


                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                i.setDataAndType(uri, "audio/*");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            }

            /*photo file selected*/
            else if (item_ext.equalsIgnoreCase(".jpeg") ||
                    item_ext.equalsIgnoreCase(".jpg") ||
                    item_ext.equalsIgnoreCase(".png") ||
                    item_ext.equalsIgnoreCase(".gif") ||
                    item_ext.equalsIgnoreCase(".tiff")) {

                if (file.exists()) {

                    Intent picIntent = new Intent();
                    picIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    picIntent.setDataAndType(uri, "image/*");
                    picIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(picIntent);

                }
            }

            /*video file selected--add more video formats*/
            else if (item_ext.equalsIgnoreCase(".m4v") ||
                    item_ext.equalsIgnoreCase(".3gp") ||
                    item_ext.equalsIgnoreCase(".wmv") ||
                    item_ext.equalsIgnoreCase(".mp4")) {

                if (file.exists()) {

                    Intent movieIntent = new Intent();
                    movieIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    movieIntent.setDataAndType(uri, "video/*");
                    movieIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(movieIntent);

                }
            }

            /*zip file */
            else if (item_ext.equalsIgnoreCase(".zip")) {


                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                AlertDialog alert;
                mZippedTarget = mFileMag.getCurrentDir() + "/" + item;
                CharSequence[] option = {"Extract here", "Extract to..."};

                builder.setTitle("Extract");
                builder.setItems(option, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String dir = mFileMag.getCurrentDir();
                                mHandler.unZipFile(item, dir + "/");
                                break;

                            case 1:
                                mDetailLabel.setText("Holding " + item +
                                        " to extract");
                                mHoldingZip = true;
                                mHandler.setmHoldingZip(true);
                                break;
                        }
                    }
                });

                alert = builder.create();
                alert.show();

            }

            /* gzip files, this will be implemented later */
            else if (item_ext.equalsIgnoreCase(".gzip") ||
                    item_ext.equalsIgnoreCase(".gz")) {


            }

            /*pdf file selected*/
            else if (item_ext.equalsIgnoreCase(".pdf")) {

                if (file.exists()) {

                    Intent pdfIntent = new Intent();
                    pdfIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    pdfIntent.setDataAndType(uri, "application/pdf");
                    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(contexto, "Sorry, couldn't find a pdf viewer",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }

            /*Android application file*/
            else if (item_ext.equalsIgnoreCase(".apk")) {

                if (file.exists()) {

                    Intent apkIntent = new Intent();
                    apkIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    apkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(apkIntent);

                }
            }

            /* HTML file */
            else if (item_ext.equalsIgnoreCase(".html")) {

                if (file.exists()) {

                    Intent htmlIntent = new Intent();
                    htmlIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    htmlIntent.setDataAndType(uri, "text/html");
                    htmlIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(htmlIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(contexto, "Sorry, couldn't find a HTML viewer",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }

            /* text file*/
            else if (item_ext.equalsIgnoreCase(".txt")) {

                if (file.exists()) {

                    Intent txtIntent = new Intent();
                    txtIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    txtIntent.setDataAndType(uri, "text/plain");
                    txtIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(txtIntent);
                    } catch (ActivityNotFoundException e) {
                        txtIntent.setType("text/*");
                        startActivity(txtIntent);
                    }

                }
            } else if (item_ext.equalsIgnoreCase(".db")) {

                if (file.exists()) {

                    Intent txtIntent = new Intent();
                    txtIntent.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    txtIntent.setDataAndType(uri, "text/plain");
                    txtIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(txtIntent);
                    } catch (ActivityNotFoundException e) {
                        txtIntent.setType("text/*");
                        startActivity(txtIntent);
                    }

                }
            }

            /* generic intent */
            else {
                if (file.exists()) {

                    Intent generic = new Intent();
                    generic.setAction(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                    generic.setDataAndType(uri, "text/plain");
                    generic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(generic);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(contexto, "Sorry, couldn't find anything " +
                                        "to open " + file.getName(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences.Editor editor = mSettings.edit();
        boolean check;
        boolean thumbnail;
        int color, sort, space;

        /* resultCode must equal RESULT_CANCELED because the only way
         * out of that activity is pressing the back button on the phone
         * this publishes a canceled result code not an ok result code
         */
        if (requestCode == SETTING_REQ && resultCode == RESULT_CANCELED) {
            //save the information we get from settings activity
            check = data.getBooleanExtra("HIDDEN", false);
            thumbnail = data.getBooleanExtra("THUMBNAIL", true);
            color = data.getIntExtra("COLOR", -1);
            sort = data.getIntExtra("SORT", 0);
            space = data.getIntExtra("SPACE", View.VISIBLE);

            editor.putBoolean(PREFS_HIDDEN, check);
            editor.putBoolean(PREFS_THUMBNAIL, thumbnail);
            editor.putInt(PREFS_COLOR, color);
            editor.putInt(PREFS_SORT, sort);
            editor.putInt(PREFS_STORAGE, space);
            editor.commit();

            mFileMag.setShowHiddenFiles(check);
            mFileMag.setSortType(sort);
            mHandler.setTextColor(color);
            mHandler.setShowThumbnails(thumbnail);
            mStorageLabel.setVisibility(space);
            mHandler.updateDirectory(mFileMag.getNextDir(mFileMag.getCurrentDir(), true));
        }
    }

    /* ================Menus, options menu and context menu start here=================*/

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //inflater.inflate(Estilos.getIdMenu(contexto, "menu_filemanager"), menu);

        menu.add(0, MENU_MKDIR, 0, "New Directory").setIcon(Estilos.getIdDrawable(contexto, "newfolder"));
        menu.add(0, MENU_SEARCH, 0, "Search").setIcon(Estilos.getIdDrawable(contexto, "search"));

        /* free space will be implemented at a later time */
//    	menu.add(0, MENU_SPACE, 0, "Free space").setIcon(R.drawable.space);
        menu.add(0, MENU_SETTING, 0, "Settings").setIcon(Estilos.getIdDrawable(contexto, "setting"));
        menu.add(0, MENU_QUIT, 0, "Quit").setIcon(Estilos.getIdDrawable(contexto, "logout"));

        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_MKDIR:
                onCreateDialog(MENU_MKDIR).show();
                return true;

            case MENU_SEARCH:
                onCreateDialog(MENU_SEARCH).show();
                return true;

            case MENU_SPACE: /* not yet implemented */
                return true;

            case MENU_SETTING:
    			/*
    			Intent settings_int = new Intent(this, Settings.class);
    			settings_int.putExtra("HIDDEN", mSettings.getBoolean(PREFS_HIDDEN, false));
    			settings_int.putExtra("THUMBNAIL", mSettings.getBoolean(PREFS_THUMBNAIL, true));
    			settings_int.putExtra("COLOR", mSettings.getInt(PREFS_COLOR, -1));
    			settings_int.putExtra("SORT", mSettings.getInt(PREFS_SORT, 0));
    			settings_int.putExtra("SPACE", mSettings.getInt(PREFS_STORAGE, View.VISIBLE));
    			
    			startActivityForResult(settings_int, SETTING_REQ);

    			 */
                return true;

            case MENU_QUIT:

                return true;
        }
        return false;
    }

    @Override
    protected void setPreferences() {
        super.setPreferences();


    }

	/*
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo info) {
    	//super.onCreateContextMenu(menu, v, info);

        System.out.println("info = " + info);
    	boolean multi_data = mHandler.hasMultiSelectData();
    	AdapterContextMenuInfo _info = (AdapterContextMenuInfo)info;
    	mSelectedListItem = mHandler.getData(_info.position);

    	/* is it a directory and is multi-select turned off
    	if(mFileMag.isDirectory(mSelectedListItem) && !mHandler.isMultiSelected()) {
    		menu.setHeaderTitle("Folder operations");
        	menu.add(0, D_MENU_DELETE, 0, "Delete Folder");
        	menu.add(0, D_MENU_RENAME, 0, "Rename Folder");
        	menu.add(0, D_MENU_COPY, 0, "Copy Folder");
        	menu.add(0, D_MENU_MOVE, 0, "Move(Cut) Folder");
        	menu.add(0, D_MENU_ZIP, 0, "Zip Folder");
        	menu.add(0, D_MENU_PASTE, 0, "Paste into folder").setEnabled(mHoldingFile || 
        																 multi_data);
        	menu.add(0, D_MENU_UNZIP, 0, "Extract here").setEnabled(mHoldingZip);
    		
        /* is it a file and is multi-select turned off
    	} else if(!mFileMag.isDirectory(mSelectedListItem) && !mHandler.isMultiSelected()) {
        	menu.setHeaderTitle("File Operations");
    		menu.add(0, F_MENU_DELETE, 0, "Delete File");
    		menu.add(0, F_MENU_RENAME, 0, "Rename File");
    		menu.add(0, F_MENU_COPY, 0, "Copy File");
    		menu.add(0, F_MENU_MOVE, 0, "Move(Cut) File");
    		menu.add(0, F_MENU_ATTACH, 0, "Email File");
    	}	
    }
    */

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        mSelectedListItem = mHandler.getSelectedListItem();
        switch (item.getItemId()) {
            case D_MENU_DELETE:
            case F_MENU_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                builder.setTitle("Warning ");
                builder.setIcon(Estilos.getIdDrawable(contexto, "warning"));
                builder.setMessage("Deleting " + mSelectedListItem +
                        " cannot be undone. Are you sure you want to delete?");
                builder.setCancelable(false);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.deleteFile(mFileMag.getCurrentDir() + "/" + mSelectedListItem);
                    }
                });
                AlertDialog alert_d = builder.create();
                alert_d.show();
                return true;

            case D_MENU_RENAME:
                onCreateDialog(D_MENU_RENAME).show();
                return true;

            case F_MENU_RENAME:
                onCreateDialog(F_MENU_RENAME).show();
                return true;

            case F_MENU_ATTACH:
                File file = new File(mFileMag.getCurrentDir() + "/" + mSelectedListItem);
                Intent mail_int = new Intent();

                mail_int.setAction(Intent.ACTION_SEND);
                mail_int.setType("application/mail");
                mail_int.putExtra(Intent.EXTRA_BCC, "");
                Uri uri = FileProvider.getUriForFile(contexto, AppActivity.getFileProvider(), file);
                mail_int.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(mail_int);
                return true;

            case F_MENU_MOVE:
            case D_MENU_MOVE:
            case F_MENU_COPY:
            case D_MENU_COPY:
                if (item.getItemId() == F_MENU_MOVE || item.getItemId() == D_MENU_MOVE)
                    mHandler.setDeleteAfterCopy(true);

                mHoldingFile = true;
                mHandler.setmHoldingFile(true);

                mCopiedTarget = mFileMag.getCurrentDir() + "/" + mSelectedListItem;
                mDetailLabel.setText("Holding " + mSelectedListItem);
                return true;


            case D_MENU_PASTE:
                boolean multi_select = mHandler.hasMultiSelectData();

                if (multi_select) {
                    mHandler.copyFileMultiSelect(mFileMag.getCurrentDir() + "/" + mSelectedListItem);

                } else if (mHoldingFile && mCopiedTarget.length() > 1) {

                    mHandler.copyFile(mCopiedTarget, mFileMag.getCurrentDir() + "/" + mSelectedListItem);
                    mDetailLabel.setText("");
                }

                mHoldingFile = false;
                mHandler.setmHoldingFile(false);
                return true;

            case D_MENU_ZIP:
                String dir = mFileMag.getCurrentDir();

                mHandler.zipFile(dir + "/" + mSelectedListItem);
                return true;

            case D_MENU_UNZIP:
                if (mHoldingZip && mZippedTarget.length() > 1) {
                    String current_dir = mFileMag.getCurrentDir() + "/" + mSelectedListItem + "/";
                    String old_dir = mZippedTarget.substring(0, mZippedTarget.lastIndexOf("/"));
                    String name = mZippedTarget.substring(mZippedTarget.lastIndexOf("/") + 1);

                    if (new File(mZippedTarget).canRead() && new File(current_dir).canWrite()) {
                        mHandler.unZipFileToDir(name, current_dir, old_dir);
                        mPathLabel.setText(current_dir);

                    } else {
                        Toast.makeText(contexto, "You do not have permission to unzip " + name,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                mHoldingZip = false;
                mHandler.setmHoldingZip(false);
                mDetailLabel.setText("");
                mZippedTarget = "";
                return true;
        }
        return false;
    }

    /* ================Menus, options menu and context menu end here=================*/

    protected Dialog onCreateDialog(int id) {
        final Dialog dialog = new Dialog(contexto);
        mSelectedListItem = mHandler.getSelectedListItem();

        switch (id) {
            case MENU_MKDIR:
                dialog.setContentView(Estilos.getIdLayout(contexto, "input_layout"));
                dialog.setTitle("Create New Directory");
                dialog.setCancelable(false);

                ImageView icon = dialog.findViewById(Estilos.getIdResource(contexto, "input_icon"));
                icon.setImageResource(Estilos.getIdDrawable(contexto, "newfolder"));

                TextView label = dialog.findViewById(Estilos.getIdResource(contexto, "input_label"));
                label.setText(mFileMag.getCurrentDir());
                final EditText input = dialog.findViewById(Estilos.getIdResource(contexto, "input_inputText"));

                Button cancel = dialog.findViewById(Estilos.getIdResource(contexto, "input_cancel_b"));
                Button create = dialog.findViewById(Estilos.getIdResource(contexto, "input_create_b"));

                create.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (input.getText().length() > 1) {
                            if (mFileMag.createDir(mFileMag.getCurrentDir() + "/", input.getText().toString()) == 0)
                                Toast.makeText(contexto,
                                        "Folder " + input.getText().toString() + " created",
                                        Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(contexto, "New folder was not created", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                        String temp = mFileMag.getCurrentDir();
                        mHandler.updateDirectory(mFileMag.getNextDir(temp, true));
                    }
                });
                cancel.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case D_MENU_RENAME:
            case F_MENU_RENAME:
                dialog.setContentView(Estilos.getIdLayout(contexto, "input_layout"));
                dialog.setTitle("Rename " + mSelectedListItem);
                dialog.setCancelable(false);

                ImageView rename_icon = dialog.findViewById(Estilos.getIdResource(contexto, "input_icon"));
                rename_icon.setImageResource(Estilos.getIdDrawable(contexto, "rename"));

                TextView rename_label = dialog.findViewById(Estilos.getIdResource(contexto, "input_label"));
                rename_label.setText(mFileMag.getCurrentDir());
                final EditText rename_input = dialog.findViewById(Estilos.getIdResource(contexto, "input_inputText"));

                Button rename_cancel = dialog.findViewById(Estilos.getIdResource(contexto, "input_cancel_b"));
                Button rename_create = dialog.findViewById(Estilos.getIdResource(contexto, "input_create_b"));
                rename_create.setText("Rename");

                rename_create.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (rename_input.getText().length() < 1)
                            dialog.dismiss();

                        if (mFileMag.renameTarget(mFileMag.getCurrentDir() + "/" + mSelectedListItem, rename_input.getText().toString()) == 0) {
                            Toast.makeText(contexto, mSelectedListItem + " was renamed to " + rename_input.getText().toString(),
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(contexto, mSelectedListItem + " was not renamed", Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                        String temp = mFileMag.getCurrentDir();
                        mHandler.updateDirectory(mFileMag.getNextDir(temp, true));
                    }
                });
                rename_cancel.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;

            case SEARCH_B:
            case MENU_SEARCH:
                dialog.setContentView(Estilos.getIdLayout(contexto, "input_layout"));
                dialog.setTitle("Search");
                dialog.setCancelable(false);

                ImageView searchIcon = dialog.findViewById(Estilos.getIdResource(contexto, "input_icon"));
                searchIcon.setImageResource(Estilos.getIdDrawable(contexto, "search"));

                TextView search_label = dialog.findViewById(Estilos.getIdResource(contexto, "input_label"));
                search_label.setText("Search for a file");
                final EditText search_input = dialog.findViewById(Estilos.getIdResource(contexto, "input_inputText"));

                Button search_button = dialog.findViewById(Estilos.getIdResource(contexto, "input_create_b"));
                Button cancel_button = dialog.findViewById(Estilos.getIdResource(contexto, "input_cancel_b"));
                search_button.setText("Search");

                search_button.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String temp = search_input.getText().toString();

                        if (temp.length() > 0)
                            mHandler.searchForFile(temp);
                        dialog.dismiss();
                    }
                });

                cancel_button.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
        }
        return dialog;
    }

    /*
     * (non-Javadoc)
     * This will check if the user is at root directory. If so, if they press back
     * again, it will close the application.
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */

    private View.OnKeyListener pressKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String current = mFileMag.getCurrentDir();

            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                onCreateDialog(SEARCH_B).show();

                return true;

            } else if (keyCode == KeyEvent.KEYCODE_BACK && mUseBackKey && !current.equals(mFileMag.getHomePath())) {
                if (mHandler.isMultiSelected()) {
                    viewHolderRV.killMultiSelect(true);
                    Toast.makeText(contexto, "Multi-select is now off", Toast.LENGTH_SHORT).show();

                } else {
                    //stop updating thumbnail icons if its running
                    mHandler.stopThumbnailThread();
                    mHandler.updateDirectory(mFileMag.getPreviousDir());
                    mPathLabel.setText(mFileMag.getCurrentDir());
                }
                return true;

            }

            return false;
        }

    };

    public boolean onKeyDown(int keycode, KeyEvent event) {
        String current = mFileMag.getCurrentDir();

        if (keycode == KeyEvent.KEYCODE_SEARCH) {
            onCreateDialog(SEARCH_B).show();

            return true;

        } else if (keycode == KeyEvent.KEYCODE_BACK && mUseBackKey && !current.equals(mFileMag.getHomePath())) {
            if (mHandler.isMultiSelected()) {
                viewHolderRV.killMultiSelect(true);
                Toast.makeText(contexto, "Multi-select is now off", Toast.LENGTH_SHORT).show();

            } else {
                //stop updating thumbnail icons if its running
                mHandler.stopThumbnailThread();
                mHandler.updateDirectory(mFileMag.getPreviousDir());
                mPathLabel.setText(mFileMag.getCurrentDir());
            }
            return true;

        }

        return false;
    }
}
