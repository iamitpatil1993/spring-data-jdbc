/**
 * 
 */
package com.example.spring.data.jdbc.support.sqlquery;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import com.example.spring.data.jdbc.dao.SqlStore;

/**
 * @author amit
 *
 */
public class UpdateActorSqlUpdate extends SqlUpdate {
	
	public UpdateActorSqlUpdate(DataSource dataSource) {
		super(dataSource, SqlStore.UPDATE_ACTOR_BY_ID);
		declareParameter(new SqlParameter(Types.VARCHAR));
		declareParameter(new SqlParameter(Types.VARCHAR));
		declareParameter(new SqlParameter(Types.NUMERIC));
		compile();
	}
}
