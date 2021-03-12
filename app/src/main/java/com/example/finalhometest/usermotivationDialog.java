package com.example.finalhometest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class usermotivationDialog extends Dialog {

    public interface FullNameListener {
        void fullNameEntered(String fullName);
    }
    public usermotivationDialog(Context context, usermotivationDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }
    public Context context;

    private EditText editTextNoteMoti;

    private final usermotivationDialog.FullNameListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inputmotivation_dialog);

        this.editTextNoteMoti = findViewById(R.id.editText_moti);
        Button buttonOK = findViewById(R.id.button_ok);
        Button buttonCancel  = findViewById(R.id.button_cancel);

        buttonOK.setOnClickListener(v -> buttonOKClick());
        buttonCancel.setOnClickListener(v -> buttonCancelClick());
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String textMoti = this.editTextNoteMoti.getText().toString();

        if(textMoti.isEmpty())  {
            Toast.makeText(this.context, "Bạn phải nhập gì chứ !", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.fullNameEntered(textMoti);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}