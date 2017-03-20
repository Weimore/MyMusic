package com.example.mymusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.widget.SeekBar;
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

    private static int time;

    private SeekBar mSeekBar;

    private LinearLayout firstLayout;
    ListViewFragment fragment;

    //List<Song> songlist=new ArrayList<Song>();  //存放从数据库中获得的歌曲
    List<Song> songlist=null;
    //SongAdapter adapter;
    private static Song nsong;  //当前播放的歌曲
    private static int index;
    SharedPreferences pref;

    private MusicService.MusicBinder musicBinder;

    private ActivityBroadcastReceiver receiver;

    private static Boolean play=false;

    Handler handler;
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

        mSeekBar=(SeekBar)findViewById(R.id.seek_bar);



        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicBinder.setCurrent(mSeekBar.getProgress());
            }
        });

        //songlist=new MusicLoader().queryData(); //将数据传入songlist
        songlist = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();


        final Intent intent=new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);

        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.mymusic.ACTIVITY_BROADCAST");
        receiver=new ActivityBroadcastReceiver();
        registerReceiver(receiver,filter);

        //获取service传来的index，并调整下面的歌曲显示
        pref =getSharedPreferences("DATA",MODE_PRIVATE);
        index=pref.getInt("INDEX",0);
        changeBottom(index);
        //判断此时歌曲是否在后台播放，改变播放按钮的状态
        play=pref.getBoolean("PLAYPRPAUSE",false);
        playOrStop(play);


        replaceFragment(new MainViewFragment());

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mSeekBar.setProgress(msg.what);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        // int mProgress=0;
                        Thread.sleep(500);
                        int progress=musicBinder.getCurrent();
                        //if(progress>mProgress){
                        Message message=new Message();
                        message.what=progress;
                        handler.sendMessage(message);
                        //    mProgress=progress;
                        // }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //改变视图
    public void changeBottom(int mindex){
        index=mindex;
        nsong=songlist.get(index);
        songname.setText(nsong.getSongName());
        playername.setText(nsong.getArtist());
        mSeekBar.setMax(nsong.getDuration());
        //playername.setText(getTotalTime(nsong.getDuration()));
    }

    //供碎片调用的方法,播放歌曲
    public void playSong(int mindex){
        changeBottom(mindex);
        musicBinder.chooseSong(index);
        play=true;
        playOrStop(play);
    }

    private String getTotalTime(int duration){
        String totalTime=null;

        int totalSeconds = duration/1000 ;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;

        if(seconds<10){
            totalTime=minutes+":0"+seconds;
        }else {
            totalTime=minutes+":"+seconds;
        }

        return totalTime;
    }

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder=(MusicService.MusicBinder)service;
            musicBinder.init(index);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainplay_button:
                if(play==false) {
                    musicBinder.mbplaySong(index);
                    play=true;
                    playOrStop(play);
                }else {
                    musicBinder.pauseSong();
                    play=false;
                    playOrStop(play);
                }
                break;
            case R.id.nextplay_button:
                index+=1;
                nsong=songlist.get(index);
                musicBinder.nextSong(index);
                play=true;
                playOrStop(play);
                break;
            default:
                break;
        }
        changeBottom(index);
    }

    //替换碎片
    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(receiver);
    }


    private void playOrStop(Boolean play){
        if(play==true){
            playButton.setBackgroundResource(R.drawable.play_normal);
        }else {
            playButton.setBackgroundResource(R.drawable.play_pressed);
        }
    }

    private class ActivityBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            index=intent.getIntExtra("index",0);
            changeBottom(index);
            play=intent.getBooleanExtra("PLAYORPAUSE",false);
            playOrStop(play);
        }
    }


}
