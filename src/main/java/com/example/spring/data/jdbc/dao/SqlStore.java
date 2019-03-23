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
			
}
