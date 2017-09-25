package com.example.mymusic.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by wn123 on 2017/3/19.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mDatas;
    protected int mLayoutId;
    protected Context mContext;

    public CommonAdapter(Context context,List<T> datas,int layoutId){
        mContext=context;
        mDatas=datas;
        mLayoutId=layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=ViewHolder.get(mContext,convertView,parent,mLayoutId,position);

        convert(holder,getItem(position), position);

        return holder.getmConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }
}
