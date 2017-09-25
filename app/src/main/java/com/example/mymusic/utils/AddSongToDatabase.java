package com.example.mymusic.utils;

import android.util.Log;

import com.example.mymusic.model.Song;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wn123 on 2017/3/27.
 */

public class AddSongToDatabase {




    public void addSongToRecent(Song song){
        int recentPlay=song.getRecentPlay();
        if (recentPlay!=1){
            song.setRecentPlay(1);
            song.save();
        }
//        if(Songs.size()<=0){
//            Log.d("add","aad");
//            getRecentSong(song);
//        }
////        else {//如果数据库里已有该歌曲，则将其删除，重新添加
////            for (Song recSong:Songs) {
////                DataSupport.delete(Song.class, recSong.getId());
////            }
////            getRecentSong(song);
////        }
//
//    }
//    private void getRecentSong(Song song) {
//        Song recSong=new Song(song.getId(),song.getAlbumId(),song.getAlbum(),
//                song.getSongName(),song.getArtist(),song.getSize(),song.getDuration(),song.getUrl(),song.getDisplayName());
//        recSong.save();
    }
}
