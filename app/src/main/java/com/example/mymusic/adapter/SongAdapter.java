package com.example.mymusic.adapter;

import android.content.Context;

import android.widget.TextView;

import com.example.mymusic.R;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.CommonAdapter;
import com.example.mymusic.utils.ViewHolder;

import java.util.List;

/**
 * Created by wn123 on 2017/2/27.
 */

public class SongAdapter extends CommonAdapter<Song> {
    private int mLayoutId;
    private Context mContext;

    public SongAdapter(Context context, List<Song> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Song song) {
        ((TextView)holder.getView(R.id.songlist_item_songname)).setText(song.getSongName());
        ((TextView)holder.getView(R.id.songlist_item_playername)).setText(song.getArtist());
    }
}
