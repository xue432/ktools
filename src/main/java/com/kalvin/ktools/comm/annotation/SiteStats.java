package com.kalvin.ktools.comm.annotation;

import java.lang.annotation.*;

/**
 * 网站统计注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SiteStats {

}
