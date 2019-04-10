/**
 * 
 */
package com.example.spring.data.jdbc.dao.custom.transactionattributes;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

import com.example.spring.data.jdbc.dao.InvalidDataException;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Transactional(rollbackFor = { InvalidDataException.class }, timeout = 5) 
/**
 * This is custom annotation, which enapsulates common/repeating transaction attribute details.
 * If we need same transactional configurations, we can create custom annotation like this,
 * and can replace @Transactional annotation with this annotation.
 * 
 *  It helps, grouping common/repeating transaction configurations at single place [For example Rollback rules].
 *  And can be reused.
 * @author amit
 *
 */
public @interface EmployeeTx {

}
