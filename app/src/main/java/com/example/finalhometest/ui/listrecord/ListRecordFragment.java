package com.example.finalhometest.ui.listrecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalhometest.MainActivity;
import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;
import com.example.finalhometest.ui.themtu.ThemTuFragment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class ListRecordFragment extends Fragment {
    private ListView listView;
    ArrayList<Model> mList;
    DataSource mDataSource;

    RecordListAdapter mAdapter = null;
    public ListRecordFragment() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listrecord, container, false);

        mDataSource = new DataSource(getContext());
        mDataSource.open();
        listView = root.findViewById(R.id.listviewRecord);
        mList =  new ArrayList<>();
        mAdapter = new RecordListAdapter(getContext(), R.layout.rowrecord,mList);



        listView.setAdapter(mAdapter);

        Cursor cursor = mDataSource.getWordFromDB();
        mList.clear();
        while (cursor.moveToNext()) {
            int id = 0;
            String word = cursor.getString(0);
            String mean = cursor.getString(1);
            String note = cursor.getString(2);

            mList.add(new Model(id,word,mean,note));
            id++;
        }
        mAdapter.notifyDataSetChanged();
        if(mList.size() == 0) {
            Toast.makeText(getContext(),"Bạn chưa thêm từ nào",Toast.LENGTH_LONG).show();

        }

        return root;
    }

}