package com.example.finalhometest.ui.hoclaitu;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.finalhometest.R;
import com.example.finalhometest.ViewPaperHocLaiAdapter;
import com.example.finalhometest.customDialog;
import com.example.finalhometest.helper.DataSource;

import java.util.Objects;

public class HocLaiTuFragment extends Fragment {
    private ProgressBar progressBarHocLai;
    private ObjectAnimator progressBarAnimator;
    private ImageButton arrowLeft, arrowRight;
    private TextView progressTextHocLai;
    private int propr = 1;
    private int maxNumberNotRecall = 0;

    private ViewPager viewPager;

    private boolean doubleBackToExitPressedOnce = false;

    private RelativeLayout relativelayouthoclaitu;

    protected DataSource mDataSource;

    public HocLaiTuFragment() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hoclaitu, container, false);

        relativelayouthoclaitu = root.findViewById(R.id.relativelayouthoclaitu);

        // khoi tao DataSource;
        mDataSource = new DataSource(getContext());
        mDataSource.open();

        progressTextHocLai = root.findViewById(R.id.progressTextHocLai);
        progressBarHocLai = root.findViewById(R.id.progressBarHocLai);
        ImageButton exitinstantRecall = root.findViewById(R.id.exitinstantRecall);
        arrowLeft = root.findViewById(R.id.arrowLeft);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowRight = root.findViewById(R.id.arrowRight);

        viewPager = root.findViewById(R.id.viewpaperhoclai);


        ViewPaperHocLaiAdapter viewPaperAdapter = new ViewPaperHocLaiAdapter(getContext());
        viewPager.setAdapter(viewPaperAdapter);

        maxNumberNotRecall = mDataSource.countRowLearned();

        viewPager.setOffscreenPageLimit(maxNumberNotRecall);
        progressBarHocLai.setMax(maxNumberNotRecall);

        exitinstantRecall.setOnClickListener(v -> {
            doExit();
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toggleArrowVisibility(position == 0, position == maxNumberNotRecall-1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateProgress();
        arrowRight.setOnClickListener(v -> {


            View view = viewPager.getChildAt(viewPager.getCurrentItem());
            TextView tv = view.findViewById(R.id.englishWord);

            mDataSource.updateIsReCall(tv.getText().toString());

            viewPager.arrowScroll(View.FOCUS_RIGHT);
//            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.hlhl);
//            int maxVolume = 50;
//            int currVolume = 2;
//            float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//            mp.setVolume(log1,log1); //set volume takes two paramater
            if(propr < maxNumberNotRecall) {
//                mp.start();
                propr += 1;
                updateProgress();

            } else if(propr == maxNumberNotRecall) {
//                mp.release();
//                MediaPlayer mpIncrease = MediaPlayer.create(getContext(), R.raw.tingting);
//                mpIncrease.setVolume(log1,log1); //set volume takes two paramater
//                mpIncrease.start();

                propr += 1;
                updateProgress();
            }
        });
        arrowLeft.setOnClickListener(v -> {
            viewPager.arrowScroll(View.FOCUS_LEFT);
                if(propr >= 1) {
                    propr -= 1;
                    updateProgress();
                }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mDataSource.open();


    }

    @Override
    public void onPause() {
        super.onPause();
        mDataSource.close();
    }

    private void updateProgress() {

        if(propr == maxNumberNotRecall+1) {

            showResult();
        }

        progressBarHocLai.setProgress(propr);
        progressTextHocLai.setText(propr+"/"+maxNumberNotRecall);

//        if(progressBarHocLai.getProgress() == maxNumberNotRecall) {
//            showResult();
//        }
    }

    public void toggleArrowVisibility(boolean atzero, boolean atlast) {
        if(atzero) {
            arrowLeft.setVisibility(View.INVISIBLE);
        } else {
            arrowLeft.setVisibility(View.VISIBLE);
        }
        if(atlast && progressBarHocLai.getProgress() == maxNumberNotRecall+1) {
            arrowRight.setVisibility(View.INVISIBLE);
        } else arrowRight.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        relativelayouthoclaitu.removeAllViews();

        View relativeLayoutResult = getLayoutInflater().inflate(R.layout.ontu_layout_result, relativelayouthoclaitu,false);

        Button trove = relativeLayoutResult.findViewById(R.id.trovebtn);
        Button hoclai = relativeLayoutResult.findViewById(R.id.onlaibtn);
        hoclai.setText("Học lại");
        hoclai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(R.id.nav_hoclai);
            }
        });

        Button onCungcactukhac = relativeLayoutResult.findViewById(R.id.xemtubtn);
        onCungcactukhac.setText("Ôn cùng các từ đã được thêm khác");

        onCungcactukhac.setOnClickListener(v -> {
            SharedPreferences pref = getContext().getSharedPreferences("SotuMuonOn", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            customDialog.FullNameListener listener = newfrinput -> {
                if(Integer.parseInt(newfrinput) > mDataSource.countRowLearned()) {
                    Toast.makeText(getContext(),"Số từ bạn nhập lớn hơn số từ đã được học",Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(newfrinput) == 0){
                    Toast.makeText(getContext(),"Vui lòng thực hiện lại",Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putString("sotu", newfrinput); // Storing string
                    editor.commit();
                    startIntent(R.id.nav_ontu);
                }

            };
            final customDialog dialog = new customDialog(getContext(), listener);
            dialog.show();
        });

        trove.setOnClickListener(v -> {

            startIntent(R.id.nav_home);
        });
        relativelayouthoclaitu.addView(relativeLayoutResult);
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