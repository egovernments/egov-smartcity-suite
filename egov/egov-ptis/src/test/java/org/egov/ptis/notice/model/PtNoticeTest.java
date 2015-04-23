package org.egov.ptis.notice.model;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.egov.ptis.notice.PtNotice;
import org.junit.Before;
import org.junit.Test;

public class PtNoticeTest extends AbstractPersistenceServiceTest<PtNotice,Long> {
	private NMCObjectFactory objectFactory;
	
	public PtNoticeTest(){
	}
	
	@Before
	public void setUp() {
		objectFactory = new NMCObjectFactory(session,service);
	}
	
	@Test
	public void testCreatenoticeForm()
	{
		assertTrue(true);
		Date date = new Date();
		// resolve after persisiting objects in nmcobjectfactory
		/*PtNotice saved = (PtNotice) service.create( objectFactory.createNotice());
		PtNotice retrieved = (PtNotice) service.findById(Long.valueOf(saved.getId()), false);
		assertEquals(saved,retrieved);*/
	}
	
	@Test
	public void testGetNoticeByNoticeNo()
	{
		//PtNotice actual = objectFactory.createNotice();
		//PtNotice expected=service.findByNamedQuery(NMCPTISConstants.QUERY_NOTICE_BY_NOTICENO, actual.getNoticeNo());
		//assertEquals(expected, actual);
	}

}
