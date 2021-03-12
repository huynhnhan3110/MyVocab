package com.example.finalhometest.ui.ketquathemtu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.helper.SQLiteHelper;

public class KetQuaFragment extends Fragment {
    protected DataSource mDataSource;
    protected TextView wordResult, wordTranslVNResult, noteResult;
    protected Button startlearnFromAdd;
    protected View hrmean;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ketqua, container, false);
        getActivity().invalidateOptionsMenu();

        startlearnFromAdd = root.findViewById(R.id.startLearnFromAdd);
        startlearnFromAdd.setOnClickListener(v -> {
//                int maxNumberNotLearn = mDataSource.getNotLearnWord();
//                Toast.makeText(getContext(),"Na: "+maxNumberNotLearn,Toast.LENGTH_SHORT).show();

//            MediaPlayer mpIncrease = MediaPlayer.create(getContext(), R.raw.tingting);
//            mpIncrease.start();

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_hoctucosan);

        });
        mDataSource = new DataSource(getContext());

        Button backtoadd = root.findViewById(R.id.backtoadd);
        startlearnFromAdd = root.findViewById(R.id.startLearnFromAdd);


        wordResult = root.findViewById(R.id.wordResult);
        wordTranslVNResult = root.findViewById(R.id.wordTransVNResult);
        noteResult = root.findViewById(R.id.noteResult);

        hrmean = root.findViewById(R.id.hrmean);


        backtoadd.setOnClickListener(v -> {

            // bat dau hoi co them nhieu tu khong
            AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getContext());
            dialogDelete.setTitle("Chú ý!");
            dialogDelete.setMessage("Bạn có muốn thêm nhiều từ một lượt không ?");
            dialogDelete.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showForgotDialog(getContext());
                }
            });
            dialogDelete.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    requireActivity().onBackPressed();
                }
            });
            dialogDelete.show();
            // ket thuc



        });


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mDataSource.open();

        Cursor cursor = mDataSource.getWordFromDB();
        updateToTextView(cursor);
    }

    public void updateToTextView(Cursor cursor) {
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int wordIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_WORD);
            int transl_vnIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_TRANSL_VN);
            int noteIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE);

            String word = cursor.getString(wordIndex);
            String transl_vn = cursor.getString(transl_vnIndex);
            String note = cursor.getString(noteIndex);

            wordResult.setText(word);
            wordTranslVNResult.setText(transl_vn);
            if(note.equals("")) {
               noteResult.setVisibility(View.GONE);
               hrmean.setVisibility(View.GONE);

            } else {
                noteResult.setText(note);
                noteResult.setVisibility(View.VISIBLE);
                hrmean.setVisibility(View.VISIBLE);
            }

            cursor.moveToNext();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataSource.close();
    }


    private void showForgotDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskEditText.setSingleLine(true);
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,0, 50,0);
        layout.addView(taskEditText);

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Nhập số từ mà bạn muốn thêm")
//                .setMessage("Enter your mobile number?")
                .setView(layout)
                .setPositiveButton("Bắt đầu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());

                        SharedPreferences pref = getActivity().getSharedPreferences("PREFS_NAME", 0);
                        SharedPreferences.Editor editor = pref.edit();


                        editor.putString("consecutiveNumbers",task);
                        editor.commit();

                        requireActivity().onBackPressed();


                    }
                })
                .setNegativeButton("Hủy", null)
                .create();
        dialog.show();
    }
}