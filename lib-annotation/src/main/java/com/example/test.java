package com.example;

import com.mzy.annotation.GrantPermissionSucc;

import java.io.File;

/**
 * Created by mozhenyong on 16/10/22.
 */
public class test {
    public static void main(String args[]) {
        System.out.println("Hello World!");
        File dir = new File("/Users/mozhenyong/Desktop/AnnotationTest");
        if (!dir.exists())
            dir.mkdirs();
    }
}
