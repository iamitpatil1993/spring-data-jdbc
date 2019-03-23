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
	
	public Employee update(Employee employee);
	
	public void delete(Employee employee);
	
	public List<Employee> findAllByDesignation(final String designation);
	
	public List<Employee> addAll(List<Employee> employees);
	
	public List<Employee> importAll(List<Employee> employees);

}
