/**
 * 
 */
package com.example.spring.data.jdbc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * <p>
 * This java config class defines spring data source configurations. (Different
 * ways to configure dataSource)</p?
 * <p>
 * Includes, dataSource configuration using/via,
 * </p>
 * <ul>
 * <li>Jndi</li>
 * <li>Pooled DataSource using third party implementations</li>
 * <li>Embedded DataSource [Provided by spring]</li>
 * </ul>
 * 
 * Will attach dataSource to profiles, so appropriate dataSource will be used at
 * runtime based on current active profile.
 * 
 * @author amit
 *
 */

@Configuration
@PropertySource(value = "classpath:dataSource.properties")
public class DataSourceConfig {
	
	@Value("${datasource.jndi.name}")
	private String dataSourceJndiName;
	
	@Autowired
	private Environment environment;
	
	@Value("classpath:schema.sql")
	private Resource schemaSqlScriptResource;

	@Bean
	@Profile("prod")
	public JndiObjectFactoryBean jndiDataSource() {
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName(dataSourceJndiName);
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(DataSource.class);
		
		// This will never work, because, this object JndiObjectFactoryBean, does not lookup the DataSource (or any other object) immediately, rather
		// it lookups the object using provided jndi name after it's own property set callback is called. I.e this class itself is InitializingBean aware, so it actually lookups the object
		// inside afterPropertiesSet() callback of interface. Until that time we can not get required object, we will always get null when we call getObject() on it.
		
		// Spring handles above thing so well, that we can return this JndiObjectFactoryBean object as a return value, and provide what type of object we expect from this
		// jndi lookup from this factory's getObject() method, we use  setProxyInterface() on it to specify expected object type. Spring handles everything for us,
		// and inject this object wherever required once afterproprtySet is called on this object and casting the object to setProxyInterface object.
		
		// NOTE: Even though this method is returning JndiObjectFactoryBean, it is actually declaring javax.sql.DataSource bean. 
		
		/*DataSource dataSource = jndiObjectFactoryBean.getObject();
		return dataSource;*/
		return jndiObjectFactoryBean;
	}
	
	@Bean
	@Profile("int")
	public DataSource hikariCpDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(environment.getProperty("datasource.url"));
		config.setUsername(environment.getProperty("datasource.username"));
		config.setPassword(environment.getProperty("datasource.password"));
		
		// NOTE: We do not need to specify jdbc driver class name here.HikarCP manages its. There are lots of other configurations we can set,
		// but for now, sticking to defaults.
		// read more about hikariCP at https://github.com/brettwooldridge/HikariCP
		return new HikariDataSource(config);
	}
	
	@Bean
	@Profile("dev")
	public DataSource driverManagerDataSource() {
		// this is one of the spring DataSource implementation, we could have used other implementations as well, like 
		// SingleConnectionDataSource, SimpleDataSource.
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(environment.getProperty("datasource.driver.classname"));
		driverManagerDataSource.setUrl(environment.getProperty("datasource.url"));
		driverManagerDataSource.setUsername(environment.getProperty("datasource.username"));
		driverManagerDataSource.setPassword(environment.getProperty("datasource.password"));
		
		// since, it does not provides connection pooling, we do not need to set any connection pool related properties as we do in
		// pooled connection datasources like HikariCP
		return driverManagerDataSource;
	}
	
	@Bean
	@Profile("test")
	public DataSource embeddedDataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
	}

	/**
	 * This bean actually encapsulates, the scripts than needs to be initialized as a startup script.
	 * @return
	 */
	@Bean
	public DatabasePopulator databasePopulator() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(schemaSqlScriptResource);
		databasePopulator.setIgnoreFailedDrops(true); // This sets, whether to ignore error on DROP statements in scripts, this will be useful while executing first time and tables does not exists.
		return databasePopulator;
	}
	
	/**
	 * Using this DataSourceInitializer, we can initialize any dataSource (database) whether it is embedded or 
	 * running on remote server.
	 * EmbeddedDatabaes provides a way to initialize the in-memory database, but we can use this approach,
	 * which provides consistent way to initialize any type of database, just pass DataSource instance to it.
	 * 
	 * @param dataSource DataSource to be initialized with init scripts.
	 * @return
	 */
	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource); // Need to provide dataSource
		dataSourceInitializer.setEnabled(true); // we can run this initializer conditionally, we can provide boolean value here from emvirnment/system variables to decide at runtime, to initialize or not.
		dataSourceInitializer.setDatabasePopulator(databasePopulator()); // this Populator actually points to script to be executed.
		return dataSourceInitializer;
	}
}
