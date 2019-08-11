package com.jjlacode.base.util.time;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    private long hora;

    public static TimePickerFragment newInstance(long horac, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setListener(listener);
        fragment.setHora(horac);
        return fragment;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {

        this.listener = listener;
    }

    public void setHora(long hora) {

        this.hora = hora;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        TimeZone timezone = TimeZone.getDefault();
        Calendar c = new GregorianCalendar(timezone);
        c.setTimeInMillis(hora);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), listener, hour, minute, true);

    }
}
