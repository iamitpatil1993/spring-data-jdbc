/**
 * 
 */
package com.example.spring.data.jdbc.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dao.EmployeeDao;
import com.example.spring.data.jdbc.dao.InsufficientDataException;
import com.example.spring.data.jdbc.dao.InvalidDataException;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * Implements EmployeeService functionality using programmatic transaction using
 * TransactionTemplate.
 * 
 * @author amit
 *
 */
@Service
@Qualifier("programmaticTx")
public class ProgrammaticTxEmployeeServiceImpl extends AbstractEmployeeServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammaticTxEmployeeServiceImpl.class);

	private TransactionOperations transactionOperations;

	private PlatformTransactionManager transactionManager;
	
	private EmployeeDao employeeDao;

	private AddressDao addressDao;

	@Autowired
	public ProgrammaticTxEmployeeServiceImpl(TransactionOperations transactionOperations, EmployeeDao employeeDao,
			AddressDao addressDao, PlatformTransactionManager transactionManager) {
		this.transactionOperations = transactionOperations;
		this.employeeDao = employeeDao;
		this.addressDao = addressDao;
		this.transactionManager = transactionManager;
	}

	/**
	 * uses TransactionCallbackWithoutResult implementation of TransactionCallback
	 * for transactional callbacks that do not have anything to return as a result
	 * of transaction.
	 * 
	 */
	@Override
	public void registerEmployee(Employee employee, Address address) {

		transactionOperations.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				if (!isValidEmployee(employee)) {
					LOGGER.error("Invalid employee details, {}", new InvalidDataException("Invalid employee details"));
					status.setRollbackOnly();
					return;
				}
				Employee employeeWithPk = employeeDao.add(employee);

				// create address
				address.setEmployeeId(employeeWithPk.getEmployeeId());

				// Intentionally added this check here, instead at the start of method to just
				// try Rollback Rules defined for InvalidDataException.
				if (!isValidAddress(address)) {
					LOGGER.error("Invalid address detals, :: {}", new InvalidDataException(
							"Invalid address detals for employee with ID :: " + employeeWithPk.getEmployeeId()));
					status.setRollbackOnly();
					return;
				}
				Address addressWithPk = addressDao.add(address);
				address.setId(addressWithPk.getId());
				employee.setEmployeeId(employeeWithPk.getEmployeeId());

				LOGGER.info("Employee and address created with employeeId :: {}, addressId :: {}",
						employeeWithPk.getEmployeeId(), addressWithPk.getId());
			}
		});
	}

	/**
	 * Creates TransactionTemplate at runtime, since it want different configuration that one we defined in application context.
	 * We can define TransactionTemplates at runtime with required configurations.
	 */
	@Override
	public Optional<Employee> findEmployeeById(String employeeId) {

		// create TransactionTemplate with custom configurations [Read-Only transaction]
		TransactionTemplate readOnlyTransactionTemplate = new TransactionTemplate(transactionManager);
		readOnlyTransactionTemplate.setReadOnly(true);
		
		return readOnlyTransactionTemplate.execute((TransactionStatus status) -> {
			if (employeeId == null) {
				throw new InsufficientDataException("Empty employeeId while getting employee");
			}

			Optional<Employee> employee = employeeDao.get(employeeId);

			if (employee.isPresent()) {
				List<Address> addresses = addressDao.findBymployeeId(employee.get().getEmployeeId());
				employee.get().setAddress(addresses);
			}
			return employee;
		});
	}

	@Override
	public void remove(String employeeId) {

		transactionOperations.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				if (employeeId == null) {
					throw new InsufficientDataException("Empty/Null employeeId while removing accountId");
				}
				// remove employee
				Employee employee = new Employee();
				employee.setEmployeeId(employeeId);

				// Even DAOs and all other executions (which uses DataSourceUtils) will
				// automatically participate in current transaction.
				employeeDao.delete(employee);

				// remove employee addresses
				removeEmployeeAddress(employeeId);
			}
		});
	}

	/**
	 * This participates in current programmatic transaction, since transaction is
	 * attached to current thread of execution and spring transaction APIs like
	 * transactionTemplate uses DataSourceUtils to get connection and
	 * DataSourceUtils does all the magic to return correct connection.
	 * 
	 * @param employeeId
	 */
	private void removeEmployeeAddress(String employeeId) {
		addressDao.removeByEmployeeId(employeeId);
	}
}
