package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mozhenyong on 16/10/21.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD,ElementType.TYPE,ElementType.METHOD})
public @interface Seriable {
}
