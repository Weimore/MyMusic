package com.example.mymusic.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic.R;

/**
 * Created by wn123 on 2017/3/19.
 */

public class ViewHolder {
    private Context mContext;
    private int mPosition;

    public int getmPosition() {
        return mPosition;
    }

    private SparseArray<View> mViews;

    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int LayoutId, int position){
        mContext=context;
        mPosition=position;
        mViews=new SparseArray<View>();

        mConvertView= LayoutInflater.from(mContext).inflate(LayoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context,View convertView,ViewGroup parent,int LayoutId,int position){
        if(convertView==null){
           return new ViewHolder(context,parent,LayoutId,position);
        }else{
            ViewHolder holder=(ViewHolder)convertView.getTag();
            holder.mPosition=position;
            return holder;
        }
    }

    public <T extends View>T getView(int viewId){
        View view=mViews.get(viewId);
        if (view==null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T)view;
    }

    public View getmConvertView(){
        return mConvertView;
    }
}
