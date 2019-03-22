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
			
}
