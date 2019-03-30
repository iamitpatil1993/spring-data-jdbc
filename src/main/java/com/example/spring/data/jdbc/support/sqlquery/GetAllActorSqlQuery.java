/**
 * 
 */
package com.example.spring.data.jdbc.support.sqlquery;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.object.MappingSqlQuery;

import com.example.spring.data.jdbc.dao.ActorRowMapper;
import com.example.spring.data.jdbc.dao.SqlStore;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * <p>This class extends the MappingSqlQuery which intern extends SqlQuery class. </p>
 * <p>SqlQuery and it's sub classes provides way to encapsulate JDBC Sql queries into object, and provides
 * object oriented way to execute queries using these objects. </p>
 * 
 * <p>Drawback of these classes is, we need to create separate instance of these classes for each different type 
 * of query we execute on table, which I think a big issue, because as application grows we need lots
 * of query to be executed on database, so we can't always create instances or classes to encapsulate
 * queries into objects. We can prefer JdbcTemplate or NamedParameterJdbcTemplate over this, because this approache
 * does not sales much a work that we need to do as SimpleJdbcInsert does (which saves a lot due to DataBaseMetada)</p>
 * 
 * <p>This class encapsulates single query, to get all Actors from database and their Row mapping.
 * </p>
 * @author amit
 *
 */
public class GetAllActorSqlQuery extends MappingSqlQuery<Actor> {
	
	
	public GetAllActorSqlQuery(DataSource dataSource) {
		super(dataSource, SqlStore.SELECT_ACTOR_ALL); // we need to provide DataSource and Sql query to super.
		compile(); // we need to compile the query by calling super class method.
	}

	/**
	 * Just normal RowMapper.
	 */
	@Override
	protected Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new ActorRowMapper().mapRow(rs, rowNum);
	}
}
