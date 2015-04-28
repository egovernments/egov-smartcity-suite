/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.view;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.service.TaxXMLToDBCoverterService;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.entity.property.BasicProperty;

@SuppressWarnings("serial")
@ParentPackage("egov")
public class TaxXMLMigrationAction extends PropertyTaxBaseAction implements ServletRequestAware {
	
	private static final Logger LOGGER = Logger.getLogger(TaxXMLMigrationAction.class);
	private static final String RESULT_ACK = "ack";
	private static final String MSG_ACK = "Successfully migrated the Tax XML for index number ";

	private PersistenceService basicPrpertyService;
	private String ackMessage;
	
	private HttpServletRequest request;
	private Long userId;
	private Boolean isCitizen;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private UserService UserService;
	
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
		
		if (basicProperty.getIsTaxXMLMigrated().equals(PropertyTaxConstants.STATUS_YES_XML_MIGRATION)) {
			setAckMessage("Tax XML is already migrated for " + indexNumber);
		} else {
			TaxXMLToDBCoverterService.createConverter(basicProperty, propertyTaxUtil, basicPrpertyService,
					propertyTaxNumberGenerator).migrateTaxXML();
			
			if (basicProperty.getIsTaxXMLMigrated().equals(PropertyTaxConstants.STATUS_YES_XML_MIGRATION)) {
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
			User user = UserService.getUserByUsername(PropertyTaxConstants.CITIZENUSER);
			userId = user.getId();
			EGOVThreadLocals.setUserId(userId.toString());
			session.setAttribute("com.egov.user.LoginUserName", user.getUsername());
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
