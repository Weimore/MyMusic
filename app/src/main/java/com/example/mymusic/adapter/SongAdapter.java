package com.example.mymusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mymusic.R;
import com.example.mymusic.model.Song;

import java.util.List;

/**
 * Created by wn123 on 2017/2/27.
 */

public class SongAdapter extends ArrayAdapter<Song>{
    private int resourceId;
    private Context mContext;
    public SongAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        resourceId=resource;
        mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
        view= LayoutInflater.from(mContext).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
           // viewHolder.sequence=(TextView) view.findViewById(R.id.songlist_item_sequence);
            viewHolder.songName=(TextView)view.findViewById(R.id.songlist_item_songname);
            viewHolder.playerName=(TextView)view.findViewById(R.id.songlist_item_playername);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.songName.setText(song.getSongName());
        viewHolder.playerName.setText(song.getArtist());
        return view;
    }

    class ViewHolder{
       // TextView sequence;
        TextView songName;
        TextView playerName;
    }
}
