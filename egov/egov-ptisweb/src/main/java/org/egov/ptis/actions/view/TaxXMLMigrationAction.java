package org.egov.ptis.actions.view;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.CITIZENUSER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATUS_YES_XML_MIGRATION;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.service.TaxXMLToDBCoverterService;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;

/**
 * 
 * The class <code> TaxXMLMigrationAction </code> contains methods to migrate the 
 * single <code> BasicProperty </code> tax xml to <code> UnitCalculationDetail </code>
 * 
 * <p> This class exposes a public method to do the same, no action-mappings are required <p> 
 * 
 * @author nayeem
 *
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
public class TaxXMLMigrationAction extends PropertyTaxBaseAction implements ServletRequestAware {
	
	private static final Logger LOGGER = Logger.getLogger(TaxXMLMigrationAction.class);
	private static final String RESULT_ACK = "ack";
	private static final String MSG_ACK = "Successfully migrated the Tax XML for index number ";

	private PersistenceService basicPrpertyService;
	private String ackMessage;
	
	private HttpServletRequest request;
	private Integer userId;
	private Boolean isCitizen;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	@SkipValidation
	public String migrate() {
		LOGGER.debug("Entered into TaxXMLMigrationAction.migrate, propertyId=" + indexNumber);
		
		setUserIdToThreadLocals(); 
		
		BasicProperty basicProperty = (BasicProperty) basicPrpertyService.find(
				"from BasicPropertyImpl where upicNo = ? ", indexNumber);
		
		if (basicProperty.getIsTaxXMLMigrated().equals(STATUS_YES_XML_MIGRATION)) {
			setAckMessage("Tax XML is already migrated for " + indexNumber);
		} else {
			TaxXMLToDBCoverterService.createConverter(basicProperty, propertyTaxUtil, basicPrpertyService,
					propertyTaxNumberGenerator).migrateTaxXML();
			
			if (basicProperty.getIsTaxXMLMigrated().equals(STATUS_YES_XML_MIGRATION)) {
				setAckMessage(MSG_ACK + indexNumber);
			} else {
				throw new EGOVRuntimeException("Error in XML Migration for " + indexNumber);
			}
		}
		
		LOGGER.debug("Exiting from TaxXMLMigrationAction.migrate");
		
		return RESULT_ACK;
	}

	/**
	 * 
	 */
	private void setUserIdToThreadLocals() {
		LOGGER.debug("Entered into setUserIdToThreadLocals");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("com.egov.user.LoginUserId") == null) {
			User user = new UserDAO().getUserByUserName(CITIZENUSER);
			userId = user.getId();
			EGOVThreadLocals.setUserId(userId.toString());
			session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		}
		
		LOGGER.debug("Exiting from setUserIdToThreadLocals");
	}
	

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public PersistenceService getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public void setIsCitizen(Boolean isCitizen) {
		this.isCitizen = isCitizen;
	}
	
	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	@Override
	@SkipValidation
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}
}
