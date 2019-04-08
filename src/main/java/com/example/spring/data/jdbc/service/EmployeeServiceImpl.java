/**
 * 
 */
package com.example.spring.data.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dao.EmployeeDao;
import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

/**
 * @author amit
 *
 */

@Service // Service annotation is just specialization of @Component annotation and does
// not adds anything special. We can replace it with @Component annotation and
// can have same effect. It's only for naming purpose
public class EmployeeServiceImpl implements EmployeeService {

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
	@Transactional // transaction with default TransactionDefinition details.
	public void registerEmployee(Employee employee, Address address) {

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
		Address addressWithPk = addressDao.add(address);

		address.setId(addressWithPk.getId());
		employee.setEmployeeId(employeeWithPk.getEmployeeId());
		LOGGER.info("Employee and address created with employeeId :: {}, addressId :: {}",
				employeeWithPk.getEmployeeId(), addressWithPk.getId());
	}

}
