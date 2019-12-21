package com.github.vspro.cg.exception;

import org.junit.Test;

public class InvalidExceptionTest {


	@Test
	public void exceptionTest() throws InvalidException {

		System.out.println("aa");
		throw new InvalidException("21");
	}

}
