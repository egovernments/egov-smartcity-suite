package org.egov.ptis.actions.bills;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.APPCONFIG_KEY_WARDSFOR_BULKBILL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryDAO;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.web.actions.BaseFormAction;

public class BulkBillGenerationAction extends BaseFormAction {

	Logger LOGGER = Logger.getLogger(getClass());
	private static final String STR_HYPHEN = "-";
	private static final String RESULT_ACK = "ack";
	
	private Integer wardId;
	private String ackMessage;
	private String partNo;
	
	private List<Boundary> wardList = new ArrayList<Boundary>();
	

	@Override
	public Object getModel() {
		return null;
	}

	public String newForm() {
		wardList = (List<Boundary>) getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.boundaryNum", WARD_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		return "new";
	}
	
	@Override
	public void prepare() {
		LOGGER.debug("Entered into prepare, wardNumber=" + wardId);
				
		if (wardId == null || wardId.equals(-1)) {
			addDropdownData("partNumbers", Collections.EMPTY_LIST);
		}
		
		LOGGER.debug("Exiting from prepare");
	}

	public String generateBills() {
		LOGGER.debug("generateBills method started for ward number " + wardId);
		AppConfigValues appConfigValue = null;
		
		Integer wardNumber = new BoundaryDAO().getBoundary(wardId).getBoundaryNum().intValue();
		String wardNumAndPartNo = wardNumber.toString() + STR_HYPHEN + partNo;
		
		appConfigValue = (AppConfigValues) persistenceService.find(
				"select appConfVal from AppConfigValues appConfVal left join appConfVal.key appConf "
						+ "where appConf.module=? and appConf.keyName=? and appConfVal.value=?",
				NMCPTISConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_BULKBILL, wardNumAndPartNo);
		
		if (appConfigValue == null) {
			AppConfig appConfig = (AppConfig) persistenceService.find(
					"select appConf from AppConfig appConf where appConf.module=? and appConf.keyName=?",
					NMCPTISConstants.PTMODULENAME, APPCONFIG_KEY_WARDSFOR_BULKBILL);
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
