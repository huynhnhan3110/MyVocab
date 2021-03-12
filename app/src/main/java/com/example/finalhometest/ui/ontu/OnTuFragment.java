package com.example.finalhometest.ui.ontu;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class OnTuFragment extends Fragment {
    private ProgressBar progressBar;
    private ObjectAnimator progressBarAnimator;
    private List<Integer> numbersRandom; // create array int to store total row
    private TextView progressText, frontcard, backcard;
    private int propr = 1;
    // flip card
    private Animator front_anim;
    private Animator back_anim;
    private boolean isFlip = true;
    // end flip card
    private int vitri = 0;
    // relative ontu
    RelativeLayout relativeLayoutOnTu;

    private boolean doubleBackToExitPressedOnce = false;
    protected DataSource mDataSource;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ontu, container, false);

        // linearLayout
        relativeLayoutOnTu = root.findViewById(R.id.relativeLayoutOnTu);
        // flip card
        frontcard = root.findViewById(R.id.card_front);
        backcard = root.findViewById(R.id.card_back);
        ImageButton exitinstant = root.findViewById(R.id.exitinstant);
        float scale = root.getContext().getResources().getDisplayMetrics().density;
        frontcard.setCameraDistance(8000 * scale);
        backcard.setCameraDistance(8000 * scale);

        front_anim = AnimatorInflater.loadAnimator(root.getContext(),R.animator.front_animator);
        back_anim = AnimatorInflater.loadAnimator(root.getContext(),R.animator.back_animator);
        // open database
        mDataSource = new DataSource(getContext());
        mDataSource.open();


        int countRow = mDataSource.countRowLearned(); // get total row from database

        numbersRandom = new ArrayList<Integer>(countRow); // create array int to store total row
        for (int i = countRow-1; i >=0; i--) {
            numbersRandom.add(i); // add the integer from zero to lastRow
        }

        Collections.shuffle(numbersRandom); // shuffle arrary to change the other of array.

        if(mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1") != null) {
            Cursor cursorBanDau = mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1"); // get word from database by index 'vitri'. 0->10
            while (cursorBanDau.moveToNext()){
                updateToTextView(cursorBanDau);
            }
            cursorBanDau.close();
        }



        vitri++;

        pref = getContext().getSharedPreferences("SotuMuonOn", 0); // 0 - for private mode
        editor = pref.edit();
        progressText = root.findViewById(R.id.progressText);
       Button nextbtn = root.findViewById(R.id.buttonnext);
        Button previousbtn = root.findViewById(R.id.buttonprevious);
        progressBar = root.findViewById(R.id.progressBarontu);

        exitinstant.setOnClickListener(v -> {
            doExit();
        });

        frontcard.setOnClickListener(v -> {
           if(isFlip) {
               front_anim.setTarget(frontcard);
               back_anim.setTarget(backcard);

               front_anim.start();
               back_anim.start();
                isFlip = false;
           } else {
               front_anim.setTarget(backcard);
               back_anim.setTarget(frontcard);
               back_anim.start();
               front_anim.start();
               isFlip = true;
           }

        });

        // end flip card
        if(pref.getString("sotu", null) != null) {
            progressBar.setMax(Integer.parseInt(pref.getString("sotu", null)));
        }

        updateProgress();
        nextbtn.setOnClickListener(v -> {
            // marked attendance
            Calendar cal = Calendar.getInstance();
            String dayOfMonth = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            String monthofYear = Integer.toString(cal.get(Calendar.MONTH));
            String yearToIn = Integer.toString(cal.get(Calendar.YEAR));

            mDataSource.insertToDayStreeks(yearToIn,monthofYear,dayOfMonth);


            if(pref.getString("sotu", null) == null) {
                if(propr <= 10) {

                    propr += 1;
                    updateProgress();
                }
            } else {

                String sotu = pref.getString("sotu", null);
                int numWord = Integer.parseInt(sotu);
//                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.dt);
//                int maxVolume = 50;
//                int currVolume = 2;
//                float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//                mp.setVolume(log1,log1); //set volume takes two paramater

                if(propr < numWord) {
//                    mp.start();
                    propr += 1;
                    updateProgress();
                }
                else if(propr == numWord) {
//                    mp.release();
//                    MediaPlayer mpIncrease = MediaPlayer.create(getContext(), R.raw.tingting);
//                    mpIncrease.setVolume(log1,log1); //set volume takes two paramater
//                    mpIncrease.start();
                    propr += 1;
                    updateProgress();
                }
            }
                if(vitri <= countRow-1) {
                    Cursor cursor = mDataSource.getWordRowFromDB(numbersRandom.get(vitri),"1");
                    while(cursor.moveToNext()) {
                        updateToTextView(cursor);
                    }
                    vitri++;
                }







        });
        previousbtn.setOnClickListener(v -> {
//            if(propr >= 1) {
//                propr -= 1;
//                updateProgress();
//            }
            Toast.makeText(getContext(),"Chưa làm",Toast.LENGTH_SHORT).show();


        });
        return root;
    }
    public void updateProgress() {
        if(pref.getString("sotu", null) == null) {


            progressBar.setProgress(propr*10);
            progressText.setText(propr+"/10");

            if(progressBar.getProgress() == 100) {
                showResult();
            }
        } else {

            String sotu = pref.getString("sotu", null);

            if(propr == Integer.parseInt(sotu)+1) {
                showResult();
            }
            progressBar.setProgress(propr);
            progressText.setText(propr+"/"+sotu);

        }

    }
    private void showResult() {

        relativeLayoutOnTu.removeAllViews();
        View relativeLayoutResult = getLayoutInflater().inflate(R.layout.ontu_layout_result, relativeLayoutOnTu,false);

        Button xemtu = relativeLayoutResult.findViewById(R.id.xemtubtn);

        Button trove = relativeLayoutResult.findViewById(R.id.trovebtn);
        Button onlai = relativeLayoutResult.findViewById(R.id.onlaibtn);
        xemtu.setOnClickListener(v -> {
            startIntent(R.id.nav_listviewRecord);
        });

        onlai.setOnClickListener(v -> {

            startIntent(R.id.nav_ontu);
        });
        trove.setOnClickListener(v -> {

            startIntent(R.id.nav_home);
        });
        relativeLayoutOnTu.addView(relativeLayoutResult);

    }

    @Override
    public void onPause() {
        super.onPause();
        mDataSource.close();
    }



    private void updateToTextView(Cursor cursor) {
        int wordIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_WORD);
        int transl_vnIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSL_VN);
        int noteIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE);

        String word = cursor.getString(wordIndex);
        String transl_vn = cursor.getString(transl_vnIndex);
        String note = cursor.getString(noteIndex);

        frontcard.setText(word);
//        backcard.setText(transl_vn+"\n\n"+note);

        SpannableString s1,s2;

        s2 = new SpannableString(note);
        if(note.equals("")) {
            s1= new SpannableString(transl_vn);
        } else {
            s1= new SpannableString(transl_vn);
            s2 = new SpannableString("\n\n"+note);
        }
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.NORMAL), 0, s1.length(), flag);
        s1.setSpan(new RelativeSizeSpan(1.2f),0, s1.length(), flag);
        s2.setSpan(new StyleSpan(Typeface.ITALIC), 0, s2.length(), flag);
        s2.setSpan(new RelativeSizeSpan(0.9f),0, s2.length(), flag);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);builder.append(s2);

        backcard.setText(builder);
    }
    protected void startIntent(int id) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack(R.id.nav_home, true);
        navController.navigate(id);
    }
    public void doExit() {
        if (doubleBackToExitPressedOnce) {
            startIntent(R.id.nav_home);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getContext(), "Nhấn lại lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}