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
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.infra.admin.master.entity.AppConfigValues;

import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation
public class ApprovePreapprovedVouchers extends VoucherSearchAction {

	private static final long		serialVersionUID	= 1L;
	
	private BillsAccountingService	billsAccountingService;
	
	private String[]				approveList;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@ValidationErrorPage(value=SEARCH)
	public String search() throws EGOVException, ParseException {
		String sql = "";
		if (!voucherHeader.getType().equals("-1")) {
			sql = sql + " and vh.type='" + voucherHeader.getType() + "'";
		}
		if (voucherHeader.getName() != null && !voucherHeader.getName().equalsIgnoreCase("-1")) {
			sql = sql + " and vh.name='" + voucherHeader.getName() + "'";
		}
		if (!voucherHeader.getVoucherNumber().equals("")) {
			sql = sql + " and vh.voucherNumber like '%" + voucherHeader.getVoucherNumber() + "%'";
		}
		if (!fromDate.equals("")) {
			sql = sql + " and vh.voucherDate>='" + sdf.format(fromDate) + "'";
		}
		if (!toDate.equals("")) {
			sql = sql + " and vh.voucherDate<='" + sdf.format(toDate) + "'";
		}
		if (voucherHeader.getFundId() != null) {
			sql = sql + " and vh.fundId=" + voucherHeader.getFundId().getId();
		}
		if (voucherHeader.getFundsourceId() != null) {
			sql = sql + " and vh.fundsourceId=" + voucherHeader.getFundsourceId().getId();
		}
		if (voucherHeader.getVouchermis().getDepartmentid() != null) {
			sql = sql + " and vh.vouchermis.departmentid=" + voucherHeader.getVouchermis().getDepartmentid().getId();
		}
		if (voucherHeader.getVouchermis().getSchemeid() != null) {
			sql = sql + " and vh.vouchermis.schemeid=" + voucherHeader.getVouchermis().getSchemeid().getId();
		}
		if (voucherHeader.getVouchermis().getSubschemeid() != null) {
			sql = sql + " and vh.vouchermis.subschemeid=" + voucherHeader.getVouchermis().getSubschemeid().getId();
		}
		if (voucherHeader.getVouchermis().getFunctionary() != null) {
			sql = sql + " and vh.vouchermis.functionary=" + voucherHeader.getVouchermis().getFunctionary().getId();
		}
		if (voucherHeader.getVouchermis().getDivisionid() != null) {
			sql = sql + " and vh.vouchermis.divisionid=" + voucherHeader.getVouchermis().getDivisionid().getId();
		}
		
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");
		final String statusInclude = appList.get(0).getValue();
		
		final List<CVoucherHeader> list = persistenceService.findAllBy(" from CVoucherHeader vh where vh.status  in (" + statusInclude + ") and vh.moduleId is not null " + sql
				+ " order by vh.type, vh.name,vh.voucherDate,vh.voucherNumber ");
		voucherList = new ArrayList<Map<String, Object>>();
		Map<String, Object> voucherMap = null;
		for (final CVoucherHeader voucherheader : list) {
			voucherMap = new HashMap<String, Object>();
			BigDecimal amt = BigDecimal.ZERO;
			voucherMap.put("id", voucherheader.getId());
			voucherMap.put("cgn", voucherheader.getCgn());
			voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
			voucherMap.put("type", voucherheader.getType());
			voucherMap.put("name", voucherheader.getName());
			
			voucherMap.put("voucherdate", voucherheader.getVoucherDate());
			voucherMap.put("fundname", voucherheader.getFundId().getName());
			for (final VoucherDetail detail : voucherheader.getVoucherDetail()) {
				amt = amt.add(detail.getDebitAmount());
			}
			voucherMap.put("amount", amt);
			voucherMap.put("status", (voucherheader.getStatus() == 0 ? (voucherheader.getIsConfirmed() == 0 ? "UnConfirmed" : "Confirmed") : voucherheader
					.getStatus() == 1 ? "Reversed" : voucherheader.getStatus() == 2 ? "Reversal" : ""));
			voucherList.add(voucherMap);
		}
		// is for voucher name drop down which is ajax call in jsp
		if (voucherHeader.getType() != null && !voucherHeader.getType().equals("-1")) {
			nameList = getVoucherNameMap(voucherHeader.getType());
		}
		
		return SEARCH;
	}
	
	public String approve() {
		Long vouhcerheaderid = Long.valueOf(-1);
		if(approveList!=null && approveList.length!=0)
		{
		for (final String vhIdStr : approveList) {
			vouhcerheaderid = Long.valueOf(vhIdStr);
			billsAccountingService.createVoucherfromPreApprovedVoucher(vouhcerheaderid);
		}
		addActionMessage(getText("approve.vouchers.successful"));
		}
		else
		{
		addActionMessage(getText("no.vouchers.selected"));
		}
		return SEARCH;
	}
	
	public String[] getApproveList() {
		return approveList;
	}
	
	
	public void setApproveList(final String[] approveList) {
		this.approveList = approveList;
	}
	
	public void setBillsAccountingService(final BillsAccountingService billsAccountingService) {
		this.billsAccountingService = billsAccountingService;
	}
	
}
