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
package org.egov.ptis.actions.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Namespace("/collection")
@ResultPath("/WEB-INF/jsp/")
@Results({ 
    @Result(name = CollectPropertyTaxAction.RESULT_VIEW, location = "collection/collectPropertyTax-view.jsp")
})
@ParentPackage("egov")
public class CollectPropertyTaxAction extends BaseFormAction {

	private static final String RESULT_SHOWPENALTY = "showPenalty";
	public static final String RESULT_VIEW = "view";
	private static final Logger LOGGER = Logger.getLogger(CollectPropertyTaxAction.class);

	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private PropertyTaxCollection propertyTaxCollection;
	private PTBillServiceImpl ptBillServiceImpl;
	private PropertyTaxUtil propertyTaxUtil;
	private String propertyId;
	private String collectXML = "";
	private Boolean levyPenalty = false;
	private Boolean isPenaltyConfirmed = false;
	private List<PenaltyAndRebate> instTaxBeanList = new ArrayList<PenaltyAndRebate>();
	private Map<Integer, Installment> installmentAndId = new HashMap<Integer, Installment>();
	private Map<Installment, EgDemandDetails> installmentAndDemandDetails = new HashMap<Installment, EgDemandDetails>();
	
	@Autowired
	private EgDemandDetailsDao egDemandDetailsDAO;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ApplicationContextBeanProvider beanProvider;

	@Override
	public Object getModel() {
		return null;
	}

	@Action(value = "/collectPropertyTax-generateBill")
	public String generateBill() {
		LOGGER.info("Entered method generatePropertyTaxBill, Generating bill for index no : "
				+ propertyId);

		PropertyTaxBillable nmcPTBill = (PropertyTaxBillable) beanProvider.getBean("propertyTaxBillable");

		BasicProperty basicProperty = basicPropertyService.findByNamedQuery(
				PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId);

		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);

		nmcPTBill.setLevyPenalty(true);
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(Long.valueOf(getSession().get("userid").toString()));
		nmcPTBill.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
				.getPropertyID().getWard().getBoundaryNum().toString()));
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));

		String billXml = ptBillServiceImpl.getBillXML(nmcPTBill);
		collectXML = URLEncoder.encode(billXml);
		LOGGER.info("Exiting method generatePropertyTaxBill, collectXML (before decode): "
				+ billXml);
		return RESULT_VIEW;
	}

	public void setbasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getCollectXML() {
		return collectXML;
	}

	public void setCollectXML(String collectXML) {
		this.collectXML = collectXML;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	
	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public Boolean getLevyPenalty() {
		return levyPenalty;
	}

	public void setLevyPenalty(Boolean levyPenalty) {
		this.levyPenalty = levyPenalty;
	}

	public PropertyTaxCollection getPropertyTaxCollection() {
		return propertyTaxCollection;
	}

	public void setPropertyTaxCollection(PropertyTaxCollection propertyTaxCollection) {
		this.propertyTaxCollection = propertyTaxCollection;
	}

	public List<PenaltyAndRebate> getInstTaxBeanList() {
		return instTaxBeanList;
	}

	public void setInstTaxBeanList(List<PenaltyAndRebate> instTaxBeanList) {
		this.instTaxBeanList = instTaxBeanList;
	}

	public Boolean getIsPenaltyConfirmed() {
		return isPenaltyConfirmed;
	}

	public void setIsPenaltyConfirmed(Boolean isPenaltyConfirmed) {
		this.isPenaltyConfirmed = isPenaltyConfirmed;
	}
	
        public void setPtBillServiceImpl(PTBillServiceImpl ptBillServiceImpl) {
            this.ptBillServiceImpl = ptBillServiceImpl;
        }
}
