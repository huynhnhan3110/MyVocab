package com.example.finalhometest.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class DataSource {
    private final SQLiteHelper msqLiteHelper;
    private SQLiteDatabase mDatabase;

    public  long isInsertSuccess;


    public DataSource(Context context) {
        Context mContext = context;
        this.msqLiteHelper = new SQLiteHelper(mContext);
    }


    // open
    public void open() throws SQLException {
        mDatabase = msqLiteHelper.getWritableDatabase();
    }
    // close
    public void close() {
        mDatabase.close();
    }
    // insert
    public void insertWord() {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_WORD,"DEeee");

        mDatabase.insert(SQLiteHelper.TABLE_WORDCORE,null,values);
    }
    public void insertDiem(int sodiem, String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("sodiem",sodiem);
        mDatabase.replace("bangdiem",null,contentValues);
    }
    public void insertLevel(String id, int number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("num",number);
        mDatabase.replace("level",null,contentValues);
    }
    public Cursor getbyWord() {
        Cursor cursor = mDatabase.query(
                SQLiteHelper.TABLE_WORDCORE, //table
                new String[] {SQLiteHelper.COLUMN_WORD}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    public Cursor getDiem() {
        Cursor cursor = mDatabase.query(
               "bangdiem", //table
                new String[] {"sodiem"}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    public Cursor getLevel() {
        Cursor cursor = mDatabase.query(
                "level", //table
                new String[] {"num"}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    public Cursor getByWord(String word) {
        Cursor cursor = mDatabase.query(
                SQLiteHelper.TABLE_WORDCORE, //table
                new String[] {SQLiteHelper.COLUMN_WORD,SQLiteHelper.COLUMN_TRANSL_VN,SQLiteHelper.COLUMN_NOTE}, //column names
                SQLiteHelper.COLUMN_WORD +" == ?",
                new String[] {word},
                null,
                null,
                null
        );
        return cursor;
    }
    public void deleteData(String word) {
        mDatabase.delete(SQLiteHelper.TABLE_WORDCORE,
                SQLiteHelper.COLUMN_WORD+" == ?",
                new String[]{word});
    }
    public void deleteBieuDo() {
       mDatabase.delete("bangtuantu",null,null);
    }
    public void deleteBangDiem() {
        mDatabase.delete("bangdiem",null,null);
    }

    public void deleteCBTu() {
        mDatabase.delete("bangtucb",null,null);
    }
    public void deleteBangXemTu() {
        mDatabase.delete("bangxemtu",null,null);
    }
    // select
    public Cursor getWordFromDB() {
        Cursor cursor = mDatabase.query(
                SQLiteHelper.TABLE_WORDCORE, //table
                new String[] {SQLiteHelper.COLUMN_WORD, SQLiteHelper.COLUMN_TRANSL_VN, SQLiteHelper.COLUMN_NOTE}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    public Cursor getWordFromTempViewTable() {
        Cursor cursor = mDatabase.query(
                "bangxemtu", //table
                new String[] {"tienganh", "nghia", "ghichu"}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    public Cursor getWordRowFromDB(int posAt,String wordStr) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_WORDCORE+" WHERE "+SQLiteHelper.COLUMN_ISLEARN+" = ?  LIMIT 1 OFFSET "+posAt, new String[] {wordStr});
       return cursor;
    }

    public Cursor getWordRowFromCB(int posAt) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM bangtucb LIMIT 1 OFFSET "+posAt, null);
        return cursor;
    }

    public int countRowLearned() {
        Cursor cursor = mDatabase.query(SQLiteHelper.TABLE_WORDCORE,
                new String[] {SQLiteHelper.COLUMN_ISLEARN},
                "ISLEARN == ?",
                new String[] {1+""},
                null,
                null,
                null);
        return cursor.getCount();
    }

    public int countRowStreek() {
        Cursor cursor = mDatabase.query("daystreek",
                new String[] {"day"},
                null,
                null,
                null,
                null,
                null);
        return cursor.getCount();
    }
//    public long countRow() {
//        return DatabaseUtils.queryNumEntries(mDatabase,SQLiteHelper.TABLE_WORDCORE);
//    }
//    public Cursor getRandomRowFromDB() {
//        Cursor cursor;
//        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_WORDCORE+" WHERE "+SQLiteHelper.COLUMN_ISLEARN+" = ?  LIMIT 1 OFFSET "+posAt, new String[] {wordStr});
//        return cursor;
//    }

    public Cursor getWordRowNotReCallFromDB(int posAt,String wordStr) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_WORDCORE+" WHERE "+SQLiteHelper.COLUMN_ISRECALL+" = ?  LIMIT 1 OFFSET "+posAt, new String[] {wordStr});
        return cursor;
    }
    public int getNotLearnWord() {
        int totalNotLearn;
        String whereClause = SQLiteHelper.COLUMN_ISLEARN + " = ?";
        Cursor cursor = mDatabase.query(
                SQLiteHelper.TABLE_WORDCORE, //table
                new String[] {SQLiteHelper.COLUMN_ISLEARN}, //column names
                whereClause,
                new String[] { 0+""},
                null,
                null,
                null
        );
        totalNotLearn = cursor.getCount();
        cursor.close();
        return totalNotLearn;
    }
    public int getNotRecallWord() {
        int totalNotRecall;
        String whereClause = SQLiteHelper.COLUMN_ISRECALL + " = ?";
        Cursor cursor = mDatabase.query(
                SQLiteHelper.TABLE_WORDCORE, //table
                new String[] {SQLiteHelper.COLUMN_ISRECALL}, //column names
                whereClause,
                new String[] { 0+""},
                null,
                null,
                null
        );
        totalNotRecall = cursor.getCount();
        cursor.close();
        return totalNotRecall;
    }
    public String getsotucuathu(String ngaythu) {
        String sotu = "";
        Cursor cursor = mDatabase.rawQuery("select * from bangtuantu where thu=?",
                new String[] {ngaythu});
        if(cursor.moveToNext()) {
            sotu = cursor.getString(1);
        }
        cursor.close();
        return sotu;
    }
    public Boolean insertToSotuantu(String dayofweek) {
        int sotucu = 0;
        if(!giatribangtuantu(dayofweek).equals("")) {
            sotucu = Integer.parseInt(giatribangtuantu(dayofweek));
        }

        int tucuInc = ++ sotucu;

        ContentValues contentValues = new ContentValues();
        contentValues.put("thu",dayofweek+"");
        contentValues.put("sotu",tucuInc);
        mDatabase.replace("bangtuantu",null,contentValues);
        return true;
    }
    public Boolean removeSotuTheongay(String dayofweek) {
        int sotucu = 0;
        if(!giatribangtuantu(dayofweek).equals("")) {
            sotucu = Integer.parseInt(giatribangtuantu(dayofweek));
        }

        int tucuInc =-- sotucu;
        if(tucuInc == -1) {
            return true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("thu",dayofweek+"");
        contentValues.put("sotu",tucuInc);
        mDatabase.replace("bangtuantu",null,contentValues);
        return true;
    }

    public String giatribangtuantu(String thu) {
        String sotu = "";
        Cursor cursor = mDatabase.rawQuery("select * from bangtuantu where thu=?",
                new String[] {thu});
        if(cursor.moveToNext()) {
            sotu = cursor.getString(1);
        }
        cursor.close();
        return sotu;
    }
    public Boolean insertToDayStreeks(String year, String month, String day) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("year",year);
        contentValues.put("month",month);
        contentValues.put("day",day);
        mDatabase.replace("daystreek",null,contentValues);
        return true;
    }

    public Boolean insertToBangTuCB(String english, String vn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tienganh",english);
        contentValues.put("nghia",vn);
        mDatabase.replace("bangtucb",null,contentValues);
        return true;
    }
    public Boolean insertToBangXemTu(String english, String vn, String note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tienganh",english);
        contentValues.put("nghia",vn);
        contentValues.put("ghichu",note);
        mDatabase.replace("bangxemtu",null,contentValues);
        return true;
    }


    public void insertWordToDB(String word, String transl_vn, String note) {
        mDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_WORD,word);
            values.put(SQLiteHelper.COLUMN_TRANSL_VN,transl_vn);
            values.put(SQLiteHelper.COLUMN_NOTE,note);
            values.put(SQLiteHelper.COLUMN_ISLEARN,0);
            values.put(SQLiteHelper.COLUMN_ISRECALL,0);
            isInsertSuccess = mDatabase.insert(SQLiteHelper.TABLE_WORDCORE,null,values);


            mDatabase.setTransactionSuccessful();
        }finally {
            mDatabase.endTransaction();
        }


    }
    public Boolean insertImage(String x, Integer i) {
        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);

            ContentValues contentValues = new ContentValues();
            contentValues.put("id",i);
            contentValues.put("img",imgByte);
            mDatabase.replace("images",null,contentValues);
            fs.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Bitmap getImage(Integer id) {
        Bitmap bt = null;
        Cursor cursor = mDatabase.rawQuery("select * from images where id=?",
                                            new String[] {String.valueOf(id)});
        if(cursor.moveToNext()) {
            byte[] imag = cursor.getBlob(1);
            bt = BitmapFactory.decodeByteArray(imag,0,imag.length);

        }
        cursor.close();
        return bt;
    }
    public Boolean insertYourName(String x, Integer i) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",i);
            contentValues.put("yourname",x);
            mDatabase.replace("yournames",null,contentValues);
            return true;
    }
    public String getYourname(Integer id) {
        String name = "Your name";
        Cursor cursor = mDatabase.rawQuery("select * from yournames where id=?",
                new String[] {String.valueOf(id)});
        if(cursor.moveToNext()) {
            name = cursor.getString(1);
        }
        cursor.close();
        return name;
    }
    public void updateIsLearn(String word) {
      ContentValues values = new ContentValues();
      values.put(SQLiteHelper.COLUMN_ISLEARN,1);
        String whereClause = SQLiteHelper.COLUMN_WORD+" = ?";
        String[] values1 = new String[]{word};
      mDatabase.update(SQLiteHelper.TABLE_WORDCORE,values,whereClause,values1);
    }
    public void updateData(String wordNew, String mean, String note, String wordOld) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_WORD,wordNew);
        values.put(SQLiteHelper.COLUMN_TRANSL_VN,mean);
        values.put(SQLiteHelper.COLUMN_NOTE,note);
        String whereClause = SQLiteHelper.COLUMN_WORD+" = ?";
        String[] values1 = new String[]{wordOld};
        mDatabase.update(SQLiteHelper.TABLE_WORDCORE,values,whereClause,values1);
    }
    public void updateIsReCall(String word) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_ISRECALL,1);
        String whereClause = SQLiteHelper.COLUMN_WORD+" = ?";
        String[] values1 = new String[]{word};
        mDatabase.update(SQLiteHelper.TABLE_WORDCORE,values,whereClause,values1);
    }

    public Cursor getDayStreekFromDB() {
        Cursor cursor = mDatabase.query(
                "daystreek", //table
                new String[] {"year", "month", "day"}, //column names
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }


}
