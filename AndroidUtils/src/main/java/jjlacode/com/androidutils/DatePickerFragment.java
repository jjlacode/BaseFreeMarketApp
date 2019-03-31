package jjlacode.com.androidutils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    private long fecha;

    public static DatePickerFragment newInstance(long fechac, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        fragment.setFecha(fechac);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void setFecha(long fecha) {

        this.fecha = fecha;

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        TimeZone timezone = TimeZone.getDefault();
        Calendar c = new GregorianCalendar(timezone);
        c.setTimeInMillis(fecha);
        //final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

}
