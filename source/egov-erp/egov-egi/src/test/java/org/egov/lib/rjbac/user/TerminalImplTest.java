package org.egov.lib.rjbac.user;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TerminalImplTest {

	@Test
	public void testColsuresTerminal() {
		final TerminalImpl terminal = new TerminalImpl();
		terminal.setId(1);
		terminal.setIpAddress("some");
		terminal.setTerminalDesc("desc");
		terminal.setTerminalName("name");
		assertEquals(Integer.valueOf(1), terminal.getId());
		assertEquals("some", terminal.getIpAddress());
		assertEquals("desc", terminal.getTerminalDesc());
		assertEquals("name", terminal.getTerminalName());
	}

}
