package com.example.spring.data.jdbc;

import org.springframework.stereotype.Component;


/**
 * Bean to demonstrate project setup is correct.
 */
@Component
public class ExampleService implements Service {
	
	/**
	 * Reads next record from input
	 */
	public String getMessage() {
		return "Hello world!";	
	}

}
