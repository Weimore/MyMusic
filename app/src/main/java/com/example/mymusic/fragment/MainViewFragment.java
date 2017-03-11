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

import java.util.List;

/**
 * Created by wn123 on 2017/3/11.
 */

public class MainViewFragment extends Fragment implements View.OnClickListener{

    private Button localButton;
    private Button myFavorite;

    private ListViewFragment fragment;

    private List<Song> songlist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main_view_fag,container,false);
        localButton=(Button)view.findViewById(R.id.local_button);
        myFavorite=(Button)view.findViewById(R.id.myfavorite_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localButton.setOnClickListener(this);
        myFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_button:
                fragment=new ListViewFragment();
                fragment.addListData(MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData(),"本地音乐");
                replaceFragment(fragment);
                break;
            case R.id.myfavorite_button:
                fragment=new ListViewFragment();
                fragment.addListData(MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData(),"我喜欢");
                replaceFragment(fragment);
                break;
        }
    }

    //替换碎片
    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment,fragment).addToBackStack(null).commit();
    }
}
