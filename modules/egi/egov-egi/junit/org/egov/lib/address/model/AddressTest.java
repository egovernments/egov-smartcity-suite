package org.egov.lib.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class AddressTest {
	private Address address;

	@Before
	public void runBefore() {
		this.address = new Address();
	}

	@Test
	public void testColusures() {
		this.address.setAddressID(1);
		assertEquals(Integer.valueOf(1), this.address.getAddressID());
		final AddressTypeMaster addresstYpe = new AddressTypeMaster();
		this.address.setAddTypeMaster(addresstYpe);
		assertEquals(addresstYpe, this.address.getAddTypeMaster());
		this.address.setBlock("block");
		assertEquals("block", this.address.getBlock());
		this.address.setBlockLocal("blockl");
		assertEquals("blockl", this.address.getBlockLocal());
		this.address.setCityTownVillage("cityTownVillage");
		assertEquals("cityTownVillage", this.address.getCityTownVillage());
		this.address.setCityTownVillageLocal("cityTownVillagel");
		assertEquals("cityTownVillagel", this.address.getCityTownVillageLocal());
		this.address.setDistrict("district");
		assertEquals("district", this.address.getDistrict());
		this.address.setDistrictLocal("districtl");
		assertEquals("districtl", this.address.getDistrictLocal());
		this.address.setHouseNo("houseNo");
		assertEquals("houseNo", this.address.getHouseNo());
		this.address.setLastUpdatedTimeStamp(new Date());
		assertNotNull(this.address.getLastUpdatedTimeStamp());
		this.address.setLocality("locality");
		assertEquals("locality", this.address.getLocality());
		this.address.setLocalityLocal("localityl");
		assertEquals("localityl", this.address.getLocalityLocal());
		this.address.setPinCode(1);
		assertEquals(Integer.valueOf(1), this.address.getPinCode());
		this.address.setState("state");
		assertEquals("state", this.address.getState());
		this.address.setStateLocal("statel");
		assertEquals("statel", this.address.getStateLocal());
		this.address.setStreetAddress1("streetAddress1");
		assertEquals("streetAddress1", this.address.getStreetAddress1());
		this.address.setStreetAddress1Local("streetAddress1l");
		assertEquals("streetAddress1l", this.address.getStreetAddress1Local());
		this.address.setStreetAddress2("streetAddress2");
		assertEquals("streetAddress2", this.address.getStreetAddress2());
		this.address.setStreetAddress2Local("streetAddress2l");
		assertEquals("streetAddress2l", this.address.getStreetAddress2Local());
		this.address.setTaluk("taluk");
		assertEquals("taluk", this.address.getTaluk());
		this.address.setTalukLocal("talukl");
		assertEquals("talukl", this.address.getTalukLocal());
	}

	@Test
	public void testEqualAndHashcode() {
		this.address.setAddressID(1);
		assertTrue(this.address.equals(this.address));
		assertFalse(this.address.equals(null));
		assertFalse(this.address.equals(new Object()));
		Address add = new Address();
		add.setAddressID(2);
		assertFalse(this.address.equals(add));
		add.setAddressID(1);
		assertTrue(this.address.equals(add));
		add.setAddressID(null);
		assertFalse(this.address.equals(add));
		this.address.hashCode();
		assertTrue(this.address.validate());
		add = new Address(null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null);
		assertNotNull(add);
	}
}
