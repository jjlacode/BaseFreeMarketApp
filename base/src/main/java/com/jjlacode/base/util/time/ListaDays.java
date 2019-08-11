package com.jjlacode.base.util.time;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

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
