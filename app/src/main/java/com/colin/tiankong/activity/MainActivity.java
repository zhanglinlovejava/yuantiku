package com.colin.tiankong.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.colin.tiankong.R;

/**
 * Created by Colin.Zhang on 2017/3/30.
 */

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        findViewById(R.id.tvReadTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadTestActivity.actionLaunch(MainActivity.this);
            }
        });
        findViewById(R.id.tvGSWMX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GSWMXActivity.actionLaunch(MainActivity.this);
            }
        });findViewById(R.id.tvWXTK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXTKActivity.actionLaunch(MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
