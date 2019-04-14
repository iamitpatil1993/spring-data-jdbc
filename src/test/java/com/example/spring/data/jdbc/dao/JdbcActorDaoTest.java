/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcActorDaoTest extends BaseTest {

	@Autowired
	private ActorDao actorDao;
	private static Long actorId = null;
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcActorDao#add(com.example.spring.data.jdbc.dto.Actor)}.
	 */
	@Test
	public void testAdd() {
		// given
		Actor actor = new Actor();
		actor.setFirstName("Foo");
		actor.setLastName("Bar");
		
		// when
		Actor actorWithGeneratedPk = actorDao.add(actor);
		
		assertNotNull(actorWithGeneratedPk);
		assertNotNull(actorWithGeneratedPk.getId());
		
		actorId = actorWithGeneratedPk.getId();
	}
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcActorDao#findAll()}.
	 */
	@Test
	public void testFindAll() {
		// when
		List<Actor> actors = actorDao.findAll();
		
		assertNotNull(actors);
		assertTrue(actors.size() > 0);
	}
	
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcActorDao#update(com.example.spring.data.jdbc.dto.Actor)}.
	 */
	@Test
	public void testUpdate() {
		// given
		Actor actor = new Actor();
		actor.setFirstName("Bar");
		actor.setLastName("Foo");
		actor.setId(actorId);
		System.out.println("actorId :: " + actorId);
		
		// when
		actorDao.update(actor);
		
		// then
		Optional<Actor> updatedActor = actorDao.findAll().stream().filter(tempActor -> tempActor.getId().equals(actor.getId())).findFirst();
		assertTrue(updatedActor.isPresent());
		assertEquals(actor.getFirstName(), updatedActor.get().getFirstName());
		assertEquals(actor.getLastName(), updatedActor.get().getLastName());
	}
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcActorDao#findAllByFirstNames(java.util.List<java.lang.String>)}.
	 */
	@Test
	public void testFindAllByFirstNamesWithNotRecordsFound() {
		// given
		List<String> firstNames = new ArrayList<>(5);
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());
		
		// when
		List<Actor> actors = actorDao.findAllByFirstNames(firstNames);
		
		assertNotNull(actors);
		assertTrue(actors.isEmpty());
	}
	
	/**
	 * Test method for {@link com.example.spring.data.jdbc.dao.JdbcActorDao#findAllByFirstNames(java.util.List<java.lang.String>)}.
	 */
	@Test
	public void testFindAllByFirstNamesWithFound() {
		// given
		List<String> firstNames = new ArrayList<>(4);
		firstNames.add("Foo");
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());
		firstNames.add(UUID.randomUUID().toString());

		// when
		List<Actor> actors = actorDao.findAllByFirstNames(firstNames);
		
		assertNotNull(actors);
		assertTrue(!actors.isEmpty());
		assertEquals(1, actors.size());
	}
}
