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

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.style.Estilos;

public class WirelessManager extends FragmentBase {
    //index values to access the elements in the TextView array.
    private final int SSTRENGTH = 0;
    private final int WIFISTATE = 1;
    private final int IPADD = 2;
    private final int MACADD = 3;
    private final int SSID = 4;
    private final int LINKSPD = 5;

    private TextView[] data_labels;
    private TextView name_label;
    private TextView enable_label;
    private Button state_button;
    private Button back_button;
    private WifiManager wifi;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        wifi = (WifiManager) contexto.getSystemService(Context.WIFI_SERVICE);
        TextView[] titles = new TextView[6];
        data_labels = new TextView[6];

        int[] left_views = {Estilos.getIdResource(contexto, "first_title"),
                Estilos.getIdResource(contexto, "second_title, R.id.third_title"),
                Estilos.getIdResource(contexto, "fourth_title"),
                Estilos.getIdResource(contexto, "fifth_title")};

        /*R.layout.info_layout is the same layout used for directory info.
         *Re-using the layout for this activity, so id tag names may not make sense,
         *but are in the correct order.
         */
        int[] right_views = {Estilos.getIdResource(contexto, "dirs_label"),
                Estilos.getIdResource(contexto, "files_label"),
                Estilos.getIdResource(contexto, "time_stamp"),
                Estilos.getIdResource(contexto, "total_size"),
                Estilos.getIdResource(contexto, "free_space")};
        String[] labels = {"Signal strength", "WIFI State", "ip address",
                "mac address", "SSID", "link speed"};

        for (int i = 0; i < 5; i++) {
            titles[i] = view.findViewById(left_views[i]);
            titles[i].setText(labels[i]);

            data_labels[i] = view.findViewById(right_views[i]);
            data_labels[i].setText("N/A");
        }

        name_label = view.findViewById(Estilos.getIdResource(contexto, "name_label"));
        enable_label = view.findViewById(Estilos.getIdResource(contexto, "path_label"));
        state_button = view.findViewById(Estilos.getIdResource(contexto, "back_button"));
        back_button = view.findViewById(Estilos.getIdResource(contexto, "zip_button"));
        back_button.setText(" Back ");

        state_button.setOnClickListener(new ButtonHandler());
        back_button.setOnClickListener(new ButtonHandler());

        ImageView icon = view.findViewById(Estilos.getIdResource(contexto, "info_icon"));
        icon.setImageResource(Estilos.getIdDrawable(contexto, "wireless"));

        get_wifi();

    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    private void get_wifi() {
        if (CheckPermisos.validarPermisos(getActivity(), CheckPermisos.ACCESS_WIFI_STATE, 555)) {
            @SuppressLint("MissingPermission") WifiInfo info = wifi.getConnectionInfo();
            @SuppressLint("MissingPermission") int state = wifi.getWifiState();
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            boolean enabled = wifi.isWifiEnabled();

            name_label.setText(info.getSSID());
            enable_label.setText(enabled ? "Your wifi is enabled" : "Your wifi is not enabled");
            state_button.setText(enabled ? "Disable wifi" : "Enable wifi");

            switch (state) {
                case WifiManager.WIFI_STATE_ENABLED:
                    data_labels[WIFISTATE].setText(" Enabled");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    data_labels[WIFISTATE].setText(" Disabled");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    data_labels[WIFISTATE].setText(" Being Disabled");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    data_labels[WIFISTATE].setText(" Being Enabled");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    data_labels[WIFISTATE].setText(" Unknown");
                    break;
            }
            if (enabled) {
                data_labels[IPADD].setText(FileManager.integerToIPAddress(info.getIpAddress()));
                data_labels[MACADD].setText(info.getMacAddress());
                data_labels[SSID].setText(info.getSSID());
                data_labels[LINKSPD].setText(info.getLinkSpeed() + " Mbps");
                data_labels[SSTRENGTH].setText("strength " + strength);
            } else {
                data_labels[IPADD].setText("N/A");
                data_labels[MACADD].setText(info.getMacAddress());
                data_labels[SSID].setText("N/A");
                data_labels[LINKSPD].setText("N/A");
                data_labels[SSTRENGTH].setText("N/A");
            }
        }
    }

    private class ButtonHandler implements OnClickListener {

        @SuppressLint("MissingPermission")
        public void onClick(View v) {

            if (v.getId() == Estilos.getIdResource(contexto, "back_button")) {
                if (CheckPermisos.validarPermisos(getActivity(), CheckPermisos.CHANGE_WIFI_STATE, 444)) {
                    if (wifi.isWifiEnabled()) {
                        wifi.setWifiEnabled(false);
                        state_button.setText("Enable wifi");
                    } else {
                        wifi.setWifiEnabled(true);
                        state_button.setText("Disable wifi");
                        get_wifi();
                    }
                }
            } else if (v.getId() == Estilos.getIdResource(contexto, "zip_button")) {
                //finish();
            }
        }
    }

}
