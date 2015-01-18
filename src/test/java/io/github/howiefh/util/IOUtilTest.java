package io.github.howiefh.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOUtilTest {

	@Test
	public void test() {
		assertNotEquals("not equal",null , IOUtil.getResource("log4j2.xml"));
	}

}
