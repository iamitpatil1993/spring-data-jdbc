package com.example.spring.data.jdbc.service;

import java.util.List;

import com.example.spring.data.jdbc.dto.Actor;

public interface NestedTxServiceCaller {
	
	public List<Actor> invokeNestedTx();
	
	public List<Actor> invokeNestedTxWithError();

}
