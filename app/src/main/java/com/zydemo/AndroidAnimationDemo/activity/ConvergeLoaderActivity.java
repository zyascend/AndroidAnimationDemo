package com.zydemo.AndroidAnimationDemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zydemo.AndroidAnimationDemo.R;
import com.zydemo.AndroidAnimationDemo.views.ConvergeLoader;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ConvergeLoaderActivity extends AppCompatActivity {
    private Button go;
    private Button stop;
    private ConvergeLoader view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_layout);
        view = (ConvergeLoader) findViewById(R.id.loader);
        go = (Button) findViewById(R.id.go);
        stop = (Button) findViewById(R.id.stop);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.stop();
            }
        });

    }


}
