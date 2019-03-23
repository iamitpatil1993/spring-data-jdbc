/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import java.util.List;
import java.util.Optional;

import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amipatil
 *
 */

public interface EmployeeDao {

	public Employee add(Employee employee);
	
	public Optional<Employee> get(final String employeeId);
	
	public List<Employee> findAll();

}
