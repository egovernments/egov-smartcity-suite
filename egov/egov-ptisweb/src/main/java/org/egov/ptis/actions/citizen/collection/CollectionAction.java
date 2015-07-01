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
package org.egov.ptis.actions.citizen.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.PropertyInstTaxBean;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@Namespace("/citizen/collection")
@ResultPath("/WEB-INF/jsp/")
@Results({ 
    @Result(name = CollectionAction.RESULT_COLLECTTAX, location = "citizen/collection/collection-collectTax.jsp"),
    @Result(name = CollectionAction.RESULT_TAXPAID, location = "citizen/collection/collection-taxPaid.jsp")
})
@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class CollectionAction extends BaseFormAction {
    private final Logger LOGGER = Logger.getLogger(getClass());
    public static final String RESULT_COLLECTTAX = "collectTax";
    public static final String RESULT_TAXPAID = "taxPaid";
    
    private PersistenceService<BasicProperty, Long> basicPropertyService;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private PTBillServiceImpl nmcPtBillServiceImpl;
    private String collectXML;
    private String indexNum;
    private Long userId;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationContextBeanProvider beanProvider;

    public void prepare() {
        LOGGER.debug("Entered into prepare method");
        User usr = (User) userService.getUserByUsername(PropertyTaxConstants.CITIZENUSER);
        setUserId(usr.getId().longValue());
        EgovThreadLocals.setUserId(usr.getId());
        LOGGER.debug("Exit from prepare method");
    }

    @Action(value = "/collection-generateBill")
    public String generateBill() {
        LOGGER.debug("Entered method generatePropertyTaxBill, indexNum: " + indexNum);
        
        PropertyTaxBillable nmcPTBill = (PropertyTaxBillable) beanProvider.getBean(BEANNAME_PROPERTY_TAX_BILLABLE);
        BasicProperty basicProperty = basicPropertyService.findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO, indexNum);
        
        LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
        Map<String, BigDecimal> demandCollMap = propertyTaxUtil.getDemandAndCollection(basicProperty.getProperty());
        BigDecimal currDue = demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR));
        BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));

        if (currDue.compareTo(BigDecimal.ZERO) <= 0 && arrDue.compareTo(BigDecimal.ZERO) <= 0) {
            return RESULT_TAXPAID;
        }
        
        nmcPTBill.setBasicProperty(basicProperty);
        nmcPTBill.setLevyPenalty(true);
        nmcPTBill.setUserId(userId);
        nmcPTBill.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID()
                .getWard().getBoundaryNum().toString()));
        nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
        collectXML = URLEncoder.encode(nmcPtBillServiceImpl.getBillXML(nmcPTBill));
        LOGGER.info("Exiting method generatePropertyTaxBill, collectXML: " + collectXML);
        
        return RESULT_COLLECTTAX;
    }

    public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
        return basicPropertyService;
    }

    public void setbasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
        return propertyTaxNumberGenerator;
    }

    public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public PTBillServiceImpl getNmcPtBillServiceImpl() {
        return nmcPtBillServiceImpl;
    }

    public void setNmcPtBillServiceImpl(PTBillServiceImpl nmcPtBillServiceImpl) {
        this.nmcPtBillServiceImpl = nmcPtBillServiceImpl;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public String getCollectXML() {
        return collectXML;
    }

    public void setCollectXML(String collectXML) {
        this.collectXML = collectXML;
    }

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
