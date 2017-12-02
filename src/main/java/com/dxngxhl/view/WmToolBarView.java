package com.dxngxhl.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxngxhl.R;
import com.dxngxhl.R2;

import butterknife.BindView;

/**
 * Created by dxngxhl on 2017/12/2.
 * titleview
 */

public class WmToolBarView extends Toolbar implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @BindView(R2.id.toolbar)
    Toolbar toolBar;
    @BindView(R2.id.title_back)
    ImageView titleBack;
    @BindView(R2.id.title_name)
    TextView titleName;
    @BindView(R2.id.title_rightTv)
    TextView titleRightTv;
    /*是否第一次加载图标(主要针对首页一对多fragment)*/
    private boolean title_menu_first = true;
    /*是否第一次加载返回*/
    private boolean title_back_first = true;
    /*是否是返回(有可能是代表别的功能)*/
    private boolean is_title_back = true;

    public WmToolBarView(Context context) {
        super(context);
        initView(context);
    }

    public WmToolBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WmToolBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.toolbar_view,this,true);
        setTitleLeft(true,0);
        setRightRes();
    }
    /**
     * 初始化title
     * @param title :titleName
     * @param resId :展示右边按钮图标，menu->toolbar_menu中定义的相应id,例如:R.id.title_add
     * */
//    public void initTitle(String title,int ...resId){
//        setTitleName(title);
//        showTitleIcon(resId);
//    }
    /**
     * 设置返回
     * @param back :是否返回：是-->返回，不是则设置其他图标
     * @param resourcesId :图标id,返回时随意设置，不使用
     * */
    public WmToolBarView setTitleLeft(final boolean back,int resourcesId){
        is_title_back = back;
        if (title_back_first || titleBack == null){
            titleBack.setOnClickListener(this);
            title_back_first = false;
        }
        titleBack.setVisibility(View.VISIBLE);
        if (!back){
            titleBack.setImageResource(resourcesId);
        }
        return this;
    }
    /**
     * 设置title
     * @param title ：title
     * */
    public WmToolBarView setTitleName(String title){
        titleName.setText(title);
        return this;
    }
    /**
     * title右侧:图标类
     * */
    public WmToolBarView setRightRes(){
        //扩展menu
        toolBar.inflateMenu(R.menu.toolbar_menu);
        //添加监听
        toolBar.setOnMenuItemClickListener(this);
        return this;
    }
    /**
     * 显示title图标
     * @param itemId :itemId :图标对应的选项id（1个到3个）,最多显示3两个
     * */
    public WmToolBarView showTitleIcon(int... itemId){
        for (int item:itemId){
            //显示
            toolBar.getMenu().findItem(item).setVisible(true);//通过id查找,也可以用setIcon()设置图标
//            toolBar.getMenu().getItem(0).setVisible(true);//通过位置查找
        }
        return this;
    }
    /**
     * 隐藏title图标
     * @param itemId :图标对应的选项id
     * */
    public WmToolBarView hideTitleIcon(int... itemId){
        if (titleBack != null)
            titleBack.setVisibility(View.GONE);
        for (int item:itemId){
            //隐藏
            toolBar.getMenu().findItem(item).setVisible(false);
        }
        return this;
    }
    /**
     * title右侧文字
     * @param str :文字内容
     * */
    public WmToolBarView setTitleRightText(String str){
        titleRightTv.setVisibility(View.VISIBLE);
        titleRightTv.setText(str);
        titleRightTv.setOnClickListener(this);
        return this;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_back && is_title_back){
//            onBackPressed();
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
