package com.example.spring.data.jdbc.dao;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spring.data.jdbc.dto.Employee;

/**
 * 
 * @author amipatil
 *
 */

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	// Always prefer constructor/setter based injection, which helps in test case injection.
	@Autowired
	public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Creates Employee in underlying persistence store.
	 * @param employee Employee details to be created.
	 * @return Employee with employeeId.
	 */
	@Override
	public Employee add(Employee employee) {
		String empId = UUID.randomUUID().toString();
		
		int rowsUpdated = jdbcTemplate.update(SqlStore.INSERT_EMPLOYEE, empId,
				employee.getFirstName(), employee.getLastName(), employee.getDateOfJoining(),
				employee.getDesignation());

		LOGGER.info("Employee created with ID :: {}, insertCount :: {}", empId, rowsUpdated);
		employee.setEmployeeId(empId);
		return employee;
	}
}
