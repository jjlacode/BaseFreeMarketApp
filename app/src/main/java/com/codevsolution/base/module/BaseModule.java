package com.codevsolution.base.module;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.controls.ViewGroupLayout;

public abstract class BaseModule {

    protected ViewGroup viewGroupParent;
    protected Fragment fragmentParent;
    protected Context context;
    protected ViewGroupLayout vistaMain;

    public BaseModule(ViewGroup parent, Context context, Fragment fragment) {
        fragmentParent = fragment;
        viewGroupParent = parent;
        this.context = context;
        vistaMain = new ViewGroupLayout(context, parent);
    }

    public abstract void init();
}
