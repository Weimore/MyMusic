package com.example.mymusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.mymusic.Server.MusicService;
import com.example.mymusic.adapter.SongAdapter;
import com.example.mymusic.fragment.ListViewFragment;
import com.example.mymusic.fragment.MainViewFragment;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.MusicLoader;
import com.example.mymusic.utils.MyApplication;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private ListView listView;

    private TextView sequence;
    private TextView songname;
    private TextView playername;
    private ImageView songimage;
    private Button playButton;
    private Button nextButton;

//    private Button localButton;
//    private Button myfavoriteButton;
//    private Button downloadButton;

    private LinearLayout firstLayout;
    ListViewFragment fragment;

    //List<Song> songlist=new ArrayList<Song>();  //存放从数据库中获得的歌曲
    List<Song> songlist=null;
    //SongAdapter adapter;
    private static Song nsong;  //当前播放的歌曲
    private static int index;
    SharedPreferences pref;

    private MusicService.MusicBinder musicBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songimage=(ImageView)findViewById(R.id.song_iamge);
        songname=(TextView)findViewById(R.id.song_name);
        playername=(TextView)findViewById(R.id.player_name);
        firstLayout=(LinearLayout)findViewById(R.id.first_show);

        playButton=(Button)findViewById(R.id.mainplay_button);
        nextButton=(Button)findViewById(R.id.nextplay_button);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        pref =getSharedPreferences("listIndex",MODE_PRIVATE);
        //songlist=new MusicLoader().queryData(); //将数据传入songlist
        songlist = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();

        final Intent intent=new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);

        replaceFragment(new MainViewFragment());
    }

    //供碎片调用的方法
    public void changeBottom(int mindex,Song song){
        index=mindex;
        nsong=song;
        songname.setText(nsong.getSongName());
        playername.setText(nsong.getArtist());
        musicBinder.chooseSong(index);
    }

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder=(MusicService.MusicBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainplay_button:
                musicBinder.playSong(index);
                songname.setText(nsong.getSongName());
                playername.setText(nsong.getArtist());
                break;
            case R.id.nextplay_button:
                index+=1;
                nsong=songlist.get(index);
                musicBinder.nextSong(index);
                songname.setText(nsong.getSongName());
                playername.setText(nsong.getArtist());
                break;
            default:
                break;
        }
    }

    //替换碎片
    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
