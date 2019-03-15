package com.example.spring.data.jdbc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExampleServiceTests extends BaseTest {

	@Autowired
	private Service service;
	
	@Test
	public void testReadOnce() throws Exception {
		assertEquals("Hello world!", service.getMessage());
	}

}
