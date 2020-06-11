package com.example.createnewproject2.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.createnewproject2.R;
import com.example.createnewproject2.net.VolleyLo;
import com.example.createnewproject2.net.VolleyTo;
import com.example.createnewproject2.util.GifUtil;
import com.example.createnewproject2.util.ShowDialog;
import com.google.gson.Gson;
import com.lchad.gifflen.Gifflen;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int WRITE_STORAGE = 1;
    public static final int READ_STORAGE = 2;
    @BindView(R.id.image_1)
    ImageView image1;
    @BindView(R.id.image_2)
    ImageView image2;
    @BindView(R.id.image_3)
    ImageView image3;
    @BindView(R.id.image_4)
    ImageView image4;
    @BindView(R.id.image_5)
    ImageView image5;
    private AlertDialog alertDialog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startAppOpen();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        alertDialog = new AlertDialog.Builder(this).setView(new ProgressBar(this))
                .setMessage("正在生成gif图片").create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setVolley();
    }

    /**
     * 开启式获取应用权限
     */
    private void startAppOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_STORAGE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{
                    ShowDialog.ShowMyDialog(this, "无存储权限，不能继续使用此应用", new ShowDialog.ShowClick() {
                        @Override
                        public void onclick1(DialogInterface dialogInterface) {
                            finish();
                        }

                        @Override
                        public void onclick2(DialogInterface dialogInterface) {
                            finish();
                        }
                    });
                }
                break;
            case READ_STORAGE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{
                    ShowDialog.ShowMyDialog(this, "无读取权限，不能继续使用此应用", new ShowDialog.ShowClick() {
                        @Override
                        public void onclick1(DialogInterface dialogInterface) {
                            finish();
                        }

                        @Override
                        public void onclick2(DialogInterface dialogInterface) {
                            finish();
                        }
                    });
                }
                break;
        }
    }

    private Images images;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    List<Bitmap> bitmaps = new ArrayList<>();
    List<Images.ROWSDETAILBean> stringList = new ArrayList<>();

    private void setVolley() {
        VolleyTo volleyTo = new VolleyTo();
        volleyTo.setUrl("get_type1_image")
                .setJsonObject("UserName", "user1")
                .setVolleyLo(new VolleyLo() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        images = new Gson().fromJson(jsonObject.toString(), Images.class);
                        stringList = images.getROWS_DETAIL();
                        handler.sendEmptyMessage(0);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    bitmap1 = Glide.with(MainActivity.this).asBitmap().load(stringList.get(0).getImage()).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                    bitmap2 = Glide.with(MainActivity.this).asBitmap().load(stringList.get(1).getImage()).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                    bitmap3 = Glide.with(MainActivity.this).asBitmap().load(stringList.get(2).getImage()).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                    bitmap4 = Glide.with(MainActivity.this).asBitmap().load(stringList.get(3).getImage()).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                bitmaps.add(bitmap1);
                                bitmaps.add(bitmap2);
                                bitmaps.add(bitmap3);
                                bitmaps.add(bitmap4);

                                handler.sendEmptyMessage(0);
                            }
                        }).start();


                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("aaa", "onErrorResponse: ");
                    }
                }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            image1.setImageBitmap(bitmaps.get(0));
            image2.setImageBitmap(bitmaps.get(1));
            image3.setImageBitmap(bitmaps.get(2));
            image4.setImageBitmap(bitmaps.get(3));
            Toast.makeText(this, "共有:" + bitmaps.size(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.my_button)
    public void onViewClicked() {
        alertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Gifflen gifflen = new Gifflen.Builder()
//                        .color(256)
//                        .delay(500)
//                        .quality(10)
//                        .width(400)
//                        .height(400)
//                        .build();
//                gifflen.encode(Environment.getExternalStorageDirectory().getPath() + "/GIFMakerDemo/" + "Demo" + ".gif"
//                        ,bitmaps);

                GifUtil gifUtil = new GifUtil();
                final String path = gifUtil.createGif(bitmaps, "Demo", 500, MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).clear(image5);
                        Glide.with(MainActivity.this).load(new File(path)).into(image5);
                        Toast.makeText(MainActivity.this, "保存到" + path, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

            }
        }).start();

    }
}
