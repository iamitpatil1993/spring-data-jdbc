package com.example.spring.data.jdbc.dao;

import java.util.List;

import com.example.spring.data.jdbc.dto.Actor;

/**
 * This interface is to demonstrate the use of SimleJdbcInsert in
 * sprinf-data-jdbc. It demonstrates how SimpleJdbcInsert can simplify insert
 * queries using Database metadata detection from jdbc driver.
 * 
 * It also demonstrates, how to read auto-generated primary keys from database
 * after record creation. For more information on how it actually works, read
 * spring docs
 * https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/data-access.html#jdbc-simple-jdbc
 * 
 * @author amit
 *
 */
public interface ActorDao {

	public Actor add(Actor actor);
	
	public List<Actor> findAll();
	
}
