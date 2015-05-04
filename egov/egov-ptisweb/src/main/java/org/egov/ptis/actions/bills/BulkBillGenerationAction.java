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
package org.egov.ptis.actions.bills;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_KEY_WARDSFOR_BULKBILL;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Namespace("/bills")
@Transactional(readOnly = true)
public class BulkBillGenerationAction extends BaseFormAction {

	Logger LOGGER = Logger.getLogger(getClass());
	private static final String STR_HYPHEN = "-";
	private static final String RESULT_ACK = "ack";
	
	private Integer wardId;
	private String ackMessage;
	private String partNo;
	
	@Autowired
	private BoundaryDAO boundaryDAO;
	
	private List<Boundary> wardList = new ArrayList<Boundary>();
	

	@Override
	public Object getModel() {
		return null;
	}

	@Action(value="/bulkBillGeneration-newForm", results = { @Result(name = NEW) })
	public String newForm() {
		wardList = (List<Boundary>) getPersistenceService().findAllBy(
				"from Boundary BI where BI.boundaryType.name=? and BI.boundaryType.hierarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.boundaryNum", WARD_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		return NEW;
	}
	
	@Override
	public void prepare() {
		LOGGER.debug("Entered into prepare, wardNumber=" + wardId);
				
		if (wardId == null || wardId.equals(-1)) {
			addDropdownData("partNumbers", Collections.EMPTY_LIST);
		}
		
		LOGGER.debug("Exiting from prepare");
	}

	@Action(value="/bulkBillGeneration-generateBills", results = { @Result(name = RESULT_ACK) })
	public String generateBills() {
		LOGGER.debug("generateBills method started for ward number " + wardId);
		AppConfigValues appConfigValue = null;
		
		Integer wardNumber = boundaryDAO.getBoundary(Long.valueOf(wardId)).getBoundaryNum().intValue();
		String wardNumAndPartNo = wardNumber.toString() + STR_HYPHEN + partNo;
		
		appConfigValue = (AppConfigValues) persistenceService.find(
				"select appConfVal from AppConfigValues appConfVal left join appConfVal.key appConf "
						+ "where appConf.module=? and appConf.keyName=? and appConfVal.value=?",
				PropertyTaxConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_BULKBILL, wardNumAndPartNo);
		
		if (appConfigValue == null) {
			AppConfig appConfig = (AppConfig) persistenceService.find(
					"select appConf from AppConfig appConf where appConf.module=? and appConf.keyName=?",
					PropertyTaxConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_BULKBILL);
			appConfigValue = new AppConfigValues();
			appConfigValue.setKey(appConfig);
			appConfigValue.setValue(wardNumAndPartNo);
			appConfigValue.setEffectiveFrom(new Date());
			persistenceService.setType(AppConfigValues.class);
			getPersistenceService().persist(appConfigValue);
			setAckMessage("Bill generation scheduled for ward " + wardNumber + " and for part number " + partNo
					+ ", you can check the bill generation status using ");
		} else {
			setAckMessage("Bill generation already scheduled for ward " + wardNumber + " and for part number " + partNo
					+ ", you can check the bill generation status after some time using ");
		}
		LOGGER.debug("generateBills method ended for ward number " + wardNumber  + " and for part number " + partNo);
		return RESULT_ACK;
	}

	public List<Boundary> getWardList() {
		return wardList;
	}

	public void setWardList(List<Boundary> wardList) {
		this.wardList = wardList;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
}
