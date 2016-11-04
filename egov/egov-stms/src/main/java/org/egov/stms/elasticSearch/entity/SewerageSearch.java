/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.elasticSearch.entity;

import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

public class SewerageSearch implements Indexable {

	@Override
	public String getIndexId() {

		return ApplicationThreadLocals.getCityCode() + "-" + applicationNumber;
	}
	
	@Searchable(name = "consumernumber", group = Searchable.Group.SEARCHABLE)
	private String consumerNumber;
	
	@Searchable(name = "applicationnumber", group = Searchable.Group.CLAUSES)
        private String applicationNumber;
	
	@Searchable(name = "type", group = Searchable.Group.CLAUSES)
	private String applicationType;
	
	@Searchable(name = "applicationdate", group = Searchable.Group.CLAUSES)
	private Date applicationDate;
	
	@Searchable(name = "disposaldate", group = Searchable.Group.CLAUSES)
	private Date disposalDate;

	@Searchable(name = "status", group = Searchable.Group.SEARCHABLE)
	private String applicationStatus;

	@Searchable(name = "estimationnumber", group = Searchable.Group.SEARCHABLE)
	private String estimationNumber;
	
	@Searchable(name = "estimationdate", group = Searchable.Group.CLAUSES)
	private Date estimationDate;

	@Searchable(name = "workordernumber", group = Searchable.Group.SEARCHABLE)
	private String workOrderNumber;
	
	@Searchable(name = "workorderdate", group = Searchable.Group.CLAUSES)
	private Date workOrderDate;
	
	@Searchable(name= "closurenoticenumber", group=Searchable.Group.SEARCHABLE)
	private String closureNoticeNumber;
	
	@Searchable(name= "closurenoticedate", group=Searchable.Group.CLAUSES)
	private Date closureNoticeDate;
	
	@Searchable(name = "createdby", group = Searchable.Group.CLAUSES)
	private String applicationCreatedBy;

	@Searchable(name = "createdDate", group = Searchable.Group.COMMON)
	private Date createdDate;

	@Searchable(name = "shscnumber", group = Searchable.Group.SEARCHABLE)
	private String shscNumber;

	@Searchable(name = "ptassesmentno", group = Searchable.Group.CLAUSES)
	private String propertyIdentifier;

	@Searchable(name = "propertytype", group = Searchable.Group.CLAUSES)
	private String propertyType;
	
    @Searchable(name = "noofclosetsresidential", group = Searchable.Group.CLAUSES)
    private Integer noOfClosets_residential;
    
    @Searchable(name = "noofclosetsnonresidential", group = Searchable.Group.CLAUSES)
    private Integer noOfClosets_nonResidential;

	@Searchable(name = "connectionstatus", group = Searchable.Group.CLAUSES)
	private String connectionStatus;
	
	@Searchable(name = "executiondate", group = Searchable.Group.CLAUSES)
	private Date executionDate;
	
	@Searchable(name = "islegacy", group = Searchable.Group.CLAUSES)
	private Boolean islegacy;
	
	@Searchable(name = "cityname", group = Searchable.Group.CLAUSES)
	private String ulbName;

	@Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
	private String districtName;

	@Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
	private String regionName;

	@Searchable(name = "citygrade", group = Searchable.Group.CLAUSES)
	private String ulbGrade;

	@Searchable(name = "citycode", group = Searchable.Group.CLAUSES)
	private String ulbCode;
	
	@Searchable(name = "mobilenumber", group = Searchable.Group.CLAUSES)
	private String mobileNumber;
	
	@Searchable(name = "revwardname", group = Searchable.Group.CLAUSES)
	private String ward;
	
	@Searchable(name = "address", group = Searchable.Group.SEARCHABLE)
	private String address;
	
	@Searchable(name = "consumername", group = Searchable.Group.SEARCHABLE)
	private String consumerName;
	
	@Searchable(name = "doorno", group = Searchable.Group.CLAUSES)
	private String doorNo;
	
	// isActive used for setting application status is active or not
	// on connection execution application is marked as active
	@Searchable(name = "isactive", group = Searchable.Group.CLAUSES)
        private boolean isActive;
	
	public SewerageSearch(final String consumerNumber,
			final String ulbName, final String ulbCode, final Date createdDate,
			final String districtName, final String regionName,
			final String ulbGrade) {
		this.consumerNumber = consumerNumber;
		this.ulbName = ulbName;
		this.createdDate = createdDate;
		this.districtName = districtName;
		this.regionName = regionName;
		this.ulbGrade = ulbGrade;
		this.ulbCode = ulbCode;
	}
	
	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getDisposalDate() {
		return disposalDate;
	}

	public void setDisposalDate(Date disposalDate) {
		this.disposalDate = disposalDate;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public Date getWorkOrderDate() {
		return workOrderDate;
	}

	public void setWorkOrderDate(Date workOrderDate) {
		this.workOrderDate = workOrderDate;
	}

	public String getApplicationCreatedBy() {
		return applicationCreatedBy;
	}

	public void setApplicationCreatedBy(String applicationCreatedBy) {
		this.applicationCreatedBy = applicationCreatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getShscNumber() {
		return shscNumber;
	}

	public void setShscNumber(String shscNumber) {
		this.shscNumber = shscNumber;
	}

	public String getPropertyIdentifier() {
		return propertyIdentifier;
	}

	public void setPropertyIdentifier(String propertyIdentifier) {
		this.propertyIdentifier = propertyIdentifier;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public Date getExecutionDate() {
		return executionDate; 
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public Boolean getIslegacy() {
		return islegacy;
	}

	public void setIslegacy(Boolean islegacy) {
		this.islegacy = islegacy;
	}

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getUlbGrade() {
		return ulbGrade;
	}

	public void setUlbGrade(String ulbGrade) {
		this.ulbGrade = ulbGrade;
	}

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

	public Integer getNoOfClosets_residential() {
		return noOfClosets_residential;
	}

	public void setNoOfClosets_residential(Integer noOfClosets_residential) {
		this.noOfClosets_residential = noOfClosets_residential;
	}

	public Integer getNoOfClosets_nonResidential() {
		return noOfClosets_nonResidential;
	}

	public void setNoOfClosets_nonResidential(Integer noOfClosets_nonResidential) {
		this.noOfClosets_nonResidential = noOfClosets_nonResidential;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

        public String getEstimationNumber() {
            return estimationNumber;
        }
    
        public void setEstimationNumber(String estimationNumber) {
            this.estimationNumber = estimationNumber;
        }
    
        public Date getEstimationDate() {
            return estimationDate;
        }
    
        public void setEstimationDate(Date estimationDate) {
            this.estimationDate = estimationDate;
        }
	
        public void setIsActive(boolean b) {
            this.isActive = b;
        }
        
        public boolean getIsActive() {
            return isActive;
        }

        public String getConsumerNumber() {
            return consumerNumber;
        }

        public void setConsumerNumber(String consumerNumber) {
            this.consumerNumber = consumerNumber;
        }

        public String getClosureNoticeNumber() {
            return closureNoticeNumber;
        }

        public void setClosureNoticeNumber(String closureNoticeNumber) {
            this.closureNoticeNumber = closureNoticeNumber;
        }

        public Date getClosureNoticeDate() {
            return closureNoticeDate;
        }

        public void setClosureNoticeDate(Date closureNoticeDate) {
            this.closureNoticeDate = closureNoticeDate;
        }
}