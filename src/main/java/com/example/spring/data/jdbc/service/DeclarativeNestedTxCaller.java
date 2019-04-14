/**
 * 
 */
package com.example.spring.data.jdbc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring.data.jdbc.dao.ActorDao;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * This service class calls other service which executes in nested transaction.
 * 
 * @author amit
 *
 */
@Service
public class DeclarativeNestedTxCaller implements NestedTxServiceCaller {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeclarativeNestedTxCaller.class);
	private NestedTxTestService nestedTxService;
	
	private ActorDao actorDao;
	
	@Autowired
	public DeclarativeNestedTxCaller(NestedTxTestService nestedTxService, ActorDao actorDao) {
		this.actorDao = actorDao;
		this.nestedTxService = nestedTxService;
	}
	
	/**
	 * Calls service method of NestedTxTestService which executes in nested transaction and returns successfully.
	 * So, no transaction rollback.
	 */
	@Override
	@Transactional
	public List<Actor> invokeNestedTx() {
		List<Actor> createdActors = new ArrayList<>(2);
		
		// create actor in parent physical transaction
		Actor actor = new Actor();
		actor.setFirstName(UUID.randomUUID().toString());
		actor.setLastName(UUID.randomUUID().toString());
		Actor createdActor = actorDao.add(actor);

		// create actor in nested under same physical transaction but with savepoint to manage nested Tx.
		Actor actorCreatedInNestedTx = nestedTxService.executeInNestedTx();
		
		createdActors.add(createdActor);
		createdActors.add(actorCreatedInNestedTx);
		
		LOGGER.info("created :: {}, {}", createdActors.get(0).getId(), createdActors.get(1).getId());
		return createdActors;
	}

	/**
	 * Try rollback of nested transaction only without affecting outer physical transaction.
	 * 
	 * This method runs in transaction and creates physical transaction, even though nested transaction in nestedTxService.executeInNestedTxWithError()
	 * is marked to be rollback, this physical transaction is not affected, only changes in nested transaction (after savepoint) will be rollback
	 * and changes made in this method does not get affected.
	 */
	@Override
	@Transactional
	public List<Actor> invokeNestedTxWithError() {
		List<Actor> createdActors = new ArrayList<>(2);

		// create actor 
		Actor actor = new Actor();
		actor.setFirstName(UUID.randomUUID().toString());
		actor.setLastName(UUID.randomUUID().toString());
		Actor createdActor = actorDao.add(actor);

		// create actor in nested tx.
		Actor actorCreatedInNestedTx = nestedTxService.executeInNestedTxWithError();
		
		createdActors.add(createdActor);
		createdActors.add(actorCreatedInNestedTx);
		return createdActors;
	}

}
