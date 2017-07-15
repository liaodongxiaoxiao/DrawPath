package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etX;
    private EditText etY;
    private GPathView path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = (GPathView) findViewById(R.id.path);
        etX = (EditText) findViewById(R.id.ed_x);
        etY = (EditText) findViewById(R.id.ed_y);

        //path.setMyLocation(new Point(116428994,39899438));
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
