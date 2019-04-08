/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dto.Address;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder()
public class JdbcAddressDaoTest extends BaseTest {

	@Autowired
	private AddressDao addressDao;
	
	private static String addressId;
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcAddressDao#add(com.example.spring.data.jdbc.dto.Address)}.
	 */
	@Test
	public void testAdd() {
		// given
		String employeeId = UUID.randomUUID().toString();
		
		Address address = new Address();
		address.setAddress("foo address");
		address.setCity("Foo City");
		address.setCountry("Foo Country");
		address.setEmployeeId(employeeId);
		address.setLocality("Foo locality");
		address.setRegion("Foo Region");
		address.setState("Foo state");
		address.setZipcode("Foo zipcode");
		
		// when
		Address addressWithPk = addressDao.add(address);
		
		// then
		assertNotNull(addressWithPk);
		assertNotNull(addressWithPk.getId());
		addressId = addressWithPk.getId();
	}
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcAddressDao#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		// when
		Optional<Address> addressWithPk = addressDao.get(addressId);
		
		// then
		assertNotNull(addressWithPk);
		assertTrue(addressWithPk.isPresent());
	}
}
