/*
 * @(#)VoucherHeaderDAO.java 3.0, 14 Jun, 2013 11:15:44 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.dao.GenericDAO;

public interface VoucherHeaderDAO extends GenericDAO {
	public List<CVoucherHeader> getVoucherHeadersByStatus(Integer status) throws Exception;

	public List<CVoucherHeader> getVoucherHeadersByStatusAndType(Integer status, String type) throws Exception;

	public CVoucherHeader getVoucherHeadersByCGN(String cgn);
}