/**
 * 
 */
package com.example.spring.data.jdbc.dao;

/**
 * @author amit
 *
 */
public class InvalidDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7425066600635518808L;
	
	public InvalidDataException() {
	}

	public InvalidDataException(String message) {
		super(message);
	}
}
