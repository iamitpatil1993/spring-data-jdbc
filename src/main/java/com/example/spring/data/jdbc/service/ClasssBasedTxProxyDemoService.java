/**
 * 
 */
package com.example.spring.data.jdbc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.spring.data.jdbc.dao.ActorDao;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * This service class demostrates that
 * 
 * <p>
 * 1. If class do not implements any interface then, spring by default
 * (automatically) switches from default JDK based (interface based) proxy to
 * class-based proxy using CGLIB.
 * 2. Spring creates subclass of this class.
 * </p>
 * 
 * <p>
 * This class do not implements any interface, so spring automatically uses
 * class-based proxy (CGLIB) for this class.
 * </p>
 * 
 * <p>
 * Note: We do not need to add any maven dependency in pom for CGLIB, it is by
 * default available in spring core maven dependency. (core dependencies
 * transitively depends on CGLIB)
 * 
 * 
 * @author amit
 *
 */

@Service
public class ClasssBasedTxProxyDemoService {
	
	private ActorDao actorDao;
	
	public ClasssBasedTxProxyDemoService(ActorDao actorDao) {
		this.actorDao = actorDao;
	}

	@Transactional
	public Actor createActor(Actor actor) {
		assert TransactionAspectSupport.currentTransactionStatus().isNewTransaction();
		
		Actor createdActorWithPk = actorDao.add(actor);
		
		return createdActorWithPk;
	}
}
