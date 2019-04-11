package com.example.spring.data.jdbc.service;

import com.example.spring.data.jdbc.dto.Address;
import com.example.spring.data.jdbc.dto.Employee;

public abstract class AbstractEmployeeServiceImpl implements EmployeeService {

	protected boolean isValidAddress(Address address) {
		return address.getCity() != null && address.getState() != null && address.getCountry() != null && address.getZipcode() != null;
	}

	protected boolean isValidEmployee(Employee employee) {
		return employee.getFirstName() != null && employee.getLastName() != null;
	}
}
