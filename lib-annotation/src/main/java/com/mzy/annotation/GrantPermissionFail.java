package com.mzy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mozhenyong on 16/10/25.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface GrantPermissionFail {
    int value();
}
