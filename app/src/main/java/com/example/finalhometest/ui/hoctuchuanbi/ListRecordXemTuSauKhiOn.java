package com.example.finalhometest.ui.hoctuchuanbi;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.ui.listrecord.Model;
import com.example.finalhometest.ui.listrecord.RecordListAdapter;

import java.util.ArrayList;

public class ListRecordXemTuSauKhiOn extends Fragment {
    private ListView listView;
    ArrayList<Model> mList;
    DataSource mDataSource;

    RecordListAdapter mAdapter = null;
    public ListRecordXemTuSauKhiOn() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listrecordxemtusauon, container, false);

        mDataSource = new DataSource(getContext());
        mDataSource.open();
        listView = root.findViewById(R.id.listviewRecordxemtusauon);
        mList =  new ArrayList<>();
        mAdapter = new RecordListAdapter(getContext(), R.layout.rowrecordtemptable,mList);



        listView.setAdapter(mAdapter);

        Cursor cursor = mDataSource.getWordFromTempViewTable();
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