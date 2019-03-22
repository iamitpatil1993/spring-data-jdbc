/**
 * 
 */
package com.example.spring.data.jdbc;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.example.spring.data.jdbc.config.AppConfig;

/**
 * @author amit
 *
 */

@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles(profiles = "test")
public class BaseTest {

	// Nothing to do here for now.
}
