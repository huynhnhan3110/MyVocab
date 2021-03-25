package com.example.finalhometest.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static String TABLE_WORDCORE = "WORDCORE";
    public static String COLUMN_WORD = "WORD";
    public static String COLUMN_TRANSL_VN = "TRANSL_VN";
    public static String COLUMN_NOTE = "NOTE";
    public static String COLUMN_ISLEARN = "ISLEARN";
    public static String COLUMN_ISRECALL = "ISRECALL";
    public static String COLUMN_STT = "STT";

    private static final String DB_NAME = "WordCore.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE =
            "CREATE TABLE "+TABLE_WORDCORE+ "("+
                    COLUMN_WORD+" TEXT NOT NULL,"+
                    COLUMN_TRANSL_VN+" TEXT NOT NULL,"+
                    COLUMN_NOTE+" TEXT, "+
                    COLUMN_ISLEARN+" INTEGER, "+
                    COLUMN_ISRECALL+" INTEGER, PRIMARY KEY (WORD,TRANSL_VN))";
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table yournames(id integer primary key, yourname text not null)");
        db.execSQL("create table images(id integer primary key, img blob not null)");
        db.execSQL("create table daystreek(year text, month text, day text, PRIMARY KEY (year, month,day))");

        db.execSQL("create table bangtuantu(thu text primary key, sotu text default 0 not null)");
        db.execSQL("create table bangtucb(tienganh text primary key, nghia text)");
        db.execSQL("create table bangxemtu(tienganh text primary key, nghia text, ghichu text)");
        db.execSQL("create table bangdiem(id integer primary key, sodiem integer)");
        db.execSQL("create table level(id integer primary key, num integer default 0)");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
