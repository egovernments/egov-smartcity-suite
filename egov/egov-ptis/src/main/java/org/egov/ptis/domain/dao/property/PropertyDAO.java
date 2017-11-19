/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.domain.dao.property;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyDAO {

	public Property getPropertyByID(String propID);

	public Property getPropertyByBasicPropertyID(BasicProperty basicProperty);

	public List getAllNonDefaultProperties(BasicProperty basicProp);

	public List getAllProperties(BasicProperty basicProp);

	public List getOnlineDateByWardID(Integer wardId);

	public Property getPropertyForInstallment(BasicProperty basicProp, Installment insatllment,
			PropertySource src);

	public List getAllHistories(BasicProperty basicProp, PropertySource src);

	public List getWardWiseProperties();

	public Property getPropertyBySource(String src);

	public List getAllPropertiesForGivenBndryListAndSrc(List bndryList, String src);

	public List getAllPropertiesForGivenBndryListSrcAndInst(List bndryList, String src,
			Installment inst);

	public List getAllNonHistoryPropertiesForSrc(BasicProperty basicProperty, PropertySource src);

	public Property getPropertyBySrcAndBp(BasicProperty basicProperty, PropertySource src);

	public boolean checkIfPropCountExceeds500(List bndryList);

	public List getBasicPropertyListByDcNo(String dcNo) throws ApplicationRuntimeException;

	public List getPtDemandArvProposedList(Property property);

	public Citizen getOwnerByOwnerId(Long id);

	public List getPropertyDemand(String propertyId);

	public List getPropertyRebate(String propertyId);

	public List getPropertyCollection(String propertyId);

	public List getPTDemandArvByNoticeNumber(String noticeNo);

	public List getPropsMrkdForDeactByWard(Boundary boundary) throws PropertyNotFoundException;

	/**
	 * To get the list of required values as of the Projection,restriction and
	 * order in which the client passes as parameters.All these values are taken
	 * from PropertyMaterlizeView table.
	 * 
	 * @param org
	 *            .hibernate.criterion.Projection projection
	 * @param org
	 *            .hibernate.criterion.Criterion criterion
	 * @param org
	 *            .hibernate.criterion.Order order
	 * 
	 * @return Projection list(i.e mentioned in Projection parameter) from
	 *         PropertyMaterlizeView table.
	 * 
	 */
	public List getPropMaterlizeViewList(Projection proj, Criterion criterion, Order order);

	/**
	 * To get the list of required values as of the Projection,restriction and
	 * order in which the client passes as parameters..
	 * 
	 * @param Class
	 *            projection
	 * @param org
	 *            .hibernate.criterion.Projection classObj
	 * @param org
	 *            .hibernate.criterion.Criterion criterion
	 * @param org
	 *            .hibernate.criterion.Order order
	 * 
	 * @return Projection list(i.e mentioned in Projection parameter) .
	 * 
	 */
	public List getResultsList(Class classObj, Projection projection, Criterion criterion,
			Order order);

	/**
	 * To get the list of required values
	 * 
	 * @param org
	 *            .hibernate.criterion.DetachedCriteria detachedCriteria
	 * 
	 * @return Projection list(i.e mentioned in DetachedCriteria).
	 * 
	 */
	public List getResultsList(DetachedCriteria detachedCriteria);

	public List getDmdCollAmtInstWise(EgDemand egDemand);
	
	public List getTotalDemandDetailsIncludingPenalty(EgDemand egDemand);
	
	public List getPenaltyDmdCollAmtInstWise(EgDemand egDemand);
	
	/**
	 * Called to get the EgDemandDetails Id From Installment and Egdemand.
	 * 
	 * 
	 * @param org
	 *            .egov.commons.Installment
	 * @param org
	 *            .egov.demand.model.EgDemand
	 * 
	 * @return java.util.List.
	 * 
	 */

	public List getDmdDetIdFromInstallandEgDemand(Installment installment, EgDemand egDemand);

	/**
	 * Method called to get the EgptProperty Id from the Bill Id.Property is
	 * linked with EgDemand which internally linked with egBill.
	 * 
	 * @param billId
	 *            - Id of the EgBill Object .
	 * @return java.math.BigDecimal - returns the EgptProperty Id. If the billId
	 *         is null then null is returned.
	 * 
	 */
	public BigDecimal getEgptPropertyFromBillId(Long billId);

	/**
	 * Method called to get all the Demands(i.,e including the history and non
	 * history) for a BasicProperty.
	 * 
	 * @param basicProperty
	 *            - BasicProperty Object in which Demands needs are to retrieved
	 * @return java.util.List - returns the list of Demands for the
	 *         basicProperty. If the basicPrperty is null then null is returned
	 * 
	 */
	public List getAllDemands(BasicProperty basicProperty);

	/**
	 * Method called to get EgDemandDetails Ids based on given
	 * EgDemand,Installment and Mastercode.
	 * 
	 * @param installment
	 *            - Installment in which DemandDetail belongs.
	 * @param egDemand
	 *            -EgDemand Object.
	 * @param demandReasonMasterCode
	 *            - EgDemandReasonMaster code
	 * @return egDemand - returns the list of Demands for the basicProperty.
	 * 
	 */
	public List getDmdDetIdFromInstallandEgDemand(Installment installment, EgDemand egDemand,
			String demandReasonMasterCode);

	/**
	 * Returns installment wise demand and collection for all the Demand reasons
	 * 
	 * @param egDemand
	 *            -EgDemand Object.
	 * @return returns list of installment and respective demand and collection
	 *         and rebate.
	 * 
	 */
	public List getDmdCollForAllDmdReasons(EgDemand egDemand);

	public Property findById(Integer id, boolean lock);

	public List<Property> findAll();

	public Property create(Property property);

	public void delete(Property property);

	public Property update(Property property);

	public List getInstallmentAndReasonWiseDemandDetails(EgDemand egDemand);

}
