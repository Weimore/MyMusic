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
import com.example.mymusic.fragment.ListViewFragment;
import com.example.mymusic.fragment.MainViewFragment;
import com.example.mymusic.listener.ActivityListener;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.MusicLoader;
import com.example.mymusic.utils.MyApplication;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private ListView listView;

    private TextView sequence;
    private TextView songname;
    private TextView playername;
    private ImageView songimage;
    private Button playButton;
    private Button nextButton;

    private static String NOWLIST="LOCAL";


    private SeekBar mSeekBar;

    private LinearLayout firstLayout;
    ListViewFragment fragment;

    //List<Song> songlist=new ArrayList<Song>();  //存放从数据库中获得的歌曲
    List<Song> songlist = null;
    private static Song nsong;  //当前播放的歌曲
    private static int index;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private MusicService.MusicBinder musicBinder;

    private ActivityBroadcastReceiver receiver;

    private static Boolean play = false;
    private static boolean threadControl = false;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songimage = (ImageView) findViewById(R.id.song_iamge);
        songname = (TextView) findViewById(R.id.song_name);
        playername = (TextView) findViewById(R.id.player_name);
        firstLayout = (LinearLayout) findViewById(R.id.first_show);

        playButton = (Button) findViewById(R.id.mainplay_button);
        nextButton = (Button) findViewById(R.id.nextplay_button);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);


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

        //songlist=new MusicLoader().queryData();
        // 将数据传入songlist
        pref = getSharedPreferences("DATA", MODE_PRIVATE);
        NOWLIST=pref.getString("LIST","LOCAL");
        //判断当前是哪一个播放list
       if (NOWLIST.equals("LOCAL")){
            songlist = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();
        }else if(NOWLIST.equals("RECENT")){
            songlist= DataSupport.where("recentPlay = ?","1").order("id desc").find(Song.class);
        }


        final Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.mymusic.ACTIVITY_BROADCAST");
        receiver = new ActivityBroadcastReceiver();
        registerReceiver(receiver, filter);

        //获取service传来的index，并调整下面的歌曲显示

        index = pref.getInt("INDEX", 0);
        changeBottom(index);

        threadControl = false;

        //判断此时歌曲是否在后台播放，改变播放按钮的状态
        play = pref.getBoolean("PLAYPRPAUSE", false);
        playOrStop(play);


        replaceFragment(new MainViewFragment());


        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (threadControl == true) {
                        break;
                    }
                    try {
                        // int mProgress=0;
                        Thread.sleep(500);
                        final int progress = musicBinder.getCurrent();
                        Log.d("线程is running", progress + " " + play);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSeekBar.setProgress(progress);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


    //改变视图
    public void changeBottom(int mindex) {
        index = mindex;
        nsong = songlist.get(index);
        songimage.setImageBitmap(nsong.getBitmap());
        songname.setText(nsong.getSongName());
        playername.setText(nsong.getArtist());
        mSeekBar.setMax(nsong.getDuration());
        //playername.setText(getTotalTime(nsong.getDuration()));
    }

    //供碎片调用的方法,播放歌曲
    public void playSong(int mindex,String nowlist) {

        if (nowlist.equals("LOCAL")){
            songlist = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();
        }else if(nowlist.equals("RECENT")){
            songlist= DataSupport.where("recentPlay = ?","1").order("id desc").find(Song.class);
        }
        editor=pref.edit();
        editor.putString("LIST",nowlist);
        editor.commit();
        changeBottom(mindex);
        musicBinder.chooseSong(index,nowlist);
        play = true;
        playOrStop(play);
    }

    private String getTotalTime(int duration) {
        String totalTime = null;

        int totalSeconds = duration / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;

        if (seconds < 10) {
            totalTime = minutes + ":0" + seconds;
        } else {
            totalTime = minutes + ":" + seconds;
        }

        return totalTime;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            musicBinder.init(index);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainplay_button:
                if (play == false) {
                    musicBinder.mbplaySong(index);
                    play = true;
                    playOrStop(play);
                } else {
                    musicBinder.pauseSong();
                    play = false;
                    playOrStop(play);
                }
                break;
            case R.id.nextplay_button:
                index += 1;
                if(index>=songlist.size()){
                    index=0;
                }
                nsong = songlist.get(index);
                musicBinder.nextSong(index);
                play = true;
                playOrStop(play);
                break;
            default:
                break;
        }
        changeBottom(index);
    }

    //替换碎片
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor = pref.edit();
        editor.putBoolean("PLAYPRPAUSE", play);
        editor.commit();
        threadControl = true;
        unbindService(connection);
        unregisterReceiver(receiver);
    }


    private void playOrStop(Boolean play) {
        if (play == true) {
            playButton.setBackgroundResource(R.drawable.play_normal);
        } else {
            playButton.setBackgroundResource(R.drawable.play_pressed);
        }

    }

    private class ActivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            index = intent.getIntExtra("index", 0);
            changeBottom(index);
            play = intent.getBooleanExtra("PLAYORPAUSE", false);
            playOrStop(play);
        }
    }

//    ActivityListener listener=new ActivityListener() {
//        @Override
//        public void changeList(String code) {
//            if (code.equals("LOCAL")){
//                songlist = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();
//            }else if(code.equals("RECENT")){
//                songlist= DataSupport.order("id desc").find(Song.class);
//            }
//        }
//    };
}
