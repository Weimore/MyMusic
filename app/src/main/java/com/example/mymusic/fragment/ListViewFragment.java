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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;

import com.example.mymusic.listener.ActivityListener;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.CommonAdapter;
import com.example.mymusic.utils.MyApplication;
import com.example.mymusic.utils.ViewHolder;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wn123 on 2017/3/10.
 */

public class ListViewFragment extends BaseListFragment<Song> {


    public ListViewFragment(int layoutId, int itemLayoutId, List<Song> lists, String text) {
        super(layoutId, itemLayoutId, lists, text);
    }

    @Override
    public void setMyAdapter(Activity activity, List<Song> lists, int itemLayoutId, ListView listView) {
        SongAdapter songAdapter=new SongAdapter(activity,lists,itemLayoutId);
        listView.setAdapter(songAdapter);
    }



    @Override
    public String setChangeList(SharedPreferences.Editor editor) {
        String s="LOCAL";
        editor.putString("LIST",s);
        editor.commit();
        return s;
    }

    public class SongAdapter extends CommonAdapter<Song> {

        public SongAdapter(Context context, List<Song> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, Song song) {
            ((TextView)holder.getView(R.id.songlist_item_songname)).setText(song.getSongName());
            ((TextView)holder.getView(R.id.songlist_item_playername)).setText(song.getArtist());
        }
    }

    //    private TextView titleText;
//    private Button backButton;
//    private ListView listView;
//    private View view;
//
//    List<Song> songlist=null;
//    SongAdapter adapter;
//
//    MainActivity activity;
//
//    private String mtext;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.list_view_frag,container,false);
//        activity=(MainActivity)getActivity();
//        titleText=(TextView)view.findViewById(R.id.title_text);
//        backButton=(Button)view.findViewById(R.id.back_button);
//        listView=(ListView)view.findViewById(R.id.list_view);
//
//        adapter=new SongAdapter(activity,songlist,R.layout.songlist_item);
//        listView.setAdapter(adapter);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        titleText.setText(mtext);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                activity.playSong(position);
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();  //按上面的返回键返回之前的碎片界面
//            }
//        });
//    }
//
//    public void addListData(List<Song> list,String text){
//        songlist=list;
//        mtext=text;
//    }
}
