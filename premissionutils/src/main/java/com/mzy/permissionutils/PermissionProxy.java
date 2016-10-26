package com.mzy.permissionutils;

/**
 * Created by mozhenyong on 16/10/25.
 */
public interface PermissionProxy<T> {
    void permissionSucc(T impl,int requestCode);
    void permissionFailed(T impl,int requestCode);
}
