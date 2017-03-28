package com.example.mymusic.model;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by wn123 on 2017/2/26.
 */

public class Song extends DataSupport{
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    private long id;           //歌曲ID
    private long albumId;    //专辑ID
    private String album;     //歌曲专辑名
    private String songName;  //歌曲名
    private String artist;    //歌手名
    private long size;        //歌曲大小
    private int duration;   //歌曲长度
    private String url ;     //歌曲文件路径
    private String displayName; //显示文件名

    private Bitmap bitmap;

    public Song(long id,long albumId,String album,String songName,String artist,long size,int duration,String url,String displayName){
        this.id=id;
        this.albumId=albumId;
        this.album=album;
        this.songName=songName;
        this.artist=artist;
        this.size=size;
        this.duration=duration;
        this.url=url;
        this.displayName=displayName;
    }
}
