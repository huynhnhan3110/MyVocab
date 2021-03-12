package com.example.finalhometest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewPaperHocLaiAdapter extends PagerAdapter {
    protected DataSource mDataSource;
    protected int vitri = 0;
    private final Context context;

    protected TextView englishWord, meaningWord, noteWord;
    protected int lengCount;
    private List<Integer> numbersRandom; // create array int to store total row

    public ViewPaperHocLaiAdapter(Context context) {
        this.context = context;
        mDataSource = new DataSource(context);
        mDataSource.open();

        lengCount = mDataSource.countRowLearned();
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = inflate.inflate(R.layout.slidehoctucosan,container,false);


        englishWord = view.findViewById(R.id.englishWord);
        meaningWord = view.findViewById(R.id.meaningWord);
        noteWord = view.findViewById(R.id.noteWord);
        // code random recall
        int countRow = mDataSource.countRowLearned(); // get total row from database

        numbersRandom = new ArrayList<Integer>(countRow); // create array int to store total row
        for (int i = countRow-1; i >=0; i--) {
            numbersRandom.add(i); // add the integer from zero to lastRow
        }

        Collections.shuffle(numbersRandom); // shuffle arrary to change the other of array.
        // end
        Cursor cursor = mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1"); // so 0 la chua hoc. lay tung tu chua hoc ra theo offset vitri
//        Cursor cursor = mDataSource.getWordRowFromDB(vitri);
        int count2 = cursor.getCount();
        while (cursor.moveToNext()){
            updateToTextView(cursor);
        }


        container.addView(view);
        vitri++;
        cursor.close();
        return view;
    }

    public void updateToTextView(Cursor cursor) {
            int wordIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_WORD);
            int transl_vnIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSL_VN);
            int noteIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE);

            String word = cursor.getString(wordIndex);
            String transl_vn = cursor.getString(transl_vnIndex);
            String note = cursor.getString(noteIndex);

            englishWord.setText(word);
            meaningWord.setText(transl_vn);
            noteWord.setText(note);
    }

    @Override
    public int getCount() {
        return lengCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

}
