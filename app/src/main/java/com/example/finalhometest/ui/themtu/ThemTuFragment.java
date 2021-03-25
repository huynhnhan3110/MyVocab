package com.example.finalhometest.ui.themtu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemTuFragment extends Fragment {

    protected DataSource mDataSource;
    protected EditText wordEdit, translVNEdit, noteEdit;
    protected int sotuthemlientuc = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_themtu, container, false);

        // init datasource
        mDataSource = new DataSource(root.getContext());

        TextView consecutiveNumTv = root.findViewById(R.id.consecutiveNumberTV);
        SharedPreferences pref = getActivity().getSharedPreferences("PREFS_NAME", 0);
        if(!pref.getString("consecutiveNumbers","").equals("")) {
            String sotumuon = pref.getString("consecutiveNumbers","");
            consecutiveNumTv.setVisibility(View.VISIBLE);
            sotuthemlientuc = Integer.parseInt(sotumuon);
            consecutiveNumTv.setText("Còn "+sotuthemlientuc+" từ nữa");
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("consecutiveNumbers","");
            editor.commit();
        }

        wordEdit = root.findViewById(R.id.editWord);
        wordEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(wordEdit, InputMethodManager.SHOW_FORCED);

        translVNEdit = root.findViewById(R.id.editNghia);
        noteEdit = root.findViewById(R.id.editLuuY);

        wordEdit.setSingleLine(true);
        translVNEdit.setSingleLine(true);
        noteEdit.setSingleLine(true);

        ImageButton addnewword =root.findViewById(R.id.addnewword);
        ImageButton finishadd =root.findViewById(R.id.finishadd);

        finishadd.setOnClickListener(v -> {
            wordEdit.setText("");
            translVNEdit.setText("");
            noteEdit.setText("");
        });

        addnewword.setOnClickListener(v -> {
            String checkspaceWord = wordEdit.getText().toString().replace(" ", "");
            String checkspaceMean = translVNEdit.getText().toString().replace(" ", "");

            String afterTrimWord = wordEdit.getText().toString().trim().replaceAll("\\s+"," ");
            String afterTrimMean = translVNEdit.getText().toString().trim().replaceAll("\\s+"," ");

            String wordStr = wordEdit.getText().toString();
            String transStr = translVNEdit.getText().toString();
            String noteStr = noteEdit.getText().toString();

            if(wordStr.equals("") || transStr.equals("") || checkspaceWord.length() == 0 || checkspaceMean.length() == 0 || !checkSpecialCharacter(wordStr) || !checkSpecialCharacterMean(transStr)) {
                Toast.makeText(getContext(),"Thông tin không hợp lệ",Toast.LENGTH_SHORT).show();
            } else if(!afterTrimWord.equals(wordStr) || !afterTrimMean.equals(transStr)){
                Toast.makeText(getContext(),"Quá nhiều khoảng trắng trong từ",Toast.LENGTH_SHORT).show();
            }
            else {
                mDataSource.insertWordToDB(wordStr,
                        transStr,
                        noteStr);
                if(mDataSource.isInsertSuccess == -1) {
                    Toast.makeText(getContext(),"Bạn đã thêm từ này rồi",Toast.LENGTH_SHORT).show();
                } else {

                    wordEdit.setText("");
                    translVNEdit.setText("");
                    noteEdit.setText("");

                    Calendar c = Calendar.getInstance();
                    int dayofweek = c.get(Calendar.DAY_OF_WEEK);

                    int reformat = 0;
                    switch (dayofweek) {
                        case 1:
                            reformat = 7;
                            break;
                        case 2:
                            reformat = 1;
                            break;
                        case 3:
                            reformat = 2;
                            break;
                        case 4:
                            reformat = 3;
                            break;
                        case 5:
                            reformat = 4;
                            break;
                        case 6:
                            reformat = 5;
                            break;
                        case 7:
                            reformat = 6;
                            break;
                    }



                    mDataSource.insertToSotuantu((reformat)+"");

                    sotuthemlientuc--;
                    consecutiveNumTv.setText("Còn "+sotuthemlientuc+" từ nữa");
                    if(sotuthemlientuc <= 0) {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

                        if(pref.getString("isLienTuc","").equals("true")) {

                            navController.navigate(R.id.nav_ketquathemnhieututu);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("isLienTuc","");
                            editor.commit();
                        } else {
                            navController.navigate(R.id.nav_ketquathemtu);
                        }



//                        FragmentTransaction a = requireActivity().getSupportFragmentManager().beginTransaction();
//                        a.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
//                        a.addToBackStack(null);
//                        a.commit();
                    }


                }
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

    public boolean checkSpecialCharacter(String text) {
        String regExp = "^[^<>{}\"/|;:.~!?@#$%^&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©0-9_+]*$";
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
    public boolean checkSpecialCharacterMean(String text) {
        String regExp = "^[^<>{}\"/|;:.~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©_+]*$";
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }




}