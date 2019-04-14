/**
 * 
 */
package com.example.spring.data.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionOperations;

import com.example.spring.data.jdbc.dao.AddressDao;
import com.example.spring.data.jdbc.dto.Address;

/**
 * @author amit
 *
 */

@Service
public class ProgrammaticTxAddressService implements AddressService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammaticTxAddressService.class);

	private AddressDao addressDao;

	private TransactionOperations transactionOperations;

	@Autowired
	public ProgrammaticTxAddressService(AddressDao addressDao, TransactionOperations transactionOperations) {
		this.addressDao = addressDao;
		this.transactionOperations = transactionOperations;
	}

	/**
	 * If this method is called from external and if transaction already exists (programmatic) then transactionOperations.execute()
	 * does not start new transaction, rather continues to execute in existing transaction.
	 * 
	 * So, programmatic transactions also get propagated without any specific configurations.
	 */
	@Override
	public Address create(Address address) {

		// this does not start new transaction if transaction already exists at thread level.
		return transactionOperations.execute(transactionStatus -> {
			return addressDao.add(address);
		});
	}
}
