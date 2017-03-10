package com.example.mymusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.adapter.SongAdapter;
import com.example.mymusic.model.Song;

import java.util.List;

/**
 * Created by wn123 on 2017/3/10.
 */

public class ListViewFragment extends Fragment{

    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private View view;

    List<Song> songlist=null;
    SongAdapter adapter;

    MainActivity activity;
    private static int lindex;
    private static Song lsong;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.list_view_frag,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        activity=(MainActivity)getActivity();
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);

        adapter=new SongAdapter(activity,R.layout.songlist_item,songlist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lindex=position;
                lsong=songlist.get(lindex);
                activity.changeBottom(lindex,lsong);
            }
        });
    }

}
