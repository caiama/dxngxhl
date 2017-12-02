package com.dxngxhl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    /*布局*/
    private View view;
    /**/
    boolean isVisible = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = setRootView(inflater,container);
        ButterKnife.bind(view);
        Log.d("包名："+getClass().getName(),"onCreateView");
        onVisible();
        return view;

    }
    protected View setRootView(LayoutInflater inflater, @Nullable ViewGroup container){
        return null;
    }
    protected abstract void initWidght();
    protected abstract void initData();
    /**
     * 解决预加载
     * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("包名："+getClass().getName()," setUserVisibleHint  显示"+getUserVisibleHint());
        if (getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
//            onInvisible();
        }
    }
    /**
     *  预加载--显示-加载数据
     * */
    private void onVisible() {
        Log.i("","========isVisible"+isVisible+"   view"+(view != null));
        if (isVisible && view != null){
            initWidght();
        }
    }
    /**
     * 预加载--不显示时
     * */
    private void onInvisible() {
        isVisible = false;
    }
    protected <T extends View>T getView(int resourcesId){
        return (T) view.findViewById(resourcesId);
    }
    /**
     * Toast提示;
     * @param content 提示内容;
     */
    protected void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
}