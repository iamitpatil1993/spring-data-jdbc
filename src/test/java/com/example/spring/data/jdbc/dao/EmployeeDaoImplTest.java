/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amipatil
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeDaoImplTest extends BaseTest {

	@Autowired
	private EmployeeDao employeeDao;
	
	private static String employeeId = null;

	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#add(com.example.spring.data.jdbc.dto.Employee)}.
	 */
	@Test
	public void testAdd() {
		// given
		Calendar dateOfJoining = Calendar.getInstance();
		dateOfJoining.add(Calendar.YEAR, -1);
		Employee employee = new Employee();
		employee.setFirstName("foo");
		employee.setLastName("bar");
		employee.setDateOfJoining(dateOfJoining.getTime());
		employee.setDesignation("Member of Technical Staff");

		// when
		Employee createdEmployee = employeeDao.add(employee);

		// then
		assertNotNull("EmployeeDao#add should always returned created object if successful", createdEmployee);
		assertNotNull("EmployeeDao#add should always returned created object with created employee's employeeId",
				createdEmployee.getEmployeeId());
		employeeId = createdEmployee.getEmployeeId();
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#get(java.lang.String)}.
	 */
	@Test
	public void testGetWithRecondNotFound() {
		// given
		String employeeId = UUID.randomUUID().toString();
		
		// when
		Optional<Employee> employeeOptional = employeeDao.get(employeeId);
		
		assertNotNull(employeeOptional);
		assertFalse(employeeOptional.isPresent());
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#get(java.lang.String)}.
	 */
	@Test
	public void testGetWithRecondFound() {
		// given
		String employeeId = EmployeeDaoImplTest.employeeId;
		
		// when
		Optional<Employee> employeeOptional = employeeDao.get(employeeId);
		
		assertNotNull(employeeOptional);
		assertTrue(employeeOptional.isPresent());
		assertEquals(employeeId, employeeOptional.get().getEmployeeId());
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		// when
		List<Employee> employees = employeeDao.findAll();
	
		// then
		assertNotNull(employees);
		assertFalse(employees.isEmpty());
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#update(com.example.spring.data.jdbc.dto.Employee)}.
	 */
	@Test(expected = InsufficientDataException.class)
	public void testUpdateWithEmptyEmployeeId() {
		// when
		employeeDao.update(new Employee());
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#update(com.example.spring.data.jdbc.dto.Employee)}.
	 */
	@Test()
	public void testUpdate() {
		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		employee.setFirstName("Amit");
		employee.setLastName("Patil");
		
		// when
		employeeDao.update(employee);
		
		// then
		Optional<Employee> employeeOptional = employeeDao.get(employeeId);
		assertNotNull(employeeOptional);
		assertTrue(employeeOptional.isPresent());
		assertEquals(employee.getFirstName(), employeeOptional.get().getFirstName());
		assertEquals(employee.getLastName(), employeeOptional.get().getLastName());
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.dao.EmployeeDaoImpl#delete(com.example.spring.data.jdbc.dto.Employee)}.
	 */
	@Test()
	public void testWDelete() {
		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		
		// when
		employeeDao.delete(employee);
		
		// then
		Optional<Employee> employeeOptional = employeeDao.get(employeeId);
		assertNotNull(employeeOptional);
		assertFalse(employeeOptional.isPresent());
	}
}
