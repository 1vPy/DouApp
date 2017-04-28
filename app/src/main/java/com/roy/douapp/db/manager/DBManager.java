package com.roy.douapp.db.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.roy.douapp.db.DBHelper;
import com.roy.douapp.http.bean.collection.MovieCollection;
import com.roy.douapp.http.bean.movie.Casts;
import com.roy.douapp.http.bean.movie.details.JsonDetailBean;
import com.roy.douapp.http.bean.music.playlist.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/3/28.
 */

public class DBManager {
    private static DBManager instance;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private DBManager(Context context) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 插入一条收藏
     *
     * @param jsonDetailBean
     */
    public void insertCollection(JsonDetailBean jsonDetailBean) {
        db.beginTransaction();
        String type = "";
        String stars = "";
        for (String s : jsonDetailBean.getGenres()) {
            type = type + s + " ";
        }
        for (Casts casts : jsonDetailBean.getCasts()) {
            stars = stars + casts.getName() + " ";
        }
        db.execSQL("insert into tb_collection values(null,?,?,?,?)", new Object[]{jsonDetailBean.getTitle(), type, stars, jsonDetailBean.getId()});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 查询所有收藏
     *
     * @return List<MovieCollection>
     */
    public List<MovieCollection> searchCollection() {
        List<MovieCollection> movieCollectionList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from tb_collection", null);
        if (cursor.getCount() == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
            MovieCollection movieCollection = new MovieCollection();
            movieCollection.setId(cursor.getInt(cursor.getColumnIndex("id")));
            movieCollection.setMovieName(cursor.getString(cursor.getColumnIndex("movieName")));
            movieCollection.setMovieType(cursor.getString(cursor.getColumnIndex("movieType")));
            movieCollection.setMovieStar(cursor.getString(cursor.getColumnIndex("movieStar")));
            movieCollection.setMovieId(cursor.getString(cursor.getColumnIndex("movieId")));
            movieCollectionList.add(movieCollection);
        }
        cursor.close();
        return movieCollectionList;
    }


    /**
     * 根据电影id查询电影简介
     *
     * @param movieId 电影Id
     * @return MovieCollection
     */
    public MovieCollection searchCollectionByMovieId(String movieId) {
        MovieCollection movieCollection = new MovieCollection();
        Cursor cursor = db.rawQuery("select * from tb_collection where movieId = ?", new String[]{movieId});
        if (cursor.getCount() == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
            movieCollection.setId(cursor.getInt(cursor.getColumnIndex("id")));
            movieCollection.setMovieName(cursor.getString(cursor.getColumnIndex("movieName")));
            movieCollection.setMovieType(cursor.getString(cursor.getColumnIndex("movieType")));
            movieCollection.setMovieStar(cursor.getString(cursor.getColumnIndex("movieStar")));
            movieCollection.setMovieId(cursor.getString(cursor.getColumnIndex("movieId")));
        }
        cursor.close();
        return movieCollection;
    }

    /**
     * 清空收藏表
     */
    public void clearCollection() {
        db.beginTransaction();
        db.execSQL("delete from tb_collection");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 根据电影id删除收藏
     *
     * @param movieId 电影Id
     */
    public void deleteCollectionByMovieId(String movieId) {
        db.beginTransaction();
        db.execSQL("delete from tb_collection where movieId = ?", new Object[]{movieId});
        db.setTransactionSuccessful();
        db.endTransaction();
    }



    public void insertMusic(MusicBean musicBean){
        db.beginTransaction();
        db.execSQL("insert into tb_playlist values(null,?,?,?)", new Object[]{musicBean.getMusicName(), musicBean.getSingerName(), musicBean.getMusicId()});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteMusic(String musicId){
        db.beginTransaction();
        db.execSQL("delete from tb_playlist where musicId = ?", new Object[]{musicId});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<MusicBean> searchMusic(){
        List<MusicBean> musicBeanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from tb_playlist", null);
        if (cursor.getCount() == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
            MusicBean musicBean = new MusicBean();
            musicBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            musicBean.setMusicName(cursor.getString(cursor.getColumnIndex("musicName")));
            musicBean.setSingerName(cursor.getString(cursor.getColumnIndex("singerName")));
            musicBean.setMusicId(cursor.getString(cursor.getColumnIndex("musicId")));
            musicBeanList.add(musicBean);
        }
        cursor.close();
        return musicBeanList;
    }

    public void clearPlaylist() {
        db.beginTransaction();
        db.execSQL("delete from tb_playlist");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (db != null) {
            db.close();
        }
        if (instance != null) {
            instance = null;
        }
    }
}
