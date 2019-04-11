/**
 * 
 */
package com.example.spring.data.jdbc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author amit
 *
 */
public class EmployeeServiceImplTest extends EmployeeServiceTest {

	@Autowired
	public void setEmployeeService(@Qualifier("declarativeTx") EmployeeService employeeService) {
		super.setEmployeeService(employeeService);
	};
	
}
