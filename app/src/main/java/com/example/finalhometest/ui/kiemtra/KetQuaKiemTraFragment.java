package com.example.finalhometest.ui.kiemtra;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KetQuaKiemTraFragment extends Fragment {
    private DataSource mDataSource;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ketquakiemtra, container, false);
        Button trovett = root.findViewById(R.id.trovett);
        trovett.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_home);
        });
        SharedPreferences pref = getContext().getSharedPreferences("diem", 0); // 0 - for private mode
        TextView sodiem = root.findViewById(R.id.sodiemtv);
        TextView progressText = root.findViewById(R.id.progressText);
        TextView progressText2 = root.findViewById(R.id.progressText2);
        mDataSource = new DataSource(getContext());
        mDataSource.open();
        int diemnow = 0;
        Cursor cursor = mDataSource.getDiem();
        while (cursor.moveToNext()) {
//            sodiem.setText("+"+cursor.getString(0));
            diemnow = Integer.parseInt(cursor.getString(0));
        }
        String number = "0";

        Cursor cursor2 = mDataSource.getLevel();
        while (cursor2.moveToNext()) {
            number = cursor2.getString(0);
        }
        int numIn = Integer.parseInt(number) +diemnow;
        mDataSource.insertLevel("1",numIn);

        ProgressBar progressBar = root.findViewById(R.id.progressBar);
        ProgressBar progressBar2 = root.findViewById(R.id.progressBar2);

        SharedPreferences pref2 = getContext().getSharedPreferences("SotuMuonOn", 0); // 0 - for private mode

        int countRow = mDataSource.countRowLearned();
        if(!pref2.getString("sotukiemtra", "").equals("")) {
            if (pref2.getString("sotukiemtra", "").equals("10")) {
                countRow = 10;
            } else if (pref2.getString("sotukiemtra", "").equals("20")) {
                countRow = 20;
            }
        }
        progressText2.setText(diemnow+"/"+countRow);
        if(diemnow == countRow) {
            SharedPreferences pref5= getContext().getSharedPreferences("diem", 0); // 0 - for private mode
            SharedPreferences.Editor editor4 = pref5.edit();
            editor4.putInt("isPerfect",1);
            editor4.putInt("isAsc",1);
            editor4.commit();
        }
            sodiem.setText("+"+diemnow);

        progressBar2.setMax(countRow);
        progressBar2.setProgress(diemnow);

        if(numIn < 10) {
            progressBar.setMax(10);
            progressBar.setProgress(numIn);
            int curr = numIn;
            progressText.setText(curr+"/10");
        } else if(numIn >= 10 && numIn <100) {
            progressBar.setMax(100);
            progressBar.setProgress(numIn);
            int curr = numIn - 10;
            progressText.setText(curr+"/100");
        } else if(numIn >= 100 && numIn <200) {
            progressBar.setMax(200);
            progressBar.setProgress(numIn);
            int curr = numIn - 100;
            progressText.setText(curr+"/200");
        } else if(numIn >= 200 && numIn <400) {
            progressBar.setMax(400);
            progressBar.setProgress(numIn);
            int curr = numIn - 200;
            progressText.setText(curr+"/400");
        }else if(numIn >= 400 && numIn <500) {
            progressBar.setMax(500);
            progressBar.setProgress(numIn);
            int curr = numIn - 400;
            progressText.setText(curr+"/500");
        }
        else if(numIn >= 500 && numIn <800) {
            progressBar.setMax(800);
            progressBar.setProgress(numIn);
            int curr = numIn - 500;
            progressText.setText(curr+"/800");
        }
        else if(numIn >= 800 && numIn <1000) {
            progressBar.setMax(1000);
            progressBar.setProgress(numIn);
            int curr = numIn - 800;
            progressText.setText(curr+"/1000");
        }else if(numIn >= 1000 && numIn <2000) {
            progressBar.setMax(2000);
            progressBar.setProgress(numIn);
            int curr = numIn - 1000;
            progressText.setText(curr+"/2000");
        } else{
            progressBar.setMax(3000);
            progressBar.setProgress(numIn);
            int curr = numIn - 2000;
            progressText.setText(curr+"/3000");
        }
        SharedPreferences pref3 = getContext().getSharedPreferences("SotuMuonOn", 0);
        SharedPreferences.Editor editor2 = pref3.edit();
        editor2.putInt("diem",numIn);
        editor2.commit();
        return root;
    }

}
