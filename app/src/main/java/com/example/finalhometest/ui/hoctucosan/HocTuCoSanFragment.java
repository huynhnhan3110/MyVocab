package com.example.finalhometest.ui.hoctucosan;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.finalhometest.MainActivity;
import com.example.finalhometest.R;
import com.example.finalhometest.ViewPaperAdapter;
import com.example.finalhometest.customDialog;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.ui.hoclaitu.HocLaiTuFragment;

import java.util.Calendar;

public class HocTuCoSanFragment extends Fragment {
    private ProgressBar progressBar;
    private ObjectAnimator progressBarAnimator;
    private ImageButton arrowLeft, arrowRight;
    private TextView progressText;
    private int propr = 1;
    private int maxNumberNotLearn =0 ;

    private boolean doubleBackToExitPressedOnce = false;
    private ViewPager viewPager;


    private RelativeLayout relativelayouthoctucosan;

    protected DataSource mDataSource;

    public HocTuCoSanFragment() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hoctucosan, container, false);

        relativelayouthoctucosan = root.findViewById(R.id.relativelayouthoctucosan);

        // khoi tao DataSource;
        mDataSource = new DataSource(getContext());
        mDataSource.open();
        ImageButton exitinstant = root.findViewById(R.id.exitinstantLearn);
        progressText = root.findViewById(R.id.progressText);
        progressBar = root.findViewById(R.id.progressBar);

        arrowLeft = root.findViewById(R.id.arrowLeftHoc);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowRight = root.findViewById(R.id.arrowRightHoc);

        viewPager = root.findViewById(R.id.viewpaper);

        maxNumberNotLearn = mDataSource.getNotLearnWord();
        ViewPaperAdapter viewPaperAdapter = new ViewPaperAdapter(getContext());
        viewPager.setAdapter(viewPaperAdapter);
        viewPager.setOffscreenPageLimit(maxNumberNotLearn);

        progressBar.setMax(maxNumberNotLearn);

        exitinstant.setOnClickListener(v-> {
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
                toggleArrowVisibility(position == 0, position == maxNumberNotLearn-1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateProgress();
        arrowRight.setOnClickListener(v -> {
            // marked attendance
            Calendar cal = Calendar.getInstance();
            String dayOfMonth = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            String monthofYear = Integer.toString(cal.get(Calendar.MONTH));
            String yearToIn = Integer.toString(cal.get(Calendar.YEAR));

            mDataSource.insertToDayStreeks(yearToIn,monthofYear,dayOfMonth);


            View view = viewPager.getChildAt(viewPager.getCurrentItem());
            TextView tv = view.findViewById(R.id.englishWord);
//            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.dt);
//            int maxVolume = 50;
//            int currVolume = 2;
//            float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//            mp.setVolume(log1,log1); //set volume takes two paramater
            mDataSource.updateIsLearn(tv.getText().toString());
            viewPager.arrowScroll(View.FOCUS_RIGHT);
            if(propr < maxNumberNotLearn) {
//                mp.start();
                propr += 1;
                updateProgress();

            } else if(propr == maxNumberNotLearn) {
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
        if(propr == maxNumberNotLearn+1) {
            showResult();
        }
        progressBar.setProgress(propr);
        progressText.setText(propr+"/"+maxNumberNotLearn);


    }

    public void toggleArrowVisibility(boolean atzero, boolean atlast) {
        if(atzero) {
            arrowLeft.setVisibility(View.INVISIBLE);
        } else {
            arrowLeft.setVisibility(View.VISIBLE);
        }
        if(atlast && progressBar.getProgress() == maxNumberNotLearn+1) {

            arrowRight.setVisibility(View.INVISIBLE);

        } else arrowRight.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        relativelayouthoctucosan.removeAllViews();

        getActivity().invalidateOptionsMenu();


        View relativeLayoutResult = getLayoutInflater().inflate(R.layout.ontu_layout_result, relativelayouthoctucosan,false);

        Button trove = relativeLayoutResult.findViewById(R.id.trovebtn);
        Button hoclai = relativeLayoutResult.findViewById(R.id.onlaibtn);
        hoclai.setText("Học lại");

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

        hoclai.setOnClickListener(v -> {
            startIntent(R.id.nav_hoclai);
        });
        trove.setOnClickListener(v -> {
            startIntent(R.id.nav_home);
        });
        relativelayouthoctucosan.addView(relativeLayoutResult);
    }
    protected void startIntent(int id) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
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