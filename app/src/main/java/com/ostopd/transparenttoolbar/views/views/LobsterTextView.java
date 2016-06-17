package com.ostopd.transparenttoolbar.views.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LobsterTextView extends TextView {
    public LobsterTextView(Context context) {
        super(context);
        init(context);
    }

    public LobsterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LobsterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface t = Typeface.createFromAsset(context.getAssets(), "Lobster-Regular.ttf");
        this.setTypeface(t);
    }
}
