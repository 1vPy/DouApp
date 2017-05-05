package com.roy.douapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 1vPy(Roy) on 2017/3/28.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "collection.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TB_COLLECTION = "create table if not exists tb_collection(" +
            "id INTEGER primary key not null," +
            "movieName TEXT not null," +
            "movieType TEXT," +
            "movieStar TEXT," +
            "movieId TEXT not null);";
    private static final String SQL_CREATE_TB_PLAYLIST = "create table if not exists tb_playlist(" +
            "id INTEGER primary key not null," +
            "musicName TEXT not null," +
            "singerName TEXT," +
            "musicId TEXT not null);";
    private static final String SQL_CREATE_TB_SHEETS = "create table if not exists tb_sheets(" +
            "id INTEGER primary key not null," +
            "sheetName TEXT not null,"+
            "musicCount INTEGER not null);";
    private static final String SQL_CREATE_TB_SHEETS_MUSIC = "create table if not exists tb_sheets_music(" +
            "id INTEGER primary key not null," +
            "musicName TEXT not null," +
            "singerName TEXT," +
            "sheetId INTEGER not null,"+
            "musicId TEXT not null);";
    private Context mContext;
    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DBHelper getInstance(Context context){
        if(instance == null){
            synchronized (DBHelper.class){
                if(instance == null){
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TB_COLLECTION);
        db.execSQL(SQL_CREATE_TB_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
