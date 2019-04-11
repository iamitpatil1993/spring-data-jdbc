/**
 * 
 */
package com.example.spring.data.jdbc.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.spring.data.jdbc.dao.InvalidDataException;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */
public class ProgrammaticTxEmployeeServiceImplTest extends EmployeeServiceTest {

	@Autowired
	public void setEmployeeService(@Qualifier("programmaticTx") EmployeeService employeeService) {
		super.setEmployeeService(employeeService);
	};
	
	@Override
	@Test
	public void testRemove() {
		// given
		Employee employee = super.buildDummyEmployee();	
		Address address = super.buildDummyAddress();
		try {
			getEmployeeService().registerEmployee(employee, address);
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		
		// when
		getEmployeeService().remove(employee.getEmployeeId());
		
		// then
		Optional<Employee> optional = getEmployeeService().findEmployeeById(employee.getEmployeeId());
		List<Address> addresses = addressDao.findBymployeeId(employee.getEmployeeId());
		
		assertFalse(optional.isPresent());
		assertTrue(addresses.isEmpty());
	}
	
}
