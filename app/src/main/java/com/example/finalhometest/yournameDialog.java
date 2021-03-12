package com.example.finalhometest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class yournameDialog extends Dialog {

    public interface FullNameListener {
        void fullNameEntered(String fullName);
    }
    public yournameDialog(Context context, yournameDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }
    public Context context;

    private EditText editTextSoTu;

    private final yournameDialog.FullNameListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yourname_dialog);

        this.editTextSoTu = findViewById(R.id.editText_sotu);
        Button buttonOK = findViewById(R.id.button_ok);
        Button buttonCancel  = findViewById(R.id.button_cancel);

        buttonOK.setOnClickListener(v -> buttonOKClick());
        buttonCancel.setOnClickListener(v -> buttonCancelClick());
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String sotu = this.editTextSoTu.getText().toString();

        if(sotu.isEmpty())  {
            Toast.makeText(this.context, "Vui lòng nhập tên", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.fullNameEntered(sotu);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}