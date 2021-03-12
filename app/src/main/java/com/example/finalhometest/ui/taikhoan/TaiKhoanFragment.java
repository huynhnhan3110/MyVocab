
package com.example.finalhometest.ui.taikhoan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.finalhometest.R;
import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.yournameDialog;
import com.example.finalhometest.usermotivationDialog;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class TaiKhoanFragment extends Fragment {

    protected DataSource mDataSource;

    public static final int PICK_IMAGE = 100;
    private CircleImageView img1;
    private TextView tv1;
    private Uri imageUri;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        Button changeAvatar = root.findViewById(R.id.bt2);
        Button changeUsername = root.findViewById(R.id.bt1);
        Button changeUsermoti = root.findViewById(R.id.bt3);
        TextView TvUserMotivation = root.findViewById(R.id.userMotivation);
        pref = getContext().getSharedPreferences("UserMotivation", 0); // 0 - for private mode
        editor = pref.edit();
        if(pref.getString("textMoti", null) != null) {
            TvUserMotivation.setText(pref.getString("textMoti",""));
        }

        img1 = root.findViewById(R.id.img1);
        tv1 = root.findViewById(R.id.tv1);
        mDataSource = new DataSource(getContext());
        mDataSource.open();

        TextView songaystreek = root.findViewById(R.id.ngaystreeknumber);
        if(mDataSource.countRowStreek() >0) {
            songaystreek.setText(mDataSource.countRowStreek()+"");
        }
        TextView sothanhtich = root.findViewById(R.id.sothanhtich);
        if(pref.getString("sothanhtich", null) !=null) {
            sothanhtich.setText(pref.getString("sothanhtich",""));
        }
        if(mDataSource.getImage(21)!=null) {
            img1.setImageBitmap(mDataSource.getImage(21)); // id
        }
        if(mDataSource.getYourname(11)!=null) {
            tv1.setText(mDataSource.getYourname(11)); // id
        }

        changeUsername.setOnClickListener(v -> {
            yournameDialog.FullNameListener listener = newfrinput -> {
                if (mDataSource.insertYourName(newfrinput, 11)) {
                    Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                    tv1.setText(mDataSource.getYourname(11));
                } else {
                    Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            };
            final yournameDialog dialog = new yournameDialog(getContext(), listener);
            dialog.show();
        });
        changeAvatar.setOnClickListener(v -> {

            String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE };
            if (hasPermissions(getContext(), perms)) {
                CropImage.activity().setAspectRatio(1,1).start(getContext(),this);
            } else {
                requestPermissions(perms, 1);
            }
        });
        changeUsermoti.setOnClickListener(v -> {
            usermotivationDialog.FullNameListener listener = newfrinput -> {
                editor.putString("textMoti", newfrinput); // Storing string
                TvUserMotivation.setText(newfrinput);
                editor.commit();
            };
            final usermotivationDialog dialog = new usermotivationDialog(getContext(), listener);
            dialog.show();
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK && data !=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
             imageUri = result.getUri();

            img1.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(img1);
            String x = getPath(imageUri);


            Integer num = Integer.parseInt("21"); // id
            if(mDataSource.insertImage(x,num)) {
                Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                img1.setImageBitmap(mDataSource.getImage(21)); // id

            }
        }
    }

    private String getPath(Uri uri) {
        if(uri ==null) {
            return  null;
        }
        String []Projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri,Projection,null,null,null);
        if (cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        }
        return uri.getPath();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CropImage.activity().setAspectRatio(1,1).start(getContext(),TaiKhoanFragment.this);
                    }
                }).start();
            }else{
                Toast.makeText(getContext(), "Bạn chưa cấp quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        }
    }
}