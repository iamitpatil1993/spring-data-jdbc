/**
 * 
 */
package com.example.spring.data.jdbc.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dao.ActorDao;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DeclarativeNestedTxCallerTest extends BaseTest {

	@Autowired
	private NestedTxServiceCaller nestedTxServiceCaller;

	@Autowired
	private ActorDao actorDao;

	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.DeclarativeNestedTxCaller#invokeNestedTx()}.
	 */
	@Test
	public void testInvokeNestedTx() {
		List<Actor> createdActors = nestedTxServiceCaller.invokeNestedTx();

		assertNotNull(createdActors);
		assertFalse(createdActors.isEmpty());
		List<Actor> actors = actorDao.findAll();
		createdActors.parallelStream().forEach(actor -> {
			assertNotNull(actor.getId());
			assertTrue(actors.contains(actor));
		});
	}
	
	/**
	 * Test method for
	 * {@link com.example.spring.data.jdbc.service.DeclarativeNestedTxCaller#invokeNestedTxWithError()}.
	 */
	@Test
	public void testInvokeNestedTxWithError() {
		List<Actor> createdActors = nestedTxServiceCaller.invokeNestedTxWithError();

		assertNotNull(createdActors);
		assertFalse(createdActors.isEmpty());
		List<Actor> actors = actorDao.findAll();
		
		assertTrue(actors.contains(createdActors.get(0))); // assert that rollback of nested transaction had no impact on outer transaction [physical].
		assertFalse(actors.contains(createdActors.get(1))); // assert that nested  transaction was rollback
	}
}
