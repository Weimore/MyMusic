package com.example.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.MusicLoader;
import com.example.mymusic.utils.MyApplication;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wn123 on 2017/3/11.
 */

public class MainViewFragment extends Fragment implements View.OnClickListener{




    private Button localButton;
    private Button myFavorite;
    private Button recentButton;

    private ListViewFragment listViewFragment;
    private RecentSongFragment recentSongFragment;
    private List<Song> songlist=MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main_view_fag,container,false);
        localButton=(Button)view.findViewById(R.id.local_button);
//        myFavorite=(Button)view.findViewById(R.id.myfavorite_button);
        recentButton=(Button)view.findViewById(R.id.recent_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localButton.setOnClickListener(this);
//        myFavorite.setOnClickListener(this);
        recentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_button:
                listViewFragment=new ListViewFragment(R.layout.list_view_frag,R.layout.songlist_item,songlist,"本地音乐");
                replaceFragment(listViewFragment);
                break;
//            case R.id.myfavorite_button:
//                listViewFragment=new ListViewFragment(R.layout.list_view_frag,R.layout.songlist_item,songlist,"本地音乐kk");
//                replaceFragment(listViewFragment);
//                break;
            case R.id.recent_button:
                recentSongFragment=new RecentSongFragment(R.layout.recent_frag,R.layout.recent_item,DataSupport.where("recentPlay = ?","1").order("id desc").find(Song.class),"最近播放");
                replaceFragment(recentSongFragment);
                break;
            default:
                break;
        }
    }

    //替换碎片
    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment,fragment).addToBackStack(null).commit();
    }
}
