# spring-data-jdbc
This project covers spring data using JDBC. It demonstrates the use of spring's abstraction over JDBC over plain old JDBC.

It demonstrates,

1. Configuring `DataSource` (Spring built-in dataSource, DataSource using `JNDI`, third party connection pools using `HikariCP`, In memory dataSource using H2 database).
2. Spring's persistence platform-agnostic exceptions.
3. `JdbcTemplate` (CRUD Operations).
4. `NamedParameterJdbcTemplate` - To provide JPA-style named parameters to SQL query (CRUD Operations).
5. `BatchPreparedStatementSetter` - For batch operations with a single batch of operations.
6. `ParameterizedPreparedStatementSetter` - For batch operations with multiple batch of operations.
7. `SqlParameterSource` - for a better way of passing SQL query parameters. (MapSqlParameterSource and `BeanPropertySqlParameterSource`)
8. `SQLExceptionTranslator` - To map SQL error codes (database-specific) to custom exceptions.
9. `SimpleJdbcInsert` - To simplify insert queries with auto-generated SQL queries based on table metadata.
10. `SqlQuery` - To encapsulate SQL queries as reusable, thread-safe java objects.
11. `UpdateQuery` - To encapsulate an SQL update queries as reusable, thread-safe java objects.
12. `DataSourceInitializer` & `DatabasePopulator` - To initialize data source with schema and data at application context startup using SQL scripts.
13. Expose embedded H2 database as a web server.
14. `Liquibase` - Integration with liquibase to initialize the database with liquibase scripts at application context startup.

## Spring Transactions using JDBC
The project also covers spring consistent way of providing transactions using declarative transaction based on spring AOP proxies.

It demonstrates, 

1. A declarative transaction using spring AOP proxy.
2. read-only transactions.
3. Rollback Rules - To define custom rollback rules to rollback transaction on exception.
4. Programmatic rollback of the declarative transaction.
5. Define custom `@Transactional` annotation to avoid transaction configurations duplication.
6. Custom Aspects on transactional code. (Before and after spring's transaction aspects and their ordering).
7. `TransactionTemplate` - For programmatic demarcation of transaction and propagation
8. Nested transactions using Savepoints.
9. Sub-class based AOP proxy for transactions using `CGLIB`.


# Prerequisite
1. Java Installed (Java 8 or above)
2. Maven Installed
3. Basic knowledge of Spring.
4. Basic understanding of database, SQL and JDBC.

# Usage
All samples are executed as JUnit Test cases. So use below command to execute all samples from the root directory of the project.

 1. ## Using embedded (H2) database.

	All test cases use the same embedded database which starts before test suite and shutdown after test suite, also get initialized at application startup automatically, so no need of external database server configuration.

    mvn clean test -Dspring.profiles.active=test

  
 2. ## Using external Postgres database
	 To use postgres as external database to execute tests, 
	1. Provide database connection parameters in `dataSource.properties`

       mvn clean test -Dspring.profiles.active=int

