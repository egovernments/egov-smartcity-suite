package org.egov.infstr.models;

import junit.framework.TestCase;

import org.egov.infstr.annotation.Introspection;
import org.junit.Test;

public class BaseModelTest extends TestCase {
	@Test
	public void testToString() {
		final BaseModel baseModel = new BaseModel();
		assertEquals("{ class : BaseModel  }", baseModel.toString());
		final XYZ xyz = new XYZ();
		assertEquals("{ class : XYZ, myValue : MYVAL  }", xyz.toString());

	}

	class XYZ extends BaseModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Introspection("")
		private final String myValue = "MYVAL";
	}
}
