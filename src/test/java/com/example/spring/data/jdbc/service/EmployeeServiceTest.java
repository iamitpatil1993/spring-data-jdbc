/**
 * 
 */
package com.example.spring.data.jdbc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.NoTransactionException;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dao.EmployeeDao;
import com.example.spring.data.jdbc.dao.InvalidDataException;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class EmployeeServiceTest extends BaseTest {

	private EmployeeService employeeService;

	@Autowired
	protected EmployeeDao employeeDao;

	@Autowired
	protected AddressDao addressDao;

	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.EmployeeServiceImpl#registerEmployee(com.example.spring.data.jdbc.dto.Employee, com.example.spring.data.jdbc.dto.Address)}.
	 * @throws InvalidDataException 
	 */
	@Test
	public void testRegisterEmployee() throws InvalidDataException {
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
		} catch (InvalidDataException e) {
			// Nothing to do here 
		}
		// then
		Optional<Employee> createdEmployee = employeeDao.get(employee.getEmployeeId());
		assertFalse(createdEmployee.isPresent());
		
		Optional<Address> createdAddress = addressDao.get(address.getId());
		assertFalse(createdAddress.isPresent());

	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.EmployeeServiceImpl#findEmployeeById(java.lang.String)}.
	 * @throws InvalidDataException 
	 */
	@Test
	public void testFindEmployeeById() throws InvalidDataException {
		// given
		Employee employee = buildDummyEmployee();
		Address address = buildDummyAddress();
		
		employeeService.registerEmployee(employee, address);
		
		// when
		Optional<Employee> searchResult = employeeService.findEmployeeById(employee.getEmployeeId());
		
		// then
		assertNotNull(searchResult);
		assertTrue(searchResult.isPresent());
		assertEquals(employee.getEmployeeId(), searchResult.get().getEmployeeId());
		assertNotNull(searchResult.get().getAddress());
		assertFalse(searchResult.get().getAddress().isEmpty());
		assertTrue(searchResult.get().getAddress().size() == 1);
	}

	/**
	 * Checks, transaction get rollback due to our rollback rule defined for checked
	 * exception InvalidDataException.
	 */
	@Test
	public void testRegisterEmployeeWithInvalidData() {
		// given
		Employee employee = buildDummyEmployee();
		Address address = buildDummyAddress();
		address.setCity(null); // Intentionally setting invalid values to address to trigger transaction
								// rollback due to Rollback rules.

		try {
			employeeService.registerEmployee(employee, address);
		} catch (InvalidDataException e) {
			e.printStackTrace();

			// then
			Optional<Employee> createdEmployee = employeeDao.get(employee.getEmployeeId());
			assertFalse(createdEmployee.isPresent());
		}
	}
	
	/**
	 * Asserts that, private/protected/default scope methods of proxy classes can not start new transaction on their own
	 * even if those methods are annotaed with @Transactional annotation.
	 * 
	 * remove() of service class does not starts transaction, rather calls it's private method which is annoted with @transactional annotation,
	 * but private method called locally can not start it's own transaction, so it will throw NoTransactionException, if we try
	 * to access transaction.
	 */
	@Test(expected = NoTransactionException.class)
	public void testRemove() {
		// given
		final String employeeId = UUID.randomUUID().toString();

		// when
		employeeService.remove(employeeId);
	}

	protected Address buildDummyAddress() {
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

	protected Employee buildDummyEmployee() {
		Calendar dateOfJoining = Calendar.getInstance();
		dateOfJoining.add(Calendar.YEAR, -1);
		Employee employee = new Employee();
		employee.setFirstName("foo");
		employee.setLastName("bar");
		employee.setDateOfJoining(dateOfJoining.getTime());
		employee.setDesignation("Member of Technical Staff");
		return employee;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
