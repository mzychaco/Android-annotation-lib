package com.mzy.sdks;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
String TAG=MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: "+ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) );
        Log.d(TAG, "onCreate: "+ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) );
        Log.d(TAG, "onCreate: "+ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) );
        Log.d(TAG, "onRequestPermissionsResult: create"+ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE));
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,100);
        }else{

        }

        String mSdRootPath = Environment.getExternalStorageDirectory().getPath()+"/testFile";
        File path=new File(mSdRootPath);
        if(!path.exists()){
            path.mkdirs();
        }
        File file=new File(path+"/abc.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: should"+ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]));
        switch (requestCode) {
            case 100:
                Log.d(TAG, "onRequestPermissionsResult: "+grantResults[0]);
//                Log.d(TAG, "onRequestPermissionsResult: "+grantResults[1]);
                if(grantResults[0]==0){

                }else {

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
                        Toast.makeText(this,"explanation",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(this,"XXX->setting",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
