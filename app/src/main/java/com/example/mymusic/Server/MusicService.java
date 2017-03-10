package com.example.mymusic.Server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

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
    private MediaPlayer mediaPlayer=null;
    List<Song> songList=null;
    private static int mindex;
    private static Song nsong;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        songList= MusicLoader.getInstance(MyApplication.getContext().getContentResolver()).queryData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        //退出整个程序
        exit.exitApp();
    }

    private MusicBinder mBinder=new MusicBinder();

    public class MusicBinder extends Binder{
        public void playSong(int index){
            if(!mediaPlayer.isPlaying()){
                mindex=index;
                nsong=songList.get(mindex);
                getNotification();
                initMediaPlayer(nsong.getUrl());
                mediaPlayer.start();
            }
        }

        public void pauseSong(){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }

        public void preSong(int index){
            changeSong(index);
        }
        public void nextSong(int index){
            changeSong(index);
        }
        public void chooseSong(int index){
            changeSong(index);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //该方法preSong,nextSong,chooseSong都可用
    public void changeSong(int index){
        mindex=index;
        nsong=songList.get(mindex);
        getNotification();
        mediaPlayer.reset();
        initMediaPlayer(nsong.getUrl());
        mediaPlayer.start();
    }

    private void initMediaPlayer(String path){
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通知的相关方法
    private void getNotification(){
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MyApplication.getContext())
                .setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews remoteView=new RemoteViews(getPackageName(),R.layout.notify_item);
        remoteView.setTextViewText(R.id.notify_song_name,nsong.getSongName());
        remoteView.setTextViewText(R.id.notify_player_name,nsong.getArtist());
        builder.setContent(remoteView);
        manager.notify(1,builder.build());
    }
}
