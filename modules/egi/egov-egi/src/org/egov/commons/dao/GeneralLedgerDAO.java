package org.egov.commons.dao;

import java.math.BigDecimal;
import java.util.List;

import org.egov.commons.CGeneralLedger;
import org.egov.infstr.dao.GenericDAO;

public interface GeneralLedgerDAO extends GenericDAO {
	String getActualsPrev(String accCode, String functionId, String budgetingType) throws Exception;

	String getActualsDecCurr(String accCode, String functionId, String budgetingType) throws Exception;

	List<CGeneralLedger> findCGeneralLedgerByVoucherHeaderId(Long voucherHeaderId) throws Exception;

	String getCBillDeductionAmtByVhId(Long voucherHeaderId);

	BigDecimal getGlAmountForBudgetingType(Long budType, List glcodeList, String fiscalYearID, String functionId, String schemeId, String subSchemeId, String asOnDate) throws Exception;

	BigDecimal getGlAmountbyGlcodeList(List glCodeList, BigDecimal glAmount) throws Exception;
}