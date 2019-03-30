/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
public class ActorRowMapper implements RowMapper<Actor> {

	@Override
	public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
		Actor actor = new Actor();
		actor.setId(rs.getLong("id"));
		actor.setFirstName(rs.getString("first_name"));
		actor.setLastName(rs.getString("last_name"));
		return actor;
	}
}
