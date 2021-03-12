package com.example.finalhometest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class customDialog extends Dialog {
    public final Context context;

    private EditText editTextSoTu;
    private final customDialog.FullNameListener listener;
    public interface FullNameListener {
        void fullNameEntered(String fullName);
    }
    public customDialog(Context context, customDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);

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
            Toast.makeText(this.context, "Không được để trống", Toast.LENGTH_LONG).show();
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