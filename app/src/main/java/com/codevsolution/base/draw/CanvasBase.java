package com.codevsolution.base.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

public class CanvasBase extends ViewGroup {

    public CanvasBase(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
