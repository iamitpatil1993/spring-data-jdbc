/**
 * 
 */
package com.example.spring.data.jdbc.dao;

/**
 * @author amipatil
 *
 */
public class InsufficientDataException extends RuntimeException {

	private static final long serialVersionUID = 8304452313858414504L;

	public InsufficientDataException() {
		// Nothing to do here
	}
	
	public InsufficientDataException(String message) {
		super(message);
	}
}
