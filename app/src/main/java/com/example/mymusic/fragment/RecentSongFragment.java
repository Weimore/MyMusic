package com.example.mymusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.listener.ActivityListener;
import com.example.mymusic.model.Song;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.CommonAdapter;
import com.example.mymusic.utils.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wn123 on 2017/3/26.
 */

public class RecentSongFragment extends BaseListFragment<Song> {

    private RecentAdapter recentAdapter;
    private List<Song> mList;

    public RecentSongFragment (int layoutId, int itemLayoutId, List<Song> lists, String text) {
        super(layoutId, itemLayoutId, lists, text);

    }

    @Override
    public void setMyAdapter(Activity activity, List<Song> lists, int itemLayoutId, ListView listView) {
        //mList=DataSupport.order("id desc").find(Song.class);
        recentAdapter=new RecentAdapter(activity,lists,itemLayoutId);
        listView.setAdapter(recentAdapter);
    }


    @Override
    public String setChangeList(SharedPreferences.Editor editor) {
//        mLists.clear();
//        mLists.addAll(DataSupport.order("id desc").find(Song.class));
//        recentAdapter.notifyDataSetChanged();
        String s="RECENT";
        editor.putString("LIST",s);
        editor.commit();
        return s;
    }

    class RecentAdapter extends CommonAdapter<Song>{
        public RecentAdapter(Context context, List datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, Song Song) {
            ((TextView)holder.getView(R.id.songlist_item_songname)).setText(Song.getSongName());
        }
    }
}
