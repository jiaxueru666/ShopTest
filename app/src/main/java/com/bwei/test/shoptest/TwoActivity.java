package com.bwei.test.shoptest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/6/23.
 * time:
 * author:付智焱
 */

public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camar();
            }
        });

    }
    public void toCamera(){

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param requestCode
     * @param permissions 请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            // camear 权限回调

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                // 表示用户授权
                Toast.makeText(this, " user Permission" , Toast.LENGTH_SHORT).show();

                toCamera();


            } else {

                //用户拒绝权限
                Toast.makeText(this, " no Permission" , Toast.LENGTH_SHORT).show();

            }



        }

    }



    //        checkSelfPermission 检测有没有 权限
//        PackageManager.PERMISSION_GRANTED 有权限
//        PackageManager.PERMISSION_DENIED  拒绝权限
    private void camar() {
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA))!= PackageManager.PERMISSION_DENIED){
            //权限发生了改变 true  //  false 小米
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){



                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions(TwoActivity.this,new String[]{Manifest.permission.CAMERA},1);

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();



            }else {
                ActivityCompat.requestPermissions(TwoActivity.this,new String[]{Manifest.permission.CAMERA},1);

            }
        }else{
            toCamera();
        }

    }
}
