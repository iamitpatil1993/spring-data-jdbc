/**
 * 
 */
package com.example.spring.data.jdbc.service;

import java.util.Optional;

import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */
public interface EmployeeService {
	
	
	public void registerEmployee(final Employee employee, final Address address);
	
	public Optional<Employee> findEmployeeById(final String employeeId);
	
}
