/**
 * 
 */
package com.example.spring.data.jdbc.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.spring.data.jdbc.dao.ActorDao;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
@Service
public class DeclarativeNestedTxTestService implements NestedTxTestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeclarativeNestedTxTestService.class);

	private ActorDao actorDao;

	@Autowired
	public DeclarativeNestedTxTestService(ActorDao actorDao) {
		this.actorDao = actorDao;
	}

	/**
	 * Executes in NESTED transaction, it creates savepoint to manage nested
	 * transaction.
	 */
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public Actor executeInNestedTx() {
		// assert that Nested transaction under existing physical transaction, it
		// creates save point to manage nested transaction operations.
		assert TransactionAspectSupport.currentTransactionStatus().hasSavepoint(); // throws AssertionError if savepoint
		// not exists, but in this case
		// savepoint exists.

		// create actor
		Actor actor = new Actor();
		actor.setFirstName(UUID.randomUUID().toString());
		actor.setLastName(UUID.randomUUID().toString());

		return actorDao.add(actor);
	}

	/**
	 * Executes in transaction, but this time, transaction is marked to be rollback,
	 * so spring rollbacks only chnages in this method i.e after savepoint and does
	 * not affect parent or calling transaction.
	 * 
	 * So, actor created in this method won't persist and commit in database,.
	 */
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public Actor executeInNestedTxWithError() {
		LOGGER.info("is savepoint exists :: {}", TransactionAspectSupport.currentTransactionStatus().hasSavepoint());
		assert TransactionAspectSupport.currentTransactionStatus().hasSavepoint();

		// create actor
		Actor actor = new Actor();
		actor.setFirstName(UUID.randomUUID().toString());
		actor.setLastName(UUID.randomUUID().toString());

		Actor createdActor = actorDao.add(actor);

		// this causes rollback to savepoint and discarding all changes after savepoint.
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		return createdActor;
	}
}
