/**
 * 
 */
package com.example.spring.data.jdbc.support;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

/**
 * This class extends default SQL exception translator, in order to provide
 * custom error code mapping. This class will be used to map exception, if no
 * mapping provided here, then default will be used.
 * 
 * Set, this class to jdbcTemplate instance, where we need this custom exception handler.
 * 
 * @author amit
 *
 */
public class CustomSQLErrorCodesTransalator extends SQLErrorCodeSQLExceptionTranslator {

	/**
	 * Override this method to provide custom error code handling. Return null, if
	 * could not map the error code, in that case default error code translator will
	 * be used.
	 */
	@Override
	protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx) {
		if (sqlEx.getErrorCode() == 42122) {
			return new DeadlockLoserDataAccessException(task, sqlEx);
		}
		return null;
	}
}
