package com.example.finalhometest.ui.tuchuanbi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowId;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.animation.content.Content;
import com.example.finalhometest.R;

public class ChuanBiAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] numberWord;
    private int[] numberImage;

    public ChuanBiAdapter(Context c, String[] numberWord, int[] numberImage) {
        context = c;
        this.numberWord = numberWord;
        this.numberImage = numberImage;

    }
    @Override
    public int getCount() {
        return numberWord.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_tuchuanbi,null);
        }

        ImageView imageView = convertView.findViewById(R.id.imageviewtuchuanbi);
        TextView textView = convertView.findViewById(R.id.textviewtuchuanbi);

        imageView.setImageResource(numberImage[position]);
        textView.setText(numberWord[position]);


        return convertView;
    }
}
