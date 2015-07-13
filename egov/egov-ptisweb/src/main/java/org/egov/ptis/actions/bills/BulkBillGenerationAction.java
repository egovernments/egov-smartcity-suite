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

import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.BulkBillGeneration;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({  @Result(name = BulkBillGenerationAction.NEW, location = "bulkBillGeneration-new.jsp"),
    @Result(name = BulkBillGenerationAction.RESULT_ACK, location = "bulkBillGeneration-ack.jsp")
    })
public class BulkBillGenerationAction extends BaseFormAction {

	Logger LOGGER = Logger.getLogger(getClass());
	public static final String RESULT_ACK = "ack";
	private Integer wardId;
	private String ackMessage;
	@Autowired
	private BoundaryDAO boundaryDAO;
	@Autowired
        private InstallmentDao installmentDao;
	@Autowired
        private ModuleService moduleService;
	private List<Boundary> wardList = new ArrayList<Boundary>();

	@Override
	public Object getModel() {
		return null;
	}
	@Action(value = "/bills/bulkBillGeneration-newForm")
	public String newForm() {
	        wardList = getPersistenceService().findAllBy(
                    "from Boundary BI where BI.boundaryType.name=? and BI.boundaryType.hierarchyType.name=? "
                                    + "and BI.isHistory='N' order by BI.boundaryNum", WARD,ADMIN_HIERARCHY_TYPE
                    );
		return NEW;
	}


	@Action(value = "/bills/bulkBillGeneration-generateBills")
	public String generateBills() {
		LOGGER.debug("generateBills method started for ward number " + wardId);
		BulkBillGeneration bulkBill = null;

		Integer wardNumber = boundaryDAO.getBoundary(Long.valueOf(wardId)).getBoundaryNum()
				.intValue();
		Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());

		bulkBill = (BulkBillGeneration) persistenceService.find(
				"from BulkBillGeneration where wardNumber=? and installment.id=?",
				 wardNumber.toString(),currentInstall.getId());

		if (bulkBill == null) {
		        bulkBill = new BulkBillGeneration();
		        bulkBill.setWardNumber( wardNumber.toString());
		        bulkBill.setInstallment(currentInstall);
			persistenceService.setType(BulkBillGeneration.class);
			getPersistenceService().persist(bulkBill);
			setAckMessage("Bill generation scheduled for ward " + wardNumber
					+ " and for Installment " +  currentInstall.getDescription()
					+ ", you can check the bill generation status using ");
		} else {
			setAckMessage("Bill generation already scheduled for ward " + wardNumber
					+ " and for Installment " +  currentInstall.getDescription()
					+ ", you can check the bill generation status after some time using ");
		}
		LOGGER.debug("generateBills method ended for ward number " + wardNumber);
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
}
