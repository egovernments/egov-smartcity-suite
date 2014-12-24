package org.egov.web.actions.voucher;

import java.util.List;

import org.egov.exceptions.EGOVException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.Fund;
import org.egov.commons.VoucherDetail;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class VoucherReport {
	private CGeneralLedger generalLedger = new CGeneralLedger();
	private VoucherDetail voucherDetail = new VoucherDetail();
	private PersistenceService persistenceService;
	private DepartmentImpl department;
	private static final String MULTIPLE = "MULTIPLE";
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public VoucherReport(PersistenceService persistenceService,Integer voucherId,VoucherDetail voucherDetail) {
		super();
		this.persistenceService = persistenceService;
		this.generalLedger = getGeneralLedger(voucherId,voucherDetail);
		this.voucherDetail = voucherDetail;
	}
	public CGeneralLedger getGeneralLedger() {
		return generalLedger;
	}
	public VoucherDetail getVoucherDetail() {
		return voucherDetail;
	}
	public String getSlCode() {
		persistenceService.setType(CGeneralLedgerDetail.class); 
		if(generalLedger!=null){
			List<CGeneralLedgerDetail> generalLedgerDetail = persistenceService.findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", Integer.valueOf(generalLedger.getId().toString()));
			if(generalLedgerDetail.size()>1)
				return MULTIPLE;
			if(generalLedgerDetail.size() > 0){
				Integer detailTypeId = ((CGeneralLedgerDetail)generalLedgerDetail.get(0)).getDetailTypeId();
				persistenceService.setType(Accountdetailtype.class);
				List detailType = persistenceService.findAllBy("from Accountdetailtype where id=?", detailTypeId);
				EgovCommon common = new EgovCommon();
				common.setPersistenceService(persistenceService);
				Integer detailKeyId = ((CGeneralLedgerDetail)generalLedgerDetail.get(0)).getDetailKeyId();
				EntityType entityType = null;
				try {
					entityType = common.getEntityType((Accountdetailtype) detailType.get(0),detailKeyId);
				} catch (EGOVException e) {
					e.printStackTrace();
				}

				return entityType.getCode()+"/"+entityType.getEntityDescription();
			}
		}
		return "";
	}
	
	public String getFunctionName(){
		if(generalLedger!=null){
			persistenceService.setType(CFunction.class);
			if(generalLedger.getFunctionId()!=null){
				CFunction function = fetchFunction(generalLedger.getFunctionId());
				return function == null ? "" : function.getName();
			}
		}else if(voucherDetail!=null && voucherDetail.getVoucherHeaderId()!=null && voucherDetail.getVoucherHeaderId().getFunctionId()!=null){
			CFunction function = fetchFunction(generalLedger.getFunctionId());
			return function == null ? "" : function.getName();
		}
		return "";
	}

	private CFunction fetchFunction(Integer functionId) {
		return (CFunction) persistenceService.findById(Long.valueOf(functionId), false);
	}
	
	public String getFundName(){
		if(voucherDetail!=null && voucherDetail.getVoucherHeaderId().getFundId()!=null){
			persistenceService.setType(Fund.class);
			Fund fund = (Fund) persistenceService.findById(voucherDetail.getVoucherHeaderId().getFundId().getId(), false);
			return fund == null ? "" : fund.getName();
		}
		return "";
	}

	private CGeneralLedger getGeneralLedger(Integer voucherId,VoucherDetail voucherLineId) {
		persistenceService.setType(CGeneralLedger.class);
		return (CGeneralLedger) persistenceService.find("from CGeneralLedger where voucherHeaderId.id=? and glcode=? and voucherlineId=?", Long.valueOf(voucherId),voucherLineId.getGlCode(),voucherLineId.getId());
	}

	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}

	public DepartmentImpl getDepartment() {
		return department;
	}

}
