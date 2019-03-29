/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.data.jdbc.BaseTest;
import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcActorDaoTest extends BaseTest {

	@Autowired
	private ActorDao actorDao;
	
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
	}
}
