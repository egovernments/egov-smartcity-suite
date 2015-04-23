/*
 * PropertyDAO.java Created on Oct 05 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
//TODO -- Uncomment this once demand code is available
//import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
//TODO -- Uncomment this once demand code is available
//import org.egov.lib.citizen.model.Owner;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

/**
 * <p>
 * This is an interface which would be implemented by the Individual Frameworks
 * for all the CRUD (create, read, update, delete) basic data access operations
 * for Property
 * 
 * @author Neetu
 * @version 2.00
 */
public interface PropertyDAO extends org.egov.infstr.dao.GenericDAO {
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

	public List getBasicPropertyListByDcNo(String dcNo) throws EGOVRuntimeException;

	public List getPtDemandArvProposedList(Property property);

	//TODO -- Uncomment this once demand code is available
	//public Owner getOwnerByOwnerId(Integer id);

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
	 *@param org
	 *            .hibernate.criterion.Projection projection
	 *@param org
	 *            .hibernate.criterion.Criterion criterion
	 *@param org
	 *            .hibernate.criterion.Order order
	 * 
	 *@return Projection list(i.e mentioned in Projection parameter) from
	 *         PropertyMaterlizeView table.
	 * 
	 */
	public List getPropMaterlizeViewList(Projection proj, Criterion criterion, Order order);

	/**
	 * To get the list of required values as of the Projection,restriction and
	 * order in which the client passes as parameters..
	 * 
	 *@param Class
	 *            projection
	 *@param org
	 *            .hibernate.criterion.Projection classObj
	 *@param org
	 *            .hibernate.criterion.Criterion criterion
	 *@param org
	 *            .hibernate.criterion.Order order
	 * 
	 *@return Projection list(i.e mentioned in Projection parameter) .
	 * 
	 */
	public List getResultsList(Class classObj, Projection projection, Criterion criterion,
			Order order);

	/**
	 * To get the list of required values
	 * 
	 *@param org
	 *            .hibernate.criterion.DetachedCriteria detachedCriteria
	 * 
	 *@return Projection list(i.e mentioned in DetachedCriteria).
	 * 
	 */
	public List getResultsList(DetachedCriteria detachedCriteria);

	//TODO -- Uncomment this once demand code is available
	//public List getDmdCollAmtInstWise(EgDemand egDemand);

	/**
	 * Called to get the EgDemandDetails Id From Installment and Egdemand.
	 * 
	 * 
	 *@param org
	 *            .egov.commons.Installment
	 *@param org
	 *            .egov.demand.model.EgDemand
	 * 
	 *@return java.util.List.
	 * 
	 */

	//TODO -- Uncomment this once demand code is available
	//public List getDmdDetIdFromInstallandEgDemand(Installment installment, EgDemand egDemand);

	/**
	 * Method called to get the EgptProperty Id from the Bill Id.Property is
	 * linked with EgDemand which internally linked with egBill.
	 * 
	 *@param billId
	 *            - Id of the EgBill Object .
	 *@return java.math.BigDecimal - returns the EgptProperty Id. If the billId
	 *         is null then null is returned.
	 * 
	 */
	public BigDecimal getEgptPropertyFromBillId(Long billId);

	/**
	 * Method called to get all the Demands(i.,e including the history and non
	 * history) for a BasicProperty.
	 * 
	 *@param basicProperty
	 *            - BasicProperty Object in which Demands needs are to retrieved
	 *@return java.util.List - returns the list of Demands for the
	 *         basicProperty. If the basicPrperty is null then null is returned
	 * 
	 */
	public List getAllDemands(BasicProperty basicProperty);

	/**
	 * Method called to get EgDemandDetails Ids based on given
	 * EgDemand,Installment and Mastercode.
	 * 
	 *@param installment
	 *            - Installment in which DemandDetail belongs.
	 *@param egDemand
	 *            -EgDemand Object.
	 *@param demandReasonMasterCode
	 *            - EgDemandReasonMaster code
	 *@return egDemand - returns the list of Demands for the basicProperty.
	 * 
	 */
	//TODO -- Uncomment this once demand code is available
	/*public List getDmdDetIdFromInstallandEgDemand(Installment installment, EgDemand egDemand,
			String demandReasonMasterCode);*/
	/**
	 * Returns installment wise demand and collection for all the Demand reasons
	 * 
	 * @param egDemand
	 *            -EgDemand Object.
	 * @return returns list of installment and respective demand and collection
	 *         and rebate.
	 * 
	 */
	//TODO -- Uncomment this once demand code is available
	//public List getDmdCollForAllDmdReasons(EgDemand egDemand);

}
