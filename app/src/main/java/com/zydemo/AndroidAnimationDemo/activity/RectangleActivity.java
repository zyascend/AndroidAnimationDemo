package com.zydemo.AndroidAnimationDemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zydemo.AndroidAnimationDemo.R;
import com.zydemo.AndroidAnimationDemo.views.RectangleAnimation;

/**
 *
 * Created by zyascend on 2016/10/11.
 */

public class RectangleActivity extends AppCompatActivity {

    private Button button;
    private RectangleAnimation rectangleAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rectangle_layout);
        button = (Button)findViewById(R.id.go);

        rectangleAnimation = (RectangleAnimation) findViewById(R.id.rectangleAnim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rectangleAnimation.startAnim();
            }
        });


    }
}
