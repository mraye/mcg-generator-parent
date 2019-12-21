package com.github.vspro.cg.util;

import org.junit.Test;

public class MessagesTest {


	@Test
	public void getStringTest(){
		System.out.println(Messages.getString("ValidationError.4"));
		System.out.println(Messages.getString("ValidationError.0", "hello"));
	}
}
