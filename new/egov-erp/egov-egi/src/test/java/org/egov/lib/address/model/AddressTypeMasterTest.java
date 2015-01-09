package org.egov.lib.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

public class AddressTypeMasterTest {
	private final AddressTypeMaster addressType = new AddressTypeMaster();

	@Test
	public void testGetAddressTypeID() {
		this.addressType.setAddressTypeID(1);
		assertEquals(Integer.valueOf(1), this.addressType.getAddressTypeID());
		this.addressType.setAddressTypeName("addressTypeName");
		assertEquals("addressTypeName", this.addressType.getAddressTypeName());
		this.addressType.setAddressTypeNameLocal("addressTypeNamel");
		assertEquals("addressTypeNamel",
				this.addressType.getAddressTypeNameLocal());
		this.addressType.setUpdatedTimestamp(new Date());
		assertNotNull(this.addressType.getUpdatedTimestamp());
	}

}
