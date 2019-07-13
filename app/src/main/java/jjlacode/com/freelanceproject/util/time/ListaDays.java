package jjlacode.com.freelanceproject.util.time;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import jjlacode.com.freelanceproject.util.time.Day;

import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryList;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryListDetalle;

public class ListaDays extends ArrayList<Day> implements Serializable {

    public ListaDays(int initialCapacity) {
        super(initialCapacity);
    }

    public ListaDays() {
    }

    public ListaDays(@NonNull Collection<? extends Day> c) {
        super(c);
    }
}
