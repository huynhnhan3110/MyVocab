package com.example.finalhometest.ui.gopy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalhometest.R;

import static android.app.Activity.RESULT_OK;

public class GopYFragment extends Fragment {
    private final String APP_NAME = "English Learn";

    private EditText edt1, edt2;
    private TextView tvAttachment;
    private static final int PICK_FROM_GALLERY = 101;
    private Uri URI = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gopy, container, false);
        edt1 = root.findViewById(R.id.edit1);
        edt2 = root.findViewById(R.id.edit2);
        tvAttachment = root.findViewById(R.id.tvAttachment);

        Button bt = root.findViewById(R.id.bt);
        ImageButton attach = root.findViewById(R.id.attachment);

        bt.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/html");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"hotro.ntloteam@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback from "+APP_NAME);
            i.putExtra(Intent.EXTRA_TEXT, "Name: "+edt1.getText()+"\n Message: "+edt2.getText());
            try {
                if (URI != null) {
                    i.putExtra(Intent.EXTRA_STREAM, URI);
                }
                startActivity(Intent.createChooser(i, "Vui lòng chọn Gmail"));
            }catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "Bạn chưa chọn phương thức gửi", Toast.LENGTH_SHORT).show();
            }
        });
        attach.setOnClickListener(v -> openFolder());
        return root;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            URI = data.getData();
            tvAttachment.setText(URI.getLastPathSegment());
            tvAttachment.setVisibility(View.VISIBLE);
        }
    }
    public void openFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
    }

}