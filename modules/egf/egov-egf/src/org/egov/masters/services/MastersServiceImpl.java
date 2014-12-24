/*
 * MastersManagerBean.java Created on Sep 8, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.services;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.CommonsService;
import org.egov.masters.dao.AccountEntityHibernateDAO;
import org.egov.masters.dao.AccountdetailtypeHibernateDAO;
import org.egov.masters.dao.MastersDAOFactory;
import org.egov.masters.model.AccountEntity;

/**
 * @author Sathish P
 * @version 1.00
 */
public class MastersServiceImpl implements MastersService{
	private CommonsService commonsService;
	private static final Logger LOGGER = Logger
			.getLogger(MastersServiceImpl.class);

	public AccountEntity createAccountEntity(AccountEntity accountEntity) {
		AccountEntity accEntity = null;
		Accountdetailkey adk = null;
		// String[] attrName=null;
		try {
			AccountEntityHibernateDAO accEntDao = MastersDAOFactory
					.getDAOFactory().getAccountEntityDAO();
			if (accountEntity.getAccountDetailKeyId() != null
					&& (accountEntity.getAccountdetailtype().getName()
							.equalsIgnoreCase("Creditor") || accountEntity
							.getAccountdetailtype().getName()
							.equalsIgnoreCase("Employee"))) {
				Accountdetailtype accountdetailtype = this
						.getAccountdetailtypeByName(accountEntity
								.getAccountdetailtype().getName());
				adk = new Accountdetailkey();
				adk.setGroupid(1);
				adk.setDetailkey(accountEntity.getAccountDetailKeyId());
				adk.setDetailname(accountdetailtype.getAttributename());
				adk.setAccountdetailtype(accountdetailtype);
				commonsService.createAccountdetailkey(adk);
				return accountEntity;
			} else {
				accEntity = (AccountEntity) accEntDao.create(accountEntity);
				if (accEntity.getCode() == null)
					accEntity.setCode(accEntity.getId().toString());
				Accountdetailtype accountdetailtype = this
						.getAccountdetailtypeByName(accountEntity
								.getAccountdetailtype().getName());
				adk = new Accountdetailkey();
				adk.setGroupid(1);
				adk.setDetailkey(accEntity.getId());
				adk.setDetailname(accountdetailtype.getAttributename());
				adk.setAccountdetailtype(accountdetailtype);
				commonsService.createAccountdetailkey(adk);
				return accEntity;
			}

		} catch (Exception ex) {
			LOGGER.error("Exp=" + ex.getMessage());
			// HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception: " + ex.getMessage());
		}
	}

	public void updateAccountEntity(AccountEntity accountEntity) {
		AccountEntityHibernateDAO accEntDao = MastersDAOFactory.getDAOFactory()
				.getAccountEntityDAO();
		accEntDao.update(accountEntity);
	}

	public Accountdetailtype getAccountdetailtypeById(Integer id) {
		AccountdetailtypeHibernateDAO accDtlTypeDao = MastersDAOFactory
				.getDAOFactory().getAccountdetailtypeDAO();
		return (Accountdetailtype) accDtlTypeDao.findById(id, false);
	}

	public Accountdetailtype getAccountdetailtypeByName(String name) {
		AccountdetailtypeHibernateDAO accDtlTypeDao = MastersDAOFactory
				.getDAOFactory().getAccountdetailtypeDAO();
		return (Accountdetailtype) accDtlTypeDao
				.getAccountdetailtypeByName(name);
	}

	public AccountEntity getAccountEntitybyId(Integer id) {
		AccountEntityHibernateDAO accEntDao = MastersDAOFactory.getDAOFactory()
				.getAccountEntityDAO();
		return (AccountEntity) accEntDao.findById(id, false);
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

}
