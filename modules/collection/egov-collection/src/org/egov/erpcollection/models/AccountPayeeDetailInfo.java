package org.egov.erpcollection.models;

import java.math.BigDecimal;

import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;

/**
 * The AccountPayeeDetail information class. Provides details of a AccountPayeeDetail in subledger detail.
 */
public class AccountPayeeDetailInfo {
	private final AccountPayeeDetail accountPayeeDetail;
	private EntityType entityType;
	
	public AccountPayeeDetailInfo(AccountPayeeDetail accountPayeeDetail,EgovCommon egovCommon){
		this.accountPayeeDetail = accountPayeeDetail;
		try {
			populateEntityType(accountPayeeDetail, egovCommon);
		} catch (EGOVException e) {
			throw new EGOVRuntimeException(
					"Could not get entity type for account detail type ["
							+ accountPayeeDetail.getAccountDetailType().getTablename()
							+ "], account detail key id ["
							+ accountPayeeDetail.getAccountDetailKey().getId()
							+ "]", e);
		}
	}

	public void populateEntityType(AccountPayeeDetail accountPayeeDetail,
			EgovCommon egovCommon) throws EGOVException {
		entityType = egovCommon.getEntityType(accountPayeeDetail
				.getAccountDetailType(), accountPayeeDetail
				.getAccountDetailKey().getDetailkey());
	}
	
	/**
	 * @return the GL code
	 */
	public String getGlCode() {
		return accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode()==null?
				null:accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode();
	}
	
	public String getAccountDetailTypeName(){
		return accountPayeeDetail.getAccountDetailType().getName();
	}
	
	public Accountdetailtype getAccountDetailType() {
		return accountPayeeDetail.getAccountDetailType();
	}
	
	public Accountdetailkey getAccountDetailKey() {
		return accountPayeeDetail.getAccountDetailKey();
	}
	
	public BigDecimal getAmount(){
		return accountPayeeDetail.getAmount();
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	/**
	 * @return the order number
	 */
	public Long getOrderNumber() {
		return accountPayeeDetail.getReceiptDetail().getOrdernumber()==null?
				null:accountPayeeDetail.getReceiptDetail().getOrdernumber();
	}
}
