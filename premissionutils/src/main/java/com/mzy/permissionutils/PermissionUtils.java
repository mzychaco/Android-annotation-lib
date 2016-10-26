package com.mzy.permissionutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;

import com.mzy.processor.ProxyHelper;

/**
 * Created by mozhenyong on 16/10/25.
 */
public class PermissionUtils {
    private static final String SUFFIX = "$$"+ ProxyHelper.PROXY+"Impl";

    /**
     * 一个code只能对应同一组权限申请，也就是如果想申请两个不同组的权限，应该调用两次requestPermission
     * @param context
     * @param permissions
     * @param code
     */
    public static void requestPermission(Activity context,String[] permissions,int code){
        ActivityCompat.requestPermissions(context,permissions,code);
    }

    public static void requestPermission(Fragment context, String[] permissions, int code){
        context.requestPermissions(permissions,code);
    }

    public static boolean checkPermission(Context context,String... permissions){
        for (String c : permissions) {
            if(ContextCompat.checkSelfPermission(context, c)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        if(context instanceof Activity) {
//            ((Activity)context).onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }else if(context instanceof Fragment){
//            ((Fragment)context).onRequestPermissionsResult(requestCode,permissions,grantResults);
//        }
        for (int c : grantResults) {
            if (c != PackageManager.PERMISSION_GRANTED) {
                doFail(context,requestCode);
                return;
            }
        }
        doSuccess(context,requestCode);
    }

    public static void doFail(Object context,int requestCode){
        String contextName = context.getClass().getName();
        String implName= contextName+SUFFIX;
        try {
            PermissionProxy proxyImpl = (PermissionProxy) Class.forName(implName).newInstance();
            proxyImpl.permissionFailed(context,requestCode);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void doSuccess(Object context,int requestCode){
        String contextName = context.getClass().getName();
        String implName= contextName+SUFFIX;
        try {
            PermissionProxy proxyImpl = (PermissionProxy) Class.forName(implName).newInstance();
            proxyImpl.permissionSucc(context,requestCode);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
