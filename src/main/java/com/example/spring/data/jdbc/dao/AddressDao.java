/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import java.util.List;
import java.util.Optional;

import com.example.spring.data.jdbc.dto.Address;

/**
 * @author amit
 *
 */
public interface AddressDao {
	
	public Address add(final Address address);
	
	public Optional<Address> get(final String addressId);
	
	public List<Address> findBymployeeId(final String employeeId);
	
	public void removeByEmployeeId(final String employeeId);

}
