package com.example.finalhometest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalhometest.helper.DataSource;
import com.example.finalhometest.ui.taikhoan.TaiKhoanFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    NavController navController;
    private SharedPreferences pref;
    private AppBarConfiguration mAppBarConfiguration;
    BarChart chart;
    protected DataSource mDataSource;

    private MenuItem iconNotlearn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);




        View headerView= navigationView.getHeaderView(0);
        TextView TextHeaderTitle = headerView.findViewById(R.id.yournamenav);
        TextView TextHeaderSubTitle = headerView.findViewById(R.id.emailnav);
        ImageView ImageUser = headerView.findViewById(R.id.imageView);

        TextHeaderSubTitle.setText("Bạn cảm thấy thế nào ?");

        pref = getApplicationContext().getSharedPreferences("UserMotivation", 0); // 0 - for private mode

        if(pref.getString("textMoti", null) != null) {
            TextHeaderSubTitle.setText(pref.getString("textMoti",""));
        }

        mDataSource = new DataSource(getApplicationContext());
        mDataSource.open();




        if(mDataSource.getImage(21)!=null) {
            ImageUser.setImageBitmap(mDataSource.getImage(21));
        }
        if(mDataSource.getYourname(11)!=null) {
            TextHeaderTitle.setText(mDataSource.getYourname(11));
        }
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if(mDataSource.getImage(21)!=null) {
                    ImageUser.setImageBitmap(mDataSource.getImage(21));
                }
                if(mDataSource.getYourname(11)!=null) {
                    TextHeaderTitle.setText(mDataSource.getYourname(11));
                }
                if(pref.getString("textMoti", null) != null) {
                    TextHeaderSubTitle.setText(pref.getString("textMoti",""));
                }


            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_taikhoan, R.id.nav_themtuvung,R.id.nav_ontu,R.id.nav_hoctucosan, R.id.nav_gopy)
//                .setDrawerLayout(drawer)
//                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }


        else if(navController.getCurrentDestination().getId() == R.id.nav_home) {

            doExit();
        }
        else {
            super.onBackPressed();
        }


    }
    public void doExit() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn lại lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // hien thi icon tren thanh toolbar.
        iconNotlearn = menu.findItem(R.id.action_settings);
        if(mDataSource.getNotLearnWord() > 0) {
            iconNotlearn.setVisible(true);
        } else{
            iconNotlearn.setVisible(false);
        }
        // ket thuc
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_hoctucosan);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void populateExpandableList() {

        expandableListAdapter = new com.example.finalhometest.ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {

            if (headerList.get(groupPosition).isGroup) {
                if (!headerList.get(groupPosition).hasChildren) {
                    int idFragment = headerList.get(groupPosition).idFragment;
                    navController.navigate(idFragment);
                    onBackPressed();

                }

            }

            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (childList.get(headerList.get(groupPosition)) != null) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                if (model.idFragment > 0) {
                    int idFragment = model.idFragment;

                    TextView txtListChild = v.findViewById(R.id.lblListItem);
                    Drawable img = v.getContext().getDrawable(R.drawable.ic_baseline_radio_button_checked_24);
                    img.setTint(Color.BLACK);
                    img.setBounds(0, 0, 60, 60);
                    txtListChild.setCompoundDrawables(img,null,null,null);
                    
                   navController.navigate(idFragment);


                    onBackPressed();
                }
                pref = getApplicationContext().getSharedPreferences("SotuMuonOn", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                int sotudangco = mDataSource.countRowLearned();
                if(sotudangco == 0) {
                    Toast.makeText(getApplicationContext(),"Bạn chưa thêm từ nào",Toast.LENGTH_SHORT).show();
                    return false;
                };
                if(childPosition == 0) {
                    if( sotudangco < 10) {
                        Toast.makeText(getApplicationContext(),"Hiện tại bạn chỉ có "+sotudangco+" từ",Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        editor.putString("sotu", "10"); // Storing string
                    }

                    editor.commit();
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_ontu);
                }
                if(childPosition == 1) {
                    if( sotudangco < 20) {
                        Toast.makeText(getApplicationContext(),"Hiện tại bạn chỉ có "+sotudangco+" từ",Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        editor.putString("sotu", "20"); // Storing string
                    }
                    editor.commit();
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_ontu);
                }
                if(childPosition == 2) {
                    int giamdi = mDataSource.countRowLearned();
                    editor.putString("sotu", giamdi+""); // Storing string
                    editor.commit();
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_ontu);
                }
                if(childPosition == 3) {

                    TextView txtListChild = v.findViewById(R.id.lblListItem);
                    Drawable img = v.getContext().getDrawable(R.drawable.ic_baseline_radio_button_checked_24);
                    img.setTint(Color.BLACK);
                    img.setBounds(0, 0, 60, 60);
                    txtListChild.setCompoundDrawables(img,null,null,null);

                    customDialog.FullNameListener listener = newfrinput -> {
                        if(Integer.parseInt(newfrinput) > mDataSource.countRowLearned()) {
                            Toast.makeText(getApplicationContext(),"Số từ bạn nhập lớn hơn số từ đã được học",Toast.LENGTH_SHORT).show();
                        } else if(Integer.parseInt(newfrinput) == 0){
                            Toast.makeText(getApplicationContext(),"Vui lòng thực hiện lại",Toast.LENGTH_SHORT).show();
                        } else {
                            editor.putString("sotu", newfrinput); // Storing string
                            editor.commit();

                            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                            navController.navigate(R.id.nav_ontu);
                        }

                    };
                    final customDialog dialog = new customDialog(MainActivity.this, listener);
                    dialog.show();



                }
                onBackPressed();
            }

            return false;
        });
    }


    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel("Trang chủ", true, false, R.id.nav_home); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Tài khoản", true, false, R.id.nav_taikhoan); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Thêm từ vựng", true, false, R.id.nav_themtuvung); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);


        menuModel = new MenuModel("Ôn từ vựng", true, true, 0); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(" 10 từ", false, false, 0);
        childModelsList.add(childModel);

        childModel = new MenuModel(" 20 từ", false, false, 0);
        childModelsList.add(childModel);

        childModel = new MenuModel(" Ôn tất cả", false, false, 0);
        childModelsList.add(childModel);

        childModel = new MenuModel(" Nhập số từ muốn ôn", false, false, 0);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Học từ vựng có sẵn", true, false, R.id.nav_tuchuanbi); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Từ đã thêm", true, false, R.id.nav_listviewRecord); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Góp ý - liên hệ", true, false, R.id.nav_gopy); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

    }

}