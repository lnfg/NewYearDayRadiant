package com.lnfg.newyeardayradiant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.ScaleAnimation;

import com.lnfg.newyeardayradiant.lnfg.NewYearDayRadiantView;

public class MainActivity extends AppCompatActivity {

    private NewYearDayRadiantView dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dd = (NewYearDayRadiantView)findViewById(R.id.dd);

//        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f);
//        scaleAnimation.setDuration(3500);
//        scaleAnimation.setRepeatCount(10);
//        dd.startAnimation(scaleAnimation);

    }
}