/*
 * CommonMethodsI.java  Created on Mar 28, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
import java.sql.Connection;

import com.exilant.exility.common.TaskFailedException;
public interface CommonMethodsI {

	public String getChequeInHand(int boundaryId,Connection connection) throws Exception;
	public String getCashInHand(int boundaryId,Connection connection) throws Exception;
	public String getPTCode(String forYear,Connection connection) throws Exception;
	public String getBankCode(int bankAccountId,Connection connection) throws Exception;
	public String getBankCode(int bankAccountId) throws Exception;
	public String getFiscalPeriod(String vDate,Connection connection) throws TaskFailedException,Exception;
	public String getFiscalPeriod(String vDate) throws TaskFailedException,Exception;
	public String getBankId(int bankAccountId,Connection connection) throws Exception;
	public double getAccountBalance(int bankAccountId,String vcDate,Connection connection) throws Exception;
	public String getCodeName(String purposeId,Connection connection) throws Exception;
	public String getNameFromCode(String glcode,Connection connection) throws Exception;
	public String getGlCode(String glCodeId,Connection connection) throws Exception;
	public String getGlCode(String glCodeId) throws Exception;
	public String checkRecordIdInLog(String recordId, int userId, Connection connection) throws Exception;
	public String getDivisionCode(Integer divid,Connection connection) throws Exception;
	public String getFinacialYear(String vDate,Connection connection) throws Exception;
	public Integer getDivisionId(Integer fieldId,Connection connection) throws Exception;
	//	This method gets the GlCodeId by passing GLCODE as parameter ---added by Sapna
	public String getGlCodeId(String glCode,Connection connection) throws Exception;
	public Integer getDivisionIdFromCode(String divisionCode,Connection connection) throws Exception;
	public String getTxnNumber(String txnType,String vDate,Connection connection) throws Exception;
	public String getTxnNumber(String fundId,String txnType,String vDate,Connection connection) throws Exception;
	public String getTransRunningNumber(String fundId,String txnType,String vDate,Connection connection) throws Exception;
}
