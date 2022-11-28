package com.example.imageviewer;

import com.example.imageviewer.databinding.ActivityMainBinding;
import com.example.imageviewer.ui.Gallery.Home.HomeFragment;
import com.example.imageviewer.ui.Spider.SpiderFragment;
import com.example.imageviewer.ui.Gallery.Folder.FolderFragment;
import com.example.imageviewer.ui.Gallery.Image.GalleryFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    //声明DrawerLayout布局
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    //声明侧滑菜单控件(不可以使用findById，需要单独初始化)
    private FragmentManager fragmentManager =getSupportFragmentManager();
    private FolderFragment folder;
    private SpiderFragment download;
    private HomeFragment home;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DynamicRequest();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //用于加载menu布局
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initView(){
        //实例化drawerLayout等
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        folder=new FolderFragment();
        download=new SpiderFragment();
        home=new HomeFragment();
        toolbar=binding.appBarMain.toolbar;
        //点击toolbar的nav_icon呼出侧边栏
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //调用监听函数
        setNavigationViewItemClickListener();

    }

    private void setNavigationViewItemClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                hideFragment(folder);
                hideFragment(download);
                hideFragment(home);
                switch (item.getItemId()) {
                    case R.id.nav_gallery:
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        if(!folder.isAdded()){
                            folder = new FolderFragment();
                            transaction.add(R.id.nav_host_fragment_content_main,folder).show(folder).commit();
                        }else{
                            transaction.show(folder).commit();
                        }
                        toolbar.setTitle("Gallery");
                        break;
                    case R.id.nav_download:
                        FragmentTransaction transaction2=fragmentManager.beginTransaction();
                        if(!download.isAdded()){
                            download = new SpiderFragment();
                            transaction2.add(R.id.nav_host_fragment_content_main,download).show(download).commit();
                        }else{
                            transaction2.show(download).commit();
                        }
                        toolbar.setTitle("Spider");
                        break;
                    case R.id.nav_home:
                        FragmentTransaction transaction3=fragmentManager.beginTransaction();
                        if(!home.isAdded()){
                            home=new HomeFragment();
                            transaction3.add(R.id.nav_host_fragment_content_main,home).show(home).commit();
                        }else{
                            transaction3.show(home).commit();
                        }
                        toolbar.setTitle("Home");
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }

        });
    }

    private void hideFragment(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.hide(fragment).commit();
        }

    }

    public void DynamicRequest(){
        // 动态申请权限
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        final int REQUEST_CODE = 10001;

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (String permission : permissions) {
                //  GRANTED---授权  DINIED---拒绝
                if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

                }
            }
        }
        boolean permission_readStorage = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean permission_camera = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        Log.d("ImgActivity:", "getImageFromDesc: \n");
        Log.d("ImgActivity: ", "readPermission: " + permission_readStorage + "\n");
        Log.d("ImgActivity： ", "cameraPermission: " + permission_camera + "\n");

    }



}
