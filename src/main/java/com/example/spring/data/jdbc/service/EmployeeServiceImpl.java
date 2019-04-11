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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dao.EmployeeDao;
import com.example.spring.data.jdbc.dao.InsufficientDataException;
import com.example.spring.data.jdbc.dao.InvalidDataException;
import com.example.spring.data.jdbc.dao.custom.transactionattributes.EmployeeTx;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */

@Service
@Qualifier("declarativeTx")
// Service annotation is just specialization of @Component annotation and does
// not adds anything special. We can replace it with @Component annotation and
// can have same effect. It's only for naming purpose
public class EmployeeServiceImpl extends AbstractEmployeeServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	private EmployeeDao employeeDao;

	private AddressDao addressDao;

	/**
	 * Always prefer constructor/setter based injection
	 * 
	 * @param employeeDao
	 * @param addressDao
	 */
	@Autowired
	public EmployeeServiceImpl(EmployeeDao employeeDao, AddressDao addressDao) {
		this.employeeDao = employeeDao;
		this.addressDao = addressDao;
	}

	@Override
	@EmployeeTx // transaction with default TransactionDefinition
																	// details.
																	// Defined Rollback rule to rollback transaction
																	// when CHECKED exception
																	// InvalidDataException occurs.
	public void registerEmployee(Employee employee, Address address) throws InvalidDataException {

		if (!isValidEmployee(employee)) {
			throw new InvalidDataException("Invalid employee details");
		}
		// create employee
		// calling DAO/repository for database operations, it will automatically
		// participate in transaction created by this service even though
		// DAOs/Respositories are
		// not annoted with @transactional annotation, this is because, this transaction
		// get attached/tied to current thread of execution, and
		// JdbcTemplate/NamedparameterJdbcTemplate
		// used by DAOs uses same transaction and jdbc connection because they make ue
		// of DataSourceUtils to get connection and DataSourceUtils handle thread level
		// transaction and
		// returns same if exists.
		Employee employeeWithPk = employeeDao.add(employee);

		// create address
		address.setEmployeeId(employeeWithPk.getEmployeeId());
		
		// Intentionally added this check here, instead at the start of method to just try Rollback Rules defined for InvalidDataException.
		if (!isValidAddress(address)) {
			throw new InvalidDataException("Invalid address detals for employee with ID :: " + employeeWithPk.getEmployeeId());
			/*
			 * This is how we can programmatically rollback transaction.
			 * 
			 * TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			 * return;
			 */
		}
		Address addressWithPk = addressDao.add(address);
		address.setId(addressWithPk.getId());
		employee.setEmployeeId(employeeWithPk.getEmployeeId());
		LOGGER.info("Employee and address created with employeeId :: {}, addressId :: {}",
				employeeWithPk.getEmployeeId(), addressWithPk.getId());
	}

	@Override
	@Transactional(readOnly = true) // declare transaction to be read-only, throws exception if we try to perform write operation on database.
	public Optional<Employee> findEmployeeById(String employeeId) {
		if (employeeId == null) {
			throw new InsufficientDataException("Empty employeeId while getting employee");
		}
		
		Optional<Employee> employee = employeeDao.get(employeeId);
		
		if (employee.isPresent()) {
			List<Address> addresses = addressDao.findBymployeeId(employee.get().getEmployeeId());
			employee.get().setAddress(addresses);
		}
		return employee;
	}
	
	/**
	 * Intentionally not adding @Transaactinal annotation here, rather calling private method which is annoted with @Transaactinal
	 * annotation, this method does not starts transaction, so no transaction context exists
	 */
	@Override
	public void remove(String employeeId) {
		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		
		employeeDao.delete(employee);
		
		// calling private method, which is annoted with @Transaactinal annotation.
		this.removeAddressByEmployee(employeeId);
	}
	
	/**
	 * Since 
	 * 
	 * 1. this method is called from local scope i.e it is called from this class and not externally,
	 * 2. Also this method is private
	 * 
	 * This method can not start new transaction [but can participate in existing one if exist], so it will thow 
	 * NoTransactionException if we try to access transaction.
	 * @param employeeId
	 */
	@EmployeeTx
	private void removeAddressByEmployee(final String employeeId) {
		// intentionally trying to access transaction, since transaction not exists [since this method could not create new one]
		// it will throw exception.
		// NOTE: This will not throw exception and will return transaction if transaction already exists and method called from 
		// already running transaction context [no matter called from locally or externally].
		// NOTE: Even if we change scope of this method to public, still it won't create new transaction, this method
		// must be called from external in order to intercept, and start transaction, locally called methods are not
		// get intercepted [no matter they are public/protected/default], so they can not start new transaction, but can
		// participate in existing one if already exist at thread leve [Due to magic of DataSourceUtils used under the hoods by
		// jdbcTemplate jdbc spring abstraction].
		TransactionAspectSupport.currentTransactionStatus().hashCode();
		addressDao.removeByEmployeeId(employeeId);
	}
}
