package org.egov.egf.masters.model;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;

public class FundingAgency extends BaseModel implements EntityType {
	private static final long	serialVersionUID	= -5310440748432491445L;
	
	final static Logger LOGGER=Logger.getLogger(LoanGrantReceiptDetail.class);
	private String name;
	private String code;
	private String address;
	private String remarks;
	private boolean isActive;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getBankname()
	 */
	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getEntityDescription()
	 */
	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return code;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getEntityId()
	 */
	@Override
	public Integer getEntityId() {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.id.intValue());
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getIfsccode()
	 */
	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getModeofpay()
	 */
	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getPanno()
	 */
	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.egov.commons.utils.EntityType#getTinno()
	 */
	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/*@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
