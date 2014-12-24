package org.egov.lib.rjbac.jurisdiction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.admbndry.HeirarchyTypeImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.junit.Before;
import org.junit.Test;

public class JurisdictionTest {

	private Jurisdiction jurisdiction;

	@Before
	public void runBefore() {
		this.jurisdiction = new Jurisdiction();
	}

	@Test
	public void testClosures() {
		this.jurisdiction.setId(1);
		assertEquals(Integer.valueOf(1), this.jurisdiction.getId());
		final BoundaryType bt = new BoundaryTypeImpl();
		bt.setHeirarchy((short) 1);
		HeirarchyTypeImpl ht = new HeirarchyTypeImpl();
		ht.setName("name");
		bt.setHeirarchyType(ht);
		bt.setName("myname");
		this.jurisdiction.setJurisdictionLevel(bt);
		assertNotNull(this.jurisdiction.getJurisdictionLevel());
		this.jurisdiction.setJurisdictionValues(new HashSet());
		assertNotNull(this.jurisdiction.getJurisdictionValues());
		final JurisdictionValues values = new JurisdictionValues();
		this.jurisdiction.addJurisdictionValue(values);
		this.jurisdiction.removeJurisdictionValue(values);
		assertEquals(0, this.jurisdiction.getJurisdictionValues().size());
		this.jurisdiction.setUpdateTime(new Date());
		assertNotNull(this.jurisdiction.getUpdateTime());
		final User user = new UserImpl();
		user.setUserName("userName");
		this.jurisdiction.setUser(user);
		assertNotNull(this.jurisdiction.getUser());

		// Test equals and HashCode
		assertFalse(this.jurisdiction.equals(null));
		assertTrue(this.jurisdiction.equals(this.jurisdiction));
		assertFalse(this.jurisdiction.equals(new Object()));
		final Jurisdiction juri = new Jurisdiction();
		juri.setJurisdictionLevel(this.jurisdiction.getJurisdictionLevel());
		final User user2 = new UserImpl();
		user2.setUserName("name");
		juri.setUser(user2);
		assertFalse(this.jurisdiction.equals(juri));
		juri.setUser(this.jurisdiction.getUser());
		assertTrue(this.jurisdiction.equals(juri));
		final BoundaryTypeImpl bt2 = new BoundaryTypeImpl();
		bt2.setHeirarchy((short) 1);
		ht = new HeirarchyTypeImpl();
		ht.setName("name");
		bt2.setHeirarchyType(ht);
		bt2.setName("mname");
		juri.setJurisdictionLevel(bt2);
		assertFalse(this.jurisdiction.equals(juri));
		this.jurisdiction.hashCode();

	}

	@Test
	public void testValidate() {
		final BoundaryTypeImpl bt = new BoundaryTypeImpl();
		bt.setHeirarchy((short) 1);
		bt.setName("name");
		final HeirarchyTypeImpl ht = new HeirarchyTypeImpl();
		ht.setName("myname");
		bt.setHeirarchyType(ht);
		final BoundaryTypeImpl bt2 = new BoundaryTypeImpl();
		bt2.setHeirarchy((short) 1);
		bt2.setName("nsame");
		bt2.setHeirarchyType(ht);
		this.jurisdiction.setJurisdictionLevel(bt2);

		final Set bndry = new HashSet();
		final JurisdictionValues value = new JurisdictionValues();
		final BoundaryImpl bnd = new BoundaryImpl();
		value.setBoundary(bnd);
		bnd.setBoundaryType(bt);
		bndry.add(value);
		this.jurisdiction.setJurisdictionValues(bndry);

		try {
			this.jurisdiction.validate();
		} catch (final Exception e) {
			assertTrue(true);
		}
		this.jurisdiction.setJurisdictionValues(new HashSet());
		assertTrue(this.jurisdiction.validate());
	}

}
