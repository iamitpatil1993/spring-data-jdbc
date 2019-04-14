/**
 * 
 */
package com.example.spring.data.jdbc.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
public class ClasssBasedTxProxyDemoServiceTest  extends BaseTest {

	/**
	 * Note: we are able to inject this class as a concrete type only because proxy created was class-based.
	 * If ClasssBasedTxProxyDemoService implements any interface, then we couldn't inject class 
	 * ClasssBasedTxProxyDemoService as a concrete type instead and would get error as class was 
	 * type expected was ClasssBasedTxProxyDemoService by was of type proxy, so in that case, we need to inject it via interface.
	 * 
	 * 
	 * 
	 */
	@Autowired
	private ClasssBasedTxProxyDemoService classsBasedTxProxyDemoServiceTest;
	
	@Autowired
	private ActorDao actorDao;
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.service.ClasssBasedTxProxyDemoService#createActor(com.example.spring.data.jdbc.dto.Actor)}.
	 */
	@Test
	public void testCreateActor() {
		/*// given
		Actor actor = new Actor();
		actor.setFirstName(UUID.randomUUID().toString());
		actor.setLastName(UUID.randomUUID().toString());
		
		// when
		Actor createdActorWithPk = classsBasedTxProxyDemoServiceTest.createActor(actor);
		
		// then
		assertNotNull(createdActorWithPk);
		assertNotNull(createdActorWithPk.getId());
		
		List<Actor> actors = actorDao.findAll();
		Optional<Actor> result = actors.stream().filter(tempActor -> actor.getId().equals(tempActor.getId())).findFirst();
		assertTrue(result.isPresent());*/
	}

}
