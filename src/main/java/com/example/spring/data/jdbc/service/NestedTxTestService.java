/**
 * 
 */
package com.example.spring.data.jdbc.service;

import com.example.spring.data.jdbc.dto.Actor;

/**
 * @author amit
 *
 */
public interface NestedTxTestService {
	
	public Actor executeInNestedTx();

	public Actor executeInNestedTxWithError();
}
