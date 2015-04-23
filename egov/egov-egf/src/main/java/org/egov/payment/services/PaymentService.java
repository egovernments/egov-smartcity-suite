/*
 * PaymentForm.java Created on Mar 3, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payment.services;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.payment.dao.PaymentDAOFactory;
import org.egov.payment.dao.PaymentheaderHibernateDAO;
import org.egov.payment.dao.SubledgerpaymentheaderHibernateDAO;
import org.egov.payment.model.Subledgerpaymentheader;

/**
 * @author Sathish P
 * @version 1.00
 */
public class PaymentService   
{
	 
	
	public List<Subledgerpaymentheader> getSubledgerpaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		SubledgerpaymentheaderHibernateDAO slphDao=PaymentDAOFactory.getDAOFactory().getSubledgerpaymentheaderDAO();
		return slphDao.getSubledgerpaymentheaderByVoucherHeader(voucherHeader);
	}
	public List<Paymentheader> getPaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		PaymentheaderHibernateDAO phDao=PaymentDAOFactory.getDAOFactory().getPaymentheaderDAO();
		return phDao.getPaymentheaderByVoucherHeader(voucherHeader);
	}
}
