package org.egov.infstr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testEscapeSpecialChars() {
		final StringUtils strUtil = new StringUtils();
		assertNotNull(strUtil);
		assertEquals("<br/>\\'sdd\\'",
				StringUtils.escapeSpecialChars("\r\n'sdd'"));
	}
}
