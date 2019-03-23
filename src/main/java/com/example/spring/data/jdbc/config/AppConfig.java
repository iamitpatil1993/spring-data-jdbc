/**
 * 
 */
package com.example.spring.data.jdbc.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Defines top application level configuration.
 * 
 * @author amit
 *
 */

@Configuration
@ComponentScan(basePackages = "com.example.spring.data.jdbc")
@Import(value = { DataSourceConfig.class })
public class AppConfig {
	
	/**
	 * Spring provides two flavors of JdbcTemplaes, 
	 * <p> 1. JdbcTemplate: Which is a class that implements JdbcOperations interface and provides jdbcOperations with indexed parameters.</p>
	 * <p> 2. namedJdbcTemplate: Which is a class that implements namedJdbcOperations interface and provides jdbcOperations with named parameters.</p>
	 * 
	 * I am using JdbcTemplate, because it requires less number of lines of code to build queries. (in case of other we need build and pass hashMap of named Paramters)
	 * @param dataSource
	 * @return
	 */
	@Bean
	public JdbcOperations jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	
}
