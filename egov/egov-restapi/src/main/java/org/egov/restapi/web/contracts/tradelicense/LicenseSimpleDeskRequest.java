package org.egov.restapi.web.contracts.tradelicense;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.egov.tl.entity.TradeLicense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class LicenseSimpleDeskRequest {

	private String txnNo;
	private String appSource;
	private String applicationNumber;
	
	@Override
	public String toString() {
		return "LicenseSimpleDeskRequest [txnNo=" + txnNo + ", appSource=" + appSource + ", applicationNumber="
				+ applicationNumber + "]";
	}
	public String getTxnNo() {
		return txnNo;
	}
	public void setTxnNo(String txnNo) {
		this.txnNo = txnNo;
	}
	public String getAppSource() {
		return appSource;
	}
	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	
	public Example tradeLicenseLikeSimpledesk() {
        TradeLicense license = new TradeLicense();
        if (isNotBlank(applicationNumber))
            license.setApplicationNumber(applicationNumber);
        if (isNotBlank(appSource))
            license.setApplicationSource(appSource);
        license.setNewWorkflow(true);
        license.setCollectionPending(false);
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("legacy", "isActive");
        return Example.of(license, matcher);
    }
	
}
