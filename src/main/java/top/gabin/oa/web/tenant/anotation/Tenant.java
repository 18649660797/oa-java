/**
 * Copyright (c) 2016 云智盛世
 * Created with UnTenant.
 */
package top.gabin.oa.web.tenant.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class description
 *
 *
 *
 * @author linjiabin on  16/4/18
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tenant {
    boolean enTenant() default false;
}
