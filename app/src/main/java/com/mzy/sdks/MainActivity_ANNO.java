package com.mzy.sdks;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mzy.annotation.GrantPermissionFail;
import com.mzy.annotation.GrantPermissionSucc;
import com.mzy.permissionutils.PermissionUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity_ANNO extends AppCompatActivity {
    final int code=100;
String TAG=MainActivity_ANNO.class.getName();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);

        if (!PermissionUtils.checkPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionUtils.requestPermission(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    code);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @GrantPermissionSucc(code)
    public void createFile(){
        tv.setText("createFile!");
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

    @GrantPermissionFail( code)
    public void showtoast(){
//        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
//            Toast.makeText(this,"explanation",Toast.LENGTH_LONG).show();
//        }else{
            Toast.makeText(this,"XXX->setting",Toast.LENGTH_SHORT).show();
//        }
    }
}
