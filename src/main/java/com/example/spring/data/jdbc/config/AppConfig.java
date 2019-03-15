/**
 * 
 */
package com.example.spring.data.jdbc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Defines top application level configuration.
 * @author amit
 *
 */

@Configuration
@ComponentScan(basePackages = "com.example.spring.data.jdbc")
public class AppConfig {
	// Nothing to do here for now.
}
