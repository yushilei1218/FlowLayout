package com.yushilei.flowlayout;

import android.graphics.SweepGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flow = (FlowLayout) findViewById(R.id.flow);
        
    }

    public void add(View view) {
        TextView child = (TextView) LayoutInflater.from(this).inflate(R.layout.item, flow, false);
        Random random = new Random();
        int i = random.nextInt(20) + 1;
        String text = "";
        for (int j = 0; j < i; j++) {
            text += (char) random.nextInt(126) + "";
        }
        child.setText(text);
        flow.addView(child);
    }

    public void remove(View view) {
        int childCount = flow.getChildCount();
        View childAt = flow.getChildAt(childCount - 1);
        flow.removeView(childAt);
    }
}
