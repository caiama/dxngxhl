package com.dxngxhl;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxngxhl.view.WmToolBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dxngxhl on 2017/12/1.
 */

public abstract class BaseActivity extends AppCompatActivity{
    @BindView(R2.id.titleView)
    WmToolBarView toolBarView;
    /**设置布局文件*/
    public abstract int setActivityView();
    /**初始化布局*/
    public abstract void initView();
    /**初始化title*/
    public abstract void initTitle(WmToolBarView toolBarView);
    /**初始化数据*/
    public abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setActivityView());
        ButterKnife.bind(this);
        initTitle(toolBarView);
        initView();
        initData();
    }
}
