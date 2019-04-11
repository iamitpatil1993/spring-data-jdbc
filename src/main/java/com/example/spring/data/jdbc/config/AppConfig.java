/**
 * 
 */
package com.example.spring.data.jdbc.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.spring.data.jdbc.support.CustomSQLErrorCodesTransalator;

/**
 * Defines top application level configuration.
 * 
 * @author amit
 *
 */

@Configuration
@ComponentScan(basePackages = "com.example.spring.data.jdbc")
@Import(value = { DataSourceConfig.class })
@EnableTransactionManagement(order = Integer.MAX_VALUE) // Enables declarative annotation based transaction.
														// we set order to max value, so that TransactionInterceptor aspect has lowest precedence and
														// our application defined aspects will have more precedence and they will execute before transaction aspects.
														// we can play with orders to twick aspect ordering as we want.
@EnableAspectJAutoProxy // Enable annotation based AOP
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		CustomSQLErrorCodesTransalator codesTransalator = new CustomSQLErrorCodesTransalator();
		// set dataSource, so that translator can use DataSource connection, to get
		// metadata from db, and use it to get correct instance of SqlErrorCodes instance
		// from factory using product name.
		codesTransalator.setDataSource(dataSource);

		jdbcTemplate.setExceptionTranslator(new CustomSQLErrorCodesTransalator());
		return jdbcTemplate;
	}

	/**
	 * Provides same functionality as JdbcTemplate i,e JdbcOperations, but with one additional feature of JPA Named Query style Named jdbc parameters.
	 * @param dataSource DataSource to use.
	 * @return
	 */
	@Bean
	public NamedParameterJdbcOperations namedParameterJdbcTemplate(JdbcTemplate classicJdbcTemplate) {
		// return new NamedParameterJdbcTemplate(dataSource);

		// NOTE: Above NamedParameterJdbcTemplate, creates it's own JdbcTemplate (new)
		// so, setting CustomSqlErrorCodesTranslator in jdbcTemplate(),
		// will not work in this case.
		// So, we can create NamedParameterJdbcTemplate from above singleton and
		// registered JdbcTemplate in order to use CustomSqlErrorCodesTranslator
		return new NamedParameterJdbcTemplate(classicJdbcTemplate);
	}
	
	/**
	 * Defines transaction manager for jdbc. 
	 * @param dataSource DataSoure to link/synchronize with DataSourceTransactionManager
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	/**
	 * Creates TransactionTemplate with default configuration, which will be shared
	 * by all services. Sample template can be used even if
	 * PlatformTransactionManager changes.
	 * 
	 * @param platformTransactionManager This platform transaction can be any
	 *                                   implememtation like JPA, Hibernate, JDBC
	 *                                   but transaction template code remains
	 *                                   consistent. No actual code changes required
	 *                                   in our services using TrannsactionTemplate
	 *                                   even though PlatformTransactionManager
	 *                                   implementation changes.
	 * 
	 * @return TransactionTemplate [singleton] with default configuration, which
	 *         will be shared by all services. If service needs TransactionTemplate
	 *         with different configuration, create local instance of
	 *         TransactionTemplate with desired configuration.
	 */
	@Bean
	public TransactionOperations transactionTemplate(PlatformTransactionManager platformTransactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
		transactionTemplate.setReadOnly(false);
		transactionTemplate.setTimeout(30);
		return transactionTemplate;
	}
}
