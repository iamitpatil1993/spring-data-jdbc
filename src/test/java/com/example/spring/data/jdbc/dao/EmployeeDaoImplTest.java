/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amipatil
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeDaoImplTest extends BaseTest {

	@Autowired
	private EmployeeDao employeeDao;

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
	}
}
