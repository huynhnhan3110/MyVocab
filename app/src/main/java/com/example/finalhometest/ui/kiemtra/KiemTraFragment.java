package com.example.finalhometest.ui.kiemtra;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class KiemTraFragment extends Fragment {
    private EditText meaningAnswer;
    private TextView clocktext, englishWordTest;
    private Button buttoncheck;
    private List<Integer> numbersRandom;
    private DataSource mDataSource;
    private String meanAns = null;
    private CountDownTimer timer= null;
    protected int vitri = 0;
    protected int countRow;
    private int diem=1;
    private int sotutoida;
    private int khonggioihan;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kiemtra, container, false);
        clocktext = root.findViewById(R.id.clocktext);
        englishWordTest = root.findViewById(R.id.englishWordTest);
        meaningAnswer = root.findViewById(R.id.meaningAnswer);
        buttoncheck = root.findViewById(R.id.buttoncheckenter);
        LinearLayout coinlayout = root.findViewById(R.id.cointlayout);


        downTimer();
        mDataSource = new DataSource(getContext());
        mDataSource.open();
        mDataSource.deleteBangDiem();

        SharedPreferences pref = getContext().getSharedPreferences("SotuMuonOn", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
        if(!pref.getString("sotukiemtra", "").equals("")) {
            if(pref.getString("sotukiemtra","").equals("10")) {
                sotutoida = 10;
            } else if(pref.getString("sotukiemtra","").equals("20")) {
                sotutoida = 20;
            }
        } else {
            khonggioihan = 1;
        }
        countRow = mDataSource.countRowLearned(); // get total row from database


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
            englishWordTest.setText(cursor.getString(0));
            meanAns = cursor.getString(1);
        }
        cursor.close();
        vitri++;


        buttoncheck.setOnClickListener(v ->{
            String userinp = meaningAnswer.getText().toString();

            if(userinp.equals(meanAns)) {
//                Toast.makeText(getContext(),"Chinh xac",Toast.LENGTH_SHORT).show();
               mDataSource.insertDiem(diem++,"1");



                coinlayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        meaningAnswer.setText("");
                        coinlayout.setVisibility(View.GONE);
                    }
                }, 1000);

            } else {
//                Toast.makeText(getContext(),"Sai bet",Toast.LENGTH_SHORT).show();
                meaningAnswer.setText("");

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    if(vitri <countRow && vitri <sotutoida || khonggioihan ==1 && vitri <countRow) {
                        if(timer  != null){
                            timer.cancel();
                            downTimer();
                        }
                        Cursor cursor2 = mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1"); // so 0 la chua hoc. lay tung tu chua hoc ra theo offset vitri


                        while (cursor2.moveToNext()){

                            englishWordTest.setText(cursor2.getString(0));
                            meanAns = cursor2.getString(1);
                        }
                        cursor2.close();
                        vitri++;
                    } else {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_ketquakiemtra);
                        if(timer  != null){
                            timer.cancel();
                        }
                    }
                }
            }, 1000);





        });

        return root;
    }
    private void downTimer(){
        timer = new CountDownTimer(20*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished/(1000*60)) % 60;
                clocktext.setText(minutes + ":" + second);
            }

            @Override
            public void onFinish() {
                clocktext.setText("Finish");
                nextToWord(countRow);
            }
        };
        timer.start();
    }
    protected void nextToWord(int countRow) {
        if(vitri <countRow && vitri < sotutoida || khonggioihan ==1 && vitri <countRow) {
            if(timer  != null){
                timer.cancel();
                downTimer();
            }
            Cursor cursor2 = mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1"); // so 0 la chua hoc. lay tung tu chua hoc ra theo offset vitri

            while (cursor2.moveToNext()){
                englishWordTest.setText(cursor2.getString(0));
                meanAns = cursor2.getString(1);
            }
            cursor2.close();
            vitri++;
        } else {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_ketquakiemtra);
        }
    }
}
