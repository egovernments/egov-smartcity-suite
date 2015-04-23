package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVException;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.infstr.config.AppConfigValues;
import org.egov.web.annotation.ValidationErrorPage;

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
