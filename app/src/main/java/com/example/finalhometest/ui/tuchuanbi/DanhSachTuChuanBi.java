package com.example.finalhometest.ui.tuchuanbi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;

import static android.app.Activity.RESULT_OK;

public class DanhSachTuChuanBi extends Fragment {
   GridView gridView;
   String []numberword = {"Động vật",
           "Nghề nghiệp",
           "Hoa",
           "Trái cây",
           "Bộ phận cơ thể",
           "Vật dụng gia đình",
           "Thành viên gia đình",
           "Mix 1",
           "Mix 2",
           "Mix 3",
           "Mix 4"};
   int []numberImage  = {R.drawable.cat,R.drawable.notebook,R.drawable.dahlia,
           R.drawable.lime,R.drawable.elbow,R.drawable.house,
           R.drawable.family,R.drawable.openfolder,R.drawable.openfolder,
           R.drawable.openfolder,R.drawable.openfolder};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tuchuanbi, container, false);

        gridView = root.findViewById(R.id.grid_view);
        ChuanBiAdapter adapter = new ChuanBiAdapter(getActivity(),numberword,numberImage);
        gridView.setAdapter(adapter);

        DataSource mDataSource = new DataSource(getContext());
        mDataSource.open();
        mDataSource.deleteCBTu();
        mDataSource.close();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences pref = getContext().getSharedPreferences("ChuDe", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("content", numberword[+position]); // Storing string
                editor.commit();

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_hoctuchuanbi);

            }
        });

        return root;
    }

}