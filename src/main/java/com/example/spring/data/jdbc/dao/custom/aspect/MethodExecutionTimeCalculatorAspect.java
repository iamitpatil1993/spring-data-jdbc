/**
 * 
 */
package com.example.spring.data.jdbc.dao.custom.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * This aspect calculates time taken by each service method called externally.
 * It will also include time required for transaction aspect initialization code, since this 
 * aspect executes before transaction aspect.
 * 
 * @author amit
 *
 */

@Component
@Aspect
//@Order(2) // we can define ordering of aspects as we want
public class MethodExecutionTimeCalculatorAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodExecutionTimeCalculatorAspect.class);

	@Pointcut("execution(* com.example.spring.data.jdbc.service.*Service.*(..))") // pointcut expression for exact match to
	public void executionTimeCalculatorAspectPointcut() {
		// This should be empty. Having code here won't matter, but it won't be of any  use.
	}
	
	@Around("executionTimeCalculatorAspectPointcut()")
	public Object aroundServiceMethodInvocation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		LOGGER.info("starting stop watch for :: {}", proceedingJoinPoint.getSignature());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// calling upstream.
		Object returnvalue = proceedingJoinPoint.proceed();
		
		stopWatch.stop();
		LOGGER.info("Time taken by {} is :: {} miliseconds", proceedingJoinPoint.getSignature(), stopWatch.getTotalTimeMillis());
		
		// [Important ]we need to catch and return whatever uostream returns, otherwise return value won't be returned to called, it will get lost.
		return returnvalue;
	}
}
