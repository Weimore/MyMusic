package com.example.mymusic.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.Settings;

import com.example.mymusic.MainActivity;
import com.example.mymusic.model.Song;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wn123 on 2017/2/27.
 */

public class MusicLoader {

    //搜索音乐
    private static ContentResolver contentResolver;
    //这个class的实例
    private static MusicLoader musicLoader;
    //单例模式

    //要查询音乐的数据

    public static MusicLoader getInstance(ContentResolver mcontentResolver) {
        if (contentResolver == null) {
            musicLoader = new MusicLoader();
            contentResolver = mcontentResolver;
            LogUtil.itSelf.Log("mcontentResolver"+mcontentResolver);
        }
        return musicLoader;
    }

    public static MusicLoader getInstance() {
        if (contentResolver == null) {
            musicLoader = new MusicLoader();

        }
        return musicLoader;
    }
    public MusicLoader() {
        super();
    }

    public List<Song> queryData() {
//        ContentResolver cr = MyApplication.getContext().getContentResolver();
////        if (cr == null) {
////            return;
////        }
        // 获取所有歌曲
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        Song song;
        List<Song> songList = new ArrayList<Song>();
        if(cursor!=null){
        while(cursor.moveToNext()) {

            //歌曲名
            String songName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));

            //歌手
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));

            //专辑
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));

            //长度
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));

            //时长
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));

            //歌曲ID
             long id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID));

            //专辑ID
             long albumId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

            //路径
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));

            //显示的文件名
            String displayName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

            //是否为音乐
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

            LogUtil.itSelf.Log("cursorsssss" + displayName + isMusic + album + songName);//测试用语

            //获取文件类型
            String tailName;
            if (displayName != null) {
                tailName = displayName.substring(displayName.length() - 3, displayName.length());
            } else {
                tailName = "no";
            }
            if (isMusic != 0 && (tailName.equals("mp3") || tailName.equals("wav"))) {
                song =new Song(id,albumId,album,songName,artist,size,duration,url,displayName);
                songList.add(song);
            }
        }
            cursor.close();
            //songList=DataSupport.findAll(Song.class);
            return songList;
       }else{
                return null;
            }
            }
        }

