package com.lin.yuantiku.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.lin.yuantiku.R;

import butterknife.OnClick;

/**
 * Created by Colin.Zhang on 2017/3/30.
 */

public class MainActivity extends BaseActivity {


    @OnClick({R.id.tvReadTest, R.id.tvGSWMX, R.id.tvWXTK})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvReadTest:
                YDLJActivity.actionLaunch(MainActivity.this);
                break;
            case R.id.tvGSWMX:
                GSWActivity.actionLaunch(MainActivity.this);
                break;
            case R.id.tvWXTK:
                WXTKActivity.actionLaunch(MainActivity.this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int setContentView() {
        return R.layout.act_main;
    }

    @Override
    public void initUIAndData(Bundle savedInstanceState) {

    }

    @Override
    public void initInjector() {

    }
}
