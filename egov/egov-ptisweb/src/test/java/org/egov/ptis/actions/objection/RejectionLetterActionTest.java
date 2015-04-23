/**
 * 
 */
package org.egov.ptis.actions.objection;


import java.util.Date;

import ognl.Ognl;

import org.apache.struts2.ServletActionContext;
import org.egov.demand.model.DepreciationMaster;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author manoranjan
 *
 */
public class RejectionLetterActionTest extends AbstractPersistenceServiceTest{

	private RejectionLetterAction rejectionLetterAction;
	private NMCObjectFactory objectFactory;
	private PersistenceService persistenceService;
	private ReportService reportService = null;
	private ReportOutput reportOutput;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		objectFactory = new NMCObjectFactory(session, service);
		EGOVThreadLocals.setUserId("1");
		rejectionLetterAction = new RejectionLetterAction(){
			
			protected Integer addingReportToSession(ReportOutput reportOutput){
				return Integer.valueOf(1);
			}
		};
		persistenceService = new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		rejectionLetterAction.setPersistenceService(persistenceService);
		rejectionLetterAction.setSequenceGenerator(new SequenceGenerator());
		reportService = new ReportService(){
			public ReportOutput createReport(ReportRequest reportInput){
				return reportOutput;
			}
			public boolean isValidTemplate(String templateName) {
				return false;
			}
		};
		rejectionLetterAction.setReportService(reportService);
	}

	@Test
	public void testPrint(){
		MockServletContext servletContext=new MockServletContext();
		ActionContext.setContext(new ActionContext(Ognl.createDefaultContext(null)));
		ServletActionContext.setContext(new ActionContext(Ognl.createDefaultContext(null)));
		ServletActionContext.setServletContext(servletContext);
		MockHttpServletRequest request = new MockHttpServletRequest();  
		ServletActionContext.setRequest(request);
		Objection objection = getObjection();
		rejectionLetterAction.setObjection(objection);
		rejectionLetterAction.print();
	}
	private Objection getObjection() {
		Objection objection = new Objection();
		objection.setDocNumberObjection("100");
		objection.setRecievedBy("test");
		objection.setRecievedOn(new Date());
		objection.setDetails("test");
		Property property  = getProperty();
		objection.setBasicProperty(property.getBasicProperty());
		
		return objection;
	}
	private Property getProperty() {
		Category category = objectFactory.createCategory("1.25");
		DepreciationMaster depreciationMaster = objectFactory.getDepreciationMaster("A", "1");
        Property property = (Property) objectFactory.createProperty(NMCPTISConstants.PROPTYPE_RESD, category
                .getCatBoundaries().iterator().next().getBndry(), "SELFOCC", "Owner", "1", "1.25", "1", "UFD1",
                "Residential houses", depreciationMaster, "205.7", new Date(), new Date());
      
       
        return property;
	}
}
