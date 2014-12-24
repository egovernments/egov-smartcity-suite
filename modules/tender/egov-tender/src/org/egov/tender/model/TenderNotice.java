package org.egov.tender.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.commons.Position;
import org.egov.tender.utils.TenderConstants;

@SuppressWarnings("serial")
public class TenderNotice extends StateAware{
	private String number;
	private Date noticeDate;
	
	private String tenderFileRefNumber;
	private TenderFileType tenderFileType;
	
	private Department department;
	private EgwStatus status;
	private Set<TenderUnit> tenderUnits=new HashSet<TenderUnit>(0);
	private Position position;
	
	//temprary variable
	//private List<TenderUnit> tenderUnitList;
	private Boolean combineTenderableGroups;
	
	public List<TenderUnit> getTenderUnitList() {
		return new ArrayList<TenderUnit>(tenderUnits);
	}
	
	public List<TenderUnit> getTenderUnitToCreateResponse() {
		List<TenderUnit> units=new ArrayList<TenderUnit>();
		for(TenderUnit unit:tenderUnits)
		{
			if(!TenderConstants.TENDERUNIT_ACCEPTED.equals(unit.getStatus().getCode()))
				units.add(unit);
		}
		return units;
	}


	public Position getPosition() {
		return position;
	}


	public void setPosition(Position positionId) {
		this.position = positionId;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public Date getNoticeDate() {
		return noticeDate;
	}


	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}


	public Set<TenderUnit> getTenderUnits() {
		return tenderUnits;
	}


	public void setTenderUnits(Set<TenderUnit> tenderUnits) {
		this.tenderUnits = tenderUnits;
	}

	

	public String getTenderFileRefNumber() {
		return tenderFileRefNumber;
	}


	public void setTenderFileRefNumber(String tenderFileRefNumber) {
		this.tenderFileRefNumber = tenderFileRefNumber;
	}

	public TenderFileType getTenderFileType() {
		return tenderFileType;
	}

	public void setTenderFileType(TenderFileType tenderFileType) {
		this.tenderFileType = tenderFileType;
	}


	@Override
	public String getStateDetails() {
		
		return TenderConstants.TENDERNOTICE_WFDESCRIPTION+ number ;
	}


	


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}


	public EgwStatus getStatus() {
		return status;
	}


	public void setStatus(EgwStatus status) {
		this.status = status;
	}


	public Boolean getCombineTenderableGroups() {
		return combineTenderableGroups;
	}


	public void setCombineTenderableGroups(Boolean combineTenderableGroups) {
		this.combineTenderableGroups = combineTenderableGroups;
	}


	
}
