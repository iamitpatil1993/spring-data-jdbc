/**
 * 
 */
package com.example.spring.data.jdbc.dao;

/**
 * @author amipatil
 *
 */

public abstract class SqlStore {

	public static final String INSERT_EMPLOYEE = "INSERT INTO employee(employee_id, first_name, last_name, date_of_joining, designation) VALUES (?, ?, ?, ?, ?)";
	
	public static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employee WHERE employee_id = ?";
	
	public static final String SELECT_EMPLOYEE_ALL = "SELECT * FROM employee";
	
	public static final String UPDTAE_EMPLOYEE_BY_ID = "UPDATE employee set first_name = ?, last_name = ?, designation = ?, updated_date = now() WHERE employee_id = ?";
	
	public static final String DELETE_EMPLOYEE_BY_ID = "DELETE FROM employee WHERE employee_id = ?";
	
	public static final String SELECT_ALL_EMPLOYEE_BY_DESIGNATION = "SELECT * FROM employee WHERE designation = :designation";
	
	public static final String SELECT_EMPLOYEE_COUNT = "SELECT COUNT(*) FROM employee";
	
	public static final String SELECT_EMPLOYEE_COUNT_BY_DESIGNATION = "SELECT COUNT(*) FROM employee WHERE designation = ?";
	
	public static final String SELECT_EMPLOYEE_BY_NAME = "SELECT * FROM employee WHERE first_name = :firstName AND last_name = :lastName";
			
	public static final String SELECT_ACTOR_ALL = "SELECT * FROM actor";
	
	public static final String UPDATE_ACTOR_BY_ID = "UPDATE actor SET first_name = ?, last_name = ? WHERE id = ?";
	
	public static final String FIND_ALL_ACTOR_BY_FIRST_NAMES = "SELECT * FROM actor WHERE first_name IN (:firstNames)";
	
	public static final String FIND_ALL_ACTOR_BY_NAMES = "SELECT * FROM actor WHERE (first_name, last_name) IN ((:firstName, :lastName))";
	
	public static final String FIND_ADDRESS_BY_ID = "SELECT * FROM address WHERE id = ?";
	
	public static final String FIND_ADDRESS_BY_EMPLOYEE_ID = "SELECT * FROM address WHERE employee_id = ?";
	
	public static final String REMOVE_ADDRESS_BY_EMPLOYEE_ID = "DELETE FROM address WHERE employee_id = ?";
}
