package com.example.spring.data.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
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

	// JdbcTemplate is implementation of JdbcOperations, we should have dependency on interface always.
	private JdbcOperations jdbcTemplate; 
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	// Always prefer constructor/setter based injection, which helps in test case injection.
	@Autowired
	public EmployeeDaoImpl(JdbcOperations jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate; 
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

	@Override
	public Optional<Employee> get(String employeeId) {
		// NOTE: As JPA's find() method, it throws EmptyResultDataAccessException
		// exception of single record not found in database for provided query.
		// All exceptions under DataAccessException, are Runtime exception, we can chose
		// to whether or not catch and handle them if we required.
		// Here I need to return Empty Optional if record not found case, so explicitly
		// catching EmptyResultDataAccessException RuntimeException.
		try {
			Employee employee = jdbcTemplate.queryForObject(SqlStore.SELECT_EMPLOYEE_BY_ID, new Object[] { employeeId }, new EmployeeRowMapper());
			return Optional.of(employee);
		} catch (EmptyResultDataAccessException e) {
			// Nothing to handle here.
		}
		return Optional.empty();
	}
	
	@Override
	public List<Employee> findAll() {
		return jdbcTemplate.query(SqlStore.SELECT_EMPLOYEE_ALL, new EmployeeRowMapper());
	}
	
	private class EmployeeRowMapper implements RowMapper<Employee> {

		@Override
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setEmployeeId(rs.getString("employee_id"));
			employee.setFirstName(rs.getString("first_name"));
			employee.setLastName(rs.getString("last_name"));
			employee.setDesignation(rs.getString("designation"));
			employee.setDateOfJoining(new Date(rs.getDate("date_of_joining").getTime()));
			return employee;
		}
	}
	
	@Override
	public Employee update(Employee employee) {
		if (employee.getEmployeeId() == null) {
			throw new InsufficientDataException("Empty employeeId while updating employee.");
		}
		int rowsUpdated = jdbcTemplate.update(SqlStore.UPDTAE_EMPLOYEE_BY_ID, employee.getFirstName(),
				employee.getLastName(), employee.getDesignation(), employee.getEmployeeId());

		LOGGER.info("Updated with employee with ID :: {}, update Count :: {}", employee.getEmployeeId(), rowsUpdated);
		return employee;
	}

	@Override
	public void delete(Employee employee) {
		if (employee.getEmployeeId() == null) {
			throw new InsufficientDataException("Empty employeeId while updating employee.");
		}
		
		jdbcTemplate.update(SqlStore.DELETE_EMPLOYEE_BY_ID, employee.getEmployeeId());
	}

	@Override
	public List<Employee> findAllByDesignation(String designation) {
		// we need to build parameter map always, which I think verbose, and positional parameters does same job in single line.
		// Provide JPA stle named parameters in query. (:parameName)
		Map<String, String> namedParameters = new HashMap<String, String>(1);
		namedParameters.put("designation", designation);
		
		return namedParameterJdbcTemplate.query(SqlStore.SELECT_ALL_EMPLOYEE_BY_DESIGNATION, namedParameters, new EmployeeRowMapper());
	}

	@Override
	public List<Employee> addAll(List<Employee> employees) {
		if (employees == null) {
			throw new InsufficientDataException();
		}
		// generate IDs
		employees.stream().forEach(emp -> emp.setEmployeeId(UUID.randomUUID().toString()));
		
		// This JDBC api executes all statements into single batch, if we need multiple batch we, need call this method using custom batch size logic or 
		// spring provide another API which handles that, interface we can use is ParameterizedPreparedStatementSetter with batchUpdate() overloaded version.
		int[] rowsCreated = jdbcTemplate.batchUpdate(SqlStore.INSERT_EMPLOYEE, new BatchPreparedStatementSetter() {
			
			/**
			 * index starts at 0
			 */
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Employee employee = employees.get(i);
				ps.setString(1, employee.getEmployeeId());
				ps.setString(2, employee.getFirstName());
				ps.setString(3, employee.getLastName());
				ps.setDate(4, new java.sql.Date(employee.getDateOfJoining().getTime()));
				ps.setString(5, employee.getDesignation());
			}

			@Override
			public int getBatchSize() {
				return employees.size();
			}
		});
		LOGGER.info("Employees created in batch, cout :: {}", rowsCreated);
		return employees;
	}
}
