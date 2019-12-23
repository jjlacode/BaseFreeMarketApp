/*
    Open Manager, an open source file manager for the Android system
    Copyright (C) 2009, 2010, 2011  Joe Berria nexesdevelopment@gmail.com

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

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.FragmentRVnoSQL;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Joe Berria
 */
public class ProcessManager extends FragmentRVnoSQL {
    private final int CONVERT = 1024;

    private PackageManager pk;
    private ActivityManager activity_man;
    private TextView availMem_label, numProc_label;
    ViewHolderRV viewHolderRV;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        pk = getActivity().getPackageManager();

        availMem_label = view.findViewById(Estilos.getIdResource(contexto, "available_mem_label"));
        numProc_label = view.findViewById(Estilos.getIdResource(contexto, "num_processes_label"));
        activity_man = (ActivityManager) contexto.getSystemService(Context.ACTIVITY_SERVICE);

        viewHolderRV = new ViewHolderRV(view);
    }

    @Override
    protected void setLayout() {
        super.setLayout();

        layoutItem = Estilos.getIdLayout(contexto, "tablerow");
        layoutCabecera = Estilos.getIdLayout(contexto, "manage_layout");

    }

    @Override
    protected void setLista() {

        lista = new ArrayList<RunningAppProcessInfo>();
        update_list();
    }

    @Override
    public void setOnClickRV(Object object) {

    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    /**
     *
     */
    private void update_labels() {
        MemoryInfo mem_info;
        double mem_size;

        mem_info = new MemoryInfo();
        activity_man.getMemoryInfo(mem_info);
        mem_size = (((double) mem_info.availMem) / (CONVERT * CONVERT));

        availMem_label.setText(String.format("Available memory:\t %.2f Mb", mem_size));
        numProc_label.setText("Number of processes:\t " + lista.size());
    }

    private void update_list() {
        List<RunningAppProcessInfo> total_process;
        int len;
        total_process = activity_man.getRunningAppProcesses();
        len = total_process.size();
        System.out.println("len = " + len);

        for (int i = 0; i < len; i++) {
            /*
             * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
             * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
             */
            if (total_process.get(i).importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    total_process.get(i).importance != RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                lista.add(total_process.get(i));
            }
        }

        update_labels();
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return viewHolderRV;
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList<?> lista, String[] campos) {
        return new AdaptadorFiltro(contexto, layoutItem, lista);
    }

    @Override
    public void setOnClickRV(Object object, int position) {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        CharSequence[] options = {"Details", "Launch"};
        final int index = position;

        builder.setTitle("Process options");

        try {
            List<RunningAppProcessInfo> list = new ArrayList<RunningAppProcessInfo>(lista);
            builder.setIcon(pk.getApplicationIcon(list.get(position).processName));

        } catch (NameNotFoundException e) {
            builder.setIcon(Estilos.getIdDrawable(contexto, "processinfo"));
        }

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {

                switch (choice) {
                    case 0:
                        List<RunningAppProcessInfo> list = new ArrayList<RunningAppProcessInfo>(lista);
                        Toast.makeText(contexto, list.get(index).processName,
                                Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        list = new ArrayList<RunningAppProcessInfo>(lista);
                        Intent i = pk.getLaunchIntentForPackage(list.get(index).processName);

                        if (i != null)
                            startActivity(i);
                        else
                            Toast.makeText(contexto, "Could not launch", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    /* (non-JavaDoc)
     * private inner class to bind the listview and its data source
     * @author Joe Berria
     */

    private class AdaptadorFiltro extends ListaAdaptadorFiltro {

        public AdaptadorFiltro(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);
        }

        @Override
        public void onEntrada(Object entrada, View view) {

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {
            return null;
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView bottom_label;
        TextView top_label;
        ImageView icon;
        RelativeLayout relativeLayout;

        public ViewHolderRV(View itemView) {
            super(itemView);

            //relativeLayout = itemView.findViewById(R.id.ry_item_list);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

			/*
			relativeLayout.setBackgroundColor(Estilos.getIdColor(contexto, "black"));
			icon = new ImageView(contexto);
			icon.setAdjustViewBounds(true);
			icon.setMaxHeight(40);
			int idicon = icon.getId();
			if (idicon < 0) {
				idicon = View.generateViewId();
				icon.setId(idicon);
			}
			Estilos.setLayoutParamsRelative(relativeLayout, icon, RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_BOTTOM});

			top_label = new TextView(contexto);
			top_label.setTextColor(Estilos.getIdColor(contexto,"white"));
			Estilos.setLayoutParamsRelative(relativeLayout, top_label, RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_PARENT_START}, new int[]{idicon, 0});
			*/
            bottom_label = itemView.findViewById(Estilos.getIdResource(contexto, "bottom_view"));
            top_label = itemView.findViewById(Estilos.getIdResource(contexto, "top_view"));
            icon = itemView.findViewById(Estilos.getIdResource(contexto, "row_image"));

            List<RunningAppProcessInfo> list = new ArrayList<RunningAppProcessInfo>((Collection<? extends RunningAppProcessInfo>) lista);

            String pkg_name = list.get(position).processName;

            top_label.setText(parse_name(pkg_name));
            bottom_label.setText(String.format("%s, pid: %d",
                    list.get(position).processName, list.get(position).pid));

            try {
                icon.setImageDrawable(pk.getApplicationIcon(pkg_name));

            } catch (NameNotFoundException e) {
                icon.setImageResource(Estilos.getIdDrawable(contexto, "processinfo"));
            }

            super.bind(lista, position);
        }

        private String parse_name(String pkgName) {
            String[] items = pkgName.split("\\.");
            String name = "";
            int len = items.length;

            for (int i = 0; i < len; i++) {
                if (!items[i].equalsIgnoreCase("com") && !items[i].equalsIgnoreCase("android") &&
                        !items[i].equalsIgnoreCase("google") && !items[i].equalsIgnoreCase("process") &&
                        !items[i].equalsIgnoreCase("htc") && !items[i].equalsIgnoreCase("coremobility"))
                    name = name.concat(items[i] + ".");
            }
            return name;
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
