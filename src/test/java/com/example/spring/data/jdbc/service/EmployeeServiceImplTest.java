/**
 * 
 */
package com.example.spring.data.jdbc.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dao.EmployeeDao;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeServiceImplTest extends BaseTest {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private AddressDao addressDao;

	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.EmployeeServiceImpl#registerEmployee(com.example.spring.data.jdbc.dto.Employee, com.example.spring.data.jdbc.dto.Address)}.
	 */
	@Test
	public void testRegisterEmployee() {
		// given
		Employee employee = buildDummyEmployee();
		Address address = buildDummyAddress();

		// when
		employeeService.registerEmployee(employee, address);
		
		// then
		assertNotNull(employee.getEmployeeId());
		assertNotNull(address.getId());
		
		Optional<Employee> createdEmployee = employeeDao.get(employee.getEmployeeId());
		assertTrue(createdEmployee.isPresent());
		
		Optional<Address> createdAddress = addressDao.get(address.getId());
		assertTrue(createdAddress.isPresent());

	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.EmployeeServiceImpl#registerEmployee(com.example.spring.data.jdbc.dto.Employee, com.example.spring.data.jdbc.dto.Address)}.
	 */
	@Test
	public void testRegisterEmployeeWithDataIntegrityVilolation() {
		// given
		Employee employee = buildDummyEmployee();
		Address address = buildDummyAddress();
		address.setZipcode(UUID.randomUUID().toString().concat(UUID.randomUUID().toString()));

		// when
		try {
			employeeService.registerEmployee(employee, address);
		} catch (DataIntegrityViolationException e) {
			// Nothing to handle here
		}
		// then
		Optional<Employee> createdEmployee = employeeDao.get(employee.getEmployeeId());
		assertFalse(createdEmployee.isPresent());
		
		Optional<Address> createdAddress = addressDao.get(address.getId());
		assertFalse(createdAddress.isPresent());

	}

	private Address buildDummyAddress() {
		Address address = new Address();
		address.setAddress("foo address");
		address.setCity("Foo City");
		address.setCountry("Foo Country");
		address.setLocality("Foo locality");
		address.setRegion("Foo Region");
		address.setState("Foo state");
		address.setZipcode("Foo zipcode");

		return address;
	}

	private Employee buildDummyEmployee() {
		Calendar dateOfJoining = Calendar.getInstance();
		dateOfJoining.add(Calendar.YEAR, -1);
		Employee employee = new Employee();
		employee.setFirstName("foo");
		employee.setLastName("bar");
		employee.setDateOfJoining(dateOfJoining.getTime());
		employee.setDesignation("Member of Technical Staff");
		return employee;
	}

}
