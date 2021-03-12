package com.example.finalhometest.ui.listrecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Model> recordList;
    private DataSource mDataSource;


    public RecordListAdapter(Context context, int layout, ArrayList<Model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;

    }
    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        ImageView imageView;
        ImageButton editRecordBtn, removeRecordBtn;
        TextView txtWord, txtMean, txtNote;
        TextView othernum;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        ViewHolder holder = new ViewHolder();
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtWord = row.findViewById(R.id.wordtxt);
            holder.txtMean = row.findViewById(R.id.meantxt);
            holder.txtNote = row.findViewById(R.id.notetxt);
            holder.editRecordBtn = row.findViewById(R.id.editRecordBtn);
            holder.removeRecordBtn = row.findViewById(R.id.removeRecordBtn);
            holder.othernum = row.findViewById(R.id.sothutu);





            row.setTag(holder);




        } else {
            holder = (ViewHolder)row.getTag();

        }

        mDataSource = new DataSource(context);
        mDataSource.open();
        holder.removeRecordBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
            dialogDelete.setTitle("Chú ý!!!");
            dialogDelete.setMessage("Bạn có xác nhận xóa từ vựng này ?");
            dialogDelete.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        mDataSource.deleteData(recordList.get(position).getWord());
                        Toast.makeText(context,"Xóa thành công",Toast.LENGTH_SHORT).show();
                        // bat dau giam so tu trong ngay
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



                        mDataSource.removeSotuTheongay((reformat)+"");

                        // ket thuc giam so tu trong ngay

                    }catch (Exception e) {
                        Log.e("Lỗi",e.getMessage());
                    }
                    updateRecordList();
                }
            });
            dialogDelete.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogDelete.show();
        });

        holder.editRecordBtn.setOnClickListener(v ->{
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.updatedialog);
            dialog.setTitle("Update");
            EditText edtWord = dialog.findViewById(R.id.wordUpdateTxt);
            EditText edtMean = dialog.findViewById(R.id.meanUpdateTxt);
            EditText edtNote = dialog.findViewById(R.id.noteUpdateTxt);
            Button updateBtn = dialog.findViewById(R.id.updateBtn);


            mDataSource.open();
            Cursor cursor = mDataSource.getByWord(recordList.get(position).getWord());
            while (cursor.moveToNext()) {
                edtWord.setText(cursor.getString(0));;
                edtMean.setText(cursor.getString(1));
                edtNote.setText(cursor.getString(2));
            }
            int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.75);
            int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.5);

            dialog.getWindow().setLayout(width,height);
            dialog.show();

            updateBtn.setOnClickListener(newv -> {
                try {
                    mDataSource.open();
                    String checkspaceWord = edtWord.getText().toString().replace(" ", "");
                    String checkspaceMean = edtMean.getText().toString().replace(" ", "");

                    String afterTrimWord = edtWord.getText().toString().trim().replaceAll("\\s+"," ");
                    String afterTrimMean = edtMean.getText().toString().trim().replaceAll("\\s+"," ");

                    String wordStr = edtWord.getText().toString();
                    String transStr = edtMean.getText().toString();
                    String noteStr = edtNote.getText().toString();

                    if(checkspaceWord.length() == 0 || checkspaceMean.length() == 0 || wordStr.equals("") || transStr.equals("") || !checkSpecialCharacter(wordStr) || !checkSpecialCharacter(transStr)) {
                        Toast.makeText(context,"Thông tin không hợp lệ",Toast.LENGTH_SHORT).show();
                    }
                    else if(!afterTrimWord.equals(wordStr) || !afterTrimMean.equals(transStr)){
                        Toast.makeText(context,"Quá nhiều khoảng trắng trong từ",Toast.LENGTH_SHORT).show();
                    } else {
                        mDataSource.updateData(wordStr,
                                transStr,
                                noteStr,
                                recordList.get(position).getWord());
                        dialog.dismiss();
                        Toast.makeText(context,"Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Update error",e.getMessage());

                }
                updateRecordList();
            });

        });



        Model model = recordList.get(position);

        holder.txtWord.setText(model.getWord());
        holder.txtMean.setText(model.getMean());
        holder.txtNote.setText(model.getNote());
        holder.othernum.setText((position+1)+"");
        return row;
    }
    public boolean checkSpecialCharacter(String text) {
        String regExp = "^[^<>{}\"/|;:.,~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©0-9_+]*$";
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
    private void updateRecordList() {
        mDataSource.open();
        Cursor cursor = mDataSource.getWordFromDB();
        recordList.clear();
        while (cursor.moveToNext()) {
            int id= 0;
            String word = cursor.getString(0);
            String mean = cursor.getString(1);
            String note = cursor.getString(2);

            recordList.add(new Model(id, word,mean,note));
            id++;
        }
        notifyDataSetChanged();

    }
}
