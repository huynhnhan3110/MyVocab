package com.example.finalhometest.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;
import com.example.finalhometest.ui.listrecord.Model;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DataSource mDataSource;
    protected List<EventDay> events = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);



        mDataSource = new DataSource(getContext());
        mDataSource.open();
        CircleImageView avatar = root.findViewById(R.id.avatar);
        TextView yournameHg = root.findViewById(R.id.yourname);

        if(mDataSource.getImage(21)!=null) {
            avatar.setImageBitmap(mDataSource.getImage(21)); // id
        }

        if(mDataSource.getYourname(11)!=null) {
            yournameHg.setText(mDataSource.getYourname(11));
        }


        sharedPreferences = getContext().getSharedPreferences("baodaxoa", 0); // 0 - for private mode
        editor = sharedPreferences.edit();

        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK);
        if(dayofweek == 1) {
            editor.putInt("isDelete",1);
            editor.commit();
        }
        int ischeck = 0;
        if(sharedPreferences.getInt("isDelete",2) == 1) {
            ischeck = 1;
        }
        if(dayofweek == 2 && ischeck== 1) {
            resetvaluescolumn();
        }

//      Bat dau barchart
        BarChart bchart = root.findViewById(R.id.chart);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 7; i++) {
            int newVal = 0;
            if(!mDataSource.getsotucuathu((i+1)+"").equals("")) {
                String newStr = mDataSource.getsotucuathu((i+1)+"");
                 newVal= Integer.parseInt(newStr);
            }

            yVals1.add(new BarEntry(i, newVal));
        }
        BarDataSet set1 = new BarDataSet(yVals1, "Số từ đã thêm");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        final String[] labels = new String[] {"Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy",
                "Chủ nhật"};
        bchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        bchart.getXAxis().setGranularity(1f);
        bchart.getXAxis().setGranularityEnabled(true);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(15f);

        data.setValueFormatter(new DefaultValueFormatter(0));

        data.setBarWidth(0.9f);

        bchart.setTouchEnabled(false);
        bchart.getAxisLeft().setDrawLabels(false);
        bchart.getAxisRight().setDrawLabels(false);
        bchart.setDescription(null);

        bchart.getAxisLeft().setDrawGridLines(false); // chieu doc
        bchart.getAxisRight().setDrawGridLines(false); // chieu ngang
        bchart.getXAxis().setDrawGridLines(false); // chieu doc
        bchart.getAxisLeft().setDrawAxisLine(false);
        bchart.getAxisRight().setDrawAxisLine(false);


        bchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        bchart.setMinimumHeight(600);

        bchart.setData(data);
        Cursor cursor = mDataSource.getDayStreekFromDB();
        events.clear();
        while (cursor.moveToNext()) {
            int year = Integer.parseInt(cursor.getString(0));
            int month = Integer.parseInt(cursor.getString(1));
            int day = Integer.parseInt(cursor.getString(2));
            events.add(new EventDay(getCalendar(year,month,day),R.drawable.ic_baseline_stars_24));
        }
        cursor.close();
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        ImageView bg1 = root.findViewById(R.id.badges1);
        ImageView bg2 = root.findViewById(R.id.badges2);
        // hiển thị badges nếu học được bao nhiêu từ tương ứng.
        int sotudahoc = mDataSource.countRowLearned();
        if(sotudahoc >= 5) {
            bg1.setVisibility(View.VISIBLE);
            editor.putString("sothanhtich","1");
            editor.commit();

        }
        // hiển thị badges nếu duy trì streek được bao nhiêu ngày tương ứng.
        int songaystreek = mDataSource.countRowStreek();
        mDataSource.close();
        if(songaystreek >= 2) {
            bg2.setVisibility(View.VISIBLE);
            editor.putString("sothanhtich","2");
            editor.commit();
        }

        return root;
    }

    public void resetvaluescolumn() {
        DataSource mDatasource = new DataSource(getContext());
        mDatasource.open();
        mDatasource.deleteBieuDo();
        mDatasource.close();
        editor.putInt("isDelete",0); // neu ngay mo cuoi cung chua cap nhat thi cap nhat tai day
        // ey t da xoa bang roi nha, dung goi lai tao nua.
        editor.commit();
    }


    private Calendar getCalendar(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c;
    }

}