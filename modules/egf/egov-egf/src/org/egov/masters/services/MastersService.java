/*
 * MastersManager.java Created on Sep 8, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.services;


import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.CommonsService;
import org.egov.masters.model.AccountEntity;
  
/**
 * @author Sathish P
 * @version 1.00
 */
public interface MastersService 
{	
	/**
     * Creates AccountEntity
     * If detailType='Creditor' or 'Employee' then accountDetailKeyId=ContractorId or SupplierId or EmployeeId need to be set in accountEntity object,
     * so it will create Accountdetailkey object. Else it will create AccountEntity and then by using AccountEntityId it will create Accountdetailkey object.
     * @param accountEntity
     */
	public AccountEntity createAccountEntity(AccountEntity accountEntity);
	
	/**
     * Updates AccountEntity
     * @param accountEntity
     */
	public void updateAccountEntity(AccountEntity accountEntity);
	
	/**
     * @param id
     * @return Accountdetailtype object
     */
	public Accountdetailtype getAccountdetailtypeById(Integer id);
	
	/**
     * @param name
     * @return Accountdetailtype object
     */
	public Accountdetailtype getAccountdetailtypeByName(String name);
	
	/**
     * @param id
     * @return AccountEntity object
     */
	public AccountEntity getAccountEntitybyId(Integer id);

}
	

