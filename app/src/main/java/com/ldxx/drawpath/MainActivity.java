package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //private EditText etX;
    //private EditText etY;
    private GPathView path;

    int x = 116428994;
    int y = 39899438;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = (GPathView) findViewById(R.id.path);
        //etX = (EditText) findViewById(R.id.ed_x);
        //etY = (EditText) findViewById(R.id.ed_y);

        //path.setMyLocation(new Point(116428994,39899438));
        Button up = (Button) findViewById(R.id.btn_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point p = path.getLastPoint();
                if (p == null) {
                    path.addPoint(new Point(x, y));
                } else {
                    path.addPoint(new Point(p.x, p.y - 100));
                }
            }
        });

        Button right = (Button) findViewById(R.id.btn_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point p = path.getLastPoint();
                if (p == null) {
                    path.addPoint(new Point(x, y));
                } else {
                    path.addPoint(new Point(p.x + 100, p.y));
                }
            }
        });

        Button down = (Button) findViewById(R.id.btn_down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point p = path.getLastPoint();
                if (p == null) {
                    path.addPoint(new Point(x, y));
                } else {
                    path.addPoint(new Point(p.x, p.y + 100));
                }
            }
        });

        Button left = (Button) findViewById(R.id.btn_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point p = path.getLastPoint();
                if (p == null) {
                    path.addPoint(new Point(x, y));
                } else {
                    path.addPoint(new Point(p.x - 100, p.y));
                }
            }
        });

    }

    private boolean start = false;

    public void addPoint(View view) {
        //116.42899,39.899438
        int x = 116428994;
        int y = 39899438;
        if (!start) {
            path.addPoint(new Point(x, y));
            start = true;
        } else {

            path.addPoint(new Point(x + getRandomInt(), y + getRandomInt()));
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int getRandomInt() {
        java.util.Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(1000);// 返回[0,10)集合中的整数，注意不包括10
        return (result + 1) * 1000;
    }
}
