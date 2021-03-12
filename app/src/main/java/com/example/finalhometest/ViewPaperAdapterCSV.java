package com.example.finalhometest;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ViewPaperAdapterCSV extends PagerAdapter {
    protected DataSource mDataSource;
    protected int vitri = 0;
    private final Context context;
    private String topic;
    protected TextView englishWord, meaningWord, noteWord;
    protected int lengCount;

    public ViewPaperAdapterCSV(Context context,String topic) {
        this.context = context;
        lengCount = 1;
        this.topic = topic;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = inflate.inflate(R.layout.slidehoctuchuanbi,container,false);


        englishWord = view.findViewById(R.id.englishWordCB);
        meaningWord = view.findViewById(R.id.meaningWordCB);
        noteWord = view.findViewById(R.id.noteWordCB);

        ArrayList<String> Stt = new ArrayList<String>();
        ArrayList<String>Chude = new ArrayList<String>();
        ArrayList<String>Tuvung = new ArrayList<String>();
        ArrayList<String>Nghia = new ArrayList<String>();

        try {
            InputStream is = context.getAssets().open("volccsv.csv");
            InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));

            CSVReader csvReader = new CSVReader(reader);
            String[] line;
            // throw away the header
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                Stt.add(line[0]);
                Chude.add(line[1]);

                Tuvung.add(line[2]);
                Nghia.add(line[3]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> rangeChuDe = new ArrayList<Integer>();
        for(int i = 0; i<Chude.size();i++) {
            if(Chude.get(i).equals(topic)){
                rangeChuDe.add(i);
            }
        }

        updateToTextView(Tuvung.get(rangeChuDe.get(vitri)),Nghia.get(rangeChuDe.get(vitri)));
        mDataSource = new DataSource(context);
        mDataSource.open();
        mDataSource.insertToBangTuCB(Tuvung.get(rangeChuDe.get(vitri)),Nghia.get(rangeChuDe.get(vitri)));
        container.addView(view);
        vitri++;
        return view;
    }

    public void updateToTextView(String tuvung, String nghia) {
            englishWord.setText(tuvung);
            meaningWord.setText(nghia);
    }

    @Override
    public int getCount() {
        return 20;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

}
