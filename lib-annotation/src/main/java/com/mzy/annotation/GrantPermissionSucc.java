package com.mzy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mozhenyong on 16/10/24.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface GrantPermissionSucc {
    int value();
}
