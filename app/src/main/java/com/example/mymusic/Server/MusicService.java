package com.example.mymusic.Server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.model.Song;
import com.example.mymusic.utils.ExitApp;
import com.example.mymusic.utils.MusicLoader;
import com.example.mymusic.utils.MyApplication;

import java.io.IOException;
import java.util.List;

/**
 * Created by wn123 on 2017/3/3.
 */

public class MusicService extends Service {

    ExitApp exit;
    private MediaPlayer mediaPlayer = null;
    List<Song> songList = null;
    private static int mindex;
    private static Song nsong;
    private static int mProgress;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    NotifyBroadcastReceiver receiver;

    RemoteViews remoteView;

    Button playButton;
    View view;

    private static Boolean mPlay = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        songList = MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();
        pref = getSharedPreferences("DATA", MODE_PRIVATE);
        mindex = pref.getInt("INDEX", 0);
        nsong = songList.get(mindex);
        getNotification();
        initMediaPlayer(nsong.getUrl());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.mymusic.SERVICE_BROADCAST");
        receiver = new NotifyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mPlay = false;
            playOrStop(mPlay);
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unregisterReceiver(receiver);
        //退出整个程序
        exit.exitApp();
    }

    private MusicBinder mBinder = new MusicBinder();

    public class MusicBinder extends Binder {
        public void init(int index) {
            mindex = index;
            nsong = songList.get(mindex);
            //initMediaPlayer(nsong.getUrl());
        }

        public void mbplaySong(int index) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                mPlay = true;
                playOrStop(mPlay);
            }
        }

        public void pauseSong() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mPlay = false;
                playOrStop(mPlay);
            }
        }

        public void preSong(int index) {
            changeSong(index);
        }

        public void nextSong(int index) {
            changeSong(index);
        }

        public void chooseSong(int index) {
            changeSong(index);
        }

        public int getCurrent(){
            //return mProgress;
            if(mediaPlayer!=null){
                return mediaPlayer.getCurrentPosition();
            }
            return 0;
        }

        public void setCurrent(int progress){
            if (mediaPlayer!=null) {
                mediaPlayer.seekTo(progress);
            }
        }

    }




    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //该方法preSong,nextSong,chooseSong都可用
    public void changeSong(int index) {
        mindex = index;
        nsong = songList.get(mindex);
        getNotification();
        mediaPlayer.reset();
        initMediaPlayer(nsong.getUrl());
        mediaPlayer.start();
        mPlay = true;
        playOrStop(mPlay);

        editor = pref.edit();                //如果activity未启动，则接收这个参数
        editor.putInt("INDEX", mindex);
        editor.commit();
    }

    //service中的playsong方法
    public void playSong(int index) {
        if (!mediaPlayer.isPlaying()) {
            mindex = index;
            nsong = songList.get(mindex);
            getNotification();
            initMediaPlayer(nsong.getUrl());
            mediaPlayer.start();
            mPlay = true;
            playOrStop(mPlay);
        }
    }

    private void initMediaPlayer(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通知的相关方法
    private void getNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext())
                .setSmallIcon(R.mipmap.ic_launcher);
        View view = LayoutInflater.from(this).inflate(R.layout.notify_item, null);


        remoteView = new RemoteViews(getPackageName(), R.layout.notify_item);
        remoteView.setTextViewText(R.id.notify_song_name, nsong.getSongName());
        remoteView.setTextViewText(R.id.notify_player_name, nsong.getArtist());


        //playButton=(Button)view.findViewById(R.id.notify_play);


        //该段是对notification上按键的监听，点击后会发送广播，修改service上的geq显示，以及更改播放的歌曲
        Intent intent1 = new Intent("com.example.mymusic.SERVICE_BROADCAST");
        intent1.putExtra("CODE", "playorpause");
        PendingIntent pi1 = PendingIntent.getBroadcast(MyApplication.getContext(), 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        intent1.putExtra("CODE", "next");
        PendingIntent pi2 = PendingIntent.getBroadcast(MyApplication.getContext(), 2, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

        remoteView.setOnClickPendingIntent(R.id.notify_play, pi1);
        remoteView.setOnClickPendingIntent(R.id.notify_next, pi2);

        builder.setContent(remoteView);
        startForeground(1, builder.build());
    }

    private class NotifyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intent2 = new Intent("com.example.mymusic.ACTIVITY_BROADCAST");

            String code = intent.getStringExtra("CODE");
            if (code.equals("playorpause")) {
                if (mPlay == true) {
                    mediaPlayer.pause();
                    mPlay = false;
                    playOrStop(mPlay);
                } else {
                    mediaPlayer.start();
                    mPlay = true;
                    playOrStop(mPlay);
                }
            } else if (code.equals("next")) {
                mindex += 1;
                changeSong(mindex);
            }
            intent2.putExtra("index", mindex);  //发送给activity的广播接收器的参数
            intent2.putExtra("PLAYORPAUSE", mPlay);
            sendBroadcast(intent2);


            editor = pref.edit();                //如果activity未启动，则接收这个参数
            editor.putInt("INDEX", mindex);
            editor.putBoolean("PLAYORPAUSE", mPlay);
            editor.commit();
        }

    }

    //notification上播放或暂停按钮的显示
    private void playOrStop(Boolean play) {
        if (play == true) {
            //playButton.setBackgroundResource(R.drawable.start1);
            remoteView.setImageViewResource(R.id.notify_play, R.drawable.start1);
        } else {
            //playButton.setBackgroundResource(R.drawable.pause2);
            remoteView.setImageViewResource(R.id.notify_play, R.drawable.pause2);
        }
    }

}
