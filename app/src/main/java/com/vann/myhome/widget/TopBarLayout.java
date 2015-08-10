package com.vann.myhome.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vann.myhome.R;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class TopBarLayout extends LinearLayout {

    public TopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.topbar, this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });

    }

}
